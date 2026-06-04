package de.thecitycrafter.modlock.attachments;

import de.thecitycrafter.modlock.ModLock;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.attachment.AttachmentType;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.NeoForgeRegistries;

import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;




public class Attachments {

    public static final DeferredRegister<AttachmentType<?>> ATTACHMENT_TYPES =
            DeferredRegister.create(NeoForgeRegistries.Keys.ATTACHMENT_TYPES, ModLock.MODID);

    public static final Supplier<AttachmentType<AllowedModsData>> ALLOWED_MODS =
            ATTACHMENT_TYPES.register(
                    "allowed_mods",
                    () -> AttachmentType.builder(AllowedModsData::new)
                            .serialize(AllowedModsCodec.CODEC)
                            .build()
            );
    public static void init(IEventBus bus) {
        ATTACHMENT_TYPES.register(bus);
    }

    @SubscribeEvent // on the game event bus
    public void onClone(PlayerEvent.Clone event) {
        if (event.isWasDeath() && event.getOriginal().hasData(ALLOWED_MODS.get())) {
            event.getEntity().getData(ALLOWED_MODS).set(event.getOriginal().getData(ALLOWED_MODS.get()).getMods());
        }
    }
}