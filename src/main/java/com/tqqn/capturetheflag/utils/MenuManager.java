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

    /**
     * Creates a MenuManager Object.
     */
    public MenuManager() {
        this.openMenus = new HashMap<>();

        Bukkit.getPluginManager().registerEvents(new MenuListener(), CaptureTheFlag.getInstance());
    }

    /**
     * Void Method that will register a new Menu.
     * @param toRegister UUID
     * @param menu Menu
     */
    public void registerMenu(UUID toRegister, Menu menu) {
        openMenus.put(toRegister, menu);
    }

    /**
     * Void Method that will unregister a Menu.
     * @param toUnRegister UUID
     */
    public void unregisterMenu(UUID toUnRegister) {
        openMenus.remove(toUnRegister);
    }

    /**
     * Gets the Menu of the user.
     * @param user UUID (Player)
     */
    public Menu matchMenu(UUID user) {
        return openMenus.get(user);
    }
}
