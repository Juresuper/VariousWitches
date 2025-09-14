package net.burkey.variouswitchesmod.effect;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.gameevent.GameEvent;

public class BlazingTrailEffect extends MobEffect {
    protected BlazingTrailEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        Level level = pLivingEntity.level();

        BlockState blockstate = Blocks.FIRE.defaultBlockState();

        for(int i = 0; i < 4; ++i) {
            int j = Mth.floor(pLivingEntity.getX() + (double)((float)(i % 2 * 2 - 1) * 0.25F));
            int k = Mth.floor(pLivingEntity.getY());
            int l = Mth.floor(pLivingEntity.getZ() + (double)((float)(i / 2 % 2 * 2 - 1) * 0.25F));
            BlockPos firepos = new BlockPos(j, k, l);
            if (level.isEmptyBlock(firepos) && blockstate.canSurvive(level, firepos)) {
                level.setBlockAndUpdate(firepos, blockstate);
                level.gameEvent(GameEvent.BLOCK_PLACE, firepos, GameEvent.Context.of(pLivingEntity, blockstate));
            }
        }


        
        super.applyEffectTick(pLivingEntity, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;

    }
}
