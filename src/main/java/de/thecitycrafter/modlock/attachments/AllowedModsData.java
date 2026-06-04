package de.thecitycrafter.modlock.attachments;

import net.neoforged.neoforge.server.command.ModIdArgument;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AllowedModsData {
    private List<String> mods = Collections.synchronizedList(new ArrayList<>());



    public List<String> getMods() {
        return mods;
    }

    public void add(String modid) {
        mods.add(modid);
    }

    public void remove(String modid) {
        mods.remove(modid);
    }

    public void set(List<String> mods) {
        this.mods = mods;
    }
}
