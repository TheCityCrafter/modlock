package de.thecitycrafter.modlock.events;

import de.thecitycrafter.modlock.Config;
import de.thecitycrafter.modlock.attachments.Attachments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;

public class OnBlockUse {

    @SubscribeEvent
    public void onBlockUse(PlayerInteractEvent.RightClickBlock event) {
        Player player = event.getEntity();

        if (!Config.BLOCK_USE.get()) {
            return;
        }

        var level = event.getLevel();
        var pos = event.getPos();
        var state = level.getBlockState(pos);

        if (state.getMenuProvider(level, pos) == null) {
            return;
        }

        String modID = BuiltInRegistries.BLOCK.getKey(state.getBlock()).getNamespace();

        if (!player.hasData(Attachments.ALLOWED_MODS)) {
            return;
        }

        var allowedMods = player.getData(Attachments.ALLOWED_MODS).getMods();
        var defaultAllowedMods = Config.DEFAULT_ALLOWED_MODS.get();

        if (!allowedMods.contains(modID) && !defaultAllowedMods.contains(modID)) {

            event.setCanceled(true);

            player.sendSystemMessage(
                    Component.translatable(
                            "modlock.event.block_use.fail",
                            state.getBlock().getName().withStyle(ChatFormatting.AQUA),
                            Component.literal(modID).withStyle(ChatFormatting.AQUA)
                    )
            );
        }
    }
}