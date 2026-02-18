package net.pneumono.gdb;

import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.trade.TradeOfferHelper;
import net.fabricmc.fabric.impl.content.registry.FlammableBlockRegistryImpl;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.tags.TagKey;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.villager.VillagerTrades;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.FlowerBlock;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.material.PushReaction;

import java.util.Optional;

public class GDBRegistry {
    public static final Block GOLDEN_DANDELION_BLOCK = registerBlock();
    public static final Item GOLDEN_DANDELION_ITEM = registerItem();

    public static final SoundEvent USE_SOUND = registerSound("item.gdb.golden_dandelion.use");
    public static final SoundEvent UNUSE_SOUND = registerSound("item.gdb.golden_dandelion.unuse");

    public static final TagKey<EntityType<?>> CANNOT_BE_AGE_LOCKED = TagKey.create(Registries.ENTITY_TYPE, GoldenDandelionBackport.id("cannot_be_age_locked"));

    @SuppressWarnings("UnstableApiUsage")
    public static final AttachmentType<AgeLockData> AGE_LOCK_DATA = AttachmentRegistry.create(
            GoldenDandelionBackport.id("age_lock_data"),
            builder -> builder
                    .initializer(() -> AgeLockData.DEFAULT)
                    .persistent(AgeLockData.CODEC)
                    .copyOnDeath()
    );

    private static Block registerBlock() {
        ResourceKey<Block> blockKey = ResourceKey.create(Registries.BLOCK, GoldenDandelionBackport.id("golden_dandelion"));
        return Registry.register(BuiltInRegistries.BLOCK, blockKey, new FlowerBlock(
                MobEffects.SATURATION, 0.35f,
                BlockBehaviour.Properties.of()
                        .mapColor(MapColor.PLANT)
                        .noCollision()
                        .instabreak()
                        .sound(SoundType.GRASS)
                        .offsetType(BlockBehaviour.OffsetType.XZ)
                        .pushReaction(PushReaction.DESTROY)
                        .setId(blockKey)
        ));
    }

    private static Item registerItem() {
        ResourceKey<Item> itemKey = ResourceKey.create(Registries.ITEM, GoldenDandelionBackport.id("golden_dandelion"));
        return Registry.register(BuiltInRegistries.ITEM, itemKey, new GoldenDandelionItem(
                GOLDEN_DANDELION_BLOCK,
                new Item.Properties()
                        .useBlockDescriptionPrefix()
                        .setId(itemKey)
        ));
    }

    private static SoundEvent registerSound(String name) {
        Identifier useSoundId = GoldenDandelionBackport.id(name);
        return Registry.register(BuiltInRegistries.SOUND_EVENT, useSoundId, new SoundEvent(useSoundId, Optional.empty()));
    }

    protected static void register() {
        FlammableBlockRegistryImpl.getInstance(Blocks.FIRE).add(GOLDEN_DANDELION_BLOCK, 60, 100);

        ItemGroupEvents.modifyEntriesEvent(CreativeModeTabs.FOOD_AND_DRINKS)
                .register(entries -> entries.addAfter(Items.BEETROOT, GOLDEN_DANDELION_ITEM));

        TradeOfferHelper.registerWanderingTraderOffers(builder -> builder.addOffersToPool(
                TradeOfferHelper.WanderingTraderOffersBuilder.SELL_COMMON_ITEMS_POOL,
                new VillagerTrades.ItemsForEmeralds(GOLDEN_DANDELION_ITEM, 2, 1, 12, 1, 0.05F)
        ));
    }
}
