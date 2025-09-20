package net.burkey.variouswitchesmod;

import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import net.burkey.variouswitchesmod.block.ModBlocks;
import net.burkey.variouswitchesmod.config.VariousWitchesModClientConfigs;
import net.burkey.variouswitchesmod.config.VariousWitchesModCommonConfigs;
import net.burkey.variouswitchesmod.effect.ModEffects;
import net.burkey.variouswitchesmod.entity.ModEntities;
import net.burkey.variouswitchesmod.entity.client.CombustiveArrowRenderer;
import net.burkey.variouswitchesmod.entity.client.FireCloudRenderer;
import net.burkey.variouswitchesmod.entity.client.FireWitchRenderer;
import net.burkey.variouswitchesmod.entity.memorymodules.ModMemoryModules;
import net.burkey.variouswitchesmod.event.ModEvents;
import net.burkey.variouswitchesmod.item.ModCreativeModeTabs;
import net.burkey.variouswitchesmod.item.ModItems;
import net.burkey.variouswitchesmod.potion.ModPotions;
import net.minecraft.client.renderer.entity.EntityRenderers;
import net.minecraft.client.renderer.entity.ThrownItemRenderer;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.world.StructureModifier;
import net.minecraftforge.event.BuildCreativeModeTabContentsEvent;
import net.minecraftforge.event.server.ServerStartingEvent;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import org.slf4j.Logger;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(VariousWitchesMod.MODID)
public class VariousWitchesMod
{
    // Define mod id in a common place for everything to reference
    public static final String MODID = "variouswitches";
    // Directly reference a slf4j logger
    private static final Logger LOGGER = LogUtils.getLogger();

    private static final DeferredRegister<Codec<? extends StructureModifier>> STRUCTURE_MODIFIER_SERIALIZERS =
            DeferredRegister.create(ForgeRegistries.Keys.STRUCTURE_MODIFIER_SERIALIZERS, MODID);

//    public static final RegistryObject<Codec<? extends StructureModifier>> STRUCTURE_SPAWN_MODIFIER =
//            STRUCTURE_MODIFIER_SERIALIZERS.register("structure_spawn_modifier", () -> StructureSpawnModifier.CODEC);


    public VariousWitchesMod(FMLJavaModLoadingContext context)
    {
        IEventBus modEventBus = context.getModEventBus();
        //configs
        context.registerConfig(ModConfig.Type.CLIENT, VariousWitchesModClientConfigs.SPEC,"variouswitches-client.toml");
        context.registerConfig(ModConfig.Type.CLIENT, VariousWitchesModCommonConfigs.SPEC,"variouswitches-common.toml");

        ModItems.register(modEventBus);
        ModBlocks.register(modEventBus);
        ModEntities.register(modEventBus);
        ModEffects.register(modEventBus);
        ModCreativeModeTabs.register(modEventBus);



        ModPotions.register(modEventBus);
        ModMemoryModules.MEMORY_MODULE_TYPES.register(modEventBus);
        STRUCTURE_MODIFIER_SERIALIZERS.register(modEventBus);


        // Register the commonSetup method for modloading
        modEventBus.addListener(this::commonSetup);

        // Register ourselves for server and other game events we are interested in
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ModEvents());




        // Register the item to a creative tab
        modEventBus.addListener(this::addCreative);





    }

    private void commonSetup(final FMLCommonSetupEvent event)
    {

    }

    // Add the example block item to the building blocks tab
    private void addCreative(BuildCreativeModeTabContentsEvent event)
    {
        if(event.getTabKey() == CreativeModeTabs.INGREDIENTS){
            event.accept(ModItems.PIGLIN_WITCH_SHARD);
            event.accept(ModItems.WATER_WITCH_SHARD);
            event.accept(ModItems.COMBUSTION_ARROW);
        }
    }

    // You can use SubscribeEvent and let the Event Bus discover methods to call
    @SubscribeEvent
    public void onServerStarting(ServerStartingEvent event)
    {

    }

    // You can use EventBusSubscriber to automatically register all static methods in the class annotated with @SubscribeEvent
    @Mod.EventBusSubscriber(modid = MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
    public static class ClientModEvents
    {
        @SubscribeEvent
        public static void onClientSetup(FMLClientSetupEvent event)
        {
            EntityRenderers.register(ModEntities.PIGLIN_WITCH.get(), FireWitchRenderer::new);
            EntityRenderers.register(ModEntities.INCENDIARY_FLASK_ENTITY.get(), ThrownItemRenderer::new);
            EntityRenderers.register(ModEntities.FIRE_AOE_CLOUD.get(), FireCloudRenderer::new);
            EntityRenderers.register(ModEntities.COMBUSTIVE_ARROW_ENTITY.get(), CombustiveArrowRenderer::new);
        }
    }
}
