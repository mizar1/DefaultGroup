package org.royaldev.defaultgroup;

import net.milkbowl.vault.permission.Permission;
import org.bukkit.event.HandlerList;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class DefaultGroup extends JavaPlugin {

    private boolean enabled;
    private String defaultGroup;
    private Permission permission = null;

    private void loadConfigValues() {
        this.reloadConfig();
        this.enabled = this.getConfig().getBoolean("enabled");
        this.defaultGroup = this.getConfig().getString("default_group");
    }

    private boolean setupPermissions() {
        final RegisteredServiceProvider<Permission> rsp = this.getServer().getServicesManager().getRegistration(Permission.class);
        if (rsp != null) this.permission = rsp.getProvider();
        return this.permission != null;
    }

    String getDefaultGroup() {
        return this.defaultGroup;
    }

    Permission getPermission() {
        return this.permission;
    }

    boolean isListenerEnabled() {
        return this.enabled;
    }

    @Override
    public void onDisable() {
        HandlerList.unregisterAll(this);
    }

    @Override
    public void onEnable() {
        if (!new File(this.getDataFolder(), "config.yml").exists()) {
            if (!this.getDataFolder().mkdirs()) this.getLogger().warning("Couldn't make the plugin data directory.");
            this.saveDefaultConfig();
        }
        this.loadConfigValues();
        final PluginManager pm = this.getServer().getPluginManager();
        if (pm.getPlugin("Vault") == null) { // NOTE: Doesn't actually check if this is the right Vault
            this.getLogger().severe("Vault not found. Disabling.");
            pm.disablePlugin(this);
            return;
        }
        this.setupPermissions();
        pm.registerEvents(new WowListener(this), this);
    }

}
