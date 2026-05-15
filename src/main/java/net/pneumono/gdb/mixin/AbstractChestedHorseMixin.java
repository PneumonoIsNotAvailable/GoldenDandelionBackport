package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractChestedHorse.class)
public abstract class AbstractChestedHorseMixin extends AbstractHorse {
    protected AbstractChestedHorseMixin(EntityType<? extends AbstractHorse> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/horse/AbstractChestedHorse;isVehicle()Z"
            )
    )
    private boolean preventUnwantedFeed(boolean original, @Local(argsOnly = true) Player player, @Local(argsOnly = true) InteractionHand hand) {
        return original || GDBUtil.isAgeLockableContext(player.getItemInHand(hand), isBaby(), this);
    }
}
