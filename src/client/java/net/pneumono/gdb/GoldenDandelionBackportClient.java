package net.pneumono.gdb;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class GoldenDandelionBackportClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putBlock(GDBRegistry.GOLDEN_DANDELION_BLOCK, ChunkSectionLayer.CUTOUT);
		BlockRenderLayerMap.putBlock(GDBRegistry.POTTED_GOLDEN_DANDELION, ChunkSectionLayer.CUTOUT);

		ParticleFactoryRegistry.getInstance().register(GDBRegistry.USE_PARTICLE, GoldenDandelionParticle.PauseMobGrowthProvider::new);
		ParticleFactoryRegistry.getInstance().register(GDBRegistry.UNUSE_PARTICLE, GoldenDandelionParticle.ResetMobGrowthProvider::new);
	}
}