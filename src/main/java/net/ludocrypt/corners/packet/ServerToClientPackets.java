package net.ludocrypt.corners.packet;

import java.util.Comparator;
import java.util.List;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.ludocrypt.corners.access.MusicTrackerAccess;
import net.ludocrypt.corners.client.sound.LoopingPositionedSoundInstance;
import net.ludocrypt.corners.init.CornerBlocks;
import net.ludocrypt.corners.init.CornerRadioRegistry;
import net.ludocrypt.corners.util.DimensionalPaintingMotive;
import net.ludocrypt.limlib.access.SoundSystemAccess;
import net.ludocrypt.limlib.mixin.SoundManagerAccessor;
import net.minecraft.entity.decoration.painting.PaintingEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.Vec3d;

public class ServerToClientPackets {

	public static void manageServerToClientPackets() {
		ClientPlayNetworking.registerGlobalReceiver(ClientToServerPackets.PLAY_RADIO, (client, handler, buf, responseSender) -> {
			BlockPos pos = buf.readBlockPos();
			boolean start = buf.readBoolean();

			client.execute(() -> {
				SoundEvent id = CornerRadioRegistry.getCurrent(client);
				if (client.world.getBlockState(pos).isOf(CornerBlocks.TUNED_RADIO)) {
					List<PaintingEntity> closestPaintings = client.world.getEntitiesByClass(PaintingEntity.class, Box.from(Vec3d.of(pos)).expand(16.0D), (entity) -> entity.motive instanceof DimensionalPaintingMotive).stream().sorted(Comparator.comparing((entity) -> entity.squaredDistanceTo(Vec3d.of(pos)))).toList();
					if (!closestPaintings.isEmpty()) {
						id = CornerRadioRegistry.getCurrent(((DimensionalPaintingMotive) closestPaintings.get(0).motive).radioRedirect);
					}
				}

				SoundSystemAccess.get(((SoundManagerAccessor) client.getSoundManager()).getSoundSystem()).stopSoundsAtPosition(pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5, null, SoundCategory.RECORDS);
				((MusicTrackerAccess) (client.getMusicTracker())).getRadioPositions().remove(pos);
				if (start) {
					((MusicTrackerAccess) (client.getMusicTracker())).getRadioPositions().add(pos);
					LoopingPositionedSoundInstance.play(client.world, pos, id, SoundCategory.RECORDS, 1.0F, 1.0F, pos.getX() + 0.5, pos.getY() + 1.0, pos.getZ() + 0.5);
				}
			});
		});
	}

}
