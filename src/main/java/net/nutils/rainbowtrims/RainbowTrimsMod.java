package net.nutils.rainbowtrims;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfig;
import me.shedaniel.autoconfig.serializer.GsonConfigSerializer;
import net.fabricmc.api.ClientModInitializer;

public class RainbowTrimsMod implements ClientModInitializer, ModMenuApi {

    @Override
    public void onInitializeClient() {
        AutoConfig.register(RainbowTrimsConfig.class, GsonConfigSerializer::new);
    }

    @Override
    public ConfigScreenFactory<?> getModConfigScreenFactory() {
        return parent -> AutoConfig.getConfigScreen(RainbowTrimsConfig.class, parent).get();
    }

}
