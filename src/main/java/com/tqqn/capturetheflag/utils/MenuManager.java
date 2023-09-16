package com.tqqn.capturetheflag.utils;

import com.tqqn.capturetheflag.CaptureTheFlag;
import com.tqqn.capturetheflag.utils.menubuilder.Menu;
import com.tqqn.capturetheflag.utils.menubuilder.MenuListener;
import org.bukkit.Bukkit;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MenuManager {

    private final Map<UUID, Menu> openMenus;

    public MenuManager() {
        this.openMenus = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new MenuListener(), CaptureTheFlag.getInstance());
    }

    public void registerMenu(UUID toRegister, Menu menu) {
        openMenus.put(toRegister, menu);
    }

    public void unregisterMenu(UUID toUnRegister) {
        openMenus.remove(toUnRegister);
    }

    public Menu matchMenu(UUID user) {
        return openMenus.get(user);
    }
}
