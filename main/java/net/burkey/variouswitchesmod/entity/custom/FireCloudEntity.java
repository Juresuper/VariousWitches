package net.burkey.variouswitchesmod.entity.custom;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;

import java.util.List;

public class FireCloudEntity extends Entity {
    private int lifetime = 60; // 3 seconds (60 ticks)
    private double range = 1.5D;

    public FireCloudEntity(EntityType<?> type, Level level) {
        super(type, level);
        this.noPhysics = true;
    }

    @Override
    public void tick() {
        super.tick();

        if (level().isClientSide) {
                double dx = (random.nextDouble() - 0.5) * 2;
                double dz = (random.nextDouble() - 0.5) * 2;
                double px = getX() + dx;
                double pz = getZ() + dz;
                double py = getY();

                level().addParticle(ParticleTypes.FLAME, px, py, pz, 0, 0.01, 0);
                level().addParticle(ParticleTypes.SMOKE, px, py + 0.1, pz, 0, 0.01, 0);

        }
        if (!level().isClientSide && this.tickCount % 10 == 0) {
            List<LivingEntity> targets = level().getEntitiesOfClass(LivingEntity.class, this.getBoundingBox().inflate(range), e -> e.isAlive());

            for (LivingEntity target : targets) {
                    if(!target.hasEffect(MobEffects.FIRE_RESISTANCE)) {
                        target.setSecondsOnFire(3);
                        target.hurt(level().damageSources().magic(), 1.0f);
                    }
            }
        }


        lifetime--;
        if (lifetime <= 0) {
            discard();
        }
    }

    @Override
    protected void defineSynchedData() {}

    @Override
    protected void readAdditionalSaveData(CompoundTag pCompound) {}

    @Override
    protected void addAdditionalSaveData(CompoundTag pCompound) {}
}

