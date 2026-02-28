package net.pneumono.gdb;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.frog.Tadpole;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import org.jspecify.annotations.NonNull;

public class GoldenDandelionItem extends BlockItem {
    public GoldenDandelionItem(Block block, Properties properties) {
        super(block, properties);
    }

    @SuppressWarnings("UnstableApiUsage")
    @Override
    public @NonNull InteractionResult interactLivingEntity(
            @NonNull ItemStack stack,
            @NonNull Player player,
            @NonNull LivingEntity livingEntity,
            @NonNull InteractionHand hand
    ) {
        if (livingEntity.getType().is(GDBRegistry.CANNOT_BE_AGE_LOCKED)) return InteractionResult.PASS;

        if (livingEntity instanceof AgeableMob ageableMob) {
            AgeLockData data = ageableMob.getAttachedOrCreate(GDBRegistry.AGE_LOCK_DATA);

            if (ageableMob.isBaby() && data.ageLockCooldown() == 0) {
                lockAge(player, stack, ageableMob, data);
                return InteractionResult.SUCCESS;
            }

        } else if (livingEntity instanceof Tadpole tadpole) {
            AgeLockData data = tadpole.getAttachedOrCreate(GDBRegistry.AGE_LOCK_DATA);

            if (data.ageLockCooldown() == 0) {
                lockAge(player, stack, tadpole, data);
                return InteractionResult.SUCCESS;
            }
        }

        return InteractionResult.PASS;
    }

    @SuppressWarnings("UnstableApiUsage")
    public static void lockAge(Player player, ItemStack stack, Entity entity, AgeLockData data) {
        Level level = player.level();
        BlockPos pos = entity.getOnPos();

        boolean lockAge = !data.ageLocked();
        entity.setAttached(GDBRegistry.AGE_LOCK_DATA, new AgeLockData(lockAge, 40));
        stack.consume(1, player);
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
                entity.getY((2.0 * entity.getRandom().nextDouble() - 1.0) * 0.2) + entity.getBbHeight() + yParticleOffset,
                entity.getRandomZ(1.0)
        );
        level.addParticle(
                lockingAge ? GDBRegistry.USE_PARTICLE : GDBRegistry.UNUSE_PARTICLE, spawnPosition.x, spawnPosition.y, spawnPosition.z, 0.0, 0.0, 0.0
        );
    }
}
