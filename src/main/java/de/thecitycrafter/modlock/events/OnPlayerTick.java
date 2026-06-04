package de.thecitycrafter.modlock.events;

import de.thecitycrafter.modlock.Config;
import de.thecitycrafter.modlock.attachments.Attachments;
import net.minecraft.ChatFormatting;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static net.minecraft.world.item.Items.AIR;

public class OnPlayerTick {
    @SubscribeEvent
    public void onPlayerTick(PlayerTickEvent.Post event) {
            Player player = event.getEntity();

            if (player.level().isClientSide()) {
                return;
            }

            if (player.tickCount % 20 != 0) {
                return;
            }

            checkInventory(player);
        }

    private static void checkInventory(Player player) {

        var data = player.getData(Attachments.ALLOWED_MODS);

        Set<String> allowedMods = new HashSet<>();;

        if (data.getMods() != null || !data.getMods().isEmpty()) {
            allowedMods = new HashSet<>(data.getMods());
        }
        var defaultAllowedMods = Config.DEFAULT_ALLOWED_MODS.get();
        for (int i = 0; i < player.getInventory().getContainerSize(); i++) {

            ItemStack stack = player.getInventory().getItem(i);

            if (stack.isEmpty()) {
                continue;
            }

            ResourceLocation key = BuiltInRegistries.ITEM.getKey(stack.getItem());
            String modID = key.getNamespace();

            if (!allowedMods.contains(modID) && !defaultAllowedMods.contains(modID)) {

                player.getInventory().setItem(i, ItemStack.EMPTY);

                player.drop(stack, false);

                player.sendSystemMessage(
                        Component.translatable("modlock.event.player_tick.drop_not_allowed_item", stack.getHoverName().getString(), modID).withStyle(ChatFormatting.RED)
                );
            }
        }
    }
}
