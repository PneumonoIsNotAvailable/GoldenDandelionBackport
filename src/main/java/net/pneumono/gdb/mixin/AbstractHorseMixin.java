package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AbstractHorse.class)
public abstract class AbstractHorseMixin extends Animal {
    protected AbstractHorseMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "handleEating",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/horse/AbstractHorse;isBaby()Z"
            )
    )
    private boolean checkCanAgeUp(boolean original) {
        return original && GDBUtil.canAgeUp(this);
    }
}
