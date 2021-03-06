package claims.Utils;

import claims.Gui.Gui;
import claims.Objects.Claim;
import org.bukkit.*;
import org.bukkit.block.BlockFace;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
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

    public static HashMap<Integer, ItemStack> inv = new HashMap<>();

    public static HashMap<Integer, ItemStack> getInv() {
        return inv;
    }

    public static List<String> color(List<String> list) {
        List<String> colored = new ArrayList<>();
        for(String s : list) {
            colored.add(color(s));
        }
        return colored;
    }

    public static void makeFormat(FileConfiguration config, Gui gui, List<String> toFormat, String keyForItems) {

        int size = gui.getInventory().getSize();
        if(toFormat.size() == size / 9) {
            for(int i = 0; i < (size / 9); i++) {
                String s = toFormat.get(i);
                for(int z = 0; z < 9; z++) {
                    String removeSpaces = s.replaceAll(" ", "");
                    char individual = removeSpaces.charAt(z);
                    if(i > 0) {
                        if(config.get(keyForItems + "." + individual) == null) {
                            continue;
                        } else {
                            ItemStack stack = new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")));
                            ItemMeta im = stack.getItemMeta();
                            im.setDisplayName(Utils.color(config.getString(keyForItems + "." + individual + ".name")));
                            im.setLore(color(config.getStringList(keyForItems + "." + individual + ".lore")));
                            stack.setItemMeta(im);
                            gui.i((9 * i) + z, stack);
                            inv.put((9 * i) + z, stack);
                        }
                    } else {
                        if(config.get(keyForItems + "." + individual) == null) {
                            continue;
                        } else {
                            ItemStack stack = new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")));
                            ItemMeta im = stack.getItemMeta();
                            im.setDisplayName(config.getString(keyForItems + "." + individual + ".name"));
                            im.setLore(color(config.getStringList(keyForItems + "." + individual + ".lore")));
                            stack.setItemMeta(im);
                            gui.i(z, stack);
                            inv.put((9 * i) + z, stack);
                        }
                    }
                }

            }

        }
    }
    public static void makeFormat(Player p, String visitor, FileConfiguration config, Gui gui, List<String> toFormat, String keyForItems) {
        int size = gui.getInventory().getSize();
        if (toFormat.size() == size / 9) {
            for (int i = 0; i < (size / 9); i++) {
                String s = toFormat.get(i);
                for (int z = 0; z < 9; z++) {
                    String removeSpaces = s.replaceAll(" ", "");
                    char individual = removeSpaces.charAt(z);
                    if (i > 0) {
                        if (config.get(keyForItems + "." + individual) == null) {
                            continue;
                        } else {
                            ItemStack stack;
                            if(config.getInt(keyForItems + "." + individual + ".data") == 0) {
                                stack = new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")));
                            } else {
                                stack = new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")), 1, (short) config.getInt(keyForItems + "." + individual + ".data"));
                            }
                            ItemMeta im = stack.getItemMeta();
                            im.setDisplayName(color(config.getString(keyForItems + "." + individual + ".name")));
                            List<String> lore = new ArrayList<>();
                            for(String loreString : config.getStringList(keyForItems + "." + individual + ".lore")) {
                                if(config.getString(keyForItems + "." + individual + ".flag") != null) {
                                    String flag = config.getString(keyForItems + "." + individual + ".flag");
                                    Claim claim = Claim.playerClaims.get(p.getUniqueId()).get(0);
                                    boolean current = getValue(claim, visitor, flag);
                                    lore.add(color(loreString).replace("{status}", current + ""));
                                    continue;
                                }
                                lore.add(color(loreString));
                            }
                            im.setLore(lore);
                            stack.setItemMeta(im);


                            gui.i((9 * i) + z, stack);
                        }
                    } else {
                        if (config.get(keyForItems + "." + individual) == null) {
                            continue;
                        } else {
                            ItemStack stack;
                            if(config.getInt(keyForItems + "." + individual + ".data") == 0) {
                                stack = new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")));
                            } else {
                                stack = new ItemStack(Material.matchMaterial(config.getString(keyForItems + "." + individual + ".material")), 1, (short) config.getInt(keyForItems + "." + individual + ".data"));
                            }
                            ItemMeta im = stack.getItemMeta();
                            im.setDisplayName(color(config.getString(keyForItems + "." + individual + ".name")));
                            List<String> lore = new ArrayList<>();
                            for(String loreString : config.getStringList(keyForItems + "." + individual + ".lore")) {
                                if(config.getString(keyForItems + "." + individual + ".flag") != null) {
                                    String flag = config.getString(keyForItems + "." + individual + ".flag");
                                    Claim claim = Claim.playerClaims.get(p.getUniqueId()).get(0);
                                    boolean current = getValue(claim, visitor, flag);
                                    lore.add(color(loreString).replace("{status}", current + ""));
                                    continue;
                                }
                                lore.add(color(loreString));
                            }
                            im.setLore(lore);
                            stack.setItemMeta(im);


                            gui.i(z, stack);
                        }
                    }
                }

            }

        }
    }

    public static Location toLocation(String s) {
        String[] split = s.split(";");
        return new Location(Bukkit.getWorld(split[0]), Double.parseDouble(split[1]), Double.parseDouble(split[2]), Double.parseDouble(split[3]));
    }

    public static String toString(Location s) {
        return s.getWorld().getName() + ";" + s.getX() + ";" + s.getY() + ";" + s.getZ();
    }

    public static Boolean needPage(Gui gui) {
        ItemStack[] cont = gui.getContents();
        for(ItemStack i : cont) {
            if(cont == null) {
                return false;
            } else {
                return true;
            }
        }
        return true;
    }


    public static boolean getValue(Claim claim, String visitor, String flag) {
        switch(visitor) {
            case "visitor":
                switch(flag) {
                    case "BREAK":
                        return claim.isVisitorBlockBreak();
                    case "PLACE":
                        return claim.isVisitorBlockPlace();
                    case "USE":
                        return claim.isVisitorUse();
                    case "CHEST":
                        return claim.isVisitorOpenChest();
                    case "DOOR":
                        return claim.isVisitorOpenDoor();
                }
            case "trusted":
                switch(flag) {
                    case "BREAK":
                        return claim.isTrustedBlockBreak();
                    case "PLACE":
                        return claim.isTrustedBlockPlace();
                    case "USE":
                        return claim.isTrustedUse();
                    case "CHEST":
                        return claim.isTrustedOpenChest();
                    case "DOOR":
                        return claim.isTrustedOpenDoor();
                }
        }

        return false;
    }

}
