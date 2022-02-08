package claims.Listener;

import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

public class BlockBreak implements Listener {

    @EventHandler
    public void onBreak(BlockBreakEvent e) {
        Player p = (Player) e.getPlayer();

        Claim claim = Claim.getClaimAt(p.getWorld(), e.getBlock().getX(), e.getBlock().getZ());

        if(!claim.getOwner().equals(p.getUniqueId()) && !claim.isTrusted(p)) {
            e.setCancelled(true);
            p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not break in this claim!"));
            return;
        }

    }

}
