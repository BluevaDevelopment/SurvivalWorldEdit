package org.jachi.whirss.survivalworldedit;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.jachi.whirss.survivalworldedit.commands.PlaceCommand;
import org.jachi.whirss.survivalworldedit.commands.StickCommand;
import org.jachi.whirss.survivalworldedit.events.OnPlayerInteract;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Main extends JavaPlugin {

    public HashMap<String, Location> pos1 = new HashMap<>();
    public HashMap<String, Location> pos2 = new HashMap<>();

    //languages
    private FileConfiguration languages = null;
    private File languagesFile = null;
    private FileConfiguration es = null;
    private File esFile = null;

    public void onEnable() {
        RegisterEvents();
        RegisterCommands();

        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + " ______        _______");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "/ ___\\ \\      / | ____|           Survival World Edit");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "\\___ \\\\ \\ /\\ / /|  _|             Created with love by");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + " ___) |\\ V  V / | |___            Whirss (jachi.org)");
        Bukkit.getConsoleSender().sendMessage(ChatColor.GREEN + "|____/  \\_/\\_/  |_____|");
        Bukkit.getConsoleSender().sendMessage("");

        registerConfig();

        if(getConfig().getString("language").equals("en") || getConfig().getString("language").equals("es")) {
            registerLanguages();
            registerEs();
            Bukkit.getConsoleSender().sendMessage("[SurvivalWorldEdit] " + getLanguages().getString("messages.loaded_language"));
        } else {
            Bukkit.getConsoleSender().sendMessage("[SurvivalWorldEdit] Error getting the language set in config.yml");
            Bukkit.getConsoleSender().sendMessage("[SurvivalWorldEdit] Disabling the plugin...");
            getServer().getPluginManager().disablePlugin(this);
        }
        if(getConfig().getBoolean("metrics")) {
            int pluginId = 13455;
            Metrics metrics = new Metrics(this, pluginId);
        }
        if(getConfig().getString("no-modify").equals("a1")) {
            Bukkit.getConsoleSender().sendMessage("[SurvivalWorldEdit] Info: %%__USER__%% - %%__RESOURCE__%%");
        }
    }

    public void onDisable() {
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " ______        _______");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "/ ___\\ \\      / | ____|           Survival World Edit");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "\\___ \\\\ \\ /\\ / /|  _|             Created with love by");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + " ___) |\\ V  V / | |___            Whirss (jachi.org)");
        Bukkit.getConsoleSender().sendMessage(ChatColor.RED + "|____/  \\_/\\_/  |_____|");
        Bukkit.getConsoleSender().sendMessage("");
    }

    public void RegisterEvents() {
        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new OnPlayerInteract(this), this);
    }

    public void RegisterCommands() {
        this.getCommand("place").setExecutor(new PlaceCommand(this));
        this.getCommand("stick").setExecutor(new StickCommand(this));
    }

    public boolean removeInventoryItems(Inventory inv, Material type, int amount) {
        int ra = amount;
        boolean succeeded = false;
        for (ItemStack is : inv.getContents()) {
            if (is != null &&
                    is.getType() == type) {
                int a = is.getAmount();
                if (a >= ra) {
                    int am = a - ra;
                    inv.removeItem(new ItemStack[] { is });
                    inv.addItem(new ItemStack[] { new ItemStack(is.getType(), am) });
                    succeeded = true;
                    ra = 0;
                    break;
                }
                ra -= a;
                inv.removeItem(new ItemStack[] { is });
            }
        }
        if (!succeeded) {
            int back = amount - ra;
            if (back != 0) {
                ItemStack is = new ItemStack(type, back);
                if (is.getAmount() != 0)
                    inv.addItem(new ItemStack[] { is });
            }
        }
        return succeeded;
    }

    public static List<org.bukkit.block.Block> blocksFromTwoPoints(Location loc1, Location loc2) {
        List<org.bukkit.block.Block> blocks = new ArrayList<>();
        int topBlockX = (loc1.getBlockX() < loc2.getBlockX()) ? loc2.getBlockX() : loc1.getBlockX();
        int bottomBlockX = (loc1.getBlockX() > loc2.getBlockX()) ? loc2.getBlockX() : loc1.getBlockX();
        int topBlockY = (loc1.getBlockY() < loc2.getBlockY()) ? loc2.getBlockY() : loc1.getBlockY();
        int bottomBlockY = (loc1.getBlockY() > loc2.getBlockY()) ? loc2.getBlockY() : loc1.getBlockY();
        int topBlockZ = (loc1.getBlockZ() < loc2.getBlockZ()) ? loc2.getBlockZ() : loc1.getBlockZ();
        int bottomBlockZ = (loc1.getBlockZ() > loc2.getBlockZ()) ? loc2.getBlockZ() : loc1.getBlockZ();
        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                for (int y = bottomBlockY; y <= topBlockY; y++) {
                    Block block = loc1.getWorld().getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }

    //config files
    //config.yml
    public void registerConfig(){
        File config = new File(this.getDataFolder(),"config.yml");
        if(!config.exists()){
            Bukkit.getConsoleSender().sendMessage("[SurvivalWorldEdit] Creating new file: \\plugins\\SurvivalWorldEdit\\config.yml");
            this.getConfig().options().copyDefaults(true);
            saveDefaultConfig();
        }
    }

    //languages
    //language_--.yml:
    public FileConfiguration getLanguages() {
        if(languages == null) {
            reloadLanguages();
        }
        return languages;
    }

    public void reloadLanguages(){
        if(languages == null){
            languagesFile = new File(getDataFolder(), "/language/" + getConfig().getString("language") + ".yml");
        }
        languages = YamlConfiguration.loadConfiguration(languagesFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("language/" + getConfig().getString("language") + ".yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                languages.setDefaults(defConfig);
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void saveLanguages(){
        try{
            languages.save(languagesFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void registerLanguages(){
        languagesFile = new File(this.getDataFolder(), "/language/" + getConfig().getString("language") + ".yml");
        if(!languagesFile.exists()){
            Bukkit.getConsoleSender().sendMessage("[SurvivalWorldEdit] Creating new file: \\plugins\\SurvivalWorldEdit\\language\\" + getConfig().getString("language") + ".yml");
            this.getLanguages().options().copyDefaults(true);
            getLanguages().options().header(" ____                   _            ___        __         _     _ _____    _ _ _\n" +
                    "/ ___| _   _ _ ____   _(___   ____ _| \\ \\      / ___  _ __| | __| | ____|__| (_| |_\n" +
                    "\\___ \\| | | | '__\\ \\ / | \\ \\ / / _` | |\\ \\ /\\ / / _ \\| '__| |/ _` |  _| / _` | | __|\n" +
                    " ___) | |_| | |   \\ V /| |\\ V | (_| | | \\ V  V | (_) | |  | | (_| | |__| (_| | | |_\n" +
                    "|____/ \\__,_|_|    \\_/ |_| \\_/ \\__,_|_|  \\_/\\_/ \\___/|_|  |_|\\__,_|_____\\__,_|_|\\__|\n" +
                    "");
            saveLanguages();
        }
    }

    //es_--.yml:
    public FileConfiguration getEs() {
        if(es == null) {
            reloadEs();
        }
        return es;
    }

    public void reloadEs(){
        if(es == null){
            esFile = new File(getDataFolder(), "/language/es.yml");
        }
        es = YamlConfiguration.loadConfiguration(esFile);
        Reader defConfigStream;
        try{
            defConfigStream = new InputStreamReader(this.getResource("language/es.yml"),"UTF8");
            if(defConfigStream != null){
                YamlConfiguration defConfig = YamlConfiguration.loadConfiguration(defConfigStream);
                es.setDefaults(defConfig);
            }
        }catch(UnsupportedEncodingException e){
            e.printStackTrace();
        }
    }

    public void saveEs(){
        try{
            es.save(esFile);
        }catch(IOException e){
            e.printStackTrace();
        }
    }

    public void registerEs(){
        esFile = new File(this.getDataFolder(), "/language/es.yml");
        if(!esFile.exists()){
            Bukkit.getConsoleSender().sendMessage("[SurvivalWorldEdit] Creating new file: \\plugins\\SurvivalWorldEdit\\language\\es.yml");
            this.getEs().options().copyDefaults(true);
            getEs().options().header(" ____                   _            ___        __         _     _ _____    _ _ _\n" +
                    "/ ___| _   _ _ ____   _(___   ____ _| \\ \\      / ___  _ __| | __| | ____|__| (_| |_\n" +
                    "\\___ \\| | | | '__\\ \\ / | \\ \\ / / _` | |\\ \\ /\\ / / _ \\| '__| |/ _` |  _| / _` | | __|\n" +
                    " ___) | |_| | |   \\ V /| |\\ V | (_| | | \\ V  V | (_) | |  | | (_| | |__| (_| | | |_\n" +
                    "|____/ \\__,_|_|    \\_/ |_| \\_/ \\__,_|_|  \\_/\\_/ \\___/|_|  |_|\\__,_|_____\\__,_|_|\\__|\n" +
                    "");
            saveEs();
        }
    }
}
