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

import java.util.UUID;
import lombok.AccessLevel;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.bukkit.entity.Entity;
import org.bukkit.permissions.Permissible;
import team.idealstate.sugar.next.command.CommandSender;
import team.idealstate.sugar.validate.Validation;
import team.idealstate.sugar.validate.annotation.NotNull;

@RequiredArgsConstructor(access = AccessLevel.PRIVATE)
public final class SpigotCommandSender implements CommandSender {

    @NonNull
    private final Permissible holder;

    public static SpigotCommandSender of(@NotNull Permissible holder) {
        Validation.notNull(holder, "holder must not be null.");
        return new SpigotCommandSender(holder);
    }

    @Override
    public @NotNull UUID getUniqueId() {
        if (holder instanceof Entity) {
            return ((Entity) holder).getUniqueId();
        }
        return CONSOLE_UUID;
    }

    @Override
    public boolean isAdministrator() {
        return holder.isOp();
    }

    @Override
    public boolean hasPermission(@NotNull String permission) {
        Validation.notNull(permission, "permission must not be null.");
        return holder.hasPermission(permission);
    }
}
