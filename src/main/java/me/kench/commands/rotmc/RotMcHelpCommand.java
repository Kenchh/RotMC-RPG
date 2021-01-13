package me.kench.commands.rotmc;

import me.kench.commands.RotMcCommand;
import me.kench.commands.subcommand.Subcommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RotMcHelpCommand extends Subcommand {
    public RotMcHelpCommand() {
        super("help", 1, "rotmc.admin", false);
    }

    @Override
    public boolean execute(CommandSender sender, Command basecmd, String subcmd, String label, String[] args) {
        return RotMcCommand.sendCommandHelp(sender);
    }
}
