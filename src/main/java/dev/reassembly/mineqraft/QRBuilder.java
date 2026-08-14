package dev.reassembly.mineqraft;

import com.google.zxing.EncodeHintType;
import io.papermc.paper.persistence.PersistentDataContainerView;
import net.glxn.qrgen.javase.QRCode;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Consumer;

import static dev.reassembly.mineqraft.MapUtils.*;

public class QRBuilder {

    private static final URI DEFAULT_URI = URI.create("https://modrinth.com");

    private URI uri;
    private Color foreground = Color.BLACK;
    private Color background = Color.WHITE;

    public QRBuilder(String url) {
        try {
            this.uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning(
                    "The uri \"" + url + "\" is invalid. Defaulting to " + DEFAULT_URI);
            this.uri = DEFAULT_URI;
        }
    }

    public QRBuilder setForegroundColor(Color color) {
        foreground = color;
        return this;
    }

    public QRBuilder setBackgroundColor(Color color) {
        background = color;
        return this;
    }

    public ItemStack getMap() {

        ItemStack map = ItemStack.of(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        MapView mapView = Bukkit.createMap(Bukkit.getWorlds().getFirst());

        BufferedImage qrCode;
        try {
            qrCode = parseQRCode(uri);
        } catch (IOException e) {
            Bukkit.getLogger().warning("Something went wrong creating the QR Code");
            return null;
        }

        mapMeta.setMapView(paintMap(mapView, qrCode, foreground, background));
        map.setItemMeta(mapMeta);

        PersistentDataContainer pdc = mapView.getWorld().getPersistentDataContainer();
        List<String> urls = pdc.get(
                Mineqraft.getInstance().getMapURLKey(),
                PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING)
                );

        if (urls != null) {

            ArrayList<String> editableURLs = new ArrayList<>(urls);
            editableURLs.add(mapView.getId() + '|' + uri.getHost() + uri.getPath());
            pdc.set(
                    Mineqraft.getInstance().getMapURLKey(),
                    PersistentDataType.LIST.listTypeFrom(PersistentDataType.STRING),
                    editableURLs
            );
        }

        return map;
    }




}

