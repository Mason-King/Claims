package claims.Listener;

import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class PlayerInteract implements Listener {

    @EventHandler (priority =  EventPriority.HIGHEST)
    public void interact(PlayerInteractEvent e) {
        Player p = (Player) e.getPlayer();

        Chunk c = p.getLocation().getChunk();
        Claim claim = Claim.getClaimAt(p.getWorld(), c.getX(), c.getZ());
        if(claim == null) return;

        if(e.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            Block b = e.getClickedBlock();

            if(b.getType().equals(Material.CHEST) || b.getType().equals(Material.TRAPPED_CHEST)) {
                //Chest use flag goes here!
                if(claim.getOwner().equals(p.getUniqueId())) return;
                if(claim.isTrusted(p)) {
                    if(!claim.isTrustedOpenChest()) {
                        e.setCancelled(true);
                        p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not do this in this claim!"));
                    }
                } else {
                    if(!claim.isVisitorOpenChest()) {
                        e.setCancelled(true);
                        p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not do this in this claim!"));
                    }
                }
            } else if(b.getType().name().contains("DOOR")) {
                if(claim.getOwner().equals(p.getUniqueId())) return;
                if(claim.isTrusted(p)) {
                    if(!claim.isTrustedOpenDoor()) {
                        e.setCancelled(true);
                        p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not do this in this claim!"));
                    }
                } else {
                    if(!claim.isVisitorOpenDoor()) {
                        e.setCancelled(true);
                        p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not do this in this claim!"));
                    }
                }
            } else if(b.getType().name().contains("BUTTON") || b.getType().equals(Material.FURNACE)  || b.getType().equals(Material.CRAFTING_TABLE) || b.getType().equals(Material.LEVER) || b.getType().equals(Material.ENCHANTING_TABLE) || b.getType().name().contains("BED") || b.getType().name().contains("ANVIL")) {
                if(claim.getOwner().equals(p.getUniqueId())) return;
                if(claim.isTrusted(p)) {
                    if(!claim.isTrustedUse()) {
                        e.setCancelled(true);
                        p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not do this in this claim!"));
                    }
                } else {
                    if(!claim.isVisitorUse()) {
                        e.setCancelled(true);
                        p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not do this in this claim!"));
                    }
                }
            }
        } else {
            return;
        }

    }


}
