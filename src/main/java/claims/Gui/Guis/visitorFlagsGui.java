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

    private File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/visitorFlagsGui.yml");
    private YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public Gui gui(Player p) {
        Gui flagsGui = new Gui(Utils.color(config.getString("title")), config.getInt("size")).c();

        List<String> format = config.getStringList("format");
        Utils.makeFormat(p, "visitor", config, flagsGui, format, "items");

        flagsGui.onClick(e -> {
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
                    c.setVisitorBlockPlace(false);
                } else {
                    c.setVisitorBlockPlace(true);
                }
                updateGui(p, config, flagsGui, format, "items");
            } else if(slot == breakInt) {
                if(c.isVisitorBlockBreak()) {
                    c.setVisitorBlockBreak(false);
                } else {
                    c.setVisitorBlockBreak(true);
                }
                updateGui(p, config, flagsGui, format, "items");
            } else if(slot == chest) {
                if(c.isVisitorOpenChest()) {
                    c.setVisitorOpenChest(false);
                } else {
                    c.setVisitorOpenChest(true);
                }
                updateGui(p, config, flagsGui, format, "items");
            } else if(slot == use) {
                if(c.isVisitorUse()) {
                    c.setVisitorUse(false);
                } else {
                    c.setVisitorUse(true);
                }
                updateGui(p, config, flagsGui, format, "items");
            } else if(slot == door) {
                if(c.isVisitorOpenDoor()) {
                    c.setVisitorOpenDoor(false);
                } else {
                    c.setVisitorOpenDoor(true);
                }
                updateGui(p, config, flagsGui, format, "items");
            }
        });

        return flagsGui;
    }

    public void updateGui(Player p, YamlConfiguration config, Gui gui, List<String> format, String key) {
        for(int i = 0; i < gui.getInventory().getSize() / 9; i++) {
            gui.clearRow(i);
        }
        p.updateInventory();
        Utils.makeFormat(p, "visitor", config, gui, format, "items");
    }

}
