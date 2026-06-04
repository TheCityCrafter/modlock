package de.thecitycrafter.modlock.attachments;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

public class AllowedModsCodec {

    public static final Codec<AllowedModsData> CODEC = RecordCodecBuilder.create(instance ->
            instance.group(
                    Codec.STRING.listOf().fieldOf("mods")
                            .forGetter(data -> data.getMods())
            ).apply(instance, json -> {
                AllowedModsData data = new AllowedModsData();
                data.getMods().addAll(json);
                return data;
            })
    );
}
