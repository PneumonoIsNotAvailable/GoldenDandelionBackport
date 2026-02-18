package net.pneumono.gdb;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public record AgeLockData(boolean ageLocked, int ageLockCooldown) {
    public static final Codec<AgeLockData> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.BOOL.fieldOf("age_locked").forGetter(AgeLockData::ageLocked),
            Codec.INT.fieldOf("age_lock_cooldown").forGetter(AgeLockData::ageLockCooldown)
    ).apply(instance, AgeLockData::new));

    public static final AgeLockData DEFAULT = new AgeLockData(false, 0);
}
