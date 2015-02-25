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
package org.yaml.snakeyaml.immutable;

import java.awt.Color;
import java.awt.Insets;
import java.awt.Rectangle;

import javax.swing.BorderFactory;
import javax.swing.border.MatteBorder;

import junit.framework.TestCase;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

public class MoreImmutablesTest extends TestCase {

    public void testInsets() {
        Yaml yaml = new Yaml(new ImmutablesRepresenter());
        Insets insets = new Insets(10, 20, 30, 40);
        String dump = yaml.dump(insets);
        assertEquals("!!java.awt.Insets [10, 20, 30, 40]\n", dump);
        Object loaded = yaml.load(dump);
        assertEquals(insets, loaded);
    }

    public void testAwtColor() {
        Yaml yaml = new Yaml(new ImmutablesRepresenter());
        Color color = new Color(10, 20, 30, 40);
        String dump = yaml.dump(color);
        assertEquals("!!java.awt.Color [10, 20, 30, 40]\n", dump);
        Object loaded = yaml.load(dump);
        assertEquals(color, loaded);
    }

    public void testRectangle() {
        Yaml yaml = new Yaml(new ImmutablesRepresenter());
        Rectangle rect = new Rectangle(10, 20, 30, 40);
        String dump = yaml.dump(rect);
        assertEquals("!!java.awt.Rectangle [10, 20, 30, 40]\n", dump);
        Object loaded = yaml.load(dump);
        assertEquals(rect, loaded);
    }

    // matteborder - only with color - no icon
    public void testMatteBorder() {
        DumperOptions options = new DumperOptions();
        options.setWidth(400);
        Yaml yaml = new Yaml(new ImmutablesRepresenter(), options);
        Insets insets = new Insets(10, 20, 30, 40);
        Color color = new Color(100, 150, 200);
        MatteBorder border = BorderFactory.createMatteBorder(insets.top, insets.left,
                insets.bottom, insets.right, color);
        String dump = yaml.dump(border);
        assertEquals(
                "!!javax.swing.border.MatteBorder [!!java.awt.Insets [10, 20, 30, 40], !!java.awt.Color [100, 150, 200, 255]]\n",
                dump);
        Object loaded = yaml.load(dump);
        assertTrue(loaded instanceof MatteBorder);
        MatteBorder loadedBorder = (MatteBorder) loaded;
        assertEquals(insets, loadedBorder.getBorderInsets());
        assertEquals(color, loadedBorder.getMatteColor());
    }
}
