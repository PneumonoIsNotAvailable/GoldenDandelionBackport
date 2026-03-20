package net.pneumono.gdb.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import com.mojang.datafixers.util.Pair;
import com.mojang.serialization.DataResult;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.axolotl.Axolotl;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.pneumono.gdb.AgeLockData;
import net.pneumono.gdb.GDBUtil;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(Axolotl.class)
public abstract class AxolotlMixin extends Animal {
    protected AxolotlMixin(EntityType<? extends Animal> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(
            //? if >=1.21 {
            method = "method_57305",
            at = @At("HEAD")
            //?} else {
            /*method = "saveToBucketTag",
            at = @At("RETURN")
            *///?}
    )
    private void saveAgeLockDataToBucketTag(
            //? if >=1.21 {
            CompoundTag tag, CallbackInfo ci
            //?} else {
            /*ItemStack itemStack, CallbackInfo ci, @Local CompoundTag tag
            *///?}
    ) {
        AgeLockData data = GDBUtil.getData(this);
        if (data != null) {
            //? if >=1.21.9 {
            tag.store("age_lock_data", AgeLockData.CODEC, data);
            //?} else if >=1.21 {
            /*DataResult<Tag> result = AgeLockData.CODEC.encodeStart(NbtOps.INSTANCE, data);
            result.ifSuccess(dataTag -> tag.put("age_lock_data", dataTag));
            *///?} else {
            /*DataResult<Tag> result = AgeLockData.CODEC.encodeStart(NbtOps.INSTANCE, data);
            result.result().ifPresent(dataTag -> tag.put("age_lock_data", dataTag));
            *///?}
        }
    }

    @Inject(
            method = "loadFromBucketTag",
            at = @At("HEAD")
    )
    private void loadAgeLockDataFromBucketTag(CompoundTag tag, CallbackInfo ci) {
        //? if >=1.21.9 {
        Optional<AgeLockData> optional = tag.read("age_lock_data", AgeLockData.CODEC);
        //?} else if >=1.21 {
        /*Optional<AgeLockData> optional;
        try {
            CompoundTag dataTag = tag.getCompound("age_lock_data");
            AgeLockData data = AgeLockData.CODEC.decode(NbtOps.INSTANCE, dataTag).getOrThrow().getFirst();
            optional = Optional.of(data);
        } catch (IllegalStateException e) {
            optional = Optional.empty();
        }
        *///?} else {
        /*Optional<AgeLockData> optional;
        try {
            CompoundTag dataTag = tag.getCompound("age_lock_data");
            optional = AgeLockData.CODEC.decode(NbtOps.INSTANCE, dataTag).result().map(Pair::getFirst);
        } catch (IllegalStateException e) {
            optional = Optional.empty();
        }
        *///?}

        optional.ifPresent(data -> GDBUtil.setData(this, data));
    }
}
