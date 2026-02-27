package dev.cxd.v_o_e;

import dev.cxd.v_o_e.init.ModBlocks;
import dev.cxd.v_o_e.init.ModParticles;
import dev.cxd.v_o_e.particle.EchoDirectionalLockedParticle;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.blockrenderlayer.v1.BlockRenderLayerMap;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.render.RenderLayer;

public class VasesOfEchoClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        ParticleFactoryRegistry.getInstance().register(ModParticles.ECHO, EchoDirectionalLockedParticle.Factory::new);
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.BIG_ANCIENT_VASE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.TALL_ANCIENT_VASE, RenderLayer.getCutout());
        BlockRenderLayerMap.INSTANCE.putBlock(ModBlocks.SMALL_ANCIENT_VASE, RenderLayer.getCutout());
    }
}
