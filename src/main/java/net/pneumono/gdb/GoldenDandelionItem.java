package net.pneumono.gdb;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;

public class GoldenDandelionItem extends BlockItem {
    public GoldenDandelionItem(Block block, Properties properties) {
        super(block, properties);
    }

    public static boolean canUseGoldenDandelion(ItemStack stack, boolean isBaby, int cooldown, Mob mob) {
        return stack.getItem() instanceof GoldenDandelionItem && isBaby && cooldown == 0 && !mob.getType().is(GDBRegistry.CANNOT_BE_AGE_LOCKED);
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
}
