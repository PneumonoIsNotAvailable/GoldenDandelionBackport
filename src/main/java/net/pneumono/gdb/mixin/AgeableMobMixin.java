package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.PathfinderMob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.AgeLockData;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(AgeableMob.class)
public abstract class AgeableMobMixin extends PathfinderMob {
    @Shadow
    public abstract boolean isBaby();

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

    @Inject(
            method = "aiStep",
            at = @At("HEAD")
    )
    private void tickAgeLockCooldown(CallbackInfo ci) {
        AgeLockData data = GDBUtil.getData(this);
        if (data != null && data.ageLockCooldown() > 0) {
            GDBUtil.setData(this, new AgeLockData(data.ageLocked(), data.ageLockCooldown() - 1));
            if (level().isClientSide() && data.ageLockCooldown() % 2 == 0) {
                GDBUtil.addParticle(level(), this, data.ageLocked());
            }
        }
    }

    @Override
    protected InteractionResult mobInteract(Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        AgeLockData data = GDBUtil.getDataOrCreate(this);

        if (GDBUtil.canUseGoldenDandelion(stack, isBaby(), data.ageLockCooldown(), this)) {
            GDBUtil.lockAge(player, player.getItemInHand(hand), this, data);
            return InteractionResult.SUCCESS;
        }

        return super.mobInteract(player, hand);
    }
}
