package net.burkey.variouswitchesmod.item;

import net.burkey.variouswitchesmod.VariousWitchesMod;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VariousWitchesMod.MODID);


    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }

    public static final RegistryObject<CreativeModeTab> VARIOUS_WITCHES_TAB = CREATIVE_MODE_TABS.register("various_witches_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.COMBUSTION_ARROW.get()))
                    .title(Component.translatable("creativetab.various_witches_tab"))
                    .displayItems(((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.COMBUSTION_ARROW.get());
                        pOutput.accept(ModItems.PIGLIN_WITCH_SHARD.get());
                        pOutput.accept(ModItems.INCENDIARY_FLASK.get());
                        pOutput.accept(ModItems.CRIMSON_TRUFFLE_SEEDS.get());
                        pOutput.accept(ModItems.CRIMSON_TRUFFLE.get());
                        pOutput.accept(ModItems.CRIMSON_TRUFFLE_STEW.get());


                        pOutput.accept(ModItems.PIGLIN_WITCH_SPAWN_EGG.get());
                    }))
                    .build());
}
