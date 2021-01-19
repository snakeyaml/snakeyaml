/**
 * Copyright (c) 2008, http://www.snakeyaml.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package examples.enumset;

import java.util.EnumSet;

import org.junit.Assert;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.Node;

public class YamlEnumSetTest {

    Day day;
    EnumSet<Day> setOfDays;

    public Day getDay() {
        return this.day;
    }

    public void setDay(Day day) {
        this.day = day;
    }

    public EnumSet<Day> getSetOfDays() {
        return this.setOfDays;
    }

    public void setSetOfDays(EnumSet<Day> setOfDays) {
        this.setOfDays = setOfDays;
    }

    @Test
    public void enumSetDumpLoad() {

        YamlEnumSetTest yEST = new YamlEnumSetTest();
        yEST.day = Day.SUNDAY;
        yEST.setOfDays = EnumSet.of(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY);

        String output = createYaml().dump(yEST);
        YamlEnumSetTest loaded = createYaml().loadAs(output, YamlEnumSetTest.class);
        Assert.assertTrue(loaded.day == Day.SUNDAY);

        Object[] expected = yEST.setOfDays.toArray();
        Object[] actual = loaded.setOfDays.toArray();

        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(yEST.setOfDays, loaded.setOfDays);
    }

    @Test
    public void enumSetLoadWithoutTags() {

        YamlEnumSetTest yEST = new YamlEnumSetTest();
        yEST.day = Day.SUNDAY;
        yEST.setOfDays = EnumSet.of(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY);

        String yamlStr = "day: SUNDAY\nsetOfDays: { MONDAY, WEDNESDAY, FRIDAY }\n";
        YamlEnumSetTest loaded = createYaml().loadAs(yamlStr, YamlEnumSetTest.class);
        Assert.assertTrue(loaded.day == Day.SUNDAY);

        Object[] expected = yEST.setOfDays.toArray();
        Object[] actual = loaded.setOfDays.toArray();

        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(yEST.setOfDays, loaded.setOfDays);
    }

    @Test
    public void enumSetLoadWithoutCaseSensitive() {
        //given
        LoaderOptions loaderOptions = new LoaderOptions();
        loaderOptions.setEnumCaseSensitive(false);

        YamlEnumSetTest yEST = new YamlEnumSetTest();
        yEST.day = Day.SUNDAY;
        yEST.setOfDays = EnumSet.of(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY);

        String yamlStr = "day: SUNDAY\nsetOfDays: { MONDAY, wednesday, friDay }\n";

        //when
        YamlEnumSetTest loaded = createYaml(loaderOptions).loadAs(yamlStr, YamlEnumSetTest.class);

        //then
        Assert.assertTrue(loaded.day == Day.SUNDAY);

        Object[] expected = yEST.setOfDays.toArray();
        Object[] actual = loaded.setOfDays.toArray();

        Assert.assertArrayEquals(expected, actual);
        Assert.assertEquals(yEST.setOfDays, loaded.setOfDays);
    }

    @Test(expected = YAMLException.class)
    public void enumSetLoadWithCaseSensitive() {
        YamlEnumSetTest yEST = new YamlEnumSetTest();
        yEST.day = Day.SUNDAY;
        yEST.setOfDays = EnumSet.of(Day.MONDAY, Day.WEDNESDAY, Day.FRIDAY);

        String yamlStr = "day: SUNDAY\nsetOfDays: { MONDAY, wednesday, friDay }\n";

        //when
        createYaml().loadAs(yamlStr, YamlEnumSetTest.class);
    }

    private Yaml createYaml(LoaderOptions loaderOptions) {
        Yaml yaml = loaderOptions != null ? new Yaml(loaderOptions) : new Yaml();

        TypeDescription yamlEnumSetTD = new TypeDescription(YamlEnumSetTest.class) {

            @Override
            public Object newInstance(String propertyName, Node node) {
                if ("setOfDays".equals(propertyName)) {
                    node.setTwoStepsConstruction(true);
                    return EnumSet.noneOf(Day.class);
                }
                return super.newInstance(propertyName, node);
            }
        };

        yaml.addTypeDescription(yamlEnumSetTD);

        return yaml;
    }

    private Yaml createYaml() {
        return createYaml(null);
    }



}
