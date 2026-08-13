package dev.reassembly.mineqraft;

import com.google.zxing.EncodeHintType;
import net.glxn.qrgen.javase.QRCode;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.MapMeta;
import org.bukkit.map.*;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.beans.Encoder;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URL;

public class QRBuilder {

    private static final URI DEFAULT_URI = URI.create("https://modrinth.com");

    private URI uri;

    public QRBuilder(String url) {
        try {
            this.uri = URI.create(url);
        } catch (IllegalArgumentException e) {
            Bukkit.getLogger().warning(
                    "The uri \"" + url + "\" is invalid. Defaulting to " + DEFAULT_URI);
            this.uri = DEFAULT_URI;
        }
    }

    public ItemStack getMap() {
        ItemStack map = new ItemStack(Material.FILLED_MAP);
        MapMeta mapMeta = (MapMeta) map.getItemMeta();
        MapView mapView = Bukkit.createMap(Bukkit.getWorlds().getFirst());

        BufferedImage qrCode;
        try {
            qrCode = parseQRCode();
        } catch (IOException e) {
            Bukkit.getLogger().warning("Something went wrong creating the QR Code");
            return null;
        }

        mapView.addRenderer(new MapRenderer() {
            @Override
            public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {

                for (int x = 0; x < 128; x++) {
                    for (int y = 0; y < 128; y++) {

                        Color color;

                        if (!isPixelBlack(qrCode, x, y)) color = Color.WHITE;
                        else color = Color.BLACK;

                        canvas.setPixelColor(x, y, color);
                    }
                }
            }
        });

        mapMeta.setMapView(mapView);
        map.setItemMeta(mapMeta);
        return map;
    }

    private BufferedImage parseQRCode() throws IOException {
        ByteArrayOutputStream stream = QRCode.from(uri.getHost() + uri.getPath())
                .withSize(128, 128)
                .withHint(EncodeHintType.MARGIN, 1)
                .stream();
        ByteArrayInputStream bais = new ByteArrayInputStream(stream.toByteArray());

        return ImageIO.read(bais);
    }

    private boolean isPixelBlack(BufferedImage img, int x, int y) {
        return (img.getRGB(x, y) & 0b00111111) == 0;
    }
}

