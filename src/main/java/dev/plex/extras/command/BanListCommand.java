package dev.plex.extras.command;

import dev.plex.command.PlexCommand;
import dev.plex.command.annotation.CommandParameters;
import dev.plex.command.annotation.CommandPermissions;
import dev.plex.punishment.Punishment;
import net.kyori.adventure.text.Component;
import org.apache.commons.lang3.StringUtils;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.stream.Collectors;

@CommandParameters(name = "banlist", description = "Manages the banlist", usage = "/<command> [purge]")
@CommandPermissions(permission = "plex.tfmextras.banlist")
public class BanListCommand extends PlexCommand
{
    @Override
    protected Component execute(@NotNull CommandSender sender, @Nullable Player player, @NotNull String[] args)
    {
        if (args.length == 0)
        {
            plugin.getPunishmentManager().getActiveBans().whenComplete((punishments, throwable) ->
            {
                send(sender, mmString("<gold>Active Bans (" + punishments.size() + "): <yellow>" + StringUtils.join(punishments.stream().map(Punishment::getPunishedUsername).collect(Collectors.toList()), ", ")));
            });
            return null;
        }
        if (args[0].equalsIgnoreCase("purge"))
        {
            if (sender instanceof Player)
            {
                return messageComponent("noPermissionInGame");
            }
            if (!sender.getName().equalsIgnoreCase("console"))
            {
                if (!checkPermission(sender, "plex.tfmextras.banlist.clear"))
                {
                    return null;
                }
            }
            plugin.getPunishmentManager().getActiveBans().whenComplete((punishments, throwable) ->
            {
                punishments.forEach(plugin.getPunishmentManager()::unban);
                send(sender, mmString("<gold>Unbanned " + punishments.size() + " players."));
            });
        }
        return null;
    }
}
