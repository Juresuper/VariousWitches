package net.burkey.variouswitchesmod.entity.custom;

import net.burkey.variouswitchesmod.entity.ModEntities;
import net.burkey.variouswitchesmod.item.ModItems;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.ThrowableItemProjectile;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;

public class IncendiaryFlaskEntity extends ThrowableItemProjectile {
    public IncendiaryFlaskEntity(EntityType<? extends ThrowableItemProjectile> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    public IncendiaryFlaskEntity(Level pLevel) {
        super(ModEntities.INCENDIARY_FLASK_ENTITY.get(), pLevel);
    }
    public IncendiaryFlaskEntity(Level pLevel, LivingEntity livingEntity) {
        super(ModEntities.INCENDIARY_FLASK_ENTITY.get(), livingEntity, pLevel);
    }


    @Override
    protected Item getDefaultItem() {
        return ModItems.INCENDIARY_FLASK.get();
    }





    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        if (!this.level().isClientSide) {
            Entity entity = pResult.getEntity();
            Entity entity1 = this.getOwner();
            DamageSource fireSource = level().damageSources().thrown(entity1, this);
            int i = entity.getRemainingFireTicks();
            entity.setSecondsOnFire(5);
            if (!entity.hurt(fireSource, 2.0f)){
                entity.setRemainingFireTicks(i);
            } else if (entity1 instanceof LivingEntity) {
                this.doEnchantDamageEffects((LivingEntity)entity1, entity);
            }
            this.level().playSound(this,this.blockPosition(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0f, 1.0f);
            this.discard();

        }
    }

    protected void onHitBlock(BlockHitResult pResult) {
        super.onHitBlock(pResult);
        if (!this.level().isClientSide) {
            FireCloudEntity aoe = new FireCloudEntity(ModEntities.FIRE_AOE_CLOUD.get(), this.level());
            aoe.moveTo(this.getX(), this.getY(), this.getZ());
            this.level().addFreshEntity(aoe);
            this.level().playSound(this,this.blockPosition(), SoundEvents.SPLASH_POTION_BREAK, SoundSource.NEUTRAL, 1.0f, 1.0f);
            this.discard();


        }
    }


}
