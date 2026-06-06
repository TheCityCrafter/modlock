package de.thecitycrafter.modlock.events;

import de.thecitycrafter.modlock.Config;
import de.thecitycrafter.modlock.attachments.Attachments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;
import net.neoforged.neoforge.event.level.BlockEvent;

public class OnBlockBreak {
    @SubscribeEvent
    public void onBlockBreak(BlockEvent.BreakEvent event) {
        Player player = event.getPlayer();

        if (!Config.BLOCK_BREAK.get()) {
            return;
        }

        String modID = BuiltInRegistries.BLOCK.getKey(event.getState().getBlock()).getNamespace();

        if (player.hasData(Attachments.ALLOWED_MODS)){
            var defaultAllowedMods = Config.DEFAULT_ALLOWED_MODS.get();
            if (!player.getData(Attachments.ALLOWED_MODS).getMods().contains(modID) && !defaultAllowedMods.contains(modID)){
                event.setCanceled(true);
                player.sendSystemMessage(Component.translatable("modlock.event.block_break.fail",  event.getState().getBlock().getName().withStyle(ChatFormatting.AQUA), Component.literal(String.valueOf(modID)).withStyle(ChatFormatting.AQUA)));
            }
        }

    }
}
