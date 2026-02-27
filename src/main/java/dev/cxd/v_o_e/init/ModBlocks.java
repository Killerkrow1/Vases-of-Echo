package dev.cxd.v_o_e.init;

import dev.cxd.v_o_e.VasesOfEcho;
import dev.cxd.v_o_e.block.AncientVaseBlock;
import net.fabricmc.fabric.api.item.v1.FabricItemSettings;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemConvertible;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class ModBlocks {
    public static final Block BIG_ANCIENT_VASE;
    public static final Block TALL_ANCIENT_VASE;
    public static final Block SMALL_ANCIENT_VASE;

    public ModBlocks() {
    }

    private static Block registerBlock(String name, Block block) {
        registerBlockItem(name, block);
        return Registry.register(Registries.BLOCK, new Identifier(VasesOfEcho.MOD_ID, name), block);
    }

    private static Item registerBlockItem(String name, Block block) {
        return Registry.register(Registries.ITEM, new Identifier(VasesOfEcho.MOD_ID, name), new BlockItem(block, new FabricItemSettings()));
    }

    public static void init() {
        VasesOfEcho.LOGGER.info("Registering ModBlocks for " + VasesOfEcho.MOD_ID);
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.FUNCTIONAL).register((ItemGroupEvents.ModifyEntries) (entries) -> {
            entries.addAfter(net.minecraft.item.Items.DECORATED_POT, BIG_ANCIENT_VASE);
            entries.addAfter(BIG_ANCIENT_VASE, TALL_ANCIENT_VASE);
            entries.addAfter(TALL_ANCIENT_VASE, SMALL_ANCIENT_VASE);
        });
    }

    static {
        BIG_ANCIENT_VASE = registerBlock("big_vase", new AncientVaseBlock(FabricBlockSettings.copyOf(Blocks.DECORATED_POT).sounds(ModSounds.VASE_BLOCK_SOUNDS).pistonBehavior(PistonBehavior.BLOCK)));
        TALL_ANCIENT_VASE = registerBlock("tall_vase", new AncientVaseBlock(FabricBlockSettings.copyOf(Blocks.DECORATED_POT).sounds(ModSounds.VASE_BLOCK_SOUNDS).pistonBehavior(PistonBehavior.BLOCK)));
        SMALL_ANCIENT_VASE = registerBlock("small_vase", new AncientVaseBlock(FabricBlockSettings.copyOf(Blocks.DECORATED_POT).sounds(ModSounds.VASE_BLOCK_SOUNDS).pistonBehavior(PistonBehavior.BLOCK)));
    }
}
