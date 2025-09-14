package net.burkey.variouswitchesmod.entity.client;

import com.mojang.blaze3d.vertex.PoseStack;
import net.burkey.variouswitchesmod.entity.custom.FireCloudEntity;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.resources.ResourceLocation;

public class FireCloudRenderer extends EntityRenderer<FireCloudEntity> {
    public FireCloudRenderer(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public ResourceLocation getTextureLocation(FireCloudEntity pEntity) {
        return null;
    }

    @Override
    public void render(FireCloudEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight) {
        // You could render a small glowing cube or skip rendering entirely if it’s just particles.
    }


}

