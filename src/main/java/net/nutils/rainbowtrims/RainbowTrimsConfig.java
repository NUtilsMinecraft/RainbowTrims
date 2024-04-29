package net.nutils.rainbowtrims;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "rainbow_trims")
public class RainbowTrimsConfig implements ConfigData {

    public boolean showAnimation = true;
    public long animationSpeed = 7000;
    public boolean hideTrims = false;
    public boolean useCustomColor = false;
    public float customColorRed = 1F;
    public float customColorGreen = 1F;
    public float customColorBlue = 1F;

}
