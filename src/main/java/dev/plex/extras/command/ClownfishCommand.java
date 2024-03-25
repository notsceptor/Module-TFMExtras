package dev.plex.extras.command;

import dev.plex.command.PlexCommand;
import dev.plex.command.annotation.CommandParameters;
import dev.plex.command.annotation.CommandPermissions;
import dev.plex.extras.TFMExtras;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collections;
import java.util.List;

@CommandParameters(name = "clownfish", description = "Gives a player a clownfish capable of knocking people back")
@CommandPermissions(permission = "plex.tfmextras.clownfish")
public class ClownfishCommand extends PlexCommand
{
    @Override
    protected Component execute(@NotNull CommandSender commandSender, @Nullable Player player, @NotNull String[] args)
    {
        if (args.length == 0)
        {
            ItemStack clownfish = new ItemStack(Material.TROPICAL_FISH);
            ItemMeta meta = clownfish.getItemMeta();

            meta.displayName(Component.text("Clownfish"));
            clownfish.setItemMeta(meta);

            player.getInventory().addItem(clownfish);
            return MiniMessage.miniMessage().deserialize("<rainbow>blub blub... ><_>");
        }
        else if (args[0].equals("toggle"))
        {
            List<String> toggledPlayers = TFMExtras.getModule().getConfig().getStringList("server.clownfish.toggled_players");
            boolean isToggled = toggledPlayers.contains(player.getName());

            if (isToggled)
            {
                toggledPlayers.remove(player.getName());
            }
            else
            {
                toggledPlayers.add(player.getName());
            }

            TFMExtras.getModule().getConfig().set("server.clownfish.toggled_players", toggledPlayers);
            TFMExtras.getModule().getConfig().save();

            return MiniMessage.miniMessage().deserialize("<gray>You " + (isToggled ? "<green>will" : "<red>will no longer") + "<gray> be affected by the clownfish");
        }
        else
        {
            return MiniMessage.miniMessage().deserialize("<red>Incorrect usage. Use either /clownfish or /clownfish toggle");
        }
    }

    @Override
    public @NotNull List<String> smartTabComplete(@NotNull CommandSender sender, @NotNull String alias, @NotNull String[] args) throws IllegalArgumentException {
        if (args.length == 1)
        {
            return List.of("toggle");
        }

        return Collections.emptyList();
    }
}
