package claims.Gui.Guis;

import claims.Gui.Gui;
import claims.Main;
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
        Gui flagGui = new Gui(Utils.color(config.getString("title")), config.getInt("size"))
                .c();

        List<String> format = config.getStringList("format");
        Utils.makeFormat(p, "trusted", config, flagGui, format, "items");

        return flagGui;

    }

}
