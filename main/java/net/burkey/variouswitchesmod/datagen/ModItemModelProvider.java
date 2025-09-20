package net.burkey.variouswitchesmod.datagen;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.Item;
import net.minecraftforge.client.model.generators.ItemModelBuilder;
import net.minecraftforge.client.model.generators.ItemModelProvider;
import net.minecraftforge.common.data.ExistingFileHelper;
import net.minecraftforge.registries.RegistryObject;

public class ModItemModelProvider extends ItemModelProvider {
    public ModItemModelProvider(PackOutput output,  ExistingFileHelper existingFileHelper) {
        super(output, VariousWitchesMod.MODID, existingFileHelper);
    }

    @Override
    protected void registerModels() {
        simpleItem(ModItems.COMBUSTION_ARROW);
        simpleItem(ModItems.PIGLIN_WITCH_SHARD);
        simpleItem(ModItems.INCENDIARY_FLASK);
        simpleItem(ModItems.WATER_WITCH_SHARD);
        simpleItem(ModItems.CRIMSON_TRUFFLE_SEEDS);
        simpleItem(ModItems.CRIMSON_TRUFFLE);
        simpleItem(ModItems.CRIMSON_TRUFFLE_STEW);

        withExistingParent(ModItems.PIGLIN_WITCH_SPAWN_EGG.getId().getPath(),mcLoc("item/template_spawn_egg"));

    }
    private ItemModelBuilder simpleItem(RegistryObject<Item> item) {

        return withExistingParent(item.getId().getPath(),

                ResourceLocation.tryParse("item/generated")).texture("layer0",

                ResourceLocation.tryBuild(VariousWitchesMod.MODID, "item/" + item.getId().getPath()));
        }

    }
