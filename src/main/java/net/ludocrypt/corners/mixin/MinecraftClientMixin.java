package net.ludocrypt.corners.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.ludocrypt.corners.init.CornerSoundEvents;
import net.ludocrypt.corners.init.CornerWorld;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.world.ClientWorld;
import net.minecraft.sound.MusicSound;

@Mixin(MinecraftClient.class)
public class MinecraftClientMixin {

	@Shadow
	public ClientPlayerEntity player;

	@Shadow
	public ClientWorld world;

	@Environment(EnvType.CLIENT)
	@Inject(method = "getMusicType", at = @At("HEAD"), cancellable = true)
	private void corners$getMusicType(CallbackInfoReturnable<MusicSound> ci) {
		if (this.player != null) {
			if (this.world.getRegistryKey() == CornerWorld.YEARNING_CANAL.worldWorldRegistryKey) {
				ci.setReturnValue(new MusicSound(CornerSoundEvents.MUSIC_YEARNING_CANAL, 3000, 8000, true));
			} else if (this.world.getRegistryKey() == CornerWorld.COMMUNAL_CORRIDORS.worldWorldRegistryKey) {
				ci.setReturnValue(new MusicSound(CornerSoundEvents.MUSIC_COMMUNAL_CORRIDORS, 3000, 8000, true));
			}
		}
	}

}
