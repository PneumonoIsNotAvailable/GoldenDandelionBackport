package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.sheep.Sheep;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Sheep.class)
public abstract class SheepMixin extends Animal {
    protected SheepMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "ate",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/sheep/Sheep;isBaby()Z"
            )
    )
    private boolean checkCanAgeUp(boolean original) {
        return original && GDBUtil.canAgeUp(this);
    }
}
