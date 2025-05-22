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

package team.idealstate.minecraft.next.spigot.api;

import java.io.File;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.server.PluginEnableEvent;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.plugin.java.JavaPluginLoader;
import team.idealstate.minecraft.next.spigot.api.command.SpigotCommand;
import team.idealstate.minecraft.next.spigot.api.placeholder.Placeholder;
import team.idealstate.minecraft.next.spigot.api.placeholder.SpigotPlaceholderExpansion;
import team.idealstate.sugar.logging.Log;
import team.idealstate.sugar.logging.Logger;
import team.idealstate.sugar.next.command.Command;
import team.idealstate.sugar.next.command.annotation.CommandArgument.Converter;
import team.idealstate.sugar.next.context.Bean;
import team.idealstate.sugar.next.context.Context;
import team.idealstate.sugar.next.context.ContextHolder;
import team.idealstate.sugar.next.context.ContextLifecycle;
import team.idealstate.sugar.next.eventbus.EventBus;
import team.idealstate.sugar.validate.annotation.NotNull;

public abstract class SpigotPlugin extends JavaPlugin implements ContextHolder, ContextLifecycle {

    static {
        Logger logger = Log.getLogger(SpigotPlugin.class.getClassLoader());
        if (logger != null) {
            Log.setLogger(logger);
        }
    }

    private final Context context = Context.of(this, this, EventBus.instance());

    @Override
    public final Context getContext() {
        return context;
    }

    public SpigotPlugin() {
        context.initialize();
    }

    protected SpigotPlugin(JavaPluginLoader loader, PluginDescriptionFile description, File dataFolder, File file) {
        super(loader, description, dataFolder, file);
        context.initialize();
    }

    @Override
    public final String getVersion() {
        return getDescription().getVersion();
    }

    @Override
    public final void onLoad() {
        super.onLoad();
        context.load();
    }

    @Override
    @SuppressWarnings("rawtypes")
    public final void onEnable() {
        super.onEnable();
        context.enable();
        PluginManager pluginManager = getServer().getPluginManager();
        registerEventListeners(pluginManager);
        List<Bean<Converter>> converters = context.getBeans(Converter.class);
        registerCommands(converters);
        hookPlaceholderAPI(pluginManager, converters);
    }

    @Override
    public final void onDisable() {
        super.onDisable();
        context.disable();
        context.destroy();
    }

    private void registerEventListeners(PluginManager pluginManager) {
        List<Bean<Listener>> beans = context.getBeans(Listener.class);
        if (beans.isEmpty()) {
            return;
        }
        for (Bean<Listener> bean : beans) {
            pluginManager.registerEvents(bean.getInstance(), this);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private void registerCommands(@NotNull List<Bean<Converter>> converters) {
        List<Bean<Command>> beans = context.getBeans(Command.class);
        if (beans.isEmpty()) {
            return;
        }
        List converterList;
        if (converters.isEmpty()) {
            converterList = Collections.emptyList();
        } else {
            converterList = converters.stream().map(Bean::getInstance).collect(Collectors.toList());
        }
        for (Bean<Command> bean : beans) {
            String name = bean.getName();
            PluginCommand pluginCommand = getCommand(name);
            if (pluginCommand == null) {
                continue;
            }
            SpigotCommand spigotCommand = SpigotCommand.of(name, bean.getInstance(), converterList);
            pluginCommand.setExecutor(spigotCommand);
            pluginCommand.setTabCompleter(spigotCommand);
        }
    }

    @SuppressWarnings("rawtypes")
    private void hookPlaceholderAPI(PluginManager pluginManager, @NotNull List<Bean<Converter>> converters) {
        pluginManager.registerEvents(new PlaceholderAPIHook(context, converters), this);
    }

    @RequiredArgsConstructor
    private static final class PlaceholderAPIHook implements Listener {
        private static final String PLACEHOLDER_API = "PlaceholderAPI";

        @NonNull
        private final Context context;

        @NonNull
        @SuppressWarnings("rawtypes")
        private final List<Bean<Converter>> converters;

        @SuppressWarnings({"unchecked", "rawtypes"})
        @EventHandler(priority = EventPriority.LOWEST)
        public void onPlaceholderAPIEnabled(PluginEnableEvent event) {
            if (PLACEHOLDER_API.equals(event.getPlugin().getName())) {
                List<Bean<Placeholder>> beans = context.getBeans(Placeholder.class);
                if (beans.isEmpty()) {
                    return;
                }
                List converterList;
                if (converters.isEmpty()) {
                    converterList = Collections.emptyList();
                } else {
                    converterList = converters.stream().map(Bean::getInstance).collect(Collectors.toList());
                }
                String author = context.getName();
                String version = context.getVersion();
                for (Bean<Placeholder> bean : beans) {
                    SpigotPlaceholderExpansion.of(bean.getName(), author, version, bean.getInstance(), converterList)
                            .register();
                }
            }
        }
    }
}
