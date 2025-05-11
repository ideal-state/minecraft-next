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

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;
import org.bukkit.Bukkit;
import team.idealstate.sugar.logging.Log;
import team.idealstate.sugar.logging.LogLevel;
import team.idealstate.sugar.logging.Logger;
import team.idealstate.sugar.validate.Validation;
import team.idealstate.sugar.validate.annotation.NotNull;

public class SpigotLogger implements Logger {

    private static final Map<LogLevel, String> LEVEL_TO_COLOR =
            Collections.unmodifiableMap(new HashMap<LogLevel, String>() {
                private static final long serialVersionUID = -671457667697576628L;

                {
                    put(LogLevel.TRACE, "§7");
                    put(LogLevel.DEBUG, "§8");
                    put(LogLevel.INFO, "§a");
                    put(LogLevel.WARN, "§e");
                    put(LogLevel.ERROR, "§c");
                    put(LogLevel.FATAL, "§4");
                }
            });

    @Override
    public void println(@NotNull LogLevel level, @NotNull Supplier<String> messageProvider) {
        Validation.notNull(level, "Log level must not be null.");
        Validation.notNull(messageProvider, "Message provider must not be null.");
        if (!Log.isEnabledLevel(level)) {
            return;
        }
        String color = LEVEL_TO_COLOR.get(level);
        if (color == null) {
            Bukkit.getConsoleSender().sendMessage(messageProvider.get());
        } else {
            Bukkit.getConsoleSender().sendMessage(color + messageProvider.get());
        }
    }
}
