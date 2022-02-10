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
            p.sendMessage(Utils.color("&c&lClaims &7| You have unclaimed this chunk!"));
            Chunk c = p.getLocation().getChunk();
            Claim claim = Claim.getClaimAt(p.getWorld(),c.getX(), c.getZ());
            claim.setUnclaimed(true);
        }
        return false;
    }
}
