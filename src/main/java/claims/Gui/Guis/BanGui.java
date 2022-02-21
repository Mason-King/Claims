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

    public Gui gui(Player p) {
        Gui banGui = new Gui(Utils.color(config.getString("title")), config.getInt("size"))
                .c();

        List<String> format = config.getStringList("format");
        Utils.makeFormat(config, banGui, format, "items");

        Claim claim = Claim.getClaims(p).get(0);

        int key = 0;
        int page = 0;
        List<String> temp = new ArrayList<>();
        for(int i = 0; i < 50; i++) {
            temp.add(UUID.randomUUID().toString());
        }
        for(String s : temp) {
            if(key == config.getInt("banSlots")) {
                page++;
                key = 0;
            }
            if(page > 0) {
            }
            banGui.setItemPage(page, getBanned(s, temp.indexOf(s)));
            key++;
        }

        banGui.onClick(e -> {
            int slot = e.getSlot();

            int next = config.getInt("next");
            int previous = config.getInt("previous");


            if(slot == next) {
                System.out.println("next!");
                banGui.nextPage();
            } else if(slot == previous) {
                banGui.prevPage();
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
