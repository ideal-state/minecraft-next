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

package team.idealstate.minecraft.next.spigot.configuration;

import java.util.List;
import java.util.Map;
import java.util.Set;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import team.idealstate.sugar.next.context.annotation.component.Configuration;
import team.idealstate.sugar.next.context.annotation.feature.Environment;

@Configuration(uri = "/example/config.yml", release = "bundled:/example/config.yml")
@Environment("development")
@Data
public class ExampleConfiguration {

    @NonNull
    private Byte oneByte;

    @NonNull
    private Short oneShort;

    @NonNull
    private Integer oneInt;

    @NonNull
    private Long oneLong;

    @NonNull
    private Float oneFloat;

    @NonNull
    private Double oneDouble;

    @NonNull
    private Character oneChar;

    @NonNull
    private Boolean oneBoolean;

    @NonNull
    private String oneString;

    @NonNull
    private ExampleEnum oneEnum;

    @NonNull
    private Object[] oneArray;

    @NonNull
    private List<Integer> oneList;

    @NonNull
    private Set<String> oneSet;

    @NonNull
    private Map<String, Object> oneMap;

    public enum ExampleEnum {
        FIRST,
        SECOND
    }
}
