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

package team.idealstate.minecraft.next.spigot;

import team.idealstate.minecraft.next.spigot.api.SpigotPlugin;
import team.idealstate.sugar.next.boot.hikaricp.annotation.EnableHikariCP;
import team.idealstate.sugar.next.boot.jedis.annotation.EnableJedis;
import team.idealstate.sugar.next.boot.mybatis.annotation.EnableMyBatis;
import team.idealstate.sugar.next.context.Context;
import team.idealstate.sugar.validate.annotation.NotNull;

@EnableHikariCP
@EnableJedis
@EnableMyBatis
public final class MinecraftNext extends SpigotPlugin {
    @Override
    public void onInitialize(@NotNull Context context) {}

    @Override
    public void onInitialized(@NotNull Context context) {}

    @Override
    public void onLoad(@NotNull Context context) {}

    @Override
    public void onLoaded(@NotNull Context context) {}

    @Override
    public void onEnable(@NotNull Context context) {}

    @Override
    public void onEnabled(@NotNull Context context) {}

    @Override
    public void onDisable(@NotNull Context context) {}

    @Override
    public void onDisabled(@NotNull Context context) {}

    @Override
    public void onDestroy(@NotNull Context context) {}

    @Override
    public void onDestroyed(@NotNull Context context) {}
}
