package net.burkey.variouswitchesmod.entity.memorymodules;

import net.burkey.variouswitchesmod.VariousWitchesMod;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.Optional;

import static net.tslat.smartbrainlib.SBLForge.MEMORY_TYPES;

public class ModMemoryModules {
    public static final DeferredRegister<MemoryModuleType<?>> MEMORY_MODULE_TYPES =
            DeferredRegister.create(ForgeRegistries.MEMORY_MODULE_TYPES, VariousWitchesMod.MODID);

    public static final RegistryObject<MemoryModuleType<Boolean>> LAST_RESORT_POTION_ACTIVE =
            MEMORY_MODULE_TYPES.register("last_resort_potion_active", () -> new MemoryModuleType<>(Optional.empty()));

    public static final RegistryObject<MemoryModuleType<LivingEntity>> SUPPORT_TARGET =
            MEMORY_MODULE_TYPES.register("support_target",
                    () -> new MemoryModuleType<>(Optional.empty()));

}
