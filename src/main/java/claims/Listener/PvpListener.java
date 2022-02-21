package claims.Listener;

import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Chunk;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

public class PvpListener implements Listener {

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e) {
        System.out.println("ran");
        if(!(e.getEntity() instanceof Player) && !(e.getDamager() instanceof Player)) return;

        Player target = (Player) e.getEntity();
        Player damager = (Player) e.getDamager();

        Chunk chunk = target.getLocation().getChunk();

        System.out.println(chunk);

        System.out.println(Claim.chunkToClaims);

        if(Claim.chunkToClaims.containsKey(chunk)) { ;
            e.setCancelled(true);
            damager.sendMessage(Utils.color("&c&lClaims &7| Sorry, you can not PvP here!"));
        }

    }

}
