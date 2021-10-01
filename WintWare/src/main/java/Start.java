/*
 * Decompiled with CFR 0.150.
 */
import java.util.Arrays;
import net.minecraft.client.main.Main;

public class Start {
    public static <T> T[] da(T[] first, T[] second) {
        T[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static void main(String[] args) {
        Main.main(Start.da(new String[]{"--version", "mcp", "--accessToken", "0", "--assetsDir", "assets", "--assetIndex", "1.12.2", "--userProperties", "{}"}, args));
    }
}

