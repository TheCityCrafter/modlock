package de.thecitycrafter.modlock;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.neoforged.bus.api.Event;
import net.neoforged.neoforge.common.ModConfigSpec;

// An example config class. This is not required, but it's a good idea to have one to keep your config organized.
// Demonstrates how to use Neo's config APIs
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    public static final ModConfigSpec.ConfigValue<List<? extends String>> DEFAULT_ALLOWED_MODS = BUILDER
            .comment("A list of mods that are allowed by default.")
            .defineListAllowEmpty("mods", List.of("minecraft"), () -> "", Config::validate);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validate(final Object obj) {
        return obj instanceof String b;
    }
}
