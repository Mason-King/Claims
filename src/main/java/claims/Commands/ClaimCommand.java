package claims.Commands;

import Events.LandClaimEvent;
import claims.Gui.Guis.BanGui;
import claims.Gui.Guis.flagsGui;
import claims.Main;
import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.EntityEffect;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

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
                p.sendMessage(Utils.color(main.getConfig().getString("messages.alreadyClaimed")));

                return false;
            }
            Claim claim = new Claim(p, p.getLocation());
            p.sendMessage(Utils.color(main.getConfig().getString("messages.claimed")));
            p.playEffect(EntityEffect.FIREWORK_EXPLODE);

            LandClaimEvent landClaimEvent = new LandClaimEvent(claim.getId(), p, claim.getChunkX(), claim.getChunkZ(), claim);
            Bukkit.getPluginManager().callEvent(landClaimEvent);

            return false;
        } else {
            if(args[0].equalsIgnoreCase("help")) {
                //Claim Help message
                p.sendMessage(Utils.color("&c&lClaims &7| Claims help message!"));
                return false;
            } else if(args[0].equalsIgnoreCase("ban")) {
                if(args.length == 2) {
                    if(Bukkit.getPlayer(args[1]) == null) {
                        p.sendMessage(Utils.color("&c&lClaims &7| Invalid player!"));
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
                    p.sendMessage(Utils.color("&c&lClaims &7| You have banned x from all your claims!"));
                    return false;
                }
            } else if(args[0].equalsIgnoreCase("unban")) {
                if(args.length == 2) {
                    if(Bukkit.getPlayer(args[1]) == null) {
                        p.sendMessage(Utils.color("&c&lClaims &7| Invalid player!"));
                        return false;
                    }
                    Player target = Bukkit.getPlayer(args[1]);
                    Claim claim = Claim.getClaims(p).get(0);
                    claim.unbanClaims(target);
                    p.sendMessage(Utils.color("&c&lClaims &7| You have unbanned x from all of your claims!"));
                }
            } else if(args[0].equalsIgnoreCase("bans")) {
                new BanGui().gui(p).show(p);
            } else if(args[0].equalsIgnoreCase("trust")) {
                if(Bukkit.getPlayer(args[1]) == null) {
                    p.sendMessage(Utils.color("&c&lClaims &7| Invalid player!"));
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                Claim claim = Claim.getClaims(p).get(0);
                claim.trustClaims(target);
                p.sendMessage(Utils.color("&c&lClaims &7| You have trusted x to your claims!"));
            } else if(args[0].equalsIgnoreCase("untrust")) {
                if(Bukkit.getPlayer(args[1]) == null) {
                    p.sendMessage(Utils.color("&c&lClaims &7| Invalid player!"));
                    return false;
                }
                Player target = Bukkit.getPlayer(args[1]);
                Claim claim = Claim.getClaims(p).get(0);
                claim.untrustClaims(target);
                p.sendMessage(Utils.color("&c&lClaims &7| You have trusted x to your claims!"));
            } else if(args[0].equalsIgnoreCase("flags")) {
                new flagsGui().gui().show(p);
            } else if(args[0].equalsIgnoreCase("admin")) {
                if(!p.hasPermission("claims.admin")) {
                    p.sendMessage(Utils.color("&c&lClaims &7| Sorry, you do not have enough permission for this!"));
                    return false;
                } else {
                    if(args[1].equalsIgnoreCase("remove")) {
                        if(args.length < 3) {
                            p.sendMessage(Utils.color("&c&lClaims &7| incorrect usage try : /claim admin remove <player>"));
                            return false;
                        } else {
                            if(Bukkit.getPlayer(args[0]) == null) {
                                p.sendMessage(Utils.color("&c&lClaims &7| Invalid player!"));
                                return false;
                            } else {
                                Player target = Bukkit.getPlayer(args[0]);
                                List<Claim> claims  = Claim.getClaims(target.getPlayer());
                                for(Claim claim : claims) {
                                    claim.setUnclaimed(true);
                                }
                                p.sendMessage(Utils.color("&c&lClaims &7| You have unclaimed all of x's claims!"));
                                return false;
                            }
                        }
                    } else if(args[1].equalsIgnoreCase("unclaim")) {
                        Chunk c = p.getLocation().getWorld().getChunkAt(p.getLocation());

                        Claim claim = Claim.chunkToClaims.get(c);

                        if(claim == null) {
                            p.sendMessage(Utils.color("&c&lClaims &7| Noone has claimed this land yet!"));
                            return false;
                        }

                        claim.setUnclaimed(true);

                        p.sendMessage(Utils.color("&c&lClaims &7| You have unclaimed the land you are standing in"));
                    } else if(args[1].equalsIgnoreCase("claim")) {
                        if(args.length < 3) {
                            p.sendMessage(Utils.color("&c&lClaims &7| Incorrect usage : try /claims admin claim <player>"));
                            return false;
                        } else {
                            if(Bukkit.getPlayer(args[2]) == null) {
                                p.sendMessage(Utils.color("&c&lClaims &7| Invalid player!"));
                                return false;
                            } else {
                                Player target = Bukkit.getPlayer(args[2]);
                                Claim claim = new Claim(target, p.getLocation());

                                p.sendMessage(Utils.color("&c&lClaims &7| You have claimed a chunk of land for x"));
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
}
