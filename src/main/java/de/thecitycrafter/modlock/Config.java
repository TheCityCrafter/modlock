package de.thecitycrafter.modlock;

import java.util.List;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
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

    public static final ModConfigSpec.ConfigValue<Boolean> ITEM_PICKUP = BUILDER.comment("When enabled the player can only pickup items from mod which are unlocked for him \n§cWarning: To deactivate this feature you need to deactivate \"Item in Inventory\" as well").define("itemPickup", true);
    public static final ModConfigSpec.ConfigValue<Boolean> ITEM_IN_INVENTORY = BUILDER.comment("When enabled the player can only have items in their inventory from mod which are unlocked for him").define("itemInInventory", true);
    public static final ModConfigSpec.ConfigValue<Boolean> BLOCK_BREAK = BUILDER.comment("When enabled the player can only break blocks from mod which are unlocked for him").define("blockBreak", false);
    public static final ModConfigSpec.ConfigValue<Boolean> BLOCK_USE = BUILDER.comment("When enabled the player can only use blocks from mod which are unlocked for him").define("blockUse", true);

    static final ModConfigSpec SPEC = BUILDER.build();

    private static boolean validate(final Object obj) {
        return obj instanceof String b;
    }
}
