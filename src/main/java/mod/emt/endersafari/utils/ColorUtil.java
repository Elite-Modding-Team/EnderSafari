package mod.emt.endersafari.utils;

import java.awt.*;

public class ColorUtil {
    public static int[][] FAE = {
            {177, 255, 117},
            {255, 223, 163},
            {255, 163, 255},
            {219, 179, 255},
            {255, 98, 114},
            {255, 242, 179},
            {163, 221, 255}
    };

    public static int[][] RAINBOW(float hue) {
        int rgb = Color.HSBtoRGB(hue, 1.0F, 1.0F);
        return new int[][] {{(rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF}
        };
    }

    public static int[][] WITHER = {
            {40, 40, 40},
            {60, 60, 60},
            {0, 0, 0}
    };

    public static int[] colorTransition(int[][] colors, float progress) {
        if (colors.length == 0) {
            return new int[]{255, 255, 255};
        }

        if (colors.length == 1) {
            return colors[0];
        }

        progress = Math.max(0.0F, Math.min(1.0F, progress));
        float scaled = progress * (colors.length - 1);
        int index = (int) Math.floor(scaled);

        if (index >= colors.length - 1) {
            return colors[colors.length - 1];
        }

        float localProgress = scaled - index;
        int[] current = colors[index];
        int[] next = colors[index + 1];

        return new int[] {
                (int) (current[0] + (next[0] - current[0]) * localProgress),
                (int) (current[1] + (next[1] - current[1]) * localProgress),
                (int) (current[2] + (next[2] - current[2]) * localProgress)
        };
    }
}
