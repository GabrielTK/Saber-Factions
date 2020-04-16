package com.massivecraft.factions.listeners;

import com.massivecraft.factions.*;
import com.massivecraft.factions.cmd.CmdInfo;
import com.massivecraft.factions.cmd.audit.FLogType;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.util.serializable.ClickableItemStack;
import com.massivecraft.factions.util.serializable.GUIMenu;
import com.massivecraft.factions.zcore.fperms.Access;
import com.massivecraft.factions.zcore.fperms.PermissableAction;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.player.PlayerEditBookEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

/**
 * @author Saser
 */
public class BookEditListener implements Listener {

    public BookEditListener() {
    }

    @EventHandler
    public void onPlayerEditBook(PlayerEditBookEvent event){
        Player player = event.getPlayer();
        FPlayer fPlayer = FPlayers.getInstance().getByPlayer(player);
        if(fPlayer.hasFaction() && (fPlayer.getFaction().getAccess(fPlayer, PermissableAction.EDIT_INFO) ==
        Access.ALLOW)){
            PersistentDataContainer container = event.getPreviousBookMeta().getPersistentDataContainer();
            if(container.has(CmdInfo.BookTrackTag, PersistentDataType.STRING)){
                Faction faction = Factions.getInstance().getFactionById(container.get(CmdInfo.BookTrackTag, PersistentDataType.STRING));
                faction.setFactionBookPages(event.getNewBookMeta().getPages());
                Bukkit.getScheduler().scheduleSyncDelayedTask(FactionsPlugin.instance, () -> FactionsPlugin.instance.logFactionEvent(faction, FLogType.FINFO_EDIT, fPlayer.getName()));
                fPlayer.msg("Book Edited for "+faction.getTag());
                faction.msg("&c[&6!!&c] &aFaction InfoBook has just been edited by"+player.getDisplayName());
                for(ItemStack item : player.getInventory()){
                    if(item.hasItemMeta() && item.getItemMeta().getPersistentDataContainer().has(CmdInfo.BookTrackTag, PersistentDataType.STRING)) {
                        player.getInventory().remove(item);
                    }
                }
            }
        }
    }
}
