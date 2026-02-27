package dev.cxd.v_o_e.init;

import dev.cxd.v_o_e.VasesOfEcho;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.minecraft.particle.DefaultParticleType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModParticles {

    public static final DefaultParticleType ECHO = FabricParticleTypes.simple();

    public static void init() {
        Registry.register(Registries.PARTICLE_TYPE, Identifier.of(VasesOfEcho.MOD_ID, "echo"), ECHO);
    }
}