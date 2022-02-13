package claims.Listener;

import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = (Player) e.getPlayer();

        Chunk c = e.getBlock().getChunk();

        Claim claim = Claim.getClaimAt(p.getWorld(), c.getX(), c.getZ());
        if(claim == null) return;

        if(claim.getOwner().equals(p.getUniqueId())) {
            //the owner!
            return;
        } else if(claim.isTrusted(p)) {
            //They are trusted
            if(!claim.isTrustedBlockBreak()) {
                e.setCancelled(true);
                p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not break in this claim!"));
            }
        } else {
            //they are visitors!
            if (!claim.isVisitorBlockBreak()) {
                e.setCancelled(true);
                p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not break in this claim!"));
            }
        }
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        System.out.println("1");
        Player p = (Player) e.getPlayer();

        Chunk c = e.getBlock().getChunk();
        System.out.println(c.getWorld());

        Claim claim = Claim.getClaimAt(p.getWorld(), c.getX(), c.getZ());
        System.out.println(claim.getId());
        if(claim == null) return;


        if(claim.getOwner().equals(p.getUniqueId())) {
            //the owner!
            return;
        } else if(claim.isTrusted(p)) {
            //They are trusted
            if(!claim.isTrustedBlockPlace()) {
                e.setCancelled(true);
                p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not place in this claim!"));
            }
            return;
        } else {
            //they are visitors!
            if (!claim.isVisitorBlockPlace()) {
                e.setCancelled(true);
                p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not place in this claim!"));
            }
            return;
        }
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;

        Player target = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        Chunk chunk = target.getWorld().getChunkAt(target.getLocation().getBlockX(), target.getLocation().getBlockZ());

        if(Claim.chunkToClaims.containsKey(chunk)) {
           e.setCancelled(true);
           damager.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not PvP here!"));
        }

    }

    @EventHandler
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
            } else {
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
        }

    }

}
