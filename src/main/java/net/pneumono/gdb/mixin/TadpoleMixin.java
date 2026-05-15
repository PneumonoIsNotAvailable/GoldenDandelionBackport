package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.AbstractFish;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.AgeLockData;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Tadpole.class)
public abstract class TadpoleMixin extends AbstractFish {
    public TadpoleMixin(EntityType<? extends AbstractFish> entityType, Level level) {
        super(entityType, level);
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

    @WrapOperation(
            method = "mobInteract",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/animal/Bucketable;bucketMobPickup(Lnet/minecraft/world/entity/player/Player;Lnet/minecraft/world/InteractionHand;Lnet/minecraft/world/entity/LivingEntity;)Ljava/util/Optional;"
            )
    )
    private Optional<InteractionResult> ageLockTadpole(Player player, InteractionHand hand, LivingEntity livingEntity, Operation<Optional<InteractionResult>> original) {
        ItemStack stack = player.getItemInHand(hand);
        AgeLockData data = GDBUtil.getDataOrCreate(this);

        if (GDBUtil.canUseGoldenDandelion(stack, true, data.ageLockCooldown(), this)) {
            GDBUtil.lockAge(player, player.getItemInHand(hand), this, data);
            return Optional.of(InteractionResult.SUCCESS);
        } else {
            return original.call(player, hand, livingEntity);
        }
    }
}
