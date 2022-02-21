package claims.Commands;

import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Chunk;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.nio.Buffer;

public class UnclaimCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        Player p = (Player) sender;
        if(args.length == 0) {
            Chunk c = p.getLocation().getChunk();
            Claim claim = Claim.getClaimAt(p.getWorld(),c.getX(), c.getZ());
            if(claim == null || !claim.getOwner().equals(p.getUniqueId())) {
                p.sendMessage(Utils.color("&c&lClaims &7| You havent claimed this chunk!"));
                return false;
            }
            p.sendMessage(Utils.color("&c&lClaims &7| You have unclaimed this chunk!"));
            claim.setUnclaimed(true);
        }
        return false;
    }
}
