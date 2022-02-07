package claims.Commands;

import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.EntityEffect;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class ClaimCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!(sender instanceof Player)) return false;
        Player p = (Player) sender;
        if(args.length == 0) {
            Claim claim = new Claim(p, p.getLocation());
            p.sendMessage(Utils.color("&c&lClaims &7| You have just claimed a chunk of land!"));
            p.playEffect(EntityEffect.FIREWORK_EXPLODE);
            return false;
        } else {
            if(args.length == 1) {
                //Claim Help message
                p.sendMessage(Utils.color("&c&lClaims &7| Claims help message!"));
                return false;
            }
        }
        return false;
    }
}
