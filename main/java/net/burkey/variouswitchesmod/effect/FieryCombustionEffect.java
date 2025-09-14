package net.burkey.variouswitchesmod.effect;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.EntityDimensions;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeMap;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;

import java.util.UUID;

public class FieryCombustionEffect extends MobEffect {
    public FieryCombustionEffect(MobEffectCategory pCategory, int pColor) {
        super(pCategory, pColor);
    }
    private int anxietyTimer = 600; //30 seconds, when the sounds get intensified
    private static final UUID SPEED_MODIFIER_UUID = UUID.fromString("a5b3c7d1-e8f2-4a6b-9c0d-3e7f5b9a1c2d");

    private float calculatePitch(int remainingTicks) {
        float maxPitch = 2.0F;
        float minPitch = 0.1F;
        int finalCountdown = anxietyTimer;

        float progress = Math.min(1.0F, 1.0F - (remainingTicks / (float) finalCountdown));
        return minPitch + (maxPitch - minPitch) * progress;
    }
    private float calculateSpeed(int remainingTicks) {
        float maxSpeed = 2.0F;
        float minSpeed = 0.1F;
        int finalCountdown = 200;  // Last 10 seconds

        float progress = Math.min(1.0F, 1.0F - (remainingTicks / (float) finalCountdown));

        return minSpeed + (maxSpeed - minSpeed) * progress;
    }


    private int calculateDynamicDelay(int ticksLeft) {
        int maxDelay = 40;
        int minDelay = 1;

        int maxDuration = anxietyTimer;

        float progress = Math.max(0, Math.min(1, (maxDuration - ticksLeft) / (float) maxDuration));

        // gets faster
        float curved = progress * progress;

        return (int) (maxDelay - (maxDelay - minDelay) * curved);
    }
    private float calculateSizeMultiplier(LivingEntity livingEntity){
        EntityDimensions size = livingEntity.getDimensions(livingEntity.getPose());
        float width = size.width;
        float height = size.height;

        return (float) ((width*height)*0.3);

    }



    @Override
    public void applyEffectTick(LivingEntity pLivingEntity, int pAmplifier) {
        double sizeMultiplier = calculateSizeMultiplier(pLivingEntity);
        Level level = pLivingEntity.level();

        boolean flag = net.minecraftforge.event.ForgeEventFactory.getMobGriefingEvent(pLivingEntity.level(),pLivingEntity);

        MobEffectInstance effect = pLivingEntity.getEffect(ModEffects.FIERY_COMBUSTION_EFFECT.get());
        CompoundTag tag = pLivingEntity.getPersistentData();
        int soundCooldown = tag.getInt("FieryCombustionCooldown");

        if(effect != null){
            int remainingDuration = effect.getDuration();
            int soundDelay = calculateDynamicDelay(remainingDuration);

            //movement speed
            AttributeInstance movementSpeed = pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED);

            float speedCooeficient = calculateSpeed(remainingDuration);
            if(movementSpeed != null){
                AttributeModifier currentModifier = movementSpeed.getModifier(SPEED_MODIFIER_UUID);
                if(currentModifier != null)
                    movementSpeed.removeModifier(SPEED_MODIFIER_UUID);

                if(remainingDuration <= 200){
                    AttributeModifier speedModifier = new AttributeModifier(SPEED_MODIFIER_UUID,"FieryCombustion_speed_modifier", speedCooeficient, AttributeModifier.Operation.MULTIPLY_TOTAL);
                    movementSpeed.addTransientModifier(speedModifier);
                }
            }



            if(soundCooldown <= 0){
                float pitch = calculatePitch(remainingDuration);
                level.playSound(null,pLivingEntity.getX(),pLivingEntity.getY(),pLivingEntity.getZ(), SoundEvents.FIRE_EXTINGUISH, SoundSource.NEUTRAL, 1.0f, pitch);
                if(remainingDuration <= 200){
                    for(int i = 0; i < 10; i++){
                        level.addParticle(ParticleTypes.FLAME, pLivingEntity.getX() + level.random.nextDouble() - 0.5, pLivingEntity.getY() +1.0D + (+ level.random.nextDouble() - 0.5), pLivingEntity.getZ() + (+ level.random.nextDouble() - 0.5), 0.0D, 0.05D, 0.0D);
                    }
                } else{
                    for(int i = 0; i < 10; i++){
                        level.addParticle(ParticleTypes.SMOKE, pLivingEntity.getX() + level.random.nextDouble() - 0.5, pLivingEntity.getY() +1.0D + (+ level.random.nextDouble() - 0.5), pLivingEntity.getZ() + (+ level.random.nextDouble() - 0.5), 0.0D, 0.05D, 0.0D);
                    }
                }

                tag.putInt("FieryCombustionCooldown", soundDelay);
            }
            else{
                tag.putInt("FieryCombustionCooldown", soundCooldown - 1);
            }
            if(remainingDuration <= 1){
                if(!pLivingEntity.level().isClientSide()){
                    pLivingEntity.removeEffect(ModEffects.FIERY_COMBUSTION_EFFECT.get());
                    Explosion explosion = level.explode(pLivingEntity, pLivingEntity.getX(), pLivingEntity.getY(0.0625D), pLivingEntity.getZ(), (float) (15.0f*sizeMultiplier),flag,  Level.ExplosionInteraction.MOB);
                    pLivingEntity.hurt(explosion.getDamageSource(), (float) (30.0f*sizeMultiplier));
                }



            }
        }

    }

    @Override
    public void removeAttributeModifiers(LivingEntity pLivingEntity, AttributeMap pAttributeMap, int pAmplifier) {

        super.removeAttributeModifiers(pLivingEntity, pAttributeMap, pAmplifier);

        AttributeInstance movementSpeed = pLivingEntity.getAttribute(Attributes.MOVEMENT_SPEED);
        if(movementSpeed != null && movementSpeed.getModifier(SPEED_MODIFIER_UUID) != null){
            movementSpeed.removeModifier(SPEED_MODIFIER_UUID);
        }

    }


    @Override
    public boolean isDurationEffectTick(int pDuration, int pAmplifier) {
        return true;
    }

}
