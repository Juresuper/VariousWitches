package net.burkey.variouswitchesmod.entity.client;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.entity.custom.PiglinWitchEntity;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.renderer.GeoEntityRenderer;


public class FireWitchRenderer extends GeoEntityRenderer<PiglinWitchEntity>{
    public FireWitchRenderer(EntityRendererProvider.Context renderManager) {
        super(renderManager,new FireWitchModel());
    }
    @Override
    public ResourceLocation getTextureLocation(PiglinWitchEntity pEntity) {
        return  ResourceLocation.fromNamespaceAndPath(VariousWitchesMod.MODID, "textures/entity/piglin_witch.png");
    }


}
