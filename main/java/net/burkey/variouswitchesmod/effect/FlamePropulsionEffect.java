package net.burkey.variouswitchesmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;

public class FlamePropulsionEffect extends MobEffect {
    protected FlamePropulsionEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void applyEffectTick(LivingEntity entity, int amplifier) {
        if (!entity.level().isClientSide()) {
            if (entity.onGround()) {
                double upwardForce = 1.2D ;

                entity.setDeltaMovement(
                        entity.getDeltaMovement().x,
                        upwardForce,
                        entity.getDeltaMovement().z
                );


                entity.hurtMarked = true;
            }
        }
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return duration % 2 == 0;
    }
}