package net.burkey.variouswitchesmod.entity.behaviours;

import com.mojang.datafixers.util.Pair;
import net.burkey.variouswitchesmod.effect.ModEffects;
import net.burkey.variouswitchesmod.entity.custom.PiglinWitchEntity;
import net.burkey.variouswitchesmod.entity.memorymodules.ModMemoryModules;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.Brain;
import net.minecraft.world.entity.ai.behavior.EntityTracker;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.ai.memory.WalkTarget;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

public class LastResortPotionBehaviour<E extends PiglinWitchEntity> extends DelayedBehaviour<E> {


    public LastResortPotionBehaviour(int delayTicks) {
        super(delayTicks);
    }
    public LastResortPotionBehaviour<E> closeEnoughDist(BiFunction<E, LivingEntity, Integer> closeEnoughMod) {
        this.closeEnoughWhen = closeEnoughMod;

        return this;
    }

    public LastResortPotionBehaviour<E> targetPredicate(Function<E, Integer> supplier) {
        this.attackCooldown = supplier;
        return this;
    }
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = List.of(Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
                                                                                            Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT),
                                                                                            Pair.of(MemoryModuleType.WALK_TARGET, MemoryStatus.REGISTERED),
                                                                                            Pair.of(MemoryModuleType.LOOK_TARGET, MemoryStatus.REGISTERED),
                                                                                            //custom memory module
                                                                                            Pair.of(ModMemoryModules.LAST_RESORT_POTION_ACTIVE.get(), MemoryStatus.VALUE_ABSENT));

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }
    protected int potionDuration = 220;
    protected Function<E, Integer> attackCooldown = entity -> potionDuration;
    protected BiFunction<E, LivingEntity, Integer> closeEnoughWhen = (owner, target) -> 0;
    protected float speedModifier = 1;
    protected BiFunction<E, LivingEntity, Float> speedMod = (owner, target) -> 0.7f;



    boolean hasTriggered = false;

    protected Predicate<? extends LivingEntity> targetPredicate = entity -> true; // Predicate that determines an applicable target
    protected Predicate<E> canTargetPredicate = entity -> true; // Predicate that determines whether our entity is ready to target or not

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return this.canTargetPredicate.test(entity) && !hasTriggered;
    }

    @Override
    protected void start(E entity) {

        // Stop the entity's movement immediately
        entity.getNavigation().stop();
        BrainUtils.clearMemory(entity.getBrain(), MemoryModuleType.WALK_TARGET);
        hasTriggered = true;

        entity.playSound(SoundEvents.ZOMBIE_VILLAGER_CURE);
        MobEffectInstance fieryCombustionInstance = new MobEffectInstance(ModEffects.FIERY_COMBUSTION_EFFECT.get(), potionDuration, 1); // Duration of 200 ticks and amplifier 1
        entity.addEffect(fieryCombustionInstance);


        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        BrainUtils.setForgettableMemory(entity, ModMemoryModules.LAST_RESORT_POTION_ACTIVE.get(), true, potionDuration);

        if (target != null) {
            BrainUtils.setMemory(entity, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
            entity.triggerDrinkingAnimation();
        }
    }

    @Override
    protected void doDelayedAction(E entity) {
        Brain<?> brain = entity.getBrain();
        LivingEntity target = BrainUtils.getTargetOfEntity(entity);
        if(target == null){
            return;
        }


        BrainUtils.setMemory(brain, MemoryModuleType.LOOK_TARGET, new EntityTracker(target, true));
        BrainUtils.setMemory(brain, MemoryModuleType.WALK_TARGET, new WalkTarget(new EntityTracker(target, false), this.speedMod.apply(entity, target), this.closeEnoughWhen.apply(entity, target)));

        super.doDelayedAction(entity);
    }

    @Override
    protected boolean shouldKeepRunning(E entity) {
        return !this.hasTriggered || super.shouldKeepRunning(entity);
    }

    @Override
    protected void stop(E entity) {
        super.stop(entity);
    }
}
