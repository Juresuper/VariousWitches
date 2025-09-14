package net.burkey.variouswitchesmod.entity.behaviours;

import net.burkey.variouswitchesmod.entity.custom.FireWitchEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.monster.RangedAttackMob;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableRangedAttack;
import net.tslat.smartbrainlib.util.BrainUtils;

public class FireWitchCustomShootBehaviour<E extends FireWitchEntity & RangedAttackMob> extends AnimatableRangedAttack<E> {


    public FireWitchCustomShootBehaviour(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected void start(E entity) {
        super.start(entity);
        entity.getNavigation().stop();
        entity.triggerShootingAnimation();


    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return super.checkExtraStartConditions(level, entity) && !entity.isSupporting();
    }

    @Override
    protected void tick(E entity) {
        super.tick(entity);
        entity.getNavigation().stop();

    }

    @Override
    protected void doDelayedAction(E entity) {
        if (this.target == null)
            return;

        if (!BrainUtils.canSee(entity, this.target) || entity.distanceToSqr(this.target) > this.attackRadius)
            return;

        entity.performRangedAttack(this.target, 1.0F);
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackIntervalSupplier.apply(entity));
    }

    @Override
    protected void stop(E entity) {
        super.stop(entity);
        entity.setSharedAttackCooldown(entity.getSharedAttackCooldown());

    }
}
