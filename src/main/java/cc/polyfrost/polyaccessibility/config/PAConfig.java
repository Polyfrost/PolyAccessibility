package cc.polyfrost.polyaccessibility.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

@Config(name = "polyaccessibility")
public class PAConfig implements ConfigData {
    public boolean readBlocksToggle = true;
    public boolean readTooltipsToggle = true;
    public boolean readSignContentsToggle = true;
    public boolean keyboardInventoryControlToggle = true;
}
