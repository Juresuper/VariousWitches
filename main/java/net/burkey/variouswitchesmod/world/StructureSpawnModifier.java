package net.burkey.variouswitchesmod.world;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.core.Holder;
import net.minecraft.core.HolderSet;
import net.minecraft.core.RegistryCodecs;
import net.minecraft.core.registries.Registries;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.level.biome.MobSpawnSettings;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraftforge.common.world.ModifiableStructureInfo;
import net.minecraftforge.common.world.StructureModifier;

public record StructureSpawnModifier(
        HolderSet<Structure> structures,
        MobCategory category,
        MobSpawnSettings.SpawnerData spawn
) implements StructureModifier {

    public static final Codec<StructureSpawnModifier> CODEC = RecordCodecBuilder.create(instance -> instance.group(
            RegistryCodecs.homogeneousList(Registries.STRUCTURE, Structure.DIRECT_CODEC)
                    .fieldOf("structures").forGetter(StructureSpawnModifier::structures),
            MobCategory.CODEC.fieldOf("category").forGetter(StructureSpawnModifier::category),
            MobSpawnSettings.SpawnerData.CODEC.fieldOf("spawn").forGetter(StructureSpawnModifier::spawn)
    ).apply(instance, StructureSpawnModifier::new));

    @Override
    public void modify(Holder<Structure> structure, Phase phase, ModifiableStructureInfo.StructureInfo.Builder builder) {
        if (phase == Phase.ADD && structures.contains(structure)) {
            builder.getStructureSettings()
                    .getOrAddSpawnOverrides(category)
                    .addSpawn(spawn);
        }
    }

    @Override
    public Codec<? extends StructureModifier> codec() {
        return CODEC;
    }
}

