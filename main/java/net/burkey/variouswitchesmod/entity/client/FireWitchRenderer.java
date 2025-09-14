package net.burkey.variouswitchesmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.entity.custom.FireWitchEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.MobRenderer;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.monster.ZombieVillager;
import software.bernie.geckolib.model.GeoModel;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class FireWitchRenderer extends GeoEntityRenderer<FireWitchEntity>{
    public FireWitchRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager,new FireWitchModel());
    }
    @Override
    public ResourceLocation getTextureLocation(FireWitchEntity pEntity) {
        return  ResourceLocation.fromNamespaceAndPath(VariousWitchesMod.MODID, "textures/entity/fire_witch.png");
    }


}
