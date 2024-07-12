package dev.luminous.bettertp.asm;

import dev.luminous.bettertp.BetterTP;
import net.minecraftforge.fml.relauncher.IFMLLoadingPlugin;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.Map;

@IFMLLoadingPlugin.Name("BetterTP")
@IFMLLoadingPlugin.MCVersion("1.12.2")
public class MixinLoader implements IFMLLoadingPlugin {

    private static boolean isObfuscatedEnvironment;

    public MixinLoader() {
        BetterTP.LOGGER.info("Loading mixins...\n");
        MixinBootstrap.init();
        Mixins.addConfiguration("mixins.bettertp.json");
        MixinEnvironment.getDefaultEnvironment().setObfuscationContext("searge");
        BetterTP.LOGGER.info(MixinEnvironment.getDefaultEnvironment().getObfuscationContext());
    }

    public String[] getASMTransformerClass() {
        return new String[0];
    }

    public String getModContainerClass() {
        return null;
    }

    public String getSetupClass() {
        return null;
    }

    public void injectData(Map<String, Object> data) {
        isObfuscatedEnvironment = (Boolean) data.get("runtimeDeobfuscationEnabled");
    }

    public String getAccessTransformerClass() {
        return null;
    }
}

