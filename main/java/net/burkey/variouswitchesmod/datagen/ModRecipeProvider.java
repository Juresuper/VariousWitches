package net.burkey.variouswitchesmod.datagen;

import net.burkey.variouswitchesmod.item.ModItems;
import net.minecraft.data.PackOutput;
import net.minecraft.data.recipes.*;
import net.minecraft.world.item.Items;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;

import java.util.function.Consumer;

public class ModRecipeProvider extends RecipeProvider implements IConditionBuilder {
    public ModRecipeProvider(PackOutput pOutput) {
        super(pOutput);
    }

    @Override
    protected void buildRecipes(Consumer<FinishedRecipe> pWriter) {
        ShapedRecipeBuilder.shaped(RecipeCategory.MISC, ModItems.COMBUSTION_ARROW.get())
                .pattern("AAA")
                .pattern("APA")
                .pattern("AAA")
                .define('P', ModItems.FIRE_WITCH_SHARD.get())
                .define('A', Items.ARROW)
                .unlockedBy(getHasName(ModItems.COMBUSTION_ARROW.get()), has(ModItems.COMBUSTION_ARROW.get()))
                .save(pWriter);
        ShapelessRecipeBuilder.shapeless(RecipeCategory.FOOD,ModItems.CRIMSON_TRUFFLE_STEW.get()).
                requires(Items.BOWL).
                requires(ModItems.CRIMSON_TRUFFLE.get(), 6).
                unlockedBy(getHasName(ModItems.CRIMSON_TRUFFLE.get()), has(ModItems.CRIMSON_TRUFFLE.get()))
                .save(pWriter);

    }
}
