package dc.seniorteam.trial.Commands;

import dc.seniorteam.trial.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class KickCommand  extends BukkitCommand {

    public KickCommand() {
        //Setting command name
        super("kick");

        //Setting description
        setDescription("Kick a player");
        //Setting permission
        setPermission("st.kick");
        //Setting permission message
        setPermissionMessage(Main.getInstance().getConfig().getString("NoPermMessage"));
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        //Check if player has no permission
        if (!Main.noPermCommand(sender, getPermission(), getPermissionMessage())) return false;

        //Check if no parameter is specified
        if (args.length < 1) {
            sender.sendMessage(Main.tr("&cYou must specify a player to kick!"));
            return false;
        }

        Player target = Bukkit.getPlayer(args[0]);

        if (target == null) sender.sendMessage(Main.tr("&cCannot find player!"));

        //Checking if command sender is a player
        if (sender instanceof Player) {
            Player player = (Player) sender;
            //Checking if specified player is command sender
            if (player == target) {
                player.sendMessage(Main.tr("&cYou cannot kick yourself!"));
                return false;
            }
        }

        String reason = "Kicked by an operator";

        if (args.length > 1) reason = args[1];

        target.kickPlayer(reason);

        sender.sendMessage(Main.tr("&aSuccessfully kicked " + target.getName() + "!"));

        return false;
    }

}
