package net.burkey.variouswitchesmod.effect;

import com.google.errorprone.annotations.Var;
import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModEffects {
    public static final DeferredRegister<MobEffect> MOB_EFFECTS = DeferredRegister.create(ForgeRegistries.MOB_EFFECTS, VariousWitchesMod.MODID);

    public static final RegistryObject<MobEffect> FIERY_COMBUSTION_EFFECT = MOB_EFFECTS.register("fiery_combustion", () -> new FieryCombustionEffect(MobEffectCategory.HARMFUL, 0xBD2C0B));
    public static final RegistryObject<MobEffect> BLAZING_TRAIL_EFFECT = MOB_EFFECTS.register("blazing_trail", () -> new BlazingTrailEffect(MobEffectCategory.NEUTRAL, 0xBD2C0B));
    public static final RegistryObject<MobEffect> FLAME_PROPULSION_EFFECT = MOB_EFFECTS.register("flame_propulsion", () -> new FlamePropulsionEffect(MobEffectCategory.NEUTRAL, 0xBD2C0B));
    public static final RegistryObject<MobEffect> METEORIC_FALL_EFFECT = MOB_EFFECTS.register("meteoric_fall", () -> new MeteoricFallEffect(MobEffectCategory.BENEFICIAL, 0xBD2C0B));
    public static final RegistryObject<MobEffect> WART_SPORES_EFFECT = MOB_EFFECTS.register("wart_spores", () -> new WartSporesEffect(MobEffectCategory.BENEFICIAL, 0xBD2C0B));

    public static void register(IEventBus eventBus){
        MOB_EFFECTS.register(eventBus);
    }

}
