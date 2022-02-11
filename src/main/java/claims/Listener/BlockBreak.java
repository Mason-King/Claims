package claims.Listener;

import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = (Player) e.getPlayer();

        Chunk c = e.getBlock().getChunk();

        Claim claim = Claim.getClaimAt(p.getWorld(), c.getX(), c.getZ());
        if(claim == null) return;

        if(!claim.isTrusted(p) && !claim.isUnclaimed() && !claim.getOwner().equals(p.getUniqueId())) {
            e.setCancelled(true);
            p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not break in this claim!"));
            return;
        }

    }

    @EventHandler
    public void onPlace(BlockPlaceEvent e) {
        Player p = (Player) e.getPlayer();

        Chunk c = e.getBlock().getChunk();

        Claim claim = Claim.getClaimAt(p.getWorld(), c.getX(), c.getZ());
        if(claim == null) return;

        if(!claim.isTrusted(p) && !claim.isUnclaimed() && !claim.getOwner().equals(p.getUniqueId())) {
            e.setCancelled(true);
            p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not place in this claim!"));
            return;
        }

    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        if(!(e.getEntity() instanceof Player) || !(e.getDamager() instanceof Player)) return;

        Player target = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        Chunk chunk = target.getWorld().getChunkAt(target.getLocation().getBlockX(), target.getLocation().getBlockZ());

        if(!Claim.chunkToClaims.containsKey(chunk)) {

        }

    }

}
