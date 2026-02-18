package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.AgeableWaterCreature;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.dolphin.Dolphin;
import net.minecraft.world.entity.animal.panda.Panda;
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
                    target = "Lnet/minecraft/world/entity/animal/panda/Panda;isBaby()Z"
            )
    )
    private boolean checkCanAgeUp(boolean original) {
        return original && GDBUtil.canAgeUp(this);
    }
}
