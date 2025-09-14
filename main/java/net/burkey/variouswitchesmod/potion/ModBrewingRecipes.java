package net.burkey.variouswitchesmod.potion;

import net.burkey.variouswitchesmod.item.ModItems;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.common.brewing.BrewingRecipeRegistry;

public class ModBrewingRecipes {
    public static void registerBrewingRecipes() {

        //base fire witch pot
        BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), Potions.EMPTY)),
                Ingredient.of(ModItems.CRIMSON_TRUFFLE.get()),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.PORCINE_POTION.get())
        );

        //every pot for the firewitch

        BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.PORCINE_POTION.get())),
                Ingredient.of(Items.FIRE_CHARGE),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.FIERY_COMBUSTION_POTION.get())
        );
        BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.PORCINE_POTION.get())),
                Ingredient.of(Items.FIREWORK_STAR),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.FLAME_PROPULTION_POTION.get())
        );
        BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.PORCINE_POTION.get())),
                Ingredient.of(Items.MAGMA_CREAM),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.METEORIC_FALL_POTION.get())
        );
        BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.PORCINE_POTION.get())),
                Ingredient.of(Items.BLAZE_ROD),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.BLAZING_TRAIL_POTION.get())
        );
        BrewingRecipeRegistry.addRecipe(
                Ingredient.of(PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.PORCINE_POTION.get())),
                Ingredient.of(Items.NETHER_WART),
                PotionUtils.setPotion(new ItemStack(Items.POTION), ModPotions.WART_SPORES_POTION.get())
        );
    }
}
