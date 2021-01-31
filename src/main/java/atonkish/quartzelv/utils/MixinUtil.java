package atonkish.quartzelv.utils;

import java.util.regex.Pattern;

import net.minecraft.block.Block;

public final class MixinUtil {
    public static String extractBlockKey(Block block) {
        String translationKey = block.getTranslationKey();
        String[] keyArr = translationKey.split(Pattern.quote("."));
        return keyArr[keyArr.length - 1];
    }

    public static boolean isSmooth(String blockKey) {
        return blockKey.startsWith("smooth");
    }
}
