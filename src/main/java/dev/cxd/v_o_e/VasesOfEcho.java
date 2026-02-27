package dev.cxd.v_o_e;

import dev.cxd.v_o_e.init.VasesOfEchoInitializer;
import net.fabricmc.api.ModInitializer;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VasesOfEcho implements ModInitializer {
	public static final String MOD_ID = "v_o_e";

	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

	@Override
	public void onInitialize() {
		VasesOfEchoInitializer.init();
	}
}