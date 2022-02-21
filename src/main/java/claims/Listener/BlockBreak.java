package claims.Listener;

import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;

import java.util.EnumSet;
import java.util.Set;

public class BlockBreak implements Listener {


    @EventHandler (priority = EventPriority.LOWEST)
    public void onBreak(BlockBreakEvent e) {
        Player p = (Player) e.getPlayer();
        System.out.println("ni");


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
        Player p = (Player) e.getPlayer();

        Chunk c = e.getBlock().getChunk();

        Claim claim = Claim.getClaimAt(p.getWorld(), c.getX(), c.getZ());
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


}
