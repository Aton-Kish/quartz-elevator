package atonkish.quartzelv.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;

import atonkish.quartzelv.QuartzElevatorMod;

@Config(name = QuartzElevatorMod.MOD_ID)
public class QuartzElevatorConfig implements ConfigData {
    public int quartzElevatorDistance = 16;
    public int smoothQuartzElevatorDistance = 64;
    public boolean mixTypes = false;
    public boolean isPlayerOnly = false;
    public boolean displayParticles = true;
}
