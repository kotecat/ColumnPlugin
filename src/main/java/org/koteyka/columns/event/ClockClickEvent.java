package org.koteyka.columns.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.PlayerInventory;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.utils.Utils;

public class ClockClickEvent implements Listener {

    private final GameManager gameManager;

    public ClockClickEvent(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        PlayerInventory inventory = player.getInventory();

        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) return;

        if (inventory.getItemInMainHand().getType() == Material.CLOCK
        || inventory.getItemInOffHand().getType() == Material.CLOCK) {
            Utils.invertTime(gameManager.getWorld());
            Utils.removeOneItem(Material.CLOCK, inventory);
        }

    }

}
