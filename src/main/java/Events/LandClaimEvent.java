package Events;

import claims.Objects.Claim;
import jdk.internal.net.http.common.Cancelable;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class LandClaimEvent extends Event {

    private int id;
    private Player player;
    private int chunkX;
    private int chunkZ;
    private Claim claim;

    private static final HandlerList HANDLERS = new HandlerList();

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public LandClaimEvent(int id, Player player, int chunkX, int chunkZ, Claim claim) {
        this.id = id;
        this.player = player;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.claim = claim;
    }

    @Override
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public int getId() {
        return this.id;
    }
    public Player getPlayer() {
        return this.player;
    }

    public int getChunkX() {
        return this.chunkX;
    }

    public int getChunkZ() {
        return this.chunkZ;
    }

    public Claim getClaim() {
        return this.claim;
    }


}
