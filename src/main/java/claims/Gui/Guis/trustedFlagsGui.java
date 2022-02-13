package claims.Gui.Guis;

import claims.Gui.Gui;
import claims.Main;
import claims.Objects.Claim;
import claims.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class trustedFlagsGui {

    Main main = Main.getInstance();

    //TODO:
    // - Make it so you can click to toggle
    // - Make flags persist
    // - Add flag checks

    private File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/trustedFlagsGui.yml");
    private YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public Gui gui(Player p) {
        Gui flagGui = new Gui(Utils.color(config.getString("title")), config.getInt("size")).c();

        List<String> format = config.getStringList("format");
        Utils.makeFormat(p, "trusted", config, flagGui, format, "items");

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
                if(c.isTrustedBlockPlace()) {
                    c.setTrustedBlockPlace(false);
                } else {
                    c.setTrustedBlockPlace(true);
                }
                updateGui(p, config, flagGui, format, "items");
            } else if(slot == breakInt) {
                if(c.isTrustedBlockBreak()) {
                    c.setTrustedBlockBreak(false);
                } else {
                    c.setTrustedBlockBreak(true);
                }
                updateGui(p, config, flagGui, format, "items");
            } else if(slot == chest) {
                if(c.isTrustedOpenChest()) {
                    c.setTrustedOpenChest(false);
                } else {
                    c.setTrustedOpenChest(true);
                }
                updateGui(p, config, flagGui, format, "items");
            } else if(slot == use) {
                if(c.isTrustedUse()) {
                    c.setTrustedUse(false);
                } else {
                    c.setTrustedUse(true);
                }
                updateGui(p, config, flagGui, format, "items");
            } else if(slot == door) {
                if(c.isTrustedOpenDoor()) {
                    c.setTrustedOpenDoor(false);
                } else {
                    c.setTrustedOpenDoor(true);
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
        Utils.makeFormat(p, "trusted", config, gui, format, "items");
    }

}
