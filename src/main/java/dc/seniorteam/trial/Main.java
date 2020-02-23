package dc.seniorteam.trial;

import com.google.common.reflect.ClassPath;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.SimpleCommandMap;
import org.bukkit.command.defaults.BukkitCommand;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;

public class Main extends JavaPlugin {

    //Pre-defining main class instance
    private static Main instance;
    //Pre-defining command map
    private SimpleCommandMap commandMap;

    //Code executed on server startup
    @Override
    public void onEnable() {
        //Defining instance
        instance = this;
        //Saving config to data folder
        saveDefaultConfig();
        //Registering commands
        try {
            registerCommands();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //Automatic command loader
    private void registerCommands() throws IOException {
        //Getting class path/loader
        ClassPath cp = ClassPath.from(getClass().getClassLoader());
        //Lambda to loop through each command in Commands package
        cp.getTopLevelClassesRecursive("dc.seniorteam.trial.Commands").forEach(classInfo -> {
            try {
                //Making command class object
                Object cmd = Class.forName(classInfo.getName()).getConstructor().newInstance();

                //Don't register file if it isn't a command
                if (!(cmd instanceof BukkitCommand)) return;

                //Checking if command map isn't defined
                if (commandMap == null) {
                    //Defining package version
                    String version = getServer().getClass().getPackage().getName().split("\\.")[3];
                    //Using "Class" class instead of "CraftServer" class for version compatibility
                    Class craftServerClass = Class.forName("org.bukkit.craftbukkit." + version + ".CraftServer");
                    //Using field reflection
                    Field commandMapField = craftServerClass.getDeclaredField("commandMap");
                    //Making field accessible
                    commandMapField.setAccessible(true);
                    //Defining command map
                    commandMap = (SimpleCommandMap) commandMapField.get(craftServerClass.cast(getServer()));
                }
                //Registering command into command map
                commandMap.register(getDescription().getName(), (BukkitCommand) cmd);
            } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
                e.printStackTrace();
            }
        });
    }

    //So other classes can use functions in Main
    public static Main getInstance() { return instance; }

    //Translate color codes
    public static String tr(String msg) { return ChatColor.translateAlternateColorCodes('&', msg); }

    public static boolean consoleSentCommand(CommandSender sender) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(Main.tr(getInstance().getConfig().getString("ConsoleCommandMessage")));
            return false;
        }
        return true;
    }

    public static boolean noPermCommand(CommandSender player, String permission, String permissionMessage) {
        if (!player.hasPermission(permission)) {
            player.sendMessage(Main.tr(permissionMessage));
            return false;
        }
        return true;
    }

}
