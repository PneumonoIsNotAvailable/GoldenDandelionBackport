package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.GDBUtil;
import net.pneumono.gdb.GoldenDandelionItem;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Camel.class)
public abstract class CamelMixin extends AbstractHorse {
    protected CamelMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "handleEating",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/camel/Camel;isBaby()Z"
            )
    )
    private boolean checkCanAgeUp(boolean original) {
        return original && GDBUtil.canAgeUp(this);
    }

    @ModifyReturnValue(
            method = "mobInteract",
            at = @At("TAIL")
    )
    private InteractionResult allowAgeLockingCamel(InteractionResult original, @Local(argsOnly = true) Player player, @Local(argsOnly = true) InteractionHand hand) {
        return isBaby() && player.getItemInHand(hand).getItem() instanceof GoldenDandelionItem ? super.mobInteract(player, hand) : original;
    }
}
