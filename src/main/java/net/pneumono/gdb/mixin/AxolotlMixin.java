package net.pneumono.gdb.mixin;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.AgeLockData;
import net.pneumono.gdb.GDBRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Axolotl.class)
public abstract class AxolotlMixin extends Animal {
    protected AxolotlMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Inject(
            method = "method_57305",
            at = @At("HEAD")
    )
    private void saveAgeLockDataToBucketTag(CompoundTag tag, CallbackInfo ci) {
        AgeLockData data = getAttached(GDBRegistry.AGE_LOCK_DATA);
        if (data != null) tag.store("age_lock_data", AgeLockData.CODEC, data);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Inject(
            method = "loadFromBucketTag",
            at = @At("HEAD")
    )
    private void loadAgeLockDataFromBucketTag(CompoundTag tag, CallbackInfo ci) {
        tag.read("age_lock_data", AgeLockData.CODEC).ifPresent(data -> setAttached(GDBRegistry.AGE_LOCK_DATA, data));
    }
}
