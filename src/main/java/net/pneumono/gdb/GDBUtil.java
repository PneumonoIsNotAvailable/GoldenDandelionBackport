package net.pneumono.gdb;

import net.minecraft.world.entity.AgeableMob;

public class GDBUtil {
    @SuppressWarnings("UnstableApiUsage")
    public static boolean canAgeUp(AgeableMob mob) {
        if (!mob.isBaby()) return false;

        AgeLockData data = mob.getAttached(GDBRegistry.AGE_LOCK_DATA);
        return data == null || !data.ageLocked();
    }

    @SuppressWarnings("UnstableApiUsage")
    public static boolean isAgeLocked(AgeableMob mob) {
        AgeLockData data = mob.getAttached(GDBRegistry.AGE_LOCK_DATA);
        return data != null && data.ageLocked();
    }
}
