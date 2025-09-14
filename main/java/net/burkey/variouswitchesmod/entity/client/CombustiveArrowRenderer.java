package net.burkey.variouswitchesmod.entity.client;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.burkey.variouswitchesmod.entity.custom.CombustiveArrowEntity;
import net.minecraft.client.renderer.entity.ArrowRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;

public class CombustiveArrowRenderer extends ArrowRenderer<CombustiveArrowEntity> {
    public CombustiveArrowRenderer(EntityRendererProvider.Context pContext) {
        super(pContext);
    }

    @Override
    public ResourceLocation getTextureLocation(CombustiveArrowEntity pEntity) {
        return ResourceLocation.fromNamespaceAndPath(VariousWitchesMod.MODID, "textures/entity/projectile/combustion_arrow.png");
    }


}
