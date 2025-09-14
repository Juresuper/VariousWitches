package net.burkey.variouswitchesmod.event;

import net.burkey.variouswitchesmod.effect.ModEffects;
import net.burkey.variouswitchesmod.network.PacketHandler;
import net.burkey.variouswitchesmod.network.S2CSpawnMeteoricParticlesPacket;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.Packet;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageType;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.hoglin.Hoglin;
import net.minecraft.world.entity.monster.piglin.AbstractPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.EntityJoinLevelEvent;
import net.minecraftforge.event.entity.living.LivingDamageEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.network.PacketDistributor;
import org.apache.commons.io.monitor.FileAlterationListener;

import java.util.List;

public class ModEvents {

    private static final double RADIUS = 10.0D;



    //Logic for the meteoric fall

    @SubscribeEvent
    public void MeteoricFall(LivingFallEvent event) {
        LivingEntity entity = event.getEntity();
        float fallingDistance = event.getDistance();

        if (entity.hasEffect(ModEffects.METEORIC_FALL_EFFECT.get()) &&
                fallingDistance > 1.5F) {

            float fallingMultiplier = 0;
            //basically just the mace effect
                // First 3 blocks: 4 extra damage per block
                float firstTier = Math.min(fallingDistance, 3);
                fallingMultiplier += firstTier * 4;
                fallingDistance -= firstTier;

                // Next 5 blocks: 2 extra damage per block
                if (fallingDistance > 0) {
                    float secondTier = Math.min(fallingDistance, 5);
                    fallingMultiplier += secondTier * 2;
                    fallingDistance -= secondTier;
                }

                // Remaining blocks: 1 extra damage per block
                if (fallingDistance > 0) {
                    fallingMultiplier += fallingDistance * 1;
                }

            float knockbackPower = 0.1F * fallingMultiplier;
            float knockbackRadius = 0.2F * fallingMultiplier;

            // ound
            entity.level().playSound(
                    null,
                    entity.getX(),
                    entity.getY(),
                    entity.getZ(),
                    entity.fallDistance > 5.0F
                            ? SoundEvents.BLAZE_DEATH
                            : SoundEvents.BLAZE_HURT,
                    entity.getSoundSource(),
                    1.0F,
                    1.0F
            );

            double radius = knockbackRadius; // max radius
            int rings = 5; // number of circles
            int particlesPerRing = 20;
            if (!entity.level().isClientSide()) {
                S2CSpawnMeteoricParticlesPacket packet = new S2CSpawnMeteoricParticlesPacket(
                        entity.getX(), entity.getY(), entity.getZ(), fallingMultiplier
                );

                // Send to all players within 64 blocks
                for (Player player : entity.level().players()) {
                    if (player.distanceTo(entity) < 64.0 && player instanceof ServerPlayer serverPlayer) {
                        PacketHandler.sendToPlayer(packet, serverPlayer);
                    }
                }
            }
            //knock back nearby entities
            int fireDuration = (int) (0.33 * fallingMultiplier);

            entity.level().getEntities(entity, entity.getBoundingBox().inflate(knockbackRadius), e -> e instanceof LivingEntity && e != entity)
                    .forEach(target -> {
                        double dx = target.getX() - entity.getX();
                        double dz = target.getZ() - entity.getZ();
                        double dist = Math.sqrt(dx * dx + dz * dz);

                        if (dist != 0.0D) {
                            double scale = knockbackPower / dist;
                            target.setSecondsOnFire(fireDuration);
                            target.hurt(entity.damageSources().mobAttack(entity), 1 * knockbackPower);
                            target.push(dx * scale, 0.4D * knockbackPower, dz * scale);
                        }
                    });


        }



    }

    @SubscribeEvent
    public void onEntityInteract(PlayerInteractEvent.EntityInteract event) {

        Player player = event.getEntity();
        Entity target = event.getTarget();
        Level level = event.getLevel();
        Item itemInHand = player.getItemInHand(event.getHand()).getItem();
        if (target instanceof LivingEntity livingEntity &&
                livingEntity.hasEffect(ModEffects.WART_SPORES_EFFECT.get())) {

            if (target.getType() == EntityType.PIGLIN || target.getType() == EntityType.HOGLIN) {
                if (itemInHand == Items.GOLDEN_APPLE) {
                    livingEntity.removeEffect(ModEffects.WART_SPORES_EFFECT.get());
                    if (!level.isClientSide()) {
                        if (target instanceof AbstractPiglin piglin) {
                            piglin.setImmuneToZombification(true);

                        }else if(target instanceof Hoglin hoglin){
                            hoglin.setImmuneToZombification(true);

                        }
                        level.playSound(null, livingEntity.getX(), livingEntity.getY(), livingEntity.getZ(),
                                SoundEvents.BEACON_POWER_SELECT, SoundSource.NEUTRAL, 1.0F, 1.0F);
                    }
                    event.setCanceled(true);
                }
            }

        }
    }



}