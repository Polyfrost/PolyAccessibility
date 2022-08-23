package cc.polyfrost.polyaccessibility.mixin;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.item.TooltipContext;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.text.Text;
import net.minecraft.util.Formatting;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;

@Mixin(value = ItemStack.class, priority = 0)
public class DurabilityInject {
    @Inject(at = @At("RETURN"), method = "getTooltip")
    private void getTooltipMixin(PlayerEntity player, TooltipContext context, CallbackInfoReturnable<List<Text>> cir) throws Exception {
        if (MinecraftClient.getInstance().world == null) return;
        List<Text> list = cir.getReturnValue();
        ItemStack itemStack = (ItemStack) ((Object) this);

        // TODO: implement config system
        if (true && true) {
            if (itemStack.getItem().isDamageable()) {
                int totalDurability = itemStack.getItem().getMaxDamage();
                int currentRemainingDurability = totalDurability - itemStack.getDamage();
                list.add(1, Text.of((I18n.translate("narrate.apextended.durability", currentRemainingDurability, totalDurability).formatted(Formatting.GREEN))));
            }
        }
    }
}
