/*
 *    minecraft-next
 *    Copyright (C) 2025  ideal-state
 *
 *    This program is free software: you can redistribute it and/or modify
 *    it under the terms of the GNU General Public License as published by
 *    the Free Software Foundation, either version 3 of the License, or
 *    (at your option) any later version.
 *
 *    This program is distributed in the hope that it will be useful,
 *    but WITHOUT ANY WARRANTY; without even the implied warranty of
 *    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *    GNU General Public License for more details.
 *
 *    You should have received a copy of the GNU General Public License
 *    along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package team.idealstate.minecraft.next.spigot.api.placeholder;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import team.idealstate.minecraft.next.spigot.api.command.SpigotCommandSender;
import team.idealstate.sugar.next.command.CommandContext;
import team.idealstate.sugar.next.command.CommandLine;
import team.idealstate.sugar.next.command.CommandResult;
import team.idealstate.sugar.validate.Validation;
import team.idealstate.sugar.validate.annotation.NotNull;
import team.idealstate.sugar.validate.annotation.Nullable;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotPlaceholderExpansion extends PlaceholderExpansion {

    public static final String ARGUMENTS_DELIMITER = "_";
    public static final String ARGUMENTS_DELIMITER_1 = ":";

    @NonNull
    @Getter
    private final String identifier;

    @NonNull
    @Getter
    private final String author;

    @NonNull
    @Getter
    private final String version;

    @NonNull
    @Getter
    private final CommandLine commandLine;

    public static SpigotPlaceholderExpansion of(
            @NotNull String identifier, @NotNull String author, @NotNull String version, @NotNull Object command) {
        Validation.notNull(command, "command must not be null.");
        ;
        return new SpigotPlaceholderExpansion(identifier, author, version, CommandLine.of(identifier, command));
    }

    @Nullable
    private String[] argumentsOf(@NotNull String params) {
        Validation.notNull(params, "params must not be null.");
        if (params.isEmpty()) {
            return null;
        }
        return params.replace(ARGUMENTS_DELIMITER_1, ARGUMENTS_DELIMITER).split(ARGUMENTS_DELIMITER, -1);
    }

    @Override
    public String onRequest(OfflinePlayer offlinePlayer, String params) {
        String[] arguments = argumentsOf(params);
        if (arguments == null) {
            return super.onRequest(offlinePlayer, params);
        }
        CommandSender commandSender = null;
        if (offlinePlayer == null) {
            commandSender = Bukkit.getConsoleSender();
        } else if (offlinePlayer.isOnline()) {
            commandSender = offlinePlayer.getPlayer();
        }
        if (commandSender == null) {
            return null;
        }
        SpigotCommandSender sender = SpigotCommandSender.of(commandSender);
        CommandContext context = CommandContext.of(sender);
        CommandResult result = commandLine.execute(context, arguments);
        return result.isSuccess() ? result.getMessage() : null;
    }

    @Override
    public String onPlaceholderRequest(Player player, String params) {
        return super.onPlaceholderRequest(player, params);
    }
}
