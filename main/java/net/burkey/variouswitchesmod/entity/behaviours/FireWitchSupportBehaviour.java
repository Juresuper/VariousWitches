package net.burkey.variouswitchesmod.entity.behaviours;

import com.mojang.datafixers.util.Pair;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.burkey.variouswitchesmod.effect.ModEffects;
import net.burkey.variouswitchesmod.entity.custom.PiglinWitchEntity;
import net.burkey.variouswitchesmod.potion.ModPotions;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.behavior.BehaviorUtils;
import net.minecraft.world.entity.ai.memory.MemoryModuleType;
import net.minecraft.world.entity.ai.memory.MemoryStatus;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.projectile.ThrownPotion;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.Potion;
import net.minecraft.world.item.alchemy.PotionUtils;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.tslat.smartbrainlib.api.core.behaviour.DelayedBehaviour;
import net.tslat.smartbrainlib.util.BrainUtils;

import java.util.List;
import java.util.Random;

public class FireWitchSupportBehaviour<E extends PiglinWitchEntity> extends DelayedBehaviour<E> {
    private static final List<Pair<MemoryModuleType<?>, MemoryStatus>> MEMORY_REQUIREMENTS = ObjectArrayList.of(
            Pair.of(MemoryModuleType.NEARBY_ADULT_PIGLINS, MemoryStatus.VALUE_PRESENT),
            Pair.of(MemoryModuleType.ATTACK_COOLING_DOWN, MemoryStatus.VALUE_ABSENT)
    );

    private LivingEntity target;
    private float attackRadius = 5.0f;

    public FireWitchSupportBehaviour(int delayTicks) {
        super(delayTicks);
    }

    @Override
    protected List<Pair<MemoryModuleType<?>, MemoryStatus>> getMemoryRequirements() {
        return MEMORY_REQUIREMENTS;
    }

    @Override
    protected boolean checkExtraStartConditions(ServerLevel level, E entity) {
        List<AbstractPiglin> nearbyPiglins = BrainUtils.getMemory(entity, MemoryModuleType.NEARBY_ADULT_PIGLINS);
        System.out.println("TEST");
        AbstractPiglin randomPiglin = nearbyPiglins.get(new Random().nextInt(nearbyPiglins.size()));

        if (nearbyPiglins == entity) {
            return false;
        }

        if(!(!randomPiglin.hasEffect(MobEffects.FIRE_RESISTANCE) ||(randomPiglin.level().dimension() != Level.NETHER && !randomPiglin.hasEffect(ModEffects.WART_SPORES_EFFECT.get())|| randomPiglin.getHealth() < randomPiglin.getMaxHealth())))
            return false;
        target = randomPiglin;

        return entity.canUseSharedAttack() && entity.distanceToSqr(this.target) <= this.attackRadius;
    }

    @Override
    protected void start(E entity) {
        super.start(entity);
        BehaviorUtils.lookAtEntity(entity,target);
        entity.triggerThrowAnimation();
        entity.setSupporting(true);
    }

    @Override
    protected void doDelayedAction(E entity) {
        if (target != null && !target.isDeadOrDying()) {
            Potion potion = decidePotionEffect(target);
            if(potion != Potions.THICK){
                throwPotion(entity, target, potion);
            }
            BrainUtils.setForgettableMemory(entity, MemoryModuleType.ATTACK_COOLING_DOWN, true, 60); // 3 seconds
        }
    }

    private void throwPotion(PiglinWitchEntity witch, LivingEntity target, Potion typeOfPotion) {
        Level level = witch.level();
        ThrownPotion potion = new ThrownPotion(level, witch);

        ItemStack potionStack = PotionUtils.setPotion(new ItemStack(Items.SPLASH_POTION), typeOfPotion);
        potion.setItem(potionStack);

        double dx = target.getX() - witch.getX();
        double dy = target.getEyeY() - potion.getY();
        double dz = target.getZ() - witch.getZ();
        potion.shoot(dx, dy + 0.1, dz, 0.5F, 1.0F);

        level.addFreshEntity(potion);
    }
    private Potion decidePotionEffect(LivingEntity target){
        if(!target.hasEffect(MobEffects.FIRE_RESISTANCE)){
            return Potions.FIRE_RESISTANCE;
        }else if(target.level().dimension() != Level.NETHER && !target.hasEffect(ModEffects.WART_SPORES_EFFECT.get())){
            return ModPotions.WART_SPORES_POTION.get();
        }else if(target.getHealth() < target.getMaxHealth()){
            return Potions.HEALING;
        }else{
            return Potions.THICK;
        }
    }

    @Override
    protected void stop(E entity) {
        super.stop(entity);
        entity.setSharedAttackCooldown(entity.getSharedAttackCooldown());
        entity.setSupporting(false);
    }
}
