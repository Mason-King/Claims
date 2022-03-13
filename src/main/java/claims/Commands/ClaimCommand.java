package claims.Commands;

import Events.LandClaimEvent;
import claims.Gui.Guis.BanGui;
import claims.Gui.Guis.TrustedGui;
import claims.Gui.Guis.flagsGui;
import claims.Main;
import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ClaimCommand implements CommandExecutor {

    Main main = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if(args.length == 0) {
            Chunk c = p.getWorld().getChunkAt(p.getLocation());
            if(Claim.chunkToClaims.containsKey(c)) {
                p.sendMessage(Utils.color(main.getConfig().getString("messages.alreadyClaimed")
                        .replace("{player}", Claim.chunkToClaims.get(c).getOwnerName())));

                return false;
            }
            Claim claim = new Claim(p, p.getLocation());
            p.sendMessage(Utils.color(main.getConfig().getString("messages.claimed")));
            p.spawnParticle(Particle.FIREWORKS_SPARK, p.getLocation(), 120);

            LandClaimEvent landClaimEvent = new LandClaimEvent(claim.getId(), p, claim.getChunkX(), claim.getChunkZ(), claim);
            Bukkit.getPluginManager().callEvent(landClaimEvent);

            return false;
        } else {
            if(args[0].equalsIgnoreCase("help")) {
                //Claim Help message
                for(String str : main.getConfig().getStringList("messages.claimHelp")) {
                    p.sendMessage(Utils.color(str));
                }
                return false;
            } else if(args[0].equalsIgnoreCase("home")) {
                if(Claim.getClaims(p) == null) return false;
                Claim claim = Claim.getClaims(p).get(0);
                p.teleport(claim.getHome());
            } else if(args[0].equalsIgnoreCase("sethome")) {
                if(Claim.getClaims(p) == null) return false;
                Claim claim = Claim.getClaims(p).get(0);
                claim.setHome(p.getLocation());
                p.sendMessage(Utils.color(main.getConfig().getString("messages.setHome")));
            } else if(args[0].equalsIgnoreCase("trusted")) {
                new TrustedGui().gui(p, 0).show(p);
            } else if(args[0].equalsIgnoreCase("ban")) {
                if(args.length == 2) {
                    if(Bukkit.getPlayer(args[1]) == null) {
                        p.sendMessage(Utils.color(main.getConfig().getString("messages.invalidPlayer")));
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    Claim claim = Claim.getClaims(p).get(0);

                    if(target.getLocation().getChunk().equals(claim.getChunk())) {
                        List<Claim> claims = Claim.playerClaims.get(p.getUniqueId());
                        Claim last = claims.get(claims.size() - 1);
                        Chunk tpChunk = last.getWorld().getChunkAt(last.getChunkX() + 1, last.getChunkZ() + 1);
                        Location tpLoc = new Location(last.getWorld(), tpChunk.getX() * 16, last.getWorld().getHighestBlockAt(tpChunk.getX() * 16, tpChunk.getZ() * 16).getY() + 1, tpChunk.getZ() * 16);
                        target.teleport(tpLoc);
                    }

                    claim.banClaims(target);
                    p.sendMessage(Utils.color(main.getConfig().getString("messages.playerBanned").replace("{player}", target.getName())));
                    return false;
                }
            } else if(args[0].equalsIgnoreCase("unban")) {
                if(args.length == 2) {
                    if(Bukkit.getPlayer(args[1]) == null) {
                        p.sendMessage(Utils.color(main.getConfig().getString("messages.invalidPlayer")));
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    Claim claim = Claim.getClaims(p).get(0);
                    claim.unbanClaims(target);
                    p.sendMessage(Utils.color(main.getConfig().getString("messages.playerUnbanned").replace("{player}", target.getName())));
                }
            } else if(args[0].equalsIgnoreCase("bans")) {
                new BanGui() .gui(p, 0).show(p);
            } else if(args[0].equalsIgnoreCase("trust")) {
                if(Bukkit.getPlayer(args[1]) == null) {
                    p.sendMessage(Utils.color(main.getConfig().getString("messages.invalidPlayer")));
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                Claim claim = Claim.getClaims(p).get(0);
                claim.trustClaims(target);
                p.sendMessage(Utils.color(main.getConfig().getString("messages.playerTrusted")));
            } else if(args[0].equalsIgnoreCase("untrust")) {
                if(args.length < 2) {
                    p.sendMessage(Utils.color(main.getConfig().getString("messages.playerUntrusted")));
                    return false;
                }
                if(Bukkit.getPlayer(args[2]) == null) {
                    p.sendMessage(Utils.color(main.getConfig().getString("messages.invalidPlayer")));
                    return false;
                }
                Player target = Bukkit.getPlayer(args[2]);
                Claim claim = Claim.getClaims(p).get(0);
                claim.untrustClaims(target);
                p.sendMessage(Utils.color(main.getConfig().getString("messages.playerTrusted").replace("{player}", target.getName())));
            } else if(args[0].equalsIgnoreCase("flags")) {
                if(Claim.playerClaims.get(p.getUniqueId()) == null) {
                    p.sendMessage(Utils.color(main.getConfig().getString("messages.notClaimed")));
                    return false;
                }
                new flagsGui().gui().show(p);
            } else if(args[0].equalsIgnoreCase("view")) {
                Chunk chunk = p.getLocation().getChunk();

                int minY = p.getLocation().getBlockY();

                Particle.DustOptions dustOptions = null;
                for(Chunk c : getChunks(chunk , 1)) {
                    if(Claim.chunkToClaims.get(c) != null && Claim.chunkToClaims.get(c).getOwner().equals(p.getUniqueId())) {
                        dustOptions = new Particle.DustOptions(Color.fromRGB(0, 255, 0), 1);
                    } else {
                        dustOptions = new Particle.DustOptions(Color.fromRGB(255, 0, 0), 1);
                    }

                    int minX = c.getX()*16;
                    int minZ = c.getZ()*16;

                    for(int i = 0; i < 16; i++) {
                        int x1 = minX + i;
                        int z1 = minZ + i;

                        int x2 = (minX + 15) - i;
                        int z2 = (minZ + 15) - i;


                        p.spawnParticle(Particle.REDSTONE, x1, minY, minZ, 30, dustOptions);
                        p.spawnParticle(Particle.REDSTONE, minX, minY, z1, 30, dustOptions);
                        p.spawnParticle(Particle.REDSTONE, x2, minY, minZ + 15, 30, dustOptions);
                        p.spawnParticle(Particle.REDSTONE, minX + 15, minY, z2, 30, dustOptions);

                    }

                }

            } else if(args[0].equalsIgnoreCase("admin")) {
                if(!p.hasPermission("claims.admin")) {
                    p.sendMessage(Utils.color(main.getConfig().getString("messages.noPermission")));
                    return false;
                } else {
                    if(args[1].equalsIgnoreCase("remove")) {
                        if(args.length < 3) {
                            p.sendMessage(Utils.color(main.getConfig().getString("messages.adminRemove")));
                            return false;
                        } else {
                            if(Bukkit.getPlayer(args[2]) == null) {
                                p.sendMessage(Utils.color(main.getConfig().getString("messages.invalidPlayer")));
                                return false;
                            } else {
                                Player target = Bukkit.getPlayer(args[2]);
                                List<Claim> claims  = Claim.getClaims(target.getPlayer());
                                for(Claim claim : claims) {
                                    claim.setUnclaimed(true);
                                }
                                p.sendMessage(Utils.color(main.getConfig().getString("messages.adminUnclaimed".replace("{player}", target.getName()))));
                                return false;
                            }
                        }
                    } else if(args[1].equalsIgnoreCase("unclaim")) {
                        Chunk c = p.getLocation().getWorld().getChunkAt(p.getLocation());

                        Claim claim = Claim.chunkToClaims.get(c);

                        if(claim == null) {
                            p.sendMessage(Utils.color(main.getConfig().getString("messages.notClaimed")));
                            return false;
                        }

                        claim.setUnclaimed(true);

                        p.sendMessage(Utils.color(main.getConfig().getString("messages.unclaimed")));
                    } else if(args[1].equalsIgnoreCase("claim")) {
                        if(args.length < 3) {
                            p.sendMessage(Utils.color(main.getConfig().getString("messages.adminClaim")));
                            return false;
                        } else {
                            if(Bukkit.getPlayer(args[2]) == null) {
                                p.sendMessage(Utils.color(main.getConfig().getString("messages.invalidPlayer")));
                                return false;
                            } else {
                                Player target = Bukkit.getPlayer(args[2]);
                                Claim claim = new Claim(target, p.getLocation());

                                p.sendMessage(Utils.color(main.getConfig().getString("messages.adminClaimed")));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

    public List<Chunk> getChunks(Chunk centerChunk, int radius){
        List<Chunk> chunks = new ArrayList<>();
        for (int x = centerChunk.getX()-radius; x < centerChunk.getX() + (1 + radius); x++) {
            for (int z = centerChunk.getZ()-radius; z < centerChunk.getZ() + (1 + radius); z++) {
                Chunk chunk = centerChunk.getWorld().getChunkAt(x, z);
                chunks.add(chunk);
            }
        }
        return chunks;
    }

}
