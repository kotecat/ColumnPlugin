package org.koteyka.columns.event;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.manager.PlayerManager;

public class DamageListener implements Listener {

    private final GameManager gameManager;
    private final PlayerManager playerManager;

    public DamageListener(GameManager gameManager) {
        this.gameManager = gameManager;
        this.playerManager = gameManager.getPlayerManager();
    }

    @EventHandler
    public void onDamage(EntityDamageEvent e) {
        if (!(e.getEntity() instanceof Player)) return;
        Player player = (Player) e.getEntity();

        double newHealth = player.getHealth() - e.getFinalDamage();

        PlayerInventory inventory = player.getInventory();
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        ItemStack itemInOffHand = inventory.getItemInOffHand();

        if (itemInMainHand.getType() == Material.TOTEM_OF_UNDYING ||
                itemInOffHand.getType() == Material.TOTEM_OF_UNDYING) {
            if (e.getCause() != EntityDamageEvent.DamageCause.VOID &&
                    e.getCause() != EntityDamageEvent.DamageCause.KILL) {
                return;
            }
        }

        if (newHealth <= 0) {
            e.setCancelled(true);
            gameManager.onDie(player);
        }
    }
}
