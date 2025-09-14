package net.burkey.variouswitchesmod.datagen;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.block.ModBlocks;
import net.burkey.variouswitchesmod.block.custom.CrimsonTruffleCropBlock;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.CropBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ConfiguredModel;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

import java.util.function.Function;

public class ModBlockStateProvider extends BlockStateProvider {
    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, VariousWitchesMod.MODID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {
        makeCrimsonTruffleCrop((CropBlock) ModBlocks.CRIMSON_TRUFFLE_CROP.get(), "crimson_truffle_stage", "crimson_truffle_stage");

    }
    private void blockWithItem(RegistryObject<Block> blockRegistryObject) {
        simpleBlockWithItem(blockRegistryObject.get(), cubeAll(blockRegistryObject.get()));
    }
    public void makeCrimsonTruffleCrop(CropBlock block, String modelName, String textureName) {
        Function<BlockState, ConfiguredModel[]> function = state -> crimsonTruffleStates(state, block, modelName, textureName);

        getVariantBuilder(block).forAllStates(function);
    }

    private ConfiguredModel[] crimsonTruffleStates(BlockState state, CropBlock block, String modelName, String textureName) {
        ConfiguredModel[] models = new ConfiguredModel[1];
        models[0] = new ConfiguredModel(models().crop(modelName + state.getValue(((CrimsonTruffleCropBlock) block).getAgeProperty()),
                ResourceLocation.fromNamespaceAndPath(VariousWitchesMod.MODID, "block/" + textureName + state.getValue(((CrimsonTruffleCropBlock) block).getAgeProperty()))).renderType("cutout"));

        return models;
    }
}
