package atonkish.quartzelv.config;

import atonkish.quartzelv.QuartzElevatorMod;
import me.sargunvohra.mcmods.autoconfig1u.ConfigData;
import me.sargunvohra.mcmods.autoconfig1u.annotation.Config;

@Config(name = QuartzElevatorMod.MOD_ID)
public class QuartzElevatorConfig implements ConfigData {
    public int quartzElevatorDistance = 8;
}
