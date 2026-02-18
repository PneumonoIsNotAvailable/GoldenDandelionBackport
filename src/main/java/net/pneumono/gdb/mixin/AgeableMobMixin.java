package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.AgeLockData;
import net.pneumono.gdb.GDBRegistry;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin extends PathfinderMob {
    protected AgeableMobMixin(EntityType<? extends PathfinderMob> entityType, Level level) {
        super(entityType, level);
    }

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

    @SuppressWarnings("UnstableApiUsage")
    @Inject(
            method = "aiStep",
            at = @At("HEAD")
    )
    private void tickAgeLockCooldown(CallbackInfo ci) {
        AgeLockData data = getAttached(GDBRegistry.AGE_LOCK_DATA);
        if (data != null && data.ageLockCooldown() > 0) {
            setAttached(GDBRegistry.AGE_LOCK_DATA, new AgeLockData(data.ageLocked(), data.ageLockCooldown() - 1));
        }
    }
}
