package Events;

import claims.Objects.Claim;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class ClaimEnterEvent extends Event {

    private static final HandlerList HANDLERS = new HandlerList();

    private Player player;
    private Location from;
    private Location to;
    private Claim claim;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public ClaimEnterEvent(Player p, Location from, Location to, Claim claim) {
        this.player = p;
        this.from = from;
        this.to = to;
        this.claim = claim;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public Player getPlayer() {
        return player;
    }

    public Location getFrom() {
        return from;
    }

    public Location getTo() {
        return to;
    }

    public Claim getClaim() {
        return claim;
    }
}
