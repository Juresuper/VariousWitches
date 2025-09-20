package net.burkey.variouswitchesmod.entity.behaviours;

import net.burkey.variouswitchesmod.entity.custom.PiglinWitchEntity;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.tslat.smartbrainlib.api.core.behaviour.custom.attack.AnimatableMeleeAttack;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.function.Function;

public class FireWitchPunchBehaviour<E extends PiglinWitchEntity>  extends AnimatableMeleeAttack<E>{
    public FireWitchPunchBehaviour(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected void start(E entity) {
        entity.triggerPunchingAnimation();
        BehaviorUtils.lookAtEntity(entity, this.target);
    }
    protected Function<E, Integer> attackCooldown = entity -> 40;


    @Override
    protected void doDelayedAction(E entity) {
        if (this.target != null && entity.distanceToSqr(this.target) > 0) {
            double dx = this.target.getX() - entity.getX();
            double dz = this.target.getZ() - entity.getZ();
            double distance = Math.sqrt(dx * dx + dz * dz);

            if (distance > 0) {
                double strength = 5.2D;
                double scale = strength / distance;

                this.target.push(dx * scale, 0.4D, dz * scale);
                this.target.hurt(entity.damageSources().mobAttack(entity), 4.0f); // Deal some damage (optional)
                this.target.hasImpulse = true;
            }
        }
        BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, this.attackCooldown.apply(entity));
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        return super.checkExtraStartConditions(level, entity) && !entity.isSupporting();
    }
}
