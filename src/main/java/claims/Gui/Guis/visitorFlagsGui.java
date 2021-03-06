package claims.Gui.Guis;

import claims.Gui.Gui;
import claims.Main;
import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class visitorFlagsGui {

    Main main = Main.getInstance();

    //TODO:
    // - Make it so you can click to toggle
    // - Make flags persist
    // - Add flag checks
    // - Add check if claready claims!
    // - add pages to bans

    private File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/visitorFlagsGui.yml");
    private YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public Gui gui(Player p) {
        Gui flagGui = new Gui(Utils.color(config.getString("title")), config.getInt("size")).c();

        List<String> format = config.getStringList("format");
        Utils.makeFormat(p, "visitor", config, flagGui, format, "items");

        flagGui.onClick(e -> {
            Player player = (Player) e.getWhoClicked();
            int slot = e.getSlot();
            int place = config.getInt("place");
            int breakInt = config.getInt("break");
            int chest = config.getInt("chest");
            int use = config.getInt("use");
            int door = config.getInt("door");

            Claim c = Claim.playerClaims.get(player.getUniqueId()).get(0);

            if(slot == place) {
                if(c.isVisitorBlockPlace()) {
                    c.visitorBlockPlace(false);
                } else {
                    c.visitorBlockPlace(true);
                }
                updateGui(p, config, flagGui, format, "items");
            } else if(slot == breakInt) {
                if(c.isVisitorBlockBreak()) {
                    c.visitorBlockBreak(false);
                } else {
                    c.visitorBlockBreak(true);
                }
                updateGui(p, config, flagGui, format, "items");
            } else if(slot == chest) {
                if(c.isVisitorOpenChest()) {
                    c.visitorChestOpen(false);
                } else {
                    c.visitorChestOpen(true);
                }
                updateGui(p, config, flagGui, format, "items");
            } else if(slot == use) {
                if(c.isVisitorUse()) {
                    c.visitorUse(false);
                } else {
                    c.visitorUse(true);
                }
                updateGui(p, config, flagGui, format, "items");
            } else if(slot == door) {
                if(c.isVisitorOpenDoor()) {
                    c.visitorDoorOpen(false);
                } else {
                    c.visitorDoorOpen(true);
                }
                updateGui(p, config, flagGui, format, "items");
            }
        });

        return flagGui;

    }

    public void updateGui(Player p, YamlConfiguration config, Gui gui, List<String> format, String key) {
        for(int i = 0; i < gui.getInventory().getSize() / 9; i++) {
            gui.clearRow(i);
        }
        p.updateInventory();
        Utils.makeFormat(p, "visitor", config, gui, format, "items");
    }

}
