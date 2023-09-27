package com.tqqn.capturetheflag.game.flag;

public enum FlagStatus {

    RESPAWNING("&7&lRespawning!"),
    STOLEN("&c&lStolen!"),
    SAFE("&a&lSafe!"),
    DROPPED("&6&lDropped!");

    private final String statusName;

    FlagStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}
