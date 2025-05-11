package team.idealstate.minecraft.next.spigot.configuration;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import team.idealstate.sugar.next.context.annotation.component.Configuration;
import team.idealstate.sugar.next.context.annotation.feature.Environment;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Configuration(uri = "/example/config.yml", release = "bundled:/example/config.yml")
@Environment("development")
@Data
@AllArgsConstructor
@NoArgsConstructor
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
        FIRST, SECOND
    }
}
