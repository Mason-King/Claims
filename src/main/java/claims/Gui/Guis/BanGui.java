package claims.Gui.Guis;

import claims.Gui.Gui;
import claims.Main;
import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;

import java.io.File;
import java.util.*;

public class BanGui {

    Main main = Main.getInstance();

    private File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/banGui.yml");
    private YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public Gui gui(Player p, int page) {
        Gui banGui = new Gui(Utils.color(config.getString("title")), config.getInt("size"), config.getInt("size"))
                .c();

        List<String> format = config.getStringList("format");
        Utils.makeFormat(config, banGui, format, "items");

        Claim claim = Claim.playerClaims.get(p.getUniqueId()).get(0);

        List<String> temp = claim.banned;

        int max = config.getInt("banSlots");

        for(int i = (page * max); i < (page * max + max) && i < temp.size(); i++) {
           banGui.i(getBanned(temp.get(i), temp.indexOf(temp.get(i))));
        }

        banGui.onClick(e -> {
           int slot = e.getSlot();

           int next = config.getInt("next");
           int previous = config.getInt("previous");

           if(slot == next) {
               if((page + 1) * max > temp.size()) return;
               p.closeInventory();
               new BanGui().gui(p, page + 1).show(p);
           } else if(slot == previous){
               if(page - 1 < 0) return;
               p.closeInventory();
               new BanGui().gui(p, page - 1).show(p);
           }
        });

        return banGui;
    }

    public ItemStack getBanned(String s, int num) {
        ItemStack i = new ItemStack(Material.matchMaterial(config.getString("banItem.material")));
        if(i.getType().equals(Material.PLAYER_HEAD)) {
            SkullMeta sm = (SkullMeta) i.getItemMeta();
            sm.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(s)));
            i.setItemMeta(sm);
        }

        ItemMeta im = i.getItemMeta();
        //im.setDisplayName(Utils.color(config.getString("banItem.name").replace("{player}", Bukkit.getOfflinePlayer(UUID.fromString(s)).getName())));
        im.setDisplayName(Utils.color(config.getString("banItem.name")));
        List<String> lore = new ArrayList<>();
        for(String loreString : config.getStringList("banItem.lore")) {
            lore.add(Utils.color(loreString.replace("{player}", num + "")));
        }
        im.setLore(lore);

        i.setItemMeta(im);

        return i;
    }

}
