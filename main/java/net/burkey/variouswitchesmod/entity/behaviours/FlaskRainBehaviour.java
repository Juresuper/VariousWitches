package net.burkey.variouswitchesmod.entity.behaviours;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.burkey.variouswitchesmod.entity.custom.PiglinWitchEntity;
import net.burkey.variouswitchesmod.entity.custom.IncendiaryFlaskEntity;
import net.burkey.variouswitchesmod.entity.memorymodules.ModMemoryModules;
import net.burkey.variouswitchesmod.item.ModItems;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.function.Function;

public class FlaskRainBehaviour<E extends PiglinWitchEntity> extends DelayedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.ATTACK_TARGET, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT),
            Pair.of(ModMemoryModules.LAST_RESORT_POTION_ACTIVE.get(), MemoryStatus.VALUE_ABSENT));



    protected float attackRadius;
    protected LivingEntity target = null;
    protected int numOfPotions = 20;
    protected int potionsThrown = 0;
    protected int delayBetweenPotions = 5; // 0.25 seconds
    protected boolean isThrowingPotions = false;

    protected int maxDurationTicks = 200; // 10 seconds at 20 ticks/second
    protected int durationTicks = 0;

    public FlaskRainBehaviour(int delayTicks) {
        super(delayTicks);
        attackRadius(10);
    }

    public FlaskRainBehaviour<E> attackRadius(float radius) {
        this.attackRadius = radius * radius;
        return this;
    }

    public FlaskRainBehaviour<E> attackInterval(Function<E, Integer> supplier) {
        this.attackCooldown = supplier;
        return this;
    }

    public FlaskRainBehaviour<E> setDelayBetweenPotions(int delay) {
        this.delayBetweenPotions = delay;
        return this;
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    protected Function<E, Integer> attackCooldown = entity -> 40;

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        this.target = BrainUtils.getTargetOfEntity(entity);
        return !isThrowingPotions && BrainUtils.canSee(entity, this.target) && entity.distanceToSqr(this.target) <= this.attackRadius &&  entity.distanceToSqr(this.target) > 1.0f&& entity.canUseSharedAttack() && !entity.isSupporting();
    }

    @Override
    protected void start(E entity) {
        BehaviorUtils.lookAtEntity(entity, this.target);
        this.potionsThrown = 0;
        this.isThrowingPotions = true;
        this.durationTicks = 0;

        super.start(entity);
    }

    @Override
    protected void stop(E entity) {
        this.isThrowingPotions = false;
        this.potionsThrown = 0;
        this.target = null;
        entity.setSharedAttackCooldown(entity.getSharedAttackCooldown());
    }

    @Override
    protected void doDelayedAction(E entity) {
        this.durationTicks++;
        if (this.target == null) {
            this.isThrowingPotions = false;
            return;
        }

        if (!BrainUtils.canSee(entity, this.target) || entity.distanceToSqr(this.target) > this.attackRadius) {
            this.isThrowingPotions = false;
            return;
        }

        // Start throwing potions one by one

        if (this.potionsThrown < this.numOfPotions) {
            this.throwSinglePotion(entity);
            this.potionsThrown++;

            // Short cooldown if not all potions are thrown
            if (this.potionsThrown < this.numOfPotions) {
                BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.delayBetweenPotions);
            } else {
                // Full cooldown when over
                BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackCooldown.apply(entity));
                this.isThrowingPotions = false;
            }
        }
        entity.triggerFlaskRainAnimation();


    }
    @Override
    protected boolean shouldKeepRunning(E entity) {
        return this.isThrowingPotions && this.potionsThrown < this.numOfPotions;
    }



    private void throwSinglePotion(LivingEntity caster) {
        Level level = caster.level();
        IncendiaryFlaskEntity flask = new IncendiaryFlaskEntity(level, caster);
        flask.setItem(new ItemStack(ModItems.INCENDIARY_FLASK.get()));

        double x = caster.getX(); // -1.0 to +1.0
        double y = caster.getY() + 1.5D;
        double z = caster.getZ();

        flask.setPos(x, y, z);

        // Shoot upward with some lateral randomness
        double velocity = 0.3F + level.random.nextDouble() * 0.3F; // base upward force
        double angleXZ = level.random.nextDouble() * 2.0 * Math.PI; // random direction
        double xMotion = Math.cos(angleXZ) * 0.3;
        double zMotion = Math.sin(angleXZ) * 0.3;
        double yMotion = 0.7 + level.random.nextDouble() * 0.3;

        flask.shoot(xMotion, yMotion, zMotion, (float) velocity, 1.5F); // last is inaccuracy

        level.addFreshEntity(flask);
    }


}