package net.pneumono.gdb;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.BlockRenderLayerMap;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;

public class GoldenDandelionBackportClient implements ClientModInitializer {
	@Override
	public void onInitializeClient() {
		BlockRenderLayerMap.putBlock(GDBRegistry.GOLDEN_DANDELION_BLOCK, ChunkSectionLayer.CUTOUT);
	}
}