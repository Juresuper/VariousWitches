package net.burkey.variouswitchesmod.entity.custom;

import net.burkey.variouswitchesmod.item.ModItems;
import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;

public class CombustiveArrowEntity extends AbstractArrow {
    public CombustiveArrowEntity(EntityType<? extends AbstractArrow> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
    }
    float DETONATION_TIME  = 30f;




    public CombustiveArrowEntity(EntityType<? extends AbstractArrow> type, LivingEntity shooter, Level worldIn) {
        super(type, shooter, worldIn);
        this.setBaseDamage(2.5F);
    }
    public CombustiveArrowEntity(EntityType<? extends AbstractArrow> type, Level worldIn, double x, double y,
                                 double z) {
        this(type, worldIn);
        this.setPos(x, y, z);
        this.setBaseDamage(2.5F);
    }


    @Override
    protected ItemStack getPickupItem() {
        return new ItemStack(ModItems.COMBUSTION_ARROW.get());
    }

    @Override
    public void tick() {
        super.tick();
        Vec3 vec3 = this.getDeltaMovement();
        boolean flag = this.isNoPhysics();

        if (this.xRotO == 0.0F && this.yRotO == 0.0F) {
            double d0 = vec3.horizontalDistance();
            this.setYRot((float)(Mth.atan2(vec3.x, vec3.z) * (double)(180F / (float)Math.PI)));
            this.setXRot((float)(Mth.atan2(vec3.y, d0) * (double)(180F / (float)Math.PI)));
            this.yRotO = this.getYRot();
            this.xRotO = this.getXRot();
        }



        float flightTime = this.tickCount;

        float speedMultiplier = 1.0f;
        if(flightTime % 10 == 0){

            speedMultiplier = speedMultiplier + (flightTime * 0.02f);
            speedMultiplier = Math.min(speedMultiplier, 2.0f);

            if(speedMultiplier < 2.0f){
                Vec3 currentMotion = this.getDeltaMovement();
                Vec3 acceleratedMotion = currentMotion.scale(speedMultiplier);
                this.setDeltaMovement(acceleratedMotion);

            }else{
                if(!this.inGround){
                    if(flightTime <= DETONATION_TIME/2){
                        this.level().addParticle(ParticleTypes.SMOKE,
                                getX(), getY(), getZ(),
                                0.0D, 0.0D, 0.0D);
                    }else{
                        this.level().addParticle(ParticleTypes.FLAME,
                                getX(), getY(), getZ(),
                                0.0D, 0.0D, 0.0D);
                    }
                    }


            }

        }
        BlockPos blockpos = this.blockPosition();
        BlockState blockstate = this.level().getBlockState(blockpos);
        if (!blockstate.isAir() && !flag) {
            VoxelShape voxelshape = blockstate.getCollisionShape(this.level(), blockpos);
            if (!voxelshape.isEmpty()) {
                Vec3 vec31 = this.position();
                for(AABB aabb : voxelshape.toAabbs()) {
                    if (aabb.move(blockpos).contains(vec31)) {
                        this.inGround = true;
                        break;
                    }
                }
            }
        }
        if(flightTime == DETONATION_TIME || this.inGround){
            this.level().explode(this, this.getX(), this.getY(), this.getZ(),0.5f,true, Level.ExplosionInteraction.MOB);
            this.discard();
        }

    }

    @Override
    protected void onHitEntity(EntityHitResult pResult) {
        super.onHitEntity(pResult);
        this.level().explode(this, this.getX(), this.getY(), this.getZ(),0.5f,true, Level.ExplosionInteraction.MOB);


    }
}
