package de.thecitycrafter.modlock.events;

import de.thecitycrafter.modlock.Config;
import de.thecitycrafter.modlock.attachments.Attachments;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.util.TriState;
import net.neoforged.neoforge.event.entity.player.ItemEntityPickupEvent;

public class OnItemPickup {
    @SubscribeEvent
    public void onItemPickup(ItemEntityPickupEvent.Pre event) {

        String modID = BuiltInRegistries.ITEM.getKey(event.getItemEntity().getItem().getItem()).getNamespace();
        if (event.getPlayer().hasData(Attachments.ALLOWED_MODS)) {
            var defaultAllowedMods = Config.DEFAULT_ALLOWED_MODS.get();
            if (event.getPlayer().getData(Attachments.ALLOWED_MODS).getMods().contains(modID) || defaultAllowedMods.contains(modID)) {
                event.setCanPickup(TriState.DEFAULT);
                return;
            }
        }
        event.setCanPickup(TriState.FALSE);

    }
}
