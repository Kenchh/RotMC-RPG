package me.reykench.commands.subcommand;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {

    private String name;
    private int index;
    private String permission;
    private boolean playeronly;
    private String[] aliases;

    public SubCommand(String name, int index, String permission, boolean playeronly, String... aliases) {
        this.name = name;
        this.index = index;
        this.permission = permission;
        this.playeronly = playeronly;
        this.aliases = aliases;
    }

    public abstract boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args);

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPermission() {
        return permission;
    }

    public void setPermission(String permission) {
        this.permission = permission;
    }

    public boolean isPlayeronly() {
        return playeronly;
    }

    public void setPlayeronly(boolean playeronly) {
        this.playeronly = playeronly;
    }

    public String[] getAliases() {
        return aliases;
    }

    public void setAliases(String[] aliases) {
        this.aliases = aliases;
    }
}
