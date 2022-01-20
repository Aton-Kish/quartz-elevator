package atonkish.quartzelv.config;

import atonkish.quartzelv.QuartzElevatorMod;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = QuartzElevatorMod.MOD_ID)
public class QuartzElevatorConfig implements ConfigData {
    public int quartzElevatorDistance = 16;
    public int smoothQuartzElevatorDistance = 64;
    public boolean mixTypes = false;
    public boolean isPlayerOnly = false;
    public boolean displayParticles = true;
}
