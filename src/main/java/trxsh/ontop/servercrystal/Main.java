package trxsh.ontop.servercrystal;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import trxsh.ontop.servercrystal.command.CrystalStats;
import trxsh.ontop.servercrystal.command.GlobalCrystalToggle;
import trxsh.ontop.servercrystal.command.ToggleCrystals;
import trxsh.ontop.servercrystal.config.FileConfig;
import trxsh.ontop.servercrystal.listener.DamageEvent;
import trxsh.ontop.servercrystal.listener.JoinEvent;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.UUID;

public final class Main extends JavaPlugin {

    public static final HashMap<UUID, Boolean> players = new HashMap<>();
    public static final HashMap<UUID, Boolean> stats = new HashMap<>();

    public static boolean enableGlobalCrystals = true;

    @Override
    public void onEnable() {

        long preLoad = System.currentTimeMillis();

        Bukkit.getLogger().info("loading plugin...");

        FileConfig config = new FileConfig(new File(getDataFolder(), "config.sav"));

        if(config.exists()) {
            try {
                config.load();
                Bukkit.getLogger().info("loaded from save file.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            Bukkit.getLogger().warning("config does not exist.");
        }

        for(Player p : Bukkit.getOnlinePlayers()) {

            if(!players.containsKey(p.getUniqueId())) {

                players.put(p.getUniqueId(), false);
                p.sendMessage(ChatColor.GRAY + "Fast crystals is now " + ChatColor.RED + "disabled." + ChatColor.GRAY + " Use /togglefastcrystals to toggle fast crystals.");

            } else {

                boolean b = players.get(p.getUniqueId());

                if (b) {
                    p.sendMessage(ChatColor.GRAY + "Fast crystals is now " + ChatColor.GREEN + "enabled." + ChatColor.GRAY + " Use /togglefastcrystals to toggle fast crystals.");
                } else {
                    p.sendMessage(ChatColor.GRAY + "Fast crystals is now " + ChatColor.RED + "disabled." + ChatColor.GRAY + " Use /togglefastcrystals to toggle fast crystals.");
                }

            }

        }

        Bukkit.getPluginManager().registerEvents(new DamageEvent(), this);
        Bukkit.getPluginManager().registerEvents(new JoinEvent(), this);

        Bukkit.getPluginCommand("crystalstats").setExecutor(new CrystalStats());
        Bukkit.getPluginCommand("globalfastcrystaltoggle").setExecutor(new GlobalCrystalToggle());
        Bukkit.getPluginCommand("togglefastcrystals").setExecutor(new ToggleCrystals());

        long postLoad = System.currentTimeMillis();

        Bukkit.getLogger().info("loaded successfully. (" + (preLoad - postLoad) + "ms)");

    }

    @Override
    public void onDisable() {

        FileConfig config = new FileConfig(new File(getDataFolder(), "config.sav"));

        long preLoad = System.currentTimeMillis();

        Bukkit.getLogger().info("disabling plugin...");

        try {
            config.save();
            Bukkit.getLogger().info("saved config to file.");
        } catch (IOException e) {
            Bukkit.getLogger().warning("failed to save config!");
            e.printStackTrace();
        }

        long postLoad = System.currentTimeMillis();

        Bukkit.getLogger().info("disabled successfully. (" + (postLoad - preLoad) + "ms)");

    }
}