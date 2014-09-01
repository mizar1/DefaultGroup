package org.royaldev.defaultgroup;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class WowListener implements Listener {

    private final DefaultGroup plugin;

    WowListener(final DefaultGroup instance) {
        this.plugin = instance;
    }

    @EventHandler(priority = EventPriority.HIGHEST, ignoreCancelled = true)
    public void amaze(final PlayerJoinEvent e) {
        if (!this.plugin.isListenerEnabled() || !this.plugin.getPermission().isEnabled() || !this.plugin.getPermission().hasGroupSupport()) {
            return;
        }
        final Player p = e.getPlayer();
        boolean hasGroup;
        try {
            hasGroup = this.plugin.getPermission().getPrimaryGroup(p) != null;
        } catch (final Throwable t) { // people throw crazy things
            hasGroup = false; // hopefully
        }
        if (hasGroup) return;
        boolean addGroupErr;
        addGroupErr = this.plugin.getPermission().playerAddGroup( (String) null, p, this.plugin.getDefaultGroup());
        if ( addGroupErr ) {
            this.plugin.getLogger().info("Added " + p.getName() + " to group " + this.plugin.getDefaultGroup() + ".");
        }
    }

}
