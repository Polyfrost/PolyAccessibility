package cc.polyfrost.polyaccessibility.mixin;

import cc.polyfrost.polyaccessibility.narrator.PolyNarrator;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.text.Text;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public class ActionbarInject {
    @Inject(at = @At("HEAD"), method = "setOverlayMessage(Lnet/minecraft/text/Text;Z)V")
    public void speakActionbar(Text message, boolean tinted, CallbackInfo ci) {
        PolyNarrator.narrate(message.getString());
    }
}
