package com.massivecraft.factions.cmd;

import com.massivecraft.factions.FPlayer;
import com.massivecraft.factions.FactionsPlugin;
import com.massivecraft.factions.struct.Permission;
import com.massivecraft.factions.zcore.util.TL;
import org.apache.commons.lang.time.DurationFormatUtils;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataType;
import xyz.upperlevel.spigot.book.BookUtil;

import java.awt.print.Book;
import java.util.ArrayList;

public class CmdInfo extends FCommand {

    /**
     * @author FactionsUUID Team
     */
    public static NamespacedKey BookTrackTag = new NamespacedKey(FactionsPlugin.getInstance(), "fact-infoBookEdit");
    static net.md_5.bungee.api.chat.BaseComponent[] defaultBookPages = new BookUtil.PageBuilder().add(
            BookUtil.TextBuilder.of("Welcome to Our Faction!")
                                .color(ChatColor.GOLD)
                                .style(ChatColor.BOLD, ChatColor.ITALIC)

            .onHover(BookUtil.HoverAction.showText("Please ask a leader to set-up the faction book!")).build()).build();


    public CmdInfo() {
        super();
        this.aliases.addAll(Aliases.info);
        this.optionalArgs.put("edit", "");
        this.requirements = new CommandRequirements.Builder(Permission.STATUS)
                .playerOnly()
                .memberOnly()
                .build();
    }
    public static BookUtil.BookBuilder writtableBook() {
        return new BookUtil.BookBuilder(new ItemStack(Material.WRITABLE_BOOK));
    }
    @Override
    public void perform(CommandContext context) {


        if (context.args.size() == 0) {
            //Open InfoBook
            BookUtil.BookBuilder bookData = BookUtil.writtenBook()
                    .author(context.faction.getDescription())
                    .title("Information");
            if(context.faction.getFactionBookPages().isEmpty()) {
                bookData = bookData.pages(defaultBookPages);
            }
            else {
                bookData = bookData.pagesRaw(context.faction.getFactionBookPages());
            }
            BookUtil.openPlayer(context.player, bookData.build());


        } else {
            //Edit InfoBook
            BookUtil.BookBuilder bookData = writtableBook()
                    .author(context.faction.getDescription())
                    .title("Information");
            if(context.faction.getFactionBookPages().isEmpty()) {
                bookData = bookData.pages(defaultBookPages);
            }
            else {
                bookData = bookData.pagesRaw(context.faction.getFactionBookPages());
            }
            ItemStack book = bookData.build();
            ItemMeta bookMeta = book.getItemMeta();

            bookMeta.getPersistentDataContainer().set(BookTrackTag, PersistentDataType.STRING, context.faction.getId());
            book.setItemMeta(bookMeta);
            context.player.getInventory().addItem(book);
        }
    }

    @Override
    public TL getUsageTranslation() {
        return TL.COMMAND_HELP_INFO;
    }

}
