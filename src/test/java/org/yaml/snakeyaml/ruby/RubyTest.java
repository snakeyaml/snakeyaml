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
package org.yaml.snakeyaml.ruby;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Util;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.nodes.Tag;
import org.yaml.snakeyaml.representer.Representer;

public class RubyTest extends TestCase {

    public void testParse() {
        TestObject result = parseObject(Util.getLocalResource("ruby/ruby1.yaml"));
        assertNotNull(result);
        assertEquals(0, result.getSub1().getAtt2());
        assertEquals("MyString", result.getSub2().getAtt1());
        assertEquals(1, result.getSub2().getAtt2().size());
        assertEquals(12345, result.getSub2().getAtt3());
    }

    public void testEmitNoTags() {
        TestObject result = parseObject(Util.getLocalResource("ruby/ruby1.yaml"));
        DumperOptions options = new DumperOptions();
        options.setExplicitStart(true);
        Yaml yaml2 = new Yaml(options);
        String output = yaml2.dumpAsMap(result);
        assertFalse("No tags expected.", output.contains("Sub1"));
        // System.out.println(output);
        // parse back. Without tags it shall still work
        Yaml beanLoader = new Yaml();
        TestObject result2 = beanLoader.loadAs(output, TestObject.class);
        assertEquals(0, result2.getSub1().getAtt2());
        assertEquals("MyString", result2.getSub2().getAtt1());
        assertEquals(1, result2.getSub2().getAtt2().size());
        assertEquals(12345, result2.getSub2().getAtt3());
    }

    public void testEmitWithTags() {
        TestObject result = parseObject(Util.getLocalResource("ruby/ruby1.yaml"));
        DumperOptions options = new DumperOptions();
        options.setExplicitStart(true);
        Representer repr = new Representer();
        repr.addClassTag(TestObject.class, new Tag("!ruby/object:Test::Module::Object"));
        repr.addClassTag(Sub1.class, new Tag("!ruby/object:Test::Module::Sub1"));
        repr.addClassTag(Sub2.class, new Tag("!ruby/object:Test::Module::Sub2"));
        Yaml yaml2 = new Yaml(repr, options);
        String output = yaml2.dump(result);
        // System.out.println(output);
        assertTrue("Tags must be present.",
                output.startsWith("--- !ruby/object:Test::Module::Object"));
        assertTrue("Tags must be present: " + output,
                output.contains("!ruby/object:Test::Module::Sub1"));
        assertTrue("Tags must be present.", output.contains("!ruby/object:Test::Module::Sub2"));
        // parse back.
        TestObject result2 = parseObject(output);
        assertEquals(0, result2.getSub1().getAtt2());
        assertEquals("MyString", result2.getSub2().getAtt1());
        assertEquals(1, result2.getSub2().getAtt2().size());
        assertEquals(12345, result2.getSub2().getAtt3());
    }

    public void testEmitWithTags2WithoutTagForParentJavabean() {
        TestObject result = parseObject(Util.getLocalResource("ruby/ruby1.yaml"));
        DumperOptions options = new DumperOptions();
        options.setExplicitStart(true);
        Representer repr = new Representer();
        repr.addClassTag(Sub1.class, new Tag("!ruby/object:Test::Module::Sub1"));
        repr.addClassTag(Sub2.class, new Tag("!ruby/object:Test::Module::Sub2"));
        Yaml yaml2 = new Yaml(repr, options);
        String output = yaml2.dump(result);
        // System.out.println(output);
        assertTrue("Tags must be present.",
                output.startsWith("--- !!org.yaml.snakeyaml.ruby.TestObject"));
        assertTrue("Tags must be present: " + output,
                output.contains("!ruby/object:Test::Module::Sub1"));
        assertTrue("Tags must be present.", output.contains("!ruby/object:Test::Module::Sub2"));
        // parse back.
        TestObject result2 = parseObject(output);
        assertEquals(0, result2.getSub1().getAtt2());
        assertEquals("MyString", result2.getSub2().getAtt1());
        assertEquals(1, result2.getSub2().getAtt2().size());
        assertEquals(12345, result2.getSub2().getAtt3());
    }

    private TestObject parseObject(String input) {
        Constructor con = new Constructor(TestObject.class);
        con.addTypeDescription(new TypeDescription(TestObject.class,
                "!ruby/object:Test::Module::Object"));
        con.addTypeDescription(new TypeDescription(Sub1.class, "!ruby/object:Test::Module::Sub1"));
        con.addTypeDescription(new TypeDescription(Sub2.class, "!ruby/object:Test::Module::Sub2"));

        Yaml yaml = new Yaml(con);
        return (TestObject) yaml.load(input);
    }
}
