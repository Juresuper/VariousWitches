package net.burkey.variouswitchesmod.entity.custom;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.burkey.variouswitchesmod.entity.ModEntities;
import net.burkey.variouswitchesmod.entity.behaviours.FireWitchCustomShootBehaviour;
import net.burkey.variouswitchesmod.entity.behaviours.FireWitchPunchBehaviour;
import net.burkey.variouswitchesmod.entity.behaviours.FlaskRainBehaviour;
import net.burkey.variouswitchesmod.entity.behaviours.MeteoricFallBehaviour;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AnimationState;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.animal.IronGolem;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.minecraft.world.entity.monster.WitherSkeleton;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.monster.piglin.PiglinArmPose;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.SmartBrainOwner;
import net.tslat.smartbrainlib.api.core.BrainActivityGroup;
import net.tslat.smartbrainlib.api.core.SmartBrainProvider;
import net.tslat.smartbrainlib.api.core.behaviour.FirstApplicableBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.OneRandomBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.look.LookAtTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.misc.Idle;
import net.tslat.smartbrainlib.api.core.behaviour.custom.move.MoveToWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetRandomWalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.path.SetWalkTargetToAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.InvalidateAttackTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetPlayerLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.SetRandomLookTarget;
import net.tslat.smartbrainlib.api.core.behaviour.custom.target.TargetOrRetaliate;
import net.tslat.smartbrainlib.api.core.sensor.ExtendedSensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.HurtBySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.NearbyLivingEntitySensor;
import net.tslat.smartbrainlib.api.core.sensor.vanilla.PiglinSpecificSensor;
import org.jetbrains.annotations.Nullable;
import software.bernie.geckolib.animatable.GeoEntity;
import software.bernie.geckolib.core.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.core.animatable.instance.SingletonAnimatableInstanceCache;
import software.bernie.geckolib.core.animation.AnimatableManager;
import software.bernie.geckolib.core.animation.Animation;
import software.bernie.geckolib.core.animation.AnimationController;
import software.bernie.geckolib.core.animation.RawAnimation;
import software.bernie.geckolib.core.object.PlayState;

import java.util.List;

public class PiglinWitchEntity extends AbstractPiglin implements SmartBrainOwner<PiglinWitchEntity>, GeoEntity, RangedAttackMob {

    public final AnimationState idleAnimationState = new AnimationState();
    private int idleAnimationTimeout = 0;
    public static final int ANIMATION_IDLE = 0;
    public static final int ANIMATION_WALK = 1;
    public static final int ANIMATION_DRINK_POTION = 2;
    public static final int ANIMATION_FLASK_RAIN = 3;
    public static final int ANIMATION_METEORIC = 4;
    public static final int ANIMATION_SHOOT = 5;
    public static final int ANIMATION_PUNCH = 6;
    public static final int ANIMATION_THROW = 7;


    private AnimatableInstanceCache cache = new SingletonAnimatableInstanceCache(this);

    private int animationTimeout = 0;

    private boolean cannotHunt;

    public static final int cooldown = 100;

    public boolean isSupporting() {
        return supporting;
    }

    public void setSupporting(boolean supporting) {
        PiglinWitchEntity.supporting = supporting;
    }

    public static boolean supporting = false;


    // Entity data accessors
    private static final EntityDataAccessor<Integer> DATA_ANIMATION_STATE = SynchedEntityData.defineId(PiglinWitchEntity.class, EntityDataSerializers.INT);

    private static final EntityDataAccessor<Integer> DATA_SHARED_ATTACK_COOLDOWN = SynchedEntityData.defineId(PiglinWitchEntity.class, EntityDataSerializers.INT);




    public PiglinWitchEntity(EntityType<? extends AbstractPiglin> pEntityType, Level pLevel) {
        super(pEntityType, pLevel);
        this.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.CROSSBOW));
        this.addEffect(new MobEffectInstance(MobEffects.FIRE_RESISTANCE, 1000000));
    }


    public static AttributeSupplier.Builder createAttributes(){
        return Monster.createMonsterAttributes().add(Attributes.MAX_HEALTH, 85D)
                .add(Attributes.MOVEMENT_SPEED, 0.15D)
                .add(Attributes.ATTACK_DAMAGE, 1.0D)
                .add(Attributes.FOLLOW_RANGE, 20.0D)
                .add(Attributes.ARMOR, 1.0D);

    }
    public void addAdditionalSaveData(CompoundTag pCompound) {
        super.addAdditionalSaveData(pCompound);

        if (this.cannotHunt) {
            pCompound.putBoolean("CannotHunt", true);
        }
    }

    public void readAdditionalSaveData(CompoundTag pCompound) {
        super.readAdditionalSaveData(pCompound);
        this.setCannotHunt(pCompound.getBoolean("CannotHunt"));
    }

    private void setCannotHunt(boolean pCannotHunt) {
        this.cannotHunt = pCannotHunt;
    }

    protected boolean canHunt() {
        return !this.cannotHunt;
    }



    @Override
    protected Brain.Provider<?> brainProvider() {
        return new SmartBrainProvider<>(this);
    }

    @Override
    protected void customServerAiStep() {
        tickBrain(this);
        updateCooldowns();
    }

    @Override
    public PiglinArmPose getArmPose() {
        return null;
    }

    @Override
    protected void playConvertedSound() {
        this.playSound(SoundEvents.PIGLIN_CONVERTED_TO_ZOMBIFIED);
    }

    private void updateCooldowns() {
        if (!this.level().isClientSide()) {
            // Decrement cooldowns
            if (getSharedAttackCooldown() > 0) {
                setSharedAttackCooldown(getSharedAttackCooldown() - 1);
            }

        }
    }



    @Override
    public List<? extends ExtendedSensor<? extends PiglinWitchEntity>> getSensors() {
        return ObjectArrayList.of(
                new HurtBySensor<>(),
                new NearbyLivingEntitySensor<PiglinWitchEntity>()
                        .setPredicate((target, entity) -> target instanceof Player || target instanceof WitherSkeleton
                             ),
                new PiglinSpecificSensor<>());



    }

    @Override
    public BrainActivityGroup<? extends PiglinWitchEntity> getCoreTasks() { // These are the tasks that run all the time (usually)
        return BrainActivityGroup.coreTasks(
                new LookAtTarget<>(),// Have the entity turn to face and look at its current look target
                new SetWalkTargetToAttackTarget<>(), // ← This makes the entity move toward its attack target
                new MoveToWalkTarget<>()                // Walk towards the current walk target
                );
//new FireWitchSupportBehaviour<>(20)

    }


    @Override
    public BrainActivityGroup<? extends PiglinWitchEntity> getIdleTasks() { // These are the tasks that run when the mob isn't doing anything else (usually)
        return BrainActivityGroup.idleTasks(
                new FirstApplicableBehaviour<PiglinWitchEntity>(      // Run only one of the below behaviours, trying each one in order. Include the generic type because JavaC is silly
                        new TargetOrRetaliate<>(),
                        new SetPlayerLookTarget<>(),          // Set the look target for the nearest player
                        new SetRandomLookTarget<>()),         // Set a random look target

                new OneRandomBehaviour<>(                 // Run a random task from the below options
                        new SetRandomWalkTarget<>(), // Set a random walk target to a nearby position
                        new Idle<>().runFor(entity -> entity.getRandom().nextInt(30, 60)))); // Do nothing for 1.5->3 seconds

    }

    @Override
    public BrainActivityGroup<? extends PiglinWitchEntity> getFightTasks() {
        return BrainActivityGroup.fightTasks(
                new InvalidateAttackTarget<>(),

                new OneRandomBehaviour<>(
                                        new MeteoricFallBehaviour<>(),
                                        new FlaskRainBehaviour<>(20),
                                        new FirstApplicableBehaviour<>(new FireWitchPunchBehaviour<>(20),
                                                                        new FireWitchCustomShootBehaviour<>(22))
                                        ));
    }


    @Override
    protected void defineSynchedData() {
        super.defineSynchedData();
        this.entityData.define(DATA_ANIMATION_STATE, ANIMATION_IDLE);
        this.entityData.define(DATA_SHARED_ATTACK_COOLDOWN, cooldown);
    }


    public int getSharedAttackCooldown() {
        return this.entityData.get(DATA_SHARED_ATTACK_COOLDOWN);
    }

    public void setSharedAttackCooldown(int cooldown) {
        this.entityData.set(DATA_SHARED_ATTACK_COOLDOWN, cooldown);
    }

    public boolean canUseSharedAttack() {
        return getSharedAttackCooldown() <= 0;
    }



    public int getAnimationState() {
        return this.entityData.get(DATA_ANIMATION_STATE);
    }

    public void setAnimationState(int state) {
        this.entityData.set(DATA_ANIMATION_STATE, state);
        this.animationTimeout = 0;
    }




    @Override
    public void tick() {
        super.tick();

        if (!this.level().isClientSide()) {
            if (animationTimeout > 0) {
                animationTimeout--;
            }

            if (animationTimeout <= 0) {
                if (this.getDeltaMovement().horizontalDistanceSqr() > 1.0E-6D) {
                    setAnimationState(ANIMATION_WALK);
                } else {
                    setAnimationState(ANIMATION_IDLE);
                }
            }
        } else {
            handleClientAnimations();
        }
    }

    private void handleClientAnimations() {
        if (animationTimeout > 0) {
            animationTimeout--;
        }
    }

    public void triggerFlaskRainAnimation() {
        setAnimationState(ANIMATION_FLASK_RAIN);
        animationTimeout = 5;
    }

    public void triggerThrowAnimation() {
        setAnimationState(ANIMATION_THROW);
        animationTimeout = 15;
    }

    public void triggerPunchingAnimation() {
        setAnimationState(ANIMATION_PUNCH);
        animationTimeout = 25;
    }

    public void triggerShootingAnimation() {
        setAnimationState(ANIMATION_SHOOT);
        animationTimeout = 25;
    }

    public void triggerMeteoricFallAnimation() {
        setAnimationState(ANIMATION_METEORIC);
        animationTimeout = 10;
    }

    public void triggerDrinkingAnimation() {
        setAnimationState(ANIMATION_DRINK_POTION);
        playSound(SoundEvents.WITCH_DRINK);
        animationTimeout = 35;
    }

    //SOUNDS
    @Override
    protected @Nullable SoundEvent getAmbientSound() {
        return SoundEvents.PIGLIN_AMBIENT;
    }

    @Override
    protected SoundEvent getHurtSound(DamageSource pDamageSource) {
        return SoundEvents.PIGLIN_HURT;
    }

    @Override
    protected SoundEvent getDeathSound() {
        return SoundEvents.PIGLIN_DEATH;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllerRegistrar) {
        controllerRegistrar.add(new AnimationController<>(this, "controller", this::predicate).setAnimationSpeedHandler(this::getAnimationSpeed));
        //controllerRegistrar.add(DefaultAnimations.genericWalkIdleController(this));
    }

    private double getAnimationSpeed(PiglinWitchEntity piglinWitchEntity) {
        int currentAnimation = getAnimationState();

        if (currentAnimation == ANIMATION_DRINK_POTION) {
            return 0.5f;
        }
        return 1.0f; // Normal speed for idle/walk
    }


    private PlayState predicate(software.bernie.geckolib.core.animation.AnimationState<PiglinWitchEntity> fireWitchEntityAnimationState) {
        int currentAnimation = getAnimationState();

        switch (currentAnimation) {
            case ANIMATION_DRINK_POTION:
                fireWitchEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("drink_potion", Animation.LoopType.PLAY_ONCE));
                break;
            case ANIMATION_FLASK_RAIN:
                fireWitchEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("flask_rain", Animation.LoopType.PLAY_ONCE));
                break;
            case ANIMATION_METEORIC:
                fireWitchEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("meteoric_fall", Animation.LoopType.PLAY_ONCE));
                break;
            case ANIMATION_WALK:
                fireWitchEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("walk", Animation.LoopType.LOOP));
                break;
            case ANIMATION_SHOOT:
                fireWitchEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("shoot", Animation.LoopType.PLAY_ONCE));
                break;
            case ANIMATION_PUNCH:
                fireWitchEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("punch", Animation.LoopType.PLAY_ONCE));
                break;
            case ANIMATION_THROW:
                fireWitchEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("throw", Animation.LoopType.PLAY_ONCE));
                break;
            default:
                fireWitchEntityAnimationState.getController().setAnimation(RawAnimation.begin().then("idle", Animation.LoopType.LOOP));
                break;
        }


        return PlayState.CONTINUE;
    }


    @Override
    public boolean causeFallDamage(float fallDistance, float damageMultiplier, DamageSource source) {
        float reducedMultiplier = 0.01F;
        return super.causeFallDamage(fallDistance, reducedMultiplier, source);
    }


    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return cache;
    }


    @Override
    public void performRangedAttack(LivingEntity pTarget, float pVelocity) {

        CombustiveArrowEntity arrow = new CombustiveArrowEntity(ModEntities.COMBUSTIVE_ARROW_ENTITY.get(), this, this.level());
        arrow.setBaseDamage(1.0f);
        arrow.setKnockback(0);

        double d0 = pTarget.getX() - this.getX();
        double d1 = pTarget.getY(0.3333333333333333D) - arrow.getY();
        double d2 = pTarget.getZ() - this.getZ();
        double d3 = Math.sqrt(d0 * d0 + d2 * d2);

        arrow.shoot(d0, d1 + d3 * (double)0.2F, d2, 1.6F, (float)(14 - this.level().getDifficulty().getId() * 4));

        this.playSound(SoundEvents.ARROW_SHOOT, 1.0F, 1.0F / (this.getRandom().nextFloat() * 0.4F + 0.8F));
        this.level().addFreshEntity(arrow);
    }


}
