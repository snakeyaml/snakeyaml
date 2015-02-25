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
package org.yaml.snakeyaml;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EnumMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.yaml.snakeyaml.constructor.Constructor;

public class EnumTest extends TestCase {

    // Dumping
    public void testDumpEnum() {
        Yaml yaml = new Yaml();
        String output = yaml.dump(Suit.CLUBS);
        assertEquals("!!org.yaml.snakeyaml.Suit 'CLUBS'\n", output);
    }

    public void testDumpOverriddenToString() {
        Yaml yaml = new Yaml();
        String output = yaml.dump(DumperOptions.FlowStyle.BLOCK);
        assertEquals("!!org.yaml.snakeyaml.DumperOptions$FlowStyle 'BLOCK'\n", output);
    }

    public void testDumpEnumArray() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        String output = yaml.dump(Suit.values());
        assertEquals(
                "- !!org.yaml.snakeyaml.Suit 'CLUBS'\n- !!org.yaml.snakeyaml.Suit 'DIAMONDS'\n- !!org.yaml.snakeyaml.Suit 'HEARTS'\n- !!org.yaml.snakeyaml.Suit 'SPADES'\n",
                output);
    }

    public void testDumpEnumList() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        List<Suit> list = Arrays.asList(Suit.values());
        String output = yaml.dump(list);
        assertEquals(
                "- !!org.yaml.snakeyaml.Suit 'CLUBS'\n- !!org.yaml.snakeyaml.Suit 'DIAMONDS'\n- !!org.yaml.snakeyaml.Suit 'HEARTS'\n- !!org.yaml.snakeyaml.Suit 'SPADES'\n",
                output);
    }

    public void testDumpEnumListNoAnchor() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        List<Suit> list = new ArrayList<Suit>(3);
        list.add(Suit.CLUBS);
        list.add(Suit.DIAMONDS);
        list.add(Suit.CLUBS);
        String output = yaml.dump(list);
        assertEquals(
                "- !!org.yaml.snakeyaml.Suit 'CLUBS'\n- !!org.yaml.snakeyaml.Suit 'DIAMONDS'\n- !!org.yaml.snakeyaml.Suit 'CLUBS'\n",
                output);
    }

    public void testDumpEnumMap() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        Map<String, Suit> map = new LinkedHashMap<String, Suit>();
        map.put("c", Suit.CLUBS);
        map.put("d", Suit.DIAMONDS);
        String output = yaml.dump(map);
        assertEquals(
                "c: !!org.yaml.snakeyaml.Suit 'CLUBS'\nd: !!org.yaml.snakeyaml.Suit 'DIAMONDS'\n",
                output);
    }

    public void testDumpEnumMap2() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        Map<Suit, Integer> map = new EnumMap<Suit, Integer>(Suit.class);
        map.put(Suit.CLUBS, 0);
        map.put(Suit.DIAMONDS, 123);
        String output = yaml.dump(map);
        assertEquals(
                "!!org.yaml.snakeyaml.Suit 'CLUBS': 0\n!!org.yaml.snakeyaml.Suit 'DIAMONDS': 123\n",
                output);
    }

    public void testDumpEnumBean() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        EnumBean bean = new EnumBean();
        bean.setId(17);
        bean.setSuit(Suit.SPADES);
        LinkedHashMap<Suit, Integer> map = new LinkedHashMap<Suit, Integer>();
        map.put(Suit.CLUBS, 1);
        map.put(Suit.DIAMONDS, 2);
        bean.setMap(map);
        String output = yaml.dump(bean);
        assertEquals(
                "!!org.yaml.snakeyaml.EnumBean\nid: 17\nmap:\n  CLUBS: 1\n  DIAMONDS: 2\nsuit: SPADES\n",
                output);
    }

    // Loading
    public void testLoadEnum() {
        Yaml yaml = new Yaml();
        Suit suit = (Suit) yaml.load("!!org.yaml.snakeyaml.Suit 'CLUBS'\n");
        assertEquals(Suit.CLUBS, suit);
    }

    public void testLoadOverridenToString() {
        Yaml yaml = new Yaml();
        assertEquals(DumperOptions.FlowStyle.BLOCK,
                yaml.load("!!org.yaml.snakeyaml.DumperOptions$FlowStyle 'BLOCK'\n"));
    }

    @SuppressWarnings("unchecked")
    public void testLoadEnumList() {
        Yaml yaml = new Yaml();
        List<Suit> list = (List<Suit>) yaml
                .load("- !!org.yaml.snakeyaml.Suit 'CLUBS'\n- !!org.yaml.snakeyaml.Suit 'DIAMONDS'\n- !!org.yaml.snakeyaml.Suit 'HEARTS'\n- !!org.yaml.snakeyaml.Suit 'SPADES'");
        assertEquals(4, list.size());
        assertEquals(Suit.CLUBS, list.get(0));
        assertEquals(Suit.DIAMONDS, list.get(1));
        assertEquals(Suit.HEARTS, list.get(2));
        assertEquals(Suit.SPADES, list.get(3));
    }

    @SuppressWarnings("unchecked")
    public void testLoadEnumMap() {
        Yaml yaml = new Yaml();
        Map<Integer, Suit> map = (Map<Integer, Suit>) yaml
                .load("1: !!org.yaml.snakeyaml.Suit 'HEARTS'\n2: !!org.yaml.snakeyaml.Suit 'DIAMONDS'");
        assertEquals(2, map.size());
        assertEquals(Suit.HEARTS, map.get(1));
        assertEquals(Suit.DIAMONDS, map.get(2));
    }

    public void testLoadEnumBean() {
        Yaml yaml = new Yaml();
        EnumBean bean = (EnumBean) yaml
                .load("!!org.yaml.snakeyaml.EnumBean\nid: 174\nmap:\n  !!org.yaml.snakeyaml.Suit 'CLUBS': 1\n  !!org.yaml.snakeyaml.Suit 'DIAMONDS': 2\nsuit: CLUBS");

        LinkedHashMap<Suit, Integer> map = new LinkedHashMap<Suit, Integer>();
        map.put(Suit.CLUBS, 1);
        map.put(Suit.DIAMONDS, 2);

        assertEquals(Suit.CLUBS, bean.getSuit());
        assertEquals(174, bean.getId());
        assertEquals(map, bean.getMap());
    }

    public void testLoadEnumBean2() {
        Constructor c = new Constructor();
        TypeDescription td = new TypeDescription(EnumBean.class);
        td.putMapPropertyType("map", Suit.class, Object.class);
        c.addTypeDescription(td);
        Yaml yaml = new Yaml(c);
        EnumBean bean = (EnumBean) yaml
                .load("!!org.yaml.snakeyaml.EnumBean\nid: 174\nmap:\n  CLUBS: 1\n  DIAMONDS: 2\nsuit: CLUBS");

        LinkedHashMap<Suit, Integer> map = new LinkedHashMap<Suit, Integer>();
        map.put(Suit.CLUBS, 1);
        map.put(Suit.DIAMONDS, 2);

        assertEquals(Suit.CLUBS, bean.getSuit());
        assertEquals(174, bean.getId());
        assertEquals(map, bean.getMap());
    }

    public void testLoadWrongEnum() {
        Yaml yaml = new Yaml();
        try {
            yaml.load("1: !!org.yaml.snakeyaml.Suit 'HEARTS'\n2: !!org.yaml.snakeyaml.Suit 'KOSYR'");
            fail("KOSYR is not Suit");
        } catch (Exception e) {
            assertTrue("KOSYR must be reported",
                    e.getMessage().contains("Unable to find enum value 'KOSYR' for enum"));
        }
    }
}
