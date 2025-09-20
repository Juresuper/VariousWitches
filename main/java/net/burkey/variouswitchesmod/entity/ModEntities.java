package net.burkey.variouswitchesmod.entity;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.entity.custom.CombustiveArrowEntity;
import net.burkey.variouswitchesmod.entity.custom.FireCloudEntity;
import net.burkey.variouswitchesmod.entity.custom.PiglinWitchEntity;
import net.burkey.variouswitchesmod.entity.custom.IncendiaryFlaskEntity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES =
            DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VariousWitchesMod.MODID);

    public static final RegistryObject<EntityType<PiglinWitchEntity>> PIGLIN_WITCH =
            ENTITY_TYPES.register("piglin_witch", () -> EntityType.Builder.of(PiglinWitchEntity::new, MobCategory.MONSTER).sized(0.5f, 2.0f).build("piglin_witch"));


    public static final RegistryObject<EntityType<IncendiaryFlaskEntity>> INCENDIARY_FLASK_ENTITY =
            ENTITY_TYPES.register("incendiary_flask", () -> EntityType.Builder.<IncendiaryFlaskEntity>of(IncendiaryFlaskEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build("incendiary_flask"));

    public static final RegistryObject<EntityType<CombustiveArrowEntity>> COMBUSTIVE_ARROW_ENTITY =
            ENTITY_TYPES.register("combustive_arrow", () -> EntityType.Builder.<CombustiveArrowEntity>of(CombustiveArrowEntity::new, MobCategory.MISC).sized(0.5f, 0.5f).build("combustive_arrow"));

    public static final RegistryObject<EntityType<FireCloudEntity>> FIRE_AOE_CLOUD =
            ENTITY_TYPES.register("fire_aoe_cloud", () ->
                    EntityType.Builder.<FireCloudEntity>of(FireCloudEntity::new, MobCategory.MISC)
                            .sized(1.0f, 0.1f) // Flat cloud
                            .clientTrackingRange(10)
                            .updateInterval(1)
                            .build("fire_aoe_cloud"));

    public static void register(IEventBus eventBus){
        ENTITY_TYPES.register(eventBus);
    }
}
