package net.pneumono.gdb;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.api.particle.v1.FabricParticleTypes;
import net.fabricmc.fabric.impl.content.registry.FlammableBlockRegistryImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.SimpleParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.VillagerTrades;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

//? if >=1.21.9 {
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import java.util.Optional;
//?}

public class GDBRegistry {
    public static final Block GOLDEN_DANDELION_BLOCK = registerBlock();
    public static final Block POTTED_GOLDEN_DANDELION = registerPotted();
    public static final Item GOLDEN_DANDELION_ITEM = registerItem();

    public static final SimpleParticleType USE_PARTICLE = registerParticle("pause_mob_growth");
    public static final SimpleParticleType UNUSE_PARTICLE = registerParticle("reset_mob_growth");

    public static final SoundEvent USE_SOUND = registerSound("item.gdb.golden_dandelion.use");
    public static final SoundEvent UNUSE_SOUND = registerSound("item.gdb.golden_dandelion.unuse");

    public static final TagKey<EntityType<?>> CANNOT_BE_AGE_LOCKED = TagKey.create(Registries.ENTITY_TYPE, GoldenDandelionBackport.id("cannot_be_age_locked"));

    @SuppressWarnings("UnstableApiUsage")
    public static final AttachmentType<AgeLockData> AGE_LOCK_DATA = registerAttachmentType();

    private static Block registerBlock() {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, GoldenDandelionBackport.id("golden_dandelion"));
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, new FlowerBlock(
                MobEffects.SATURATION, /*? if >=1.21 {*/0.35f/*?} else {*//*7*//*?}*/,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        /*? if >=1.21.9 {*/.noCollision()/*?} else {*//*.noCollission()*//*?}*/
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .offsetType(BlockBehaviour.OffsetType.XZ)
                        .pushReaction(PushReaction.DESTROY)
                        //? if >=1.21.9
                        .setId(blockKey)
        ));
    }

    private static Block registerPotted() {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, GoldenDandelionBackport.id("potted_golden_dandelion"));
        return Registry.register(BuiltInRegistries.BLOCK, blockKey,
                //? if >=1.21.9 {
                new FlowerPotBlock(GOLDEN_DANDELION_BLOCK, Blocks.flowerPotProperties().setId(blockKey))
                //?} else {
                /*Blocks.flowerPot(GOLDEN_DANDELION_BLOCK)
                *///?}
        );
    }

    private static Item registerItem() {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, GoldenDandelionBackport.id("golden_dandelion"));
        return Registry.register(BuiltInRegistries.ITEM, itemKey, new GoldenDandelionItem(
                GOLDEN_DANDELION_BLOCK,
                new Item.Properties()
                        //? if >=1.21.9
                        .useBlockDescriptionPrefix().setId(itemKey)
        ));
    }

    private static SimpleParticleType registerParticle(String name) {
        return Registry.register(BuiltInRegistries.PARTICLE_TYPE, GoldenDandelionBackport.id(name), FabricParticleTypes.simple());
    }

    private static SoundEvent registerSound(String name) {
        ResourceLocation useSoundId = GoldenDandelionBackport.id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, useSoundId,
                //? if >=1.21.9 {
                new SoundEvent(useSoundId, Optional.empty())
                 //?} else {
                /*SoundEvent.createVariableRangeEvent(useSoundId)
                *///?}
        );
    }

    @SuppressWarnings("UnstableApiUsage")
    private static AttachmentType<AgeLockData> registerAttachmentType() {
        ResourceLocation id = GoldenDandelionBackport.id("age_lock_data");
        //? if >=1.21.9 {
        return AttachmentRegistry.create(
                id,
                builder -> builder
                        .initializer(() -> AgeLockData.DEFAULT)
                        .persistent(AgeLockData.CODEC)
                        .syncWith(AgeLockData.STREAM_CODEC, AttachmentSyncPredicate.all())
                        .copyOnDeath()
        );
        //?} else {
        /*return AttachmentRegistry.<AgeLockData>builder()
                .initializer(() -> AgeLockData.DEFAULT)
                .persistent(AgeLockData.CODEC)
                .copyOnDeath().buildAndRegister(id);
        *///?}
    }

    protected static void register() {
        FlammableBlockRegistryImpl.getInstance(Blocks.FIRE).add(GOLDEN_DANDELION_BLOCK, 60, 100);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register(entries -> entries.addAfter(Items.BEETROOT, GOLDEN_DANDELION_ITEM));

        VillagerTrades.ItemsForEmeralds trade = new VillagerTrades.ItemsForEmeralds(new ItemStack(GOLDEN_DANDELION_ITEM), 2, 1, 12, 1, 0.05F);
        //? if >=1.21.9 {
        TradeOfferHelper.registerWanderingTraderOffers(builder -> builder.addOffersToPool(
                TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL,
                trade
        ));
        //?} else {
        /*TradeOfferHelper.registerWanderingTraderOffers(1, trades -> trades.add(trade));
        *///?}
    }
}
