package me.kench.commands.rotmc;

import me.kench.commands.RotMCCMD;
import me.kench.commands.subcommand.SubCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RotMC_Help extends SubCommand {
    public RotMC_Help() {
        super("help", 1, "rotmc.admin", false);
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {
        return RotMCCMD.sendCommandHelp(sender);
    }
}
