package net.pneumono.gdb;

import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.resources.ResourceLocation;

//? if >=1.21 {
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
//?} else {
/*import com.mojang.serialization.Codec;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.minecraft.network.FriendlyByteBuf;
*///?}

public record AgeLockDataPacket(int syncId, AgeLockData data) /*? if >=1.21 {*/implements CustomPacketPayload/*?}*/ {
    public static final ResourceLocation ID = GoldenDandelionBackport.id("age_lock_data");

    //? if >=1.21 {
    public static final CustomPacketPayload.Type<AgeLockDataPacket> TYPE = new Type<>(ID);

    public static final StreamCodec<RegistryFriendlyByteBuf, AgeLockDataPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.INT, AgeLockDataPacket::syncId,
            AgeLockData.STREAM_CODEC, AgeLockDataPacket::data,
            AgeLockDataPacket::new
    );

    @Override
    public Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
    //?} else {
    /*public static final Codec<AgeLockDataPacket> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            Codec.INT.fieldOf("sync_id").forGetter(AgeLockDataPacket::syncId),
            AgeLockData.CODEC.fieldOf("data").forGetter(AgeLockDataPacket::data)
    ).apply(instance, AgeLockDataPacket::new));

    public FriendlyByteBuf write() {
        FriendlyByteBuf buf = PacketByteBufs.create();
        buf.writeJsonWithCodec(CODEC, this);
        return buf;
    }

    public static AgeLockDataPacket read(FriendlyByteBuf buf) {
        return buf.readJsonWithCodec(CODEC);
    }
    *///?}
}
