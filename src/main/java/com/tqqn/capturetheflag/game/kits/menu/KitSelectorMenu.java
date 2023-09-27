package com.tqqn.capturetheflag.game.kits.menu;

import com.tqqn.capturetheflag.game.arena.Arena;
import com.tqqn.capturetheflag.game.kits.types.ArcherKit;
import com.tqqn.capturetheflag.game.kits.types.TankKit;
import com.tqqn.capturetheflag.game.kits.types.WarriorKit;
import com.tqqn.capturetheflag.utils.menubuilder.Menu;
import com.tqqn.capturetheflag.utils.menubuilder.MenuButton;

public class KitSelectorMenu extends Menu {

    public KitSelectorMenu() {
        super("&cSelect your kit!", 1);

        ArcherKit archerKit = new ArcherKit();
        TankKit tankKit = new TankKit();
        WarriorKit warriorKit = new WarriorKit();

        registerButton(new MenuButton(archerKit.getIcon()).setWhoClicked(clicked -> {
            Arena.getGamePlayer(clicked.getUniqueId()).setAbstractKit(archerKit);
            clicked.closeInventory();
        }), 0);
        registerButton(new MenuButton(tankKit.getIcon()).setWhoClicked(clicked -> {
            Arena.getGamePlayer(clicked.getUniqueId()).setAbstractKit(tankKit);
            clicked.closeInventory();
        }), 1);
        registerButton(new MenuButton(warriorKit.getIcon()).setWhoClicked(clicked -> {
            Arena.getGamePlayer(clicked.getUniqueId()).setAbstractKit(warriorKit);
            clicked.closeInventory();
        }), 2);
    }
}
