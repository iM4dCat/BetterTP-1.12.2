package dev.luminous.bettertp;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(modid = BetterTP.MODID, name = BetterTP.MODNAME, version = BetterTP.MODVERISON)
public class BetterTP {

    public static final String MODID = "bettertp";
    public static final String MODNAME = "BetterTP";
    public static final String MODVERISON = "1.0";
    public static final Logger LOGGER = LogManager.getLogger("BetterTP");
    @Mod.Instance
    public static BetterTP INSTANCE;


    @Mod.EventHandler
    public void preInit(FMLPreInitializationEvent event) {

    }

    @Mod.EventHandler
    public void init(FMLInitializationEvent event) {
    }
}

