package claims.Gui;

import claims.Main;
import org.bukkit.event.Listener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;


public class Gui implements Listener {

    private String name;
    private int size;
    private int invsize;
    private clickEvent click;
    private openEvent open;
    private closeEvent close;
    private dragEvent drag;
    private Inventory inv;
    public Gui gui;
    private boolean cancel = false;
    private boolean shift = false;
    private Plugin plugin;
    private boolean listen;
    private ItemStack[] items;
    private int page = 0;
    private int pagesize = 1;
    List<String> viewing = new ArrayList<>();

    public Gui(String name, int size, int pagesize) {
        plugin = Main.getInstance();
        this.gui = this;
        this.name = color(name)[0];
        this.size = size == 1 ? 1 : getInventorySize(size);
        this.invsize = this.size == 1 ? 9 : this.getInventorySize(size);
        this.listen = true;
        items = new ItemStack[invsize * pagesize];
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.pagesize = pagesize;
    }

    public Gui(String name, List<ItemStack> pagesize) {
        plugin = Main.getInstance();
        this.gui = this;
        this.name = color(name)[0];
        this.size = size == 1 ? 1 : getInventorySize(size);
        this.invsize = Math.min(54, 9 + (pagesize.size() / 9));
        this.listen = true;
        items = new ItemStack[pagesize.size()];
        Bukkit.getPluginManager().registerEvents(this, plugin);
        this.pagesize = Integer.valueOf(Math.round(Math.ceil(invsize / Double.valueOf(pagesize.size()))) + "");
        for(int i = 0; i < pagesize.size(); i++) {
            items[i] = pagesize.get(i);
            if(i < invsize - 9) {
                getInventory().setItem(i, pagesize.get(i));
            }
        }
    }

    public Gui(String name, int size) {
        plugin = Main.getInstance();
        this.gui = this;
        this.name = color(name)[0];
        this.size = size == 1 ? 1 : getInventorySize(size);
        this.invsize = this.size == 1 ? 9 : this.getInventorySize(size);
        this.listen = true;
        items = new ItemStack[invsize];
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onPluginDisable(PluginDisableEvent event) {
        for (Player p : this.getViewers())
            close(p);
    }

    public Gui show(Player p) {
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            if (!viewing.contains(p.getName()))
                viewing.add(p.getName());
            p.openInventory(getInventory());
            if (!listen)
                listen = true;
        }, 2);
        return this;
    }

    public ItemStack[] getContents() {
        return getInventory().getContents();
    }

    public Gui c() {
        this.cancel = true;
        return this;
    }

    public Gui noShifties() {
        this.shift = true;
        return this;
    }

    public Inventory getInventory() {
        if (inv == null) {
            if (size == 1) {
                inv = Bukkit.createInventory(null, InventoryType.DROPPER, (name));
            } else if (size == 5) {
                inv = Bukkit.createInventory(null, InventoryType.HOPPER, (name));
            } else {
                inv = Bukkit.createInventory(null, invsize, (name));
            }
        }
        return inv;
    }

    public String getTitle() {
        return this.name;
    }

    public void setTitle(String name) {
        for (Player p : getViewers())
            p.closeInventory();
        this.inv = null;
        this.name = color(name)[0];
        for (Player p : getViewers())
            show(p);
    }

    public int getPage() {
        return this.page;
    }

    public int getPages() {
        return this.pagesize;
    }

    public Gui nextPage() {
        if (pagesize > page + 1) {
            page++;
            for (int i = 0; i < invsize; i++) {
                getInventory().setItem(i, items[i + (page * invsize)]);
            }
        }
        return this;
    }

    public Gui prevPage() {
        if (page > 0) {
            page--;
            for(ItemStack i : items) {
                System.out.println(i);
            }
            for (int i = 0; i < invsize; i++) {
                getInventory().setItem(i, items[i + (page * invsize)]);
            }
        }
        return this;
    }

    public Gui setItemPage(int page, int row, int position, ItemStack item, String name, String... lore) {
        items[(position + (row * 9)) + (page * invsize)] = getItem(item, color(name)[0], color(lore));
        if (this.page == page) {
            getInventory().setItem((position + (row * 9)), getItem(item, color(name)[0], color(lore)));
        }
        return this;
    }

    public Gui setItemPage(int page, int position, ItemStack item, String name, String... lore) {
        items[(position) + (page * invsize)] = getItem(item, color(name)[0], color(lore));
        if (this.page == page) {
            getInventory().setItem((position), getItem(item, color(name)[0], color(lore)));
        }
        return this;
    }

    public Gui setItemPage(int page, int row, int position, ItemStack item) {
        items[(position + (row * 9)) + (page * invsize)] = item;
        if (this.page == page) {
            getInventory().setItem((position + (row * 9)), item);
        }
        return this;
    }

    public Gui setItemsPage(int page, ItemStack item, int... slots) {
        for (int slot : slots) {
            items[slot + (page * invsize)] = item;
            if (this.page == page) {
                getInventory().setItem(slot, item);
            }
        }
        return this;
    }

    public Gui setItemsPage(int page, ItemStack item, List<Integer> slots) {
        for (int slot : slots) {
            items[slot + (page * invsize)] = item;
            if (this.page == page) {
                getInventory().setItem(slot, item);
            }
        }
        return this;
    }

    public Gui setItemPage(int page, int position, ItemStack item) {
        items[position + (page * invsize)] = item;
        if (this.page == page) {
            getInventory().setItem(position, item);
        }
        return this;
    }

    public Gui setItemPage(int page, ItemStack item) {
        items[(page * invsize)] = item;
        if (this.page == page) {
            getInventory().addItem(item);
        }
        return this;
    }

    public Gui setPageItems(ItemStack[] items, int page) {
        this.items = items;
        return this;
    }

    public Gui setItemPage(ItemStack item) {
        items[(page * invsize)] = item;
        if (this.page == page) {
            getInventory().addItem(item);
        }
        return this;
    }

    public Gui fillPage(int page, Material item, String name, String... lore) {
        for (int i = 0; i < invsize; i++) {
            if (items[i + (page * invsize)] == null) {
                items[i + (page * invsize)] = getItem(new ItemStack(item), color(name)[0], color(lore));
                if (this.page == page) {
                    getInventory().setItem(i, getItem(new ItemStack(item), color(name)[0], color(lore)));
                }
            }
        }
        return this;
    }

    public Gui fillPage(int page, ItemStack item, String name, String... lore) {
        for (int i = 0; i < invsize; i++) {
            if (items[i + (page * invsize)] == null) {
                items[i + (page * invsize)] = getItem(item, color(name)[0], color(lore));
                if (this.page == page) {
                    getInventory().setItem(i, getItem(item, color(name)[0], color(lore)));
                }
            }
        }
        return this;
    }

    public Gui fillRowPage(int page, int row, Material item, String name, String... lore) {
        for (int i = 0; i < 9; i++) {
            if (items[(i + (row * 9)) + (page * invsize)] == null) {
                items[(i + (row * 9)) + (page * invsize)] = getItem(new ItemStack(item), color(name)[0], color(lore));
                if (this.page == page) {
                    getInventory().setItem((i + (row * 9)), getItem(new ItemStack(item), color(name)[0], color(lore)));
                }
            }
        }
        return this;
    }

    public Gui fillRowPage(int page, int row, ItemStack item, String name, String... lore) {
        for (int i = 0; i < 9; i++) {
            if (items[(i + (row * 9)) + (page * invsize)] == null) {
                items[(i + (row * 9)) + (page * invsize)] = getItem(item, color(name)[0], color(lore));
                if (this.page == page) {
                    getInventory().setItem((i + (row * 9)), getItem(item, color(name)[0], color(lore)));
                }
            }
        }
        return this;
    }

    public Gui fillColumnPage(int page, int column, Material item, String name, String... lore) {
        for (int i = 0; i < invsize / 9; i++) {
            if (items[((i * 9) + column) + (page * invsize)] == null) {
                items[((i * 9) + column) + (page * invsize)] = getItem(new ItemStack(item), color(name)[0],
                        color(lore));
                if (this.page == page) {
                    getInventory().setItem(((i * 9) + column),
                            getItem(new ItemStack(item), color(name)[0], color(lore)));
                }
            }

        }
        return this;
    }

    public Gui fillColumnPage(int page, int column, ItemStack item, String name, String... lore) {
        for (int i = 0; i < invsize / 9; i++) {
            if (items[((i * 9) + column) + (page * invsize)] == null) {
                items[((i * 9) + column) + (page * invsize)] = getItem(item, color(name)[0], color(lore));
                if (this.page == page) {
                    getInventory().setItem(((i * 9) + column), getItem(item, color(name)[0], color(lore)));
                }
            }
        }
        return this;
    }
    public Gui i(int slot, ItemStack item, String name, InventoryClickEvent e, String... lore) {

        onClick(event -> {
            if(event.getSlot() == slot) {

            }
        });
        return this;
    }

    public Gui i(int row, int position, ItemStack item, String name, String... lore) {
        getInventory().setItem((position + (row * 9)), getItem(item, color(name)[0], color(lore)));
        return this;
    }

    public Gui i(ItemStack item) {
        getInventory().addItem(item);
        return this;
    }

    public Gui i(int row, int position, Material item, String name, String... lore) {
        getInventory().setItem((position + (row * 9)), getItem(new ItemStack(item), color(name)[0], color(lore)));
        return this;
    }

    public Gui i(int position, ItemStack item, String name, String... lore) {
        getInventory().setItem((position), getItem(item, color(name)[0], color(lore)));
        return this;
    }

    public Gui i(int position, Material item, String name, String... lore) {
        getInventory().setItem((position), getItem(new ItemStack(item), color(name)[0], color(lore)));
        return this;
    }

    public Gui i(int row, int position, ItemStack item) {
        getInventory().setItem((position + (row * 9)), item);
        return this;
    }

    public Gui setItems(ItemStack item, int... slots) {
        for (int slot : slots) {
            getInventory().setItem(slot, item);
        }
        return this;
    }

    public Gui setItems(ItemStack item, List<Integer> slots) {
        for (int slot : slots) {
            getInventory().setItem(slot, item);
        }
        return this;
    }

    public Gui i(int position, ItemStack item) {
        getInventory().setItem(position, item);
        return this;
    }

    public Gui clearRow(int row) {
        for (int i = 0; i < 9; i++) {
            if (getInventory().getItem(i + (row * 9)) != null)
                getInventory().setItem(i + (row * 9), new ItemStack(Material.AIR));
        }
        return this;
    }

    public Gui fill(Material item, String name, String... lore) {
        for (int i = 0; i < invsize; i++) {
            if (getInventory().getItem(i) == null) {
                getInventory().setItem(i, getItem(new ItemStack(item), color(name)[0], color(lore)));
            }
        }
        return this;
    }

    public Gui fill(ItemStack item, String name, String... lore) {
        for (int i = 0; i < invsize; i++) {
            if (getInventory().getItem(i) == null) {
                getInventory().setItem(i, getItem(item, color(name)[0], color(lore)));
            }
        }
        return this;
    }

    public Gui fillRandom(String name, ItemStack... items) {
        for (int i = 0; i < invsize; i++) {
            int rand = new Random().nextInt(items.length);
            if (getInventory().getItem(i) == null) {
                getInventory().setItem(i, getItem(items[rand], color(name)[0]));
            }
        }
        return this;
    }

    public Gui fill(ItemStack item) {
        for (int i = 0; i < invsize; i++) {
            if (getInventory().getItem(i) == null) {
                getInventory().setItem(i, item);
            }
        }
        return this;
    }

    public Gui fillRow(int row, Material item, String name, String... lore) {
        for (int i = 0; i < 9; i++) {
            if (getInventory().getItem((i + (row * 9))) == null) {
                getInventory().setItem((i + (row * 9)), getItem(new ItemStack(item), color(name)[0], color(lore)));
            }
        }
        return this;
    }

    public Gui fillRow(int row, ItemStack item, String name, String... lore) {
        for (int i = 0; i < 9; i++) {
            if (getInventory().getItem((i + (row * 9))) == null) {
                getInventory().setItem((i + (row * 9)), getItem(item, color(name)[0], color(lore)));
            }
        }
        return this;
    }

    public Gui fillColumn(int column, Material item, String name, String... lore) {
        for (int i = 0; i < invsize / 9; i++) {
            if (getInventory().getItem((i * 9) + column) == null) {
                getInventory().setItem(((i * 9) + column), getItem(new ItemStack(item), color(name)[0], color(lore)));
            }

        }
        return this;
    }

    public Gui fillColumn(int column, ItemStack item, String name, String... lore) {
        for (int i = 0; i < invsize / 9; i++) {
            if (getInventory().getItem(((i * 9) + column)) == null) {
                getInventory().setItem(((i * 9) + column), getItem(item, color(name)[0], color(lore)));
            }
        }
        return this;
    }

    public Gui close(Player p) {
        if (p.getOpenInventory().getTitle().equals(name)) {
            p.closeInventory();
            this.viewing.remove(p.getName());
            if (this.viewing.size() == 0)
                listen = false;
        }
        return this;
    }

    public List<Player> getViewers() {
        List<Player> viewers = new ArrayList<Player>();
        for (String s : viewing)
            viewers.add(Bukkit.getPlayer(s));
        return viewers;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent e) {
        if (!listen)
            return;
        if (viewing.contains(e.getWhoClicked().getName())) {
            Player p = ((Player) e.getWhoClicked());
            if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(name))) {
                if (e.getClickedInventory() == null || e.getClickedInventory().getType() != inv.getType())
                    return;
                if (this.shift) {
                    e.setCancelled(true);
                    p.updateInventory();
                }
                if (this.cancel) {
                    e.setCancelled(true);
                    p.updateInventory();
                }
                if (click == null)
                    return;
                click.click(e);
                if (e.isCancelled()) {
                    p.updateInventory();
                }
            }
        }
    }

    @EventHandler
    public void onInventoryDrag(InventoryDragEvent e) {
        if (!listen)
            return;
        if (viewing.contains(e.getWhoClicked().getName())) {
            if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(name))) {
                if (e.getInventory().getType() != inv.getType())
                    return;
                if (click != null)
                    e.setCancelled(true);
                if (drag == null)
                    return;
                if (viewing.contains(e.getWhoClicked().getName()))
                    drag.drag(e);
            }
        }
    }

    @EventHandler
    void onClose(InventoryCloseEvent e) {
        if (!listen)
            return;
        if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(name)))
            if (viewing.contains(e.getPlayer().getName())) {
                if (close != null)
                    close.close(e);
                viewing.remove(e.getPlayer().getName());
                if (viewing.size() == 0)
                    listen = false;
            }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent e) {
        if (!listen)
            return;
        if (ChatColor.stripColor(e.getView().getTitle()).equalsIgnoreCase(ChatColor.stripColor(name)))
            if (viewing.contains(e.getPlayer().getName())) {
                if (open != null)
                    open.open(e);
                listen = true;
            }
    }

    public String[] color(String... strings) {
        String[] s = new String[strings.length];
        for (int i = 0; i < strings.length; i++) {
            if (strings[i] == null)
                continue;
            s[i] = (ChatColor.translateAlternateColorCodes('&', strings[i]));
        }
        return s;
    }

    public static ItemStack getItem(ItemStack item, String name, String... lore) {
        ItemMeta im = item.getItemMeta();
        if (name != null)
            im.setDisplayName(name);
        im.setLore(Arrays.asList(lore));
        item.setItemMeta(im);
        return item;
    }

    private int getInventorySize(int size) {
        return Math.max(5, Math.min(Math.round((size / 9) * 9), 54));
    }

    public interface closeEvent {
        void close(InventoryCloseEvent e);
    }

    public interface openEvent {
        void open(InventoryOpenEvent e);
    }

    public interface dragEvent {
        void drag(InventoryDragEvent e);
    }

    public interface clickEvent {
        void click(InventoryClickEvent e);
    }

    public Gui onClick(clickEvent event) {
        this.click = event;


        return gui;
    }

    public Gui onClose(closeEvent event) {
        this.close = event;
        return gui;
    }

    public Gui onOpen(openEvent event) {
        this.open = event;
        return gui;
    }

    public Gui onDrag(dragEvent event) {
        this.drag = event;
        return gui;
    }

}