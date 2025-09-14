package net.burkey.variouswitchesmod.datagen;

import net.burkey.variouswitchesmod.block.ModBlocks;
import net.burkey.variouswitchesmod.block.custom.CrimsonTruffleCropBlock;
import net.burkey.variouswitchesmod.item.ModItems;
import net.minecraft.advancements.critereon.StatePropertiesPredicate;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.registries.RegistryObject;

import java.util.Set;

public class ModBlockLootTables extends BlockLootSubProvider {
    protected ModBlockLootTables() {
        super(Set.of(), FeatureFlags.REGISTRY.allFlags());
    }


    @Override
    protected void generate() {
        LootItemCondition.Builder lootitemcondition$builder = LootItemBlockStatePropertyCondition
                .hasBlockStateProperties(ModBlocks.CRIMSON_TRUFFLE_CROP.get())
                .setProperties(StatePropertiesPredicate.Builder.properties().hasProperty(CrimsonTruffleCropBlock.AGE, 2));

        this.add(ModBlocks.CRIMSON_TRUFFLE_CROP.get(), createCropDrops(ModBlocks.CRIMSON_TRUFFLE_CROP.get(), ModItems.CRIMSON_TRUFFLE_SEEDS.get(),
                ModItems.CRIMSON_TRUFFLE.get(), lootitemcondition$builder));

    }
    @Override
    protected Iterable<Block> getKnownBlocks() {
        return ModBlocks.BLOCKS.getEntries().stream().map(RegistryObject::get)::iterator;
    }
}
