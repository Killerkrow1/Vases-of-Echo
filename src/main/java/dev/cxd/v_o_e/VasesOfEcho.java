package dev.cxd.v_o_e;

import dev.cxd.v_o_e.init.VasesOfEchoInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabric_extras.structure_pool.api.StructurePoolAPI;
import net.fabric_extras.structure_pool.api.StructurePoolConfig;
import net.fabricmc.fabric.api.event.lifecycle.v1.ServerLifecycleEvents;
import net.minecraft.util.Identifier;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.util.ArrayList;
import java.util.List;

public class VasesOfEcho implements ModInitializer {
	public static final String MOD_ID = "v_o_e";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		VasesOfEchoInitializer.init();

		ServerLifecycleEvents.SERVER_STARTING.register(server -> {
			// Target structure pool
			var targetPool = new Identifier("minecraft:ancient_city/structures");
			// Our structure (found amongst data files)
			var structureId = new Identifier("v_o_e:ancient_city/structures");
			StructurePoolAPI.injectIntoStructurePool(
					server,
					targetPool, // Target structure pool
					structureId, // Our structure (found amongst data files)
					10 // Weight (higher numbers increase relative spawn chance)
			);
			StructurePoolAPI.limitSpawn(targetPool, structureId, 12);
		});

		// This config represents a fully serializable JSON structure
		var config = new StructurePoolConfig();
		// Now we are adding default entries, but the entire config could just be read from a JSON file
		config.entries = new ArrayList<>(List.of(
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/camp_1",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/camp_2",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/camp_3",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/chamber_1",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/chamber_2",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/chamber_3",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/large_ruin_1",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/small_statue",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/tall_ruin_1",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/tall_ruin_2",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/tall_ruin_3",
						10,
						2
				),
				new StructurePoolConfig.Entry(
						"minecraft:ancient_city/structures",
						"v_o_e:ancient_city/structures/tall_ruin_4",
						10,
						2
				)
		));
		// Inject all entries from the config
		StructurePoolAPI.injectAll(config);
	}
}