package atonkish.quartzelv.integration.modmenu;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;

import com.terraformersmc.modmenu.api.ConfigScreenFactory;
import com.terraformersmc.modmenu.api.ModMenuApi;
import me.shedaniel.autoconfig.AutoConfigClient;

import atonkish.quartzelv.QuartzElevatorConfig;

@Environment(EnvType.CLIENT)
public class ModMenu implements ModMenuApi {
  @Override
  public ConfigScreenFactory<?> getModConfigScreenFactory() {
    return parent -> AutoConfigClient.getConfigScreen(QuartzElevatorConfig.class, parent).get();
  }
}
