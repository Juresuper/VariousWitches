package net.burkey.variouswitchesmod.entity.behaviours;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.burkey.variouswitchesmod.effect.ModEffects;
import net.burkey.variouswitchesmod.entity.custom.FireWitchEntity;
import net.burkey.variouswitchesmod.entity.custom.IncendiaryFlaskEntity;
import net.burkey.variouswitchesmod.entity.memorymodules.ModMemoryModules;
import net.burkey.variouswitchesmod.item.ModItems;
import net.burkey.variouswitchesmod.potion.ModPotions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.level.Level;
import net.minecraftforge.registries.RegistryObject;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.ExtendedBehaviour;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableRangedAttack;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Function;

public class MeteoricFallBehaviour <E extends FireWitchEntity> extends ExtendedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT),
            Pair.of(ModMemoryModules.LAST_RESORT_POTION_ACTIVE.get(), MemoryStatus.VALUE_ABSENT));



    protected float attackRadius;
    protected LivingEntity target = null;
    protected int delayBetweenPotions = 5; // 0.25 seconds
    protected boolean hasThrownPotion = false;
    protected int maxDurationTicks = 200; // 10 seconds at 20 ticks/second
    protected int durationTicks = 0;

    protected Function<E, Integer> attackCooldown = entity -> 40;


    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    public MeteoricFallBehaviour<E> attackRadius(float radius) {
        this.attackRadius = radius * radius;
        return this;
    }

    public MeteoricFallBehaviour() {
        super();
        attackRadius(7);

    }



    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        this.target = BrainUtils.getTargetOfEntity(entity);
        return BrainUtils.canSee(entity, this.target) && entity.distanceToSqr(this.target) >= this.attackRadius && entity.canUseSharedAttack()&& !entity.isSupporting();

    }

    @Override
    protected void start(E entity) {

        this.durationTicks = 0;
        BehaviorUtils.lookAtEntity(entity, this.target);

        entity.triggerDrinkingAnimation();
        entity.addEffect(new MobEffectInstance(
                        ModEffects.METEORIC_FALL_EFFECT.get(), 100));
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.delayBetweenPotions);
        this.throwSinglePotion(entity, ModPotions.FLAME_PROPULTION_POTION);
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackCooldown.apply(entity));
        this.hasThrownPotion = true;
        super.start(entity);
    }

    @Override
    protected void tick(E entity) {
        super.tick(entity);
        durationTicks++;
        //launch the witch towards the player midair
        if(entity.hasEffect(ModEffects.FLAME_PROPULSION_EFFECT.get()))
            entity.triggerMeteoricFallAnimation();
        entity.getNavigation().stop();

        if(durationTicks == 30){
            double dx = target.getX() - entity.getX();
            double dz = target.getZ() - entity.getZ();

            double horizontalDistance = Math.sqrt(dx * dx + dz * dz);
            double speed = 1.5;

            if (horizontalDistance > 0) {
                dx = dx / horizontalDistance * speed;
                dz = dz / horizontalDistance * speed;
            }

            entity.setDeltaMovement(
                    dx,
                    entity.getDeltaMovement().y,
                    dz
            );
        }
    }





    @Override
    protected void stop(E entity) {
        this.hasThrownPotion = false;
        this.durationTicks =0;
        entity.setSharedAttackCooldown(entity.getSharedAttackCooldown());

    }
    private void throwSinglePotion(LivingEntity caster, RegistryObject<Potion> potion) {
        Level level = caster.level();
        ThrownPotion thrownpotion = new ThrownPotion(level, caster);
        thrownpotion.setItem(PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), potion.get()));
        thrownpotion.setXRot(-20.0F);

        double potionVelocity = 1.0;

        thrownpotion.shoot(0, potionVelocity, 0, 0.4F, 0.0F);
        level.addFreshEntity(thrownpotion);
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return this.durationTicks < this.maxDurationTicks;
    }
}
