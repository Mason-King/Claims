package claims.Utils;

import org.bukkit.ChatColor;
import org.bukkit.Chunk;
import org.bukkit.Location;

import java.util.List;

public class Utils {

    public static String color(String s) {
        return ChatColor.translateAlternateColorCodes('&', s);
    }

    public List<Chunk> getRaduis(int radius, Location start) {
        List<Chunk> chunks = null;
        chunks.add(start.getChunk());


        return chunks;
    }

}
