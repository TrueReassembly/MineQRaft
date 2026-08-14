package dev.reassembly.mineqraft;

import com.google.zxing.EncodeHintType;
import net.glxn.qrgen.javase.QRCode;
import org.bukkit.entity.Player;
import org.bukkit.map.MapCanvas;
import org.bukkit.map.MapRenderer;
import org.bukkit.map.MapView;
import org.jetbrains.annotations.NotNull;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URI;

public class MapUtils {

    public static MapView paintMap(MapView mapView, BufferedImage image, Color foreground, Color background) {
        mapView.addRenderer(new MapRenderer() {
            @Override
            public void render(@NotNull MapView map, @NotNull MapCanvas canvas, @NotNull Player player) {

                for (int x = 0; x < 128; x++) {
                    for (int y = 0; y < 128; y++) {

                        Color color;

                        if (!isPixelBlack(image, x, y)) color = background;
                        else color = foreground;

                        canvas.setPixelColor(x, y, color);
                    }
                }
            }
        });

        mapView.setLocked(true);
        return mapView;
    }

    public static boolean isPixelBlack(BufferedImage img, int x, int y) {
        return (img.getRGB(x, y) & 0b00111111) == 0;
    }

    public static BufferedImage parseQRCode(URI uri) throws IOException {
        ByteArrayOutputStream stream = QRCode.from(uri.getHost() + uri.getPath())
                .withSize(128, 128)
                .withHint(EncodeHintType.MARGIN, 1)
                .stream();
        ByteArrayInputStream bais = new ByteArrayInputStream(stream.toByteArray());

        return ImageIO.read(bais);
    }
}
