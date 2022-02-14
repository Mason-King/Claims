package claims.Listener;

import Events.ClaimEnterEvent;
import Events.ClaimExitEvent;
import Events.LandClaimEvent;
import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

public class PlayerMove implements Listener {

    @EventHandler
    public void onMove(PlayerMoveEvent e) {
        Player p = e.getPlayer();

        Chunk from = e.getFrom().getChunk();
        Chunk to = e.getTo().getChunk();

        if(!from.equals(to)) {
                //They are leaving?
            Claim toClaim = Claim.chunkToClaims.get(to);
            Claim fromClaim = Claim.chunkToClaims.get(from);

            if(fromClaim == null && toClaim != null) {
                System.out.println("entering");
                ClaimEnterEvent claimEnterEvent = new ClaimEnterEvent(p, e.getFrom(), e.getTo(), toClaim);
                Bukkit.getPluginManager().callEvent(claimEnterEvent);
            } else if(toClaim == null && fromClaim != null) {
                ClaimExitEvent claimExitEvent = new ClaimExitEvent(p, e.getFrom(), e.getTo(), fromClaim);
                Bukkit.getPluginManager().callEvent(claimExitEvent);
            }
        }

        if(!Claim.chunkToClaims.containsKey(e.getTo().getChunk())) return;
        Claim claim = Claim.chunkToClaims.get(e.getTo().getChunk());

        if(claim.isBanned(p)) {
            e.setCancelled(true);
            p.sendMessage(Utils.color("&c&lClaims &7| You have been banned from this claim!"));
        }

    }

    @EventHandler
    public void onEnter(ClaimEnterEvent e) {
        System.out.println("It rand!");
    }

    @EventHandler
    public void onClaim(LandClaimEvent e) {
        System.out.println("claimed some land!");
    }

    @EventHandler
    public void onExit(ClaimExitEvent e) {
        System.out.println("left the claim fag!");
    }



}
