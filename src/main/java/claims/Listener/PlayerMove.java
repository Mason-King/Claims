package claims.Listener;

import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();
        if(!Claim.chunkToClaims.containsKey(e.getTo().getChunk())) return;
        Claim claim = Claim.chunkToClaims.get(e.getTo().getChunk());

        if(claim.isBanned(p)) {
            e.setCancelled(true);
            p.sendMessage(Utils.color("&c&lClaims &7| You have been banned from this claim!"));
            return;
        }

    }

}
