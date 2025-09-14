package net.burkey.variouswitchesmod.potion;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.effect.ModEffects;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModPotions {
    public static final DeferredRegister<Potion> POTIONS = DeferredRegister.create(ForgeRegistries.POTIONS, VariousWitchesMod.MODID);

    public static final RegistryObject<Potion> FIERY_COMBUSTION_POTION = POTIONS.register("fiery_combustion_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.FIERY_COMBUSTION_EFFECT.get(), 600, 0)))  ;

    public static final RegistryObject<Potion> BLAZING_TRAIL_POTION = POTIONS.register("blazing_trail_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.BLAZING_TRAIL_EFFECT.get(), 600, 0)))  ;
    public static final RegistryObject<Potion> FLAME_PROPULTION_POTION = POTIONS.register("flame_propultion_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.FLAME_PROPULSION_EFFECT.get(), 30, 0)))  ;
    public static final RegistryObject<Potion> METEORIC_FALL_POTION = POTIONS.register("meteoric_fall_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.METEORIC_FALL_EFFECT.get(), 300, 0)))  ;
    public static final RegistryObject<Potion> WART_SPORES_POTION = POTIONS.register("wart_spores_potion",
            () -> new Potion(new MobEffectInstance(ModEffects.WART_SPORES_EFFECT.get(), 6000, 0)))  ;
    public static final RegistryObject<Potion> PORCINE_POTION = POTIONS.register("porcine_potion",
            Potion::new);
    public static void register(IEventBus eventBus){
        POTIONS.register(eventBus);
    }
}
