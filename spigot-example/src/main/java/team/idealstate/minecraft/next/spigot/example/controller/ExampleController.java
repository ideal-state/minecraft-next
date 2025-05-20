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

package team.idealstate.minecraft.next.spigot.example.controller;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import team.idealstate.minecraft.next.spigot.api.placeholder.Placeholder;
import team.idealstate.minecraft.next.spigot.example.configuration.ExampleConfiguration;
import team.idealstate.minecraft.next.spigot.example.service.ExampleEntityService;
import team.idealstate.sugar.banner.Banner;
import team.idealstate.sugar.logging.Log;
import team.idealstate.sugar.next.command.Command;
import team.idealstate.sugar.next.command.CommandContext;
import team.idealstate.sugar.next.command.CommandResult;
import team.idealstate.sugar.next.command.annotation.CommandArgument;
import team.idealstate.sugar.next.command.annotation.CommandArgument.ConverterResult;
import team.idealstate.sugar.next.command.annotation.CommandHandler;
import team.idealstate.sugar.next.command.exception.CommandArgumentConversionException;
import team.idealstate.sugar.next.context.ContextHolder;
import team.idealstate.sugar.next.context.annotation.component.Controller;
import team.idealstate.sugar.next.context.annotation.feature.Autowired;
import team.idealstate.sugar.next.context.annotation.feature.Environment;
import team.idealstate.sugar.next.context.annotation.feature.Named;
import team.idealstate.sugar.next.context.aware.ContextHolderAware;
import team.idealstate.sugar.validate.Validation;
import team.idealstate.sugar.validate.annotation.NotNull;

@Named("exampleminecraftnext")
@Controller
@Environment("development")
public class ExampleController implements ContextHolderAware, Command, Placeholder {

    @CommandHandler
    public CommandResult ping() {
        Log.info("ping()");
        return CommandResult.success(String.valueOf(service.ping()));
    }

    @CommandHandler
    public CommandResult show() {
        Log.info("show()");
        return CommandResult.success(configuration.toString());
    }

    @CommandHandler("show {message}")
    public CommandResult show(@CommandArgument(completer = "completeMessage") String message) {
        Log.info(String.format("show(String): %s", message));
        return CommandResult.success(message);
    }

    @CommandHandler("show {target} {message}")
    public CommandResult show(
            @CommandArgument(value = "target", completer = "completePlayer", converter = "convertToPlayer")
                    Player player,
            @CommandArgument(completer = "completeMessage") String message) {
        Log.info(String.format("show(Player, String): %s, %s", player, message));
        player.sendMessage(message);
        return CommandResult.success();
    }

    @NotNull
    public List<String> completePlayer(@NotNull CommandContext context, @NotNull String argument) {
        String lowerCase = argument.toLowerCase();
        return Bukkit.getOnlinePlayers().stream()
                .map(Player::getName)
                .map(String::toLowerCase)
                .filter(s -> s.startsWith(lowerCase))
                .collect(Collectors.toList());
    }

    @NotNull
    public List<String> completeMessage(@NotNull CommandContext context, @NotNull String argument) {
        return Arrays.asList("minecraft-next", "hello-next");
    }

    @NotNull
    public ConverterResult<Player> convertToPlayer(
            @NotNull CommandContext context, @NotNull String argument, boolean onConversion)
            throws CommandArgumentConversionException {
        Player player = Bukkit.getPlayer(argument);
        boolean canBeConvert = player != null && player.isOnline();
        if (!onConversion) {
            return canBeConvert ? ConverterResult.success() : ConverterResult.failure();
        }
        if (!canBeConvert) {
            throw new CommandArgumentConversionException(
                    String.format("The argument '%s' cannot be convert to player.", argument));
        }
        return ConverterResult.success(player);
    }

    @CommandHandler
    public CommandResult banner(CommandContext context) {
        Log.info(String.format("banner(CommandContext) %s", context.getSender().getUniqueId()));
        ContextHolder holder = contextHolder;
        if (holder == null) {
            return CommandResult.failure("ContextHolder is null");
        } else {
            Banner.lines(holder.getClass()).forEach(Log::info);
        }
        return CommandResult.success();
    }

    private volatile ContextHolder contextHolder;

    @Override
    public void setContextHolder(@NotNull ContextHolder contextHolder) {
        this.contextHolder = contextHolder;
    }

    private volatile ExampleConfiguration configuration;

    @Autowired
    public void setConfiguration(@NotNull ExampleConfiguration configuration) {
        this.configuration = configuration;
    }

    private volatile ExampleEntityService service;

    @Autowired
    public void setService(@NotNull ExampleEntityService service) {
        this.service = service;
    }
}
