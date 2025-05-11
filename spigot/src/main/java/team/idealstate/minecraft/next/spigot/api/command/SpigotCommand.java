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

package team.idealstate.minecraft.next.spigot.api.command;

import java.util.List;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import team.idealstate.sugar.next.command.CommandContext;
import team.idealstate.sugar.next.command.CommandLine;
import team.idealstate.sugar.next.command.CommandResult;
import team.idealstate.sugar.validate.Validation;
import team.idealstate.sugar.validate.annotation.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotCommand implements TabExecutor {

    @NonNull
    private final CommandLine commandLine;

    private final String failureMessage;

    @NotNull
    public static SpigotCommand of(@NotNull String name, @NotNull Object command) {
        return new SpigotCommand(CommandLine.of(name, command), null);
    }

    @NotNull
    public static SpigotCommand of(@NotNull String name, @NotNull Object command, String failureMessage) {
        Validation.notNull(name, "name must not be null.");
        Validation.notNull(command, "command must not be null.");
        CommandLine commandLine = CommandLine.of(name, command);
        return new SpigotCommand(commandLine, failureMessage);
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] arguments) {
        SpigotCommandSender sender = SpigotCommandSender.of(commandSender);
        CommandContext context = CommandContext.of(sender);
        CommandResult result = commandLine.execute(context, arguments);
        String message = result.getMessage();
        boolean success = result.isSuccess();
        if (message != null) {
            commandSender.sendMessage(message);
        } else if (!success && failureMessage != null) {
            commandSender.sendMessage(failureMessage);
        }
        return success;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] arguments) {
        SpigotCommandSender sender = SpigotCommandSender.of(commandSender);
        CommandContext context = CommandContext.of(sender);
        return commandLine.complete(context, arguments);
    }
}
