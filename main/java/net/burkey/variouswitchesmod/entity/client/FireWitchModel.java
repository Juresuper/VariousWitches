package net.burkey.variouswitchesmod.entity.client;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.entity.custom.FireWitchEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.core.animatable.model.CoreGeoBone;
import software.bernie.geckolib.core.animation.AnimationState;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.model.data.EntityModelData;

public class FireWitchModel extends GeoModel<FireWitchEntity> {
    @Override
    public ResourceLocation getModelResource(FireWitchEntity fireWitchEntity) {
        return ResourceLocation.fromNamespaceAndPath(VariousWitchesMod.MODID, "geo/fire_witch.geo.json");
    }

    @Override
    public ResourceLocation getTextureResource(FireWitchEntity fireWitchEntity) {
        return ResourceLocation.fromNamespaceAndPath(VariousWitchesMod.MODID, "textures/fire_witch.png");
    }

    @Override
    public ResourceLocation getAnimationResource(FireWitchEntity fireWitchEntity) {
        return ResourceLocation.fromNamespaceAndPath(VariousWitchesMod.MODID, "animations/fire_witch.animation.json");
    }

    @Override
    public void setCustomAnimations(FireWitchEntity animatable, long instanceId, AnimationState<FireWitchEntity> animationState) {
        CoreGeoBone head = getAnimationProcessor().getBone("head");

        if (head != null) {
            EntityModelData entityData = animationState.getData(DataTickets.ENTITY_MODEL_DATA);

            head.setRotX(entityData.headPitch() * Mth.DEG_TO_RAD);
            head.setRotY(entityData.netHeadYaw() * Mth.DEG_TO_RAD);
        }
    }
}

