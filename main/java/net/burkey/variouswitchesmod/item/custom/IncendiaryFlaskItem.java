package net.burkey.variouswitchesmod.item.custom;

import net.burkey.variouswitchesmod.entity.custom.IncendiaryFlaskEntity;
import net.minecraft.stats.Stats;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;

public class IncendiaryFlaskItem extends Item {
    public IncendiaryFlaskItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
            ItemStack itemstack = pPlayer.getItemInHand(pUsedHand);
            if (!pLevel.isClientSide) {
                IncendiaryFlaskEntity thrownflask = new IncendiaryFlaskEntity(pLevel, pPlayer);
                thrownflask.setItem(itemstack);
                thrownflask.shootFromRotation(pPlayer, pPlayer.getXRot(), pPlayer.getYRot(), -20.0F, 0.5F, 1.0F);
                pLevel.addFreshEntity(thrownflask);
            }

            pPlayer.awardStat(Stats.ITEM_USED.get(this));
            if (!pPlayer.getAbilities().instabuild) {
                itemstack.shrink(1);
            }

            return InteractionResultHolder.sidedSuccess(itemstack, pLevel.isClientSide());
        }


}
