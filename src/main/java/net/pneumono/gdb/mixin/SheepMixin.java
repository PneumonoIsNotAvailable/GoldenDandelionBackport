package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

//? if >=1.21.9 {
import net.minecraft.world.entity.animal.sheep.Sheep;
//?} else {
/*import net.minecraft.world.entity.animal.Sheep;
*///?}

@Mixin(Sheep.class)
public abstract class SheepMixin extends Animal {
    protected SheepMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "ate",
            at = @At(
                    value = "INVOKE",
                    //? if >=1.21.9 {
                    target = "Lnet/minecraft/world/entity/animal/sheep/Sheep;isBaby()Z"
                    //?} else {
                    /*target = "Lnet/minecraft/world/entity/animal/Sheep;isBaby()Z"
                    *///?}
            )
    )
    private boolean checkCanAgeUp(boolean original) {
        return original && GDBUtil.canAgeUp(this);
    }
}
