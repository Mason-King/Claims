package claims.Gui.Guis;

import claims.Gui.Gui;
import claims.Main;
import claims.Utils.Utils;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.File;
import java.util.List;

public class flagsGui {

    Main main = Main.getInstance();

    private File file = new File(main.getDataFolder().getAbsolutePath() + "/Guis/flagsGui.yml");
    private YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

    public Gui gui() {
        Gui flagGui = new Gui(Utils.color(config.getString("title")), config.getInt("size")).c();

        List<String> format = config.getStringList("format");
        Utils.makeFormat(config, flagGui, format, "items");

        flagGui.onClick(e -> {
            int slot = e.getSlot();
            int trusted = config.getInt("trusted");
            int visitor = config.getInt("visitor");
            if(slot == trusted) {
                new trustedFlagsGui().gui((Player) e.getWhoClicked()).show((Player) e.getWhoClicked());
            }
            if(slot == visitor) {
                new visitorFlagsGui().gui((Player) e.getWhoClicked()).show((Player) e.getWhoClicked());
            }
        });

        return flagGui;

    }

}
