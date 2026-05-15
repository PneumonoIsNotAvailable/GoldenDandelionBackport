package net.pneumono.gdb;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

//? if <1.21.9 {
/*import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
*///?}

public class GDBUtil {
    public static boolean canUseGoldenDandelion(ItemStack stack, boolean isBaby, int cooldown, Mob mob) {
        return cooldown == 0 && isAgeLockableContext(stack, isBaby, mob);
    }

    public static boolean isAgeLockableContext(ItemStack stack, boolean isBaby, Mob mob) {
        return stack.is(GDBRegistry.GOLDEN_DANDELION_ITEM) && isBaby && !mob.getType().is(GDBRegistry.CANNOT_BE_AGE_LOCKED);
    }

    public static void lockAge(Player player, ItemStack stack, Entity entity, AgeLockData data) {
        Level level = player.level();
        BlockPos pos = entity.getOnPos();

        boolean lockAge = !data.ageLocked();
        GDBUtil.setData(entity, new AgeLockData(lockAge, 40));

        //? if >=1.21 {
        stack.consume(1, player);
        //?} else {
        /*if (!player.isCreative()) {
            stack.shrink(1);
        }
        *///?}

        playSound(level, pos, lockAge);
    }

    public static void playSound(Level level, BlockPos pos, boolean lockingAge) {
        SoundEvent sound = lockingAge ? GDBRegistry.USE_SOUND : GDBRegistry.UNUSE_SOUND;
        level.playSound(null, pos, sound, SoundSource.PLAYERS, 1.0F, 1.0F);
    }

    public static void addParticle(Level level, Entity entity, boolean lockingAge) {
        float yParticleOffset = lockingAge ? 0.2F : 0.0F;
        Vec3 spawnPosition = new Vec3(
                entity.getRandomX(1.0),
                entity.getY((2.0 * level.getRandom().nextDouble() - 1.0) * 0.2) + entity.getBbHeight() + yParticleOffset,
                entity.getRandomZ(1.0)
        );
        level.addParticle(
                lockingAge ? GDBRegistry.USE_PARTICLE : GDBRegistry.UNUSE_PARTICLE, spawnPosition.x, spawnPosition.y, spawnPosition.z, 0.0, 0.0, 0.0
        );
    }

    public static boolean canAgeUp(AgeableMob mob) {
        if (!mob.isBaby()) return false;

        AgeLockData data = getData(mob);
        return data == null || !data.ageLocked();
    }

    public static boolean isAgeLocked(AgeableMob mob) {
        AgeLockData data = getData(mob);
        return data != null && data.ageLocked();
    }

    @SuppressWarnings("UnstableApiUsage")
    public static AgeLockData getData(Entity entity) {
        return entity.getAttached(GDBRegistry.AGE_LOCK_DATA);
    }

    public static AgeLockData getDataOrCreate(Entity entity) {
        AgeLockData data = getData(entity);
        return data == null ? AgeLockData.DEFAULT : data;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void setData(Entity entity, AgeLockData data) {
        entity.setAttached(GDBRegistry.AGE_LOCK_DATA, data);
        //? if <1.21.9 {
        /*if (!entity.level().isClientSide()) {
            AgeLockDataPacket packet = new AgeLockDataPacket(entity.getId(), data);
            for (ServerPlayer player : PlayerLookup.tracking(entity)) {
                //? if >=1.21 {
                ServerPlayNetworking.send(player, packet);
                //?} else {
                /^ServerPlayNetworking.send(player, AgeLockDataPacket.ID, packet.write());
                ^///?}
            }
        }
        *///?}
    }
}
