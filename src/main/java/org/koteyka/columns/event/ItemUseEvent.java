package org.koteyka.columns.event;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.koteyka.columns.enums.Event;
import org.koteyka.columns.manager.GameManager;
import org.koteyka.columns.utils.Utils;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class ItemUseEvent implements Listener {

    private final GameManager gameManager;

    public ItemUseEvent(GameManager gameManager) {
        this.gameManager = gameManager;
    }

    private boolean checkMaterial(Material material, PlayerInventory inventory) {
        ItemStack itemInMainHand = inventory.getItemInMainHand();
        ItemStack itemInOffHand = inventory.getItemInOffHand();
        return itemInMainHand.getType() == material || itemInOffHand.getType() == material;
    }

    private boolean checkAndRemove(Material material, PlayerInventory inventory) {
        if (checkMaterial(material, inventory)) {
            Utils.removeOneItem(material, inventory);
            return true;
        }
        return false;
    }

    @EventHandler
    public void onClickEvent(PlayerInteractEvent e) {
        Player player = e.getPlayer();
        PlayerInventory inventory = player.getInventory();

        if (e.getAction() != Action.LEFT_CLICK_AIR && e.getAction() != Action.LEFT_CLICK_BLOCK) return;

        if (checkAndRemove(Material.CLOCK, inventory)) {
            Utils.invertTime(gameManager.getWorld());
        } else if (checkAndRemove(Material.COMMAND_BLOCK, inventory)
                || checkAndRemove(Material.COMMAND_BLOCK_MINECART, inventory)
                || checkAndRemove(Material.REPEATING_COMMAND_BLOCK, inventory)
                || checkAndRemove(Material.CHAIN_COMMAND_BLOCK, inventory)) {
            List<Event> events = Arrays.asList(Event.values());
            gameManager.runEvent(events.get(new Random().nextInt(events.size())));
        }
    }
}
