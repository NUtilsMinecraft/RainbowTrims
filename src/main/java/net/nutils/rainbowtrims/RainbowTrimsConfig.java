package net.nutils.rainbowtrims;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "rainbow_trims")
public class RainbowTrimsConfig implements ConfigData {

    public boolean showAnimation = true;
    public long animationSpeed = 7000;
    public boolean hideTrims = false;
    public boolean useCustomColor = false;
    public int customColorRed = 255;
    public int customColorGreen = 255;
    public int customColorBlue = 255;

}
