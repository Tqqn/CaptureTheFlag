package com.tqqn.capturetheflag.flag;

public enum FlagStatus {

    RESPAWNING("&7&lRespawning!"),
    STOLEN("&c&lStolen!"),
    SAFE("&a&lSafe!"),
    DROPPED("&6&lDropped!"),
    NONE("&7&lNone!");

    private final String statusName;

    FlagStatus(String statusName) {
        this.statusName = statusName;
    }

    public String getStatusName() {
        return statusName;
    }
}