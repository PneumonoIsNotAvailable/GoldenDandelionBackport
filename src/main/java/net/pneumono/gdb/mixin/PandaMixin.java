package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.Panda;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Panda.class)
public abstract class PandaMixin extends Animal {
    protected PandaMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/Panda;isBaby()Z"
            )
    )
    private boolean checkCanAgeUp(boolean original) {
        return original && GDBUtil.canAgeUp(this);
    }

    @ModifyReturnValue(
            method = "mobInteract",
            at = @At("TAIL")
    )
    private InteractionResult allowAgeLockingPanda(InteractionResult original, @Local(argsOnly = true) Player player, @Local(argsOnly = true) InteractionHand hand) {
        return GDBUtil.isAgeLockableContext(player.getItemInHand(hand), isBaby(), this) ? super.mobInteract(player, hand) : original;
    }
}
