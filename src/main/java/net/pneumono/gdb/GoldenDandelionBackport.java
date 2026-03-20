package net.pneumono.gdb;

import net.fabricmc.api.ModInitializer;

import net.minecraft.resources.ResourceLocation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

//? if <1.21.9 && >=1.21
//import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;

public class GoldenDandelionBackport implements ModInitializer {
	public static final String MOD_ID = "gdb";

	public static final Logger LOGGER = LoggerFactory.getLogger("Golden Dandelion Backport");

	@Override
	public void onInitialize() {
		LOGGER.info("Initializing Golden Dandelion Backport");
		GDBRegistry.register();

		//? if <1.21.9 && >=1.21
		//PayloadTypeRegistry.playS2C().register(AgeLockDataPacket.TYPE, AgeLockDataPacket.CODEC);
	}

	public static ResourceLocation id(String path) {
		return ResourceLocation.tryBuild(MOD_ID, path);
	}
}