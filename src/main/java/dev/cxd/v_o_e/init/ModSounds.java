package dev.cxd.v_o_e.init;

import dev.cxd.v_o_e.VasesOfEcho;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Identifier;

public class ModSounds {

    public static final SoundEvent SOUND_BLOCK_BREAK = registerSoundEvent("vase_block_break");
    public static final SoundEvent SOUND_BLOCK_STEP = registerSoundEvent("vase_block_step");
    public static final BlockSoundGroup VASE_BLOCK_SOUNDS = new BlockSoundGroup(1.0F, 1.0F, SOUND_BLOCK_BREAK, SOUND_BLOCK_STEP, SoundEvents.BLOCK_METAL_PLACE, SoundEvents.BLOCK_METAL_HIT, SoundEvents.BLOCK_METAL_FALL);;

    private static SoundEvent registerSoundEvent(String name) {
        Identifier id = Identifier.of(VasesOfEcho.MOD_ID, name);
        return Registry.register(Registries.SOUND_EVENT, id, SoundEvent.of(id));
    }

    public static void init() {
        VasesOfEcho.LOGGER.info("Initializing Sounds for " + VasesOfEcho.MOD_ID);
    }

}
