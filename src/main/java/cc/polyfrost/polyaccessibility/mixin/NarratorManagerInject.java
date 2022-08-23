package cc.polyfrost.polyaccessibility.mixin;

import cc.polyfrost.polyaccessibility.pauliefill.PolyInject;
import cc.polyfrost.polyaccessibility.narrator.PolyNarrator;

import net.minecraft.client.util.NarratorManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@PolyInject(NarratorManager.class)
@Mixin(NarratorManager.class)
public class NarratorManagerInject {
    @Inject(at = @At("HEAD"), method = "narrate(Ljava/lang/String;)V", cancellable = true)
    public void sayWithNVDA(String message, CallbackInfo ci) {
        if (PolyNarrator.isNVDALoaded()) {
            PolyNarrator.narrate(message);
            ci.cancel();
        }
    }
}
