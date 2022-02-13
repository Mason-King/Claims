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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

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

        for(String s : claim.banned) {
            int key = 0, page = 0;
            if(key == config.getInt("banSlots")) {
                page++;
                key = 0;
            }
            if(page > 0) {
                HashMap<Integer, ItemStack> inv = new HashMap<>();
                for(int i = 0; i < banGui.getInventory().getContents().length -1; i++) {
                }
            }
        }

        return banGui;
    }

    public ItemStack getBanned(String s) {
        ItemStack i = new ItemStack(Material.matchMaterial(config.getString("banItem.material")));
        if(i.getType().equals(Material.PLAYER_HEAD)) {
            SkullMeta sm = (SkullMeta) i.getItemMeta();
            sm.setOwningPlayer(Bukkit.getOfflinePlayer(UUID.fromString(s)));
            i.setItemMeta(sm);
        }

        ItemMeta im = i.getItemMeta();
        im.setDisplayName(Utils.color(config.getString("banItem.name").replace("{player}", Bukkit.getOfflinePlayer(UUID.fromString(s)).getName())));
        List<String> lore = new ArrayList<>();
        for(String loreString : config.getStringList("banItem.lore")) {
            lore.add(Utils.color(loreString.replace("{player}", Bukkit.getOfflinePlayer(UUID.fromString(s)).getName())));
        }
        im.setLore(lore);

        i.setItemMeta(im);

        return i;
    }

}
