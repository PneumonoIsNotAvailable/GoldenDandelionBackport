package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.AgeableMob;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin {
    @ModifyExpressionValue(
            method = "aiStep",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/AgeableMob;isAlive()Z"
            )
    )
    private boolean preventAging(boolean original) {
        // IntelliJ says this will always be false but lowkey I don't believe it
        return original && !GDBUtil.isAgeLocked((AgeableMob)(Object)this);
    }
}
