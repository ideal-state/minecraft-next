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
import team.idealstate.sugar.next.command.annotation.CommandArgument.Converter;
import team.idealstate.sugar.validate.Validation;
import team.idealstate.sugar.validate.annotation.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotCommand implements TabExecutor {

    @NonNull
    private final CommandLine commandLine;

    private final String failureMessage;

    @NonNull
    private final List<Converter<?>> converters;

    @NotNull
    public static SpigotCommand of(
            @NotNull String name, @NotNull Object command, @NotNull List<Converter<?>> converters) {
        return new SpigotCommand(CommandLine.of(name, command), null, converters);
    }

    @NotNull
    public static SpigotCommand of(
            @NotNull String name,
            @NotNull Object command,
            String failureMessage,
            @NotNull List<Converter<?>> converters) {
        Validation.notNull(name, "Name must not be null.");
        Validation.notNull(command, "Command must not be null.");
        Validation.notNull(converters, "Converters must not be null.");
        CommandLine commandLine = CommandLine.of(name, command);
        return new SpigotCommand(commandLine, failureMessage, converters);
    }

    @Override
    @SuppressWarnings({"rawtypes", "unchecked"})
    public boolean onCommand(CommandSender commandSender, Command command, String label, String[] arguments) {
        SpigotCommandSender sender = SpigotCommandSender.of(commandSender);
        CommandContext context = CommandContext.of(sender, arguments);
        for (Converter converter : converters) {
            context.setConverter(converter.getTargetType(), converter);
        }
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
    @SuppressWarnings({"rawtypes", "unchecked"})
    public List<String> onTabComplete(CommandSender commandSender, Command command, String label, String[] arguments) {
        SpigotCommandSender sender = SpigotCommandSender.of(commandSender);
        CommandContext context = CommandContext.of(sender, arguments);
        for (Converter converter : converters) {
            context.setConverter(converter.getTargetType(), converter);
        }
        return commandLine.complete(context, arguments);
    }
}
