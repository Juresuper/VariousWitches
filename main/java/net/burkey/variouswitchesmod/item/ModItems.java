package net.burkey.variouswitchesmod.item;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.block.ModBlocks;
import net.burkey.variouswitchesmod.entity.ModEntities;
import net.burkey.variouswitchesmod.item.custom.CombustionArrowItem;
import net.burkey.variouswitchesmod.item.custom.IncendiaryFlaskItem;
import net.burkey.variouswitchesmod.item.custom.ModFoods;
import net.burkey.variouswitchesmod.item.custom.fire_witch_wand;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemNameBlockItem;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.common.ForgeSpawnEggItem;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, VariousWitchesMod.MODID);

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }

    public static final RegistryObject<Item> FIRE_WITCH_SHARD = ITEMS.register("fire_witch_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> WATER_WITCH_SHARD = ITEMS.register("water_witch_shard", () -> new Item(new Item.Properties()));
    public static final RegistryObject<Item> FIRE_WITCH_WAND = ITEMS.register("fire_witch_wand", () -> new fire_witch_wand(Tiers.NETHERITE,6,4.0F, new Item.Properties()));
    public static final RegistryObject<Item> FIRE_WITCH_SPAWN_EGG = ITEMS.register("fire_witch_spawn_egg", () -> new ForgeSpawnEggItem(ModEntities.FIRE_WITCH, 0x8a161f, 0xfcd720, new Item.Properties()));
    public static final RegistryObject<Item> INCENDIARY_FLASK = ITEMS.register("incendiary_flask", () -> new IncendiaryFlaskItem(new Item.Properties()));
    public static final RegistryObject<Item> COMBUSTION_ARROW = ITEMS.register("combustion_arrow", () -> new CombustionArrowItem(new Item.Properties()));
    public static final RegistryObject<Item> CRIMSON_TRUFFLE_SEEDS = ITEMS.register("crimson_truffle_seeds", () -> new ItemNameBlockItem(ModBlocks.CRIMSON_TRUFFLE_CROP.get(),new Item.Properties()));
    public static final RegistryObject<Item> CRIMSON_TRUFFLE = ITEMS.register("crimson_truffle", () -> new Item(new Item.Properties().food(ModFoods.CRIMSON_TRUFFLE)));
    public static final RegistryObject<Item> CRIMSON_TRUFFLE_STEW = ITEMS.register("crimson_truffle_stew", () -> new Item(new Item.Properties().food(ModFoods.CRIMSON_TRUFFLE_STEW)));



}
