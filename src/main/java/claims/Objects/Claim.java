package claims.Objects;

import claims.Main;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class Claim {

    Main main = Main.getInstance();

    private int id;
    private UUID owner;
    private String ownerName;
    private Chunk chunk;
    private World world;
    private Location chunkLocation;
    private int chunkX;
    private int chunkZ;

    //variable to know if it needs to be saved to help optimize!
    private boolean unclaimed = false;
    //Manage the players
    public List<String> banned = new ArrayList<>();
    public List<String> trusted = new ArrayList<>();

    //flags - default for visitiors
    private boolean visitorBlockBreak = false;
    private boolean visitorBlockPlace = false;
    private boolean visitorOpenChest = false;
    private boolean visitorOpenDoor = false;
    private boolean visitorUse = false;

    //flags - default for trusted
    private boolean trustedBlockBreak = true;
    private boolean trustedBlockPlace = true;
    private boolean trustedOpenChest = false;
    private boolean trustedOpenDoor = false;
    private boolean trustedUse = true;

    //List of all current claims!
    public static Map<Integer, Claim> claims = new HashMap<>();
    public static Map<Chunk, Claim> chunkToClaims = new HashMap<>();
    public static Map<UUID, List<Claim>> playerClaims = new HashMap<>();

    public Claim(int id, UUID owner, World world, String ownerName, int chunkX, int chunkZ, List<String> banned, List<String> trusted) {
        this.id = id;
        this.owner = owner;
        this.ownerName = ownerName;
        this.chunkX = chunkX;
        this.chunkZ = chunkZ;
        this.world = world;
        this.chunk = world.getChunkAt(chunkX, chunkZ);
        this.chunkLocation = new Location(world, chunkX, world.getHighestBlockYAt(chunkX, chunkZ), chunkZ);
        this.banned = banned;
        this.trusted = trusted;

        claims.put(id, this);
        chunkToClaims.put(chunk, this);
        List<Claim> temp = playerClaims.containsKey(id) ? playerClaims.get(id) : new ArrayList<>();
        temp.add(this);
        playerClaims.put(owner, temp);
    }

    public Claim(Player owner, Location loc) {
        this.owner = owner.getUniqueId();
        this.ownerName = owner.getName();
        this.chunk = loc.getChunk();
        this.world = loc.getWorld();
        this.chunkX = chunk.getX();
        this.chunkZ = chunk.getZ();
        this.id = claims.size() + 1;
        claims.put(claims.size() + 1, this);
        chunkToClaims.put(chunk, this);
        List<Claim> temp = playerClaims.containsKey(id) ? playerClaims.get(id) : new ArrayList<>();
        temp.add(this);
        playerClaims.put(owner.getUniqueId(), temp);
    }

    public static List<Claim> getClaims(Player p) {
        return playerClaims.get(p.getUniqueId());
    }

    public void banClaims(Player target) {
        for(Claim claim : playerClaims.get(owner)) {
            if(claim.isBanned(target)) continue;
            claim.banPlayer(target);
        }
    }

    public static Claim getClaimAt(World world, int x, int z) {
        Chunk c = world.getChunkAt(x, z);
        return chunkToClaims.get(c);
    }

    public void unbanClaims(Player target) {
        for(Claim claim : playerClaims.get(owner)) {
            if(!claim.isBanned(target)) continue;
            claim.unbanPlayer(target);
        }
    }

    public void banPlayer(Player p) {
        banned.add(p.getUniqueId().toString());
        banClaims(p);
    }

    public boolean isBanned(Player p) {
        if(banned.contains(p.getUniqueId().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean isTrusted(Player p) {
        if(trusted.contains(p.getUniqueId().toString())) {
            return true;
        } else {
            return false;
        }
    }

    public boolean unbanPlayer(Player p) {
        if(banned.contains(p.getUniqueId().toString())) {
            banned.remove(p.getUniqueId().toString());
            return true;
        }
        return false;
    }

    public void trustClaims(Player target) {
        for(Claim claim : playerClaims.get(owner)) {
            if(claim.isTrusted(target)) continue;
            claim.trustPlayer(target);
        }
    }

    public void untrustClaims(Player target) {
        for(Claim claim : playerClaims.get(owner)) {
            if(!claim.isTrusted(target)) continue;
            claim.unTrust(target);
        }
    }

    public void trustPlayer(Player p) {
        trusted.add(p.getUniqueId().toString());
    }

    public boolean unTrust(Player p) {
        if(trusted.contains(p.getUniqueId().toString())) {
            trusted.remove(p.getUniqueId().toString());
            return true;
        }
        return false;
    }

    public World getWorld() {
        return world;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public UUID getOwner() {
        return owner;
    }

    public int getId() {
        return id;
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
    }

    public Location getChunkLocation() {
        return chunkLocation;
    }

    public void setChunkLocation(Location chunkLocation) {
        this.chunkLocation = chunkLocation;
    }

    public int getChunkX() {
        return chunkX;
    }

    public void setChunkX(int chunkX) {
        this.chunkX = chunkX;
    }

    public int getChunkZ() {
        return chunkZ;
    }

    public void setChunkZ(int chunkZ) {
        this.chunkZ = chunkZ;
    }

    public boolean isUnclaimed() {
        return unclaimed;
    }

    public void setUnclaimed(Boolean b) {
        claims.remove(this.id);
        chunkToClaims.remove(this.chunk);
        List<Claim> temp = playerClaims.containsKey(id) ? playerClaims.get(id) : new ArrayList<>();
        temp.remove(this);
        playerClaims.remove(owner);
        playerClaims.put(owner, temp);

        this.unclaimed = b;
    }

    public boolean isVisitorBlockBreak() {
        return visitorBlockBreak;
    }

    public void setVisitorBlockBreak(boolean visitorBlockBreak) {
        this.visitorBlockBreak = visitorBlockBreak;
    }

    public boolean isVisitorBlockPlace() {
        return visitorBlockPlace;
    }

    public void setVisitorBlockPlace(boolean visitorBlockPlace) {
        this.visitorBlockPlace = visitorBlockPlace;
    }

    public boolean isVisitorOpenChest() {
        return visitorOpenChest;
    }

    public void setVisitorOpenChest(boolean visitorOpenChest) {
        this.visitorOpenChest = visitorOpenChest;
    }

    public boolean isVisitorOpenDoor() {
        return visitorOpenDoor;
    }

    public void setVisitorOpenDoor(boolean visitorOpenDoor) {
        this.visitorOpenDoor = visitorOpenDoor;
    }

    public boolean isVisitorUse() {
        return visitorUse;
    }

    public void setVisitorUse(boolean visitorUse) {
        this.visitorUse = visitorUse;
    }

    public boolean isTrustedBlockBreak() {
        return trustedBlockBreak;
    }

    public void setTrustedBlockBreak(boolean trustedBlockBreak) {
        this.trustedBlockBreak = trustedBlockBreak;
    }

    public boolean isTrustedBlockPlace() {
        return trustedBlockPlace;
    }

    public void setTrustedBlockPlace(boolean trustedBlockPlace) {
        this.trustedBlockPlace = trustedBlockPlace;
    }

    public boolean isTrustedOpenChest() {
        return trustedOpenChest;
    }

    public void setTrustedOpenChest(boolean trustedOpenChest) {
        this.trustedOpenChest = trustedOpenChest;
    }

    public boolean isTrustedOpenDoor() {
        return trustedOpenDoor;
    }

    public void setTrustedOpenDoor(boolean trustedOpenDoor) {
        this.trustedOpenDoor = trustedOpenDoor;
    }

    public boolean isTrustedUse() {
        return trustedUse;
    }

    public void setTrustedUse(boolean trustedUse) {
        this.trustedUse = trustedUse;
    }
}
