package dev.reassembly.mineqraft;

import org.bukkit.Bukkit;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;
import org.bukkit.map.MapView;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;

import java.awt.*;
import java.io.IOException;
import java.net.URI;
import java.util.List;

import static dev.reassembly.mineqraft.MapUtils.paintMap;
import static dev.reassembly.mineqraft.MapUtils.parseQRCode;

public class Mineqraft {

    private static Mineqraft instance;
    private NamespacedKey mapURLKey;

    public Mineqraft(JavaPlugin plugin) {
        instance = this;
        mapURLKey = new NamespacedKey(plugin, "map-url");
        plugin.getServer().getPluginManager().registerEvents(new QRCodeListener(), plugin);
    }

    public void loadPlacedMaps() {
        for (World world : Bukkit.getWorlds()) {
            List<String> maps = world.getPersistentDataContainer().get(
                    mapURLKey,
                    PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING)
            );

            if (maps == null) continue;

            for (String mapEntry : maps) {
                String[] splitEntry = mapEntry.split("\\|");
                MapView mapView = Bukkit.getMap(Integer.parseInt(splitEntry[0]));
                String url = splitEntry[1];

                try {
                    paintMap(mapView, parseQRCode(URI.create(url).normalize()), Color.BLACK, Color.WHITE);
                } catch (IOException e) {
                    continue;
                }


            }
        }
    }

    public static Mineqraft getInstance() {
        return instance;
    }

    public NamespacedKey getMapURLKey() {
        return mapURLKey;
    }
}
