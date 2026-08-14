package dev.reassembly.mineqraft;

import io.papermc.paper.event.player.PlayerItemFrameChangeEvent;
import io.papermc.paper.persistence.PersistentDataContainerView;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

public class QRCodeListener implements Listener {

    @EventHandler
    public void onPlayerItemFrameChange(PlayerItemFrameChangeEvent event) {
        if (event.getAction() == PlayerItemFrameChangeEvent.ItemFrameChangeAction.PLACE) {
            ItemStack item = event.getItemStack();
            if (item.getType() != Material.FILLED_MAP) return;

            String url = item.getPersistentDataContainer().get(Mineqraft.getInstance().getMapURLKey(), PersistentDataType.STRING);
            if (url == null) return;

            event.getItemFrame().getPersistentDataContainer().set(Mineqraft.getInstance().getMapURLKey(), PersistentDataType.STRING, url);
        }

    }
}
