package net.burkey.variouswitchesmod.datagen;

import net.burkey.variouswitchesmod.item.ModItems;
import net.burkey.variouswitchesmod.potion.ModPotions;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.PotionItem;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
//        ItemStack combustionPotion = new ItemStack(Items.POTION);
//        combustionPotion.getOrCreateTag().putString("Potion", "variouswitches.fiery_combustion");
//        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COMBUSTION_ARROW.get())
//                .pattern("AAA")
//                .pattern("APA")
//                .pattern("AAA")
//                .define('P', combustionPotion.getItem())
//                .define('A', Items.ARROW)
//                .unlockedBy(getHasName(combustionPotion.getItem()), has(combustionPotion.getItem()))
//                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD,ModItems.CRIMSON_TRUFFLE_STEW.get()).
                requires(Items.BOWL).
                requires(ModItems.CRIMSON_TRUFFLE.get(), 6).
                unlockedBy(getHasName(ModItems.CRIMSON_TRUFFLE.get()), has(ModItems.CRIMSON_TRUFFLE.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD,ModItems.COMBUSTION_ARROW.get()).
                requires(Items.ARROW).
                requires(ModItems.CRIMSON_TRUFFLE.get(), 1).
                requires(Items.FIRE_CHARGE, 1).
                unlockedBy(getHasName(ModItems.CRIMSON_TRUFFLE.get()), has(ModItems.CRIMSON_TRUFFLE.get()))
                .save(pWriter);


    }
}
