package cc.polyfrost.polyaccessibility.narrator;

import java.util.Locale;

import com.mojang.text2speech.Narrator;
import com.sun.jna.Library;
import com.sun.jna.Native;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.util.math.BlockPos;

@Environment(EnvType.CLIENT)
public class PolyNarrator {
    // nar rat or nar rat es all
    private interface NVDA extends Library {
        public int nvdaController_speakText(char[] text);

        public int nvdaController_cancelSpeech();
    }

    public String lastText = "";
    public String prefixAmount = "";
    public Block lastBlock = null;
    public BlockPos lastBlockPos = null;
    private static PolyNarrator instance;
    public static String previousToolTip = "";
    private NVDA nvda;

    public PolyNarrator() {
        instance = this;
        String osName = System.getProperty("os.name").toLowerCase(Locale.ROOT);
        if (osName.contains("win")) {
            try {
                this.nvda = (NVDA) Native.load("nvdaControllerClient64", NVDA.class);
            } catch (java.lang.UnsatisfiedLinkError e64) {
                try {
                    this.nvda = (NVDA) Native.load("nvdaControllerClient32", NVDA.class);
                } catch (java.lang.UnsatisfiedLinkError ignored) {
                }
            }
        }
    }

    public static boolean isNVDALoaded() {
        return instance.nvda != null;
    }

    public static void narrate(String text) {
        if (instance.nvda != null) {
            instance.nvda.nvdaController_cancelSpeech();
            char[] ch = new char[text.length() + 1];
            for (int i = 0; i < text.length(); i++) {
                ch[i] = text.charAt(i);
            }

            instance.nvda.nvdaController_speakText(ch);
        } else {
            Narrator.getNarrator().say(text, true);
        }
    }

    public static PolyNarrator getInstance() {
        return instance;
    }
}
