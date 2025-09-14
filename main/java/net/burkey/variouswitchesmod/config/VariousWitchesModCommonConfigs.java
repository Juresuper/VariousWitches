package net.burkey.variouswitchesmod.config;

import net.minecraftforge.common.ForgeConfigSpec;

public class VariousWitchesModCommonConfigs {
    public static final ForgeConfigSpec.Builder BUILDER = new ForgeConfigSpec.Builder();
    public static final ForgeConfigSpec SPEC;

//    public static final ForgeConfigSpec.ConfigValue<Integer> PIGLIN_WITCH_SPAWNRATE;
//    public static final ForgeConfigSpec.ConfigValue<Integer> PIGLIN_WITCH_BASTION_MIN_SPAWNRATE;
//    public static final ForgeConfigSpec.ConfigValue<Integer> PIGLIN_WITCH_BASTION_MAX_SPAWNRATE;



    static {
        BUILDER.push("Configs for Various Witches Mod");

//        PIGLIN_WITCH_SPAWNRATE = BUILDER.comment("Determines the weight of spawning for piglin witches (Default:3)")
//                .define("Piglin withces spawnrate", 3);





        BUILDER.pop();
        SPEC = BUILDER.build();
    }
}
