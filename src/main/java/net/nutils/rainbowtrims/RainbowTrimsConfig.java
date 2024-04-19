package net.nutils.rainbowtrims;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "rainbow_trims")
public class RainbowTrimsConfig implements ConfigData {

    public boolean isEnabled = true;
    public boolean hideTrims = false;
    public long speed = 7000;

}
