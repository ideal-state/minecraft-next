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

package team.idealstate.minecraft.next.spigot.example.mapper;

import java.util.List;
import org.apache.ibatis.annotations.Mapper;
import team.idealstate.minecraft.next.spigot.example.data.ExampleEntity;
import team.idealstate.sugar.next.context.annotation.feature.Environment;
import team.idealstate.sugar.validate.annotation.NotNull;
import team.idealstate.sugar.validate.annotation.Nullable;

/**
 * @author ketikai
 * @description 针对表【EXAMPLE_ENTITY】的数据库操作Mapper
 * @createDate 2025-05-12 23:46:43 @Entity team.idealstate.minecraft.next.spigot.data.ExampleEntity
 */
@Mapper
@Environment("development")
public interface ExampleEntityMapper {

    void createTable();

    @NotNull
    String ping();

    void insert(@NotNull ExampleEntity entity);

    boolean update(@NotNull ExampleEntity entity);

    boolean delete(@NotNull ExampleEntity entity);

    @Nullable
    ExampleEntity selectOne(@NotNull ExampleEntity entity);

    @NotNull
    List<ExampleEntity> selectAll();

    boolean exists(@NotNull ExampleEntity entity);

    int size();
}
