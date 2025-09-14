package net.burkey.variouswitchesmod.item.custom;

import net.burkey.variouswitchesmod.entity.ModEntities;
import net.burkey.variouswitchesmod.entity.custom.CombustiveArrowEntity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.entity.projectile.Arrow;
import net.minecraft.world.item.ArrowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;

public class CombustionArrowItem extends ArrowItem {
    public CombustionArrowItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public @NotNull AbstractArrow createArrow(@NotNull Level pLevel, @NotNull ItemStack pStack, @NotNull LivingEntity pShooter) {
        CombustiveArrowEntity arrow = new CombustiveArrowEntity(ModEntities.COMBUSTIVE_ARROW_ENTITY.get(), pShooter,pLevel);
        return arrow;
    }

    @Override
    public boolean isInfinite(ItemStack stack, ItemStack bow, Player player) {
        return false;
    }
}
