package net.pneumono.gdb;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;

//? if >=1.21.9 {
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
//?} else {
/*import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;
*///?}

public class GoldenDandelionBackportClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		//? if >=1.21.9 {
		BlockRenderLayerMap.putBlock(GDBRegistry.GOLDEN_DANDELION_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GDBRegistry.POTTED_GOLDEN_DANDELION, ChunkSectionLayer.CUTOUT);
		//?} else {
		/*BlockRenderLayerMap.INSTANCE.putBlock(GDBRegistry.GOLDEN_DANDELION_BLOCK, RenderType.cutout());
		BlockRenderLayerMap.INSTANCE.putBlock(GDBRegistry.POTTED_GOLDEN_DANDELION, RenderType.cutout());
		*///?}

		ParticleFactoryRegistry.getInstance().register(GDBRegistry.USE_PARTICLE, GoldenDandelionParticle.PauseMobGrowthProvider::new);
		ParticleFactoryRegistry.getInstance().register(GDBRegistry.UNUSE_PARTICLE, GoldenDandelionParticle.ResetMobGrowthProvider::new);

		//? if <1.21 {
		/*ClientPlayNetworking.registerGlobalReceiver(AgeLockDataPacket.ID, (minecraft, listener, buf, sender) -> {
			receiveAgeLockDataPacket(minecraft.level, AgeLockDataPacket.read(buf));
		});
		*///?} else if <1.21.9 {
		/*ClientPlayNetworking.registerGlobalReceiver(AgeLockDataPacket.TYPE, (packet, context) -> receiveAgeLockDataPacket(context.client().level, packet));
		*///?}
	}

	//? if <1.21.9 {
	/*private void receiveAgeLockDataPacket(Level level, AgeLockDataPacket packet) {
		Entity entity = level.getEntity(packet.syncId());
		if (entity != null) {
			GDBUtil.setData(entity, packet.data());
		} else {
			GoldenDandelionBackport.LOGGER.warn("Received Age Lock Data for non-existent entity!");
		}
	}
	*///?}
}