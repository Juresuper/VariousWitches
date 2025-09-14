package net.burkey.variouswitchesmod.entity.client;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.entity.custom.FireWitchEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
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
