package net.ludocrypt.dynamicsf.init;

import net.fabricmc.fabric.api.event.registry.FabricRegistryBuilder;
import net.fabricmc.fabric.api.event.registry.RegistryAttribute;
import net.ludocrypt.corners.TheCorners;
import net.ludocrypt.dynamicsf.config.ReverbSettings;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.RegistryKey;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.World;

public class WorldReverbRegistry {

	public static final SimpleRegistry<ReverbSettings> REVERB_REGISTRY = FabricRegistryBuilder.createDefaulted(ReverbSettings.class, TheCorners.id("reverb_registry"), TheCorners.id("default_reverb")).attribute(RegistryAttribute.SYNCED).buildAndRegister();
	public static final ReverbSettings DEFAULT = new ReverbSettings(false);

	public static void init() {
		Registry.register(REVERB_REGISTRY, TheCorners.id("default_reverb"), DEFAULT);
	}

	public static ReverbSettings register(RegistryKey<World> world, ReverbSettings reverb) {
		return Registry.register(REVERB_REGISTRY, world.getValue(), reverb);
	}

	public static ReverbSettings getCurrent(MinecraftClient client) {
		return getCurrent(client.world.getRegistryKey());
	}

	public static ReverbSettings getCurrent(RegistryKey<World> key) {
		return REVERB_REGISTRY.getOrEmpty(key.getValue()).orElse(DEFAULT);
	}

}
