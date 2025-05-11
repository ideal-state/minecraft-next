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
    private Byte aByte;
    @NonNull
    private Short aShort;
    @NonNull
    private Integer aInt;
    @NonNull
    private Long aLong;
    @NonNull
    private Float aFloat;
    @NonNull
    private Double aDouble;
    @NonNull
    private Character aChar;
    @NonNull
    private Boolean aBoolean;
    @NonNull
    private String aString;
    @NonNull
    private ExampleEnum aEnum;
    @NonNull
    private Object[] aArray;
    @NonNull
    private List<Integer> aList;
    @NonNull
    private Set<String> aSet;
    @NonNull
    private Map<String, Object> aMap;

    public enum ExampleEnum {
        FIRST, SECOND
    }
}
