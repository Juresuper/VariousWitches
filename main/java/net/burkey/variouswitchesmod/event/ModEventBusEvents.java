package net.burkey.variouswitchesmod.event;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.entity.ModEntities;
import net.burkey.variouswitchesmod.entity.custom.FireWitchEntity;
import net.burkey.variouswitchesmod.network.PacketHandler;
import net.burkey.variouswitchesmod.potion.ModBrewingRecipes;
import net.minecraft.world.entity.SpawnPlacements;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.event.entity.SpawnPlacementRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;

@Mod.EventBusSubscriber(modid = VariousWitchesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModEventBusEvents {
    @SubscribeEvent
    public static void registerAttributes(EntityAttributeCreationEvent event) {
        event.put(ModEntities.FIRE_WITCH.get(), FireWitchEntity.createAttributes().build());
    }

    @SubscribeEvent
    public static void commonSetup(FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            PacketHandler.register();
            ModBrewingRecipes.registerBrewingRecipes();

        });
    }
    @SubscribeEvent
    public static void registerSpawnPlacements(SpawnPlacementRegisterEvent event){
        event.register(ModEntities.FIRE_WITCH.get(), SpawnPlacements.Type.ON_GROUND, Heightmap.Types.WORLD_SURFACE, AbstractPiglin::checkMobSpawnRules, SpawnPlacementRegisterEvent.Operation.REPLACE);

    }









    }
