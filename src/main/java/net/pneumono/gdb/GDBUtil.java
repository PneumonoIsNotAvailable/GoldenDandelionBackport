package net.pneumono.gdb;

import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.entity.Entity;

//? if <1.21.9 {
/*import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.server.level.ServerPlayer;
*///?}

public class GDBUtil {
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
