package net.ludocrypt.corners.util;

import java.util.function.BiFunction;

import net.ludocrypt.corners.entity.DimensionalPaintingEntity;
import net.minecraft.entity.decoration.painting.PaintingMotive;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.world.TeleportTarget;
import net.minecraft.world.World;

public class DimensionalPaintingMotive extends PaintingMotive {

	public final BiFunction<ServerPlayerEntity, DimensionalPaintingEntity, RegistryKey<World>> dimension;
	public final BiFunction<ServerPlayerEntity, DimensionalPaintingEntity, TeleportTarget> teleportTarget;

	public static final BiFunction<ServerPlayerEntity, DimensionalPaintingEntity, Vec3d> overworldPaintingTarget = (player, painting) -> {
		BlockPos pos = player.getSpawnPointPosition();
		if (pos != null) {
			ServerWorld serverWorld = player.getServer().getOverworld();
			return PlayerEntity.findRespawnPosition(serverWorld, pos, player.getSpawnAngle(), player.isSpawnPointSet(), true).orElse(Vec3d.ofCenter(player.getServer().getOverworld().getSpawnPos()));
		} else {
			return Vec3d.ofCenter(player.getServer().getOverworld().getSpawnPos());
		}
	};

	public DimensionalPaintingMotive(int width, int height, BiFunction<ServerPlayerEntity, DimensionalPaintingEntity, RegistryKey<World>> dimension, BiFunction<ServerPlayerEntity, DimensionalPaintingEntity, TeleportTarget> teleportTarget) {
		super(width, height);
		this.dimension = dimension;
		this.teleportTarget = teleportTarget;
	}

	public DimensionalPaintingMotive(int width, int height, RegistryKey<World> dimension, BiFunction<ServerPlayerEntity, DimensionalPaintingEntity, TeleportTarget> teleportTarget) {
		this(width, height, (player, painting) -> dimension, teleportTarget);
	}

	public DimensionalPaintingMotive(int width, int height, RegistryKey<World> dimension, TeleportTarget teleport) {
		this(width, height, (player, painting) -> dimension, (player, painting) -> teleport);
	}

	public static DimensionalPaintingMotive create(int width, int height, RegistryKey<World> dimension, BiFunction<ServerPlayerEntity, DimensionalPaintingEntity, Vec3d> teleportTarget) {
		return new DimensionalPaintingMotive(width, height, (player, painting) -> dimension, (player, painting) -> new TeleportTarget(teleportTarget.apply(player, painting), player.getVelocity(), player.getYaw(), player.getPitch()));
	}

	public static DimensionalPaintingMotive create(int width, int height, RegistryKey<World> dimension, Vec3d dest) {
		return new DimensionalPaintingMotive(width, height, (player, painting) -> dimension, (player, painting) -> new TeleportTarget(dest, player.getVelocity(), player.getYaw(), player.getPitch()));
	}

}
