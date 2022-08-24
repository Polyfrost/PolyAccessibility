package cc.polyfrost.polyaccessibility.mixin;

import cc.polyfrost.polyaccessibility.PolyAccessibility;
import cc.polyfrost.polyaccessibility.keyboard.KeyboardController;
import cc.polyfrost.polyaccessibility.narrator.PolyNarrator;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import net.minecraft.text.Text;

import java.util.List;

@Mixin(Screen.class)
public class RenderTooltipInject {
    @Inject(at = @At("HEAD"), method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Lnet/minecraft/text/Text;II)V")
    private void readOneLineTooltips(MatrixStack matrices, Text text, int x, int y, CallbackInfo callback) {
        if (!PolyAccessibility.config.readTooltipsToggle || KeyboardController.hasControlOverMouse()) {
            return;
        }
        String nextText = text.getString();
        if (!PolyNarrator.getInstance().lastText.equals(nextText)) {
            PolyNarrator.getInstance().lastText = nextText;
            PolyNarrator.narrate(nextText);
        }
    }

    @Inject(at = @At("HEAD"), method = "renderTooltip(Lnet/minecraft/client/util/math/MatrixStack;Ljava/util/List;II)V")
    private void renderTooltip(MatrixStack matrices, List<Text> lines, int x, int y, CallbackInfo callback) {
        if (!PolyAccessibility.config.readTooltipsToggle || KeyboardController.hasControlOverMouse()) {
            return;
        }
        if (lines.size() > 0) {
            String nextText = PolyNarrator.getInstance().prefixAmount;
            for (int i = 0; i < lines.size(); i++) {
                nextText += lines.get(i).getString() + ", ";
            }
            if (!PolyNarrator.getInstance().lastText.equals(nextText)) {
                PolyNarrator.getInstance().lastText = nextText;
                PolyNarrator.narrate(nextText);
            }
        }
    }
}
