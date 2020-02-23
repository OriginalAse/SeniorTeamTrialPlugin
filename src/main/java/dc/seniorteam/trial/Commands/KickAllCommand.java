package dc.seniorteam.trial.Commands;

import dc.seniorteam.trial.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;

public class KickAllCommand extends BukkitCommand {

    public KickAllCommand() {
        super("kickall");

        setDescription("Kick all players");
        setPermission("st.kickall");
        setPermissionMessage(Main.getInstance().getConfig().getString("NoPermMessage"));
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        if (!Main.noPermCommand(sender, getPermission(), getPermissionMessage())) return false;

        int onlinesize = 0;

        for (Player player: Bukkit.getOnlinePlayers()) {
            if (player.isOp()) break;

            if (sender instanceof Player) if (sender == player) break;

            if (player.hasPermission(getPermission() + ".bypass")) break;

            String reason = "Kicked by an operator";

            if (args.length > 0) reason = args[0];

            player.kickPlayer(reason);

            onlinesize += 1;
        }

        sender.sendMessage(Main.tr("&aSuccessfully kicked &e" + onlinesize + " players!"));

        return false;
    }

}
