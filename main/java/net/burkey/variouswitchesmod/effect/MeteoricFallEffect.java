package net.burkey.variouswitchesmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class MeteoricFallEffect extends MobEffect {
    protected MeteoricFallEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {

    }
    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return true;
    }




}
