package com.oregonscientific.meep.message.common;

public class MsmGetAppsCategory extends MeepServerMessage {
    private String[] games = null;
    private String[] blacklist = null;
    
    public MsmGetAppsCategory(String proc, String opcode) {
        super(proc, opcode);
    }

    public String[] getGames() {
        return games;
    }

    public void setGames(String[] games) {
        this.games = games;
    }

    public String[] getBlacklist() {
        return blacklist;
    }

    public void setBlacklist(String[] blacklist) {
        this.blacklist = blacklist;
    }
}
