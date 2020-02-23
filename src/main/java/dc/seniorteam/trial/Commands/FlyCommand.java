package dc.seniorteam.trial.Commands;

import dc.seniorteam.trial.Main;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;

public class FlyCommand extends BukkitCommand {

    public FlyCommand() {
        //Setting command name
        super("fly");

        //Setting description
        setDescription("Toggle Fly Mode");
        //Setting permission
        setPermission("st.fly");
        //Setting permission message
        setPermissionMessage(Main.getInstance().getConfig().getString("NoPermMessage"));
    }

    @Override
    public boolean execute(CommandSender sender, String alias, String[] args) {

        //Coming from console check
        if (!Main.consoleSentCommand(sender)) return false;

        Player player = (Player) sender;

        //Check if player has no permission
        if (!Main.noPermCommand(sender, getPermission(), getPermissionMessage())) return false;

        Player target;

        //Defining target
        if (args.length < 1) target = player; else target = Bukkit.getPlayer(args[0]);

        if (target == null) player.sendMessage(Main.tr("&cCannot find player!"));
        //Check if player can fly or is allowed to fly
        if (target.getAllowFlight() || target.isFlying()) {
            //Disabling flying
            target.setFlying(false);
            //Disabling ability to fly
            target.setAllowFlight(false);
            player.sendMessage(Main.tr("&eYou have &cdisabled &eflight!"));
        } else {
            //Enabling ability to fly
            target.setAllowFlight(true);
            //Enabling flying
            target.setFlying(true);
            player.sendMessage(Main.tr("&eYou have &aenabled &eflight!"));
        }

        return false;
    }
}
