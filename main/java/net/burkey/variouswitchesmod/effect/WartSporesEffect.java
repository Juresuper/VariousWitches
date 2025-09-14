package net.burkey.variouswitchesmod.effect;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;

public class WartSporesEffect extends MobEffect {
    protected WartSporesEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }

    @Override
    public void addAttributeModifiers(LivingEntity entity, AttributeMap pAttributeMap, int pAmplifier) {
        if (entity instanceof AbstractPiglin  piglin) {
            piglin.setImmuneToZombification(true);

        }else if(entity instanceof Hoglin hoglin){
            hoglin.setImmuneToZombification(true);

        }
        super.addAttributeModifiers(entity, pAttributeMap, pAmplifier);
    }

    @Override
    public void removeAttributeModifiers(LivingEntity entity, AttributeMap pAttributeMap, int pAmplifier) {
        if (entity instanceof AbstractPiglin piglin) {
            piglin.setImmuneToZombification(false);
        }
        super.removeAttributeModifiers(entity, pAttributeMap, pAmplifier);
    }

    @Override
    public boolean isDurationEffectTick(int duration, int amplifier) {
        return false;
    }
}