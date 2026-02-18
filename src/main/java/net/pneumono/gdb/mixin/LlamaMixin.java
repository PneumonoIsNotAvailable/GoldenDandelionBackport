package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.camel.Camel;
import net.minecraft.world.entity.animal.equine.AbstractChestedHorse;
import net.minecraft.world.entity.animal.equine.AbstractHorse;
import net.minecraft.world.entity.animal.equine.Llama;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(Llama.class)
public abstract class LlamaMixin extends AbstractChestedHorse {
    protected LlamaMixin(EntityType<? extends AbstractChestedHorse> entityType, Level level) {
        super(entityType, level);
    }

    @ModifyExpressionValue(
            method = "handleEating",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/equine/Llama;isBaby()Z"
            )
    )
    private boolean checkCanAgeUp(boolean original) {
        return original && GDBUtil.canAgeUp(this);
    }
}
