package net.burkey.variouswitchesmod.event;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = VariousWitchesMod.MODID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEventBusClientEvents {
    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event){
        //event.registerLayerDefinition(ModModelLayers.FIRE_WITCH_LAYER, FireWitchModel::createBodyLayer);
        }
}
