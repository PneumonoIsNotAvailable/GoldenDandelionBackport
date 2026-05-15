package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.animal.horse.Horse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.GDBRegistry;
import net.pneumono.gdb.GoldenDandelionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Horse.class)
public abstract class HorseMixin extends AbstractHorse {
    protected HorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/horse/Horse;isVehicle()Z"
            )
    )
    private boolean preventUnwantedFeed(boolean original, @Local(argsOnly = true) Player player, @Local(argsOnly = true) InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        return original || (stack.getItem() instanceof GoldenDandelionItem && isBaby() && !getType().is(GDBRegistry.CANNOT_BE_AGE_LOCKED));
    }
}
