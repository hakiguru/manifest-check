package org;

import net.neoforged.neoforge.common.ModConfigSpec;

public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<String> PACK_ID = BUILDER
            .comment("Id ")
            .define("packId", "unknown");

    public static final ModConfigSpec.ConfigValue<String> PACK_VERSION = BUILDER
            .comment("version")
            .define("packVersion", "0");


    static final ModConfigSpec SPEC = BUILDER.build();
}
