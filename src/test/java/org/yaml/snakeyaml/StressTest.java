/**
 * Copyright (c) 2008-2009 Andrey Somov
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

import java.io.IOException;

import junit.framework.TestCase;

import org.yaml.snakeyaml.constructor.Constructor;

public class StressTest extends TestCase {
    String doc;

    public void setUp() throws IOException {
        doc = Util.getLocalResource("specification/example2_27.yaml");
    }

    public void testPerformance() throws IOException {
        long time1 = System.nanoTime();
        new Yaml(new Loader(new Constructor(Invoice.class)));
        long time2 = System.nanoTime();
        float duration = (time2 - time1) / 1000000;
        System.out.println("Init was " + duration + " ms.");

        Yaml yaml = new Yaml(new Loader(new Constructor(Invoice.class)));
        time1 = System.nanoTime();
        yaml.load(doc);
        time2 = System.nanoTime();
        duration = (time2 - time1) / 1000000;
        System.out.println("\nSingle load was " + duration + " ms.");

        yaml = new Yaml(new Loader(new Constructor(Invoice.class)));
        int[] range = new int[] { 1000, 2000 };
        System.out.println("\nOne instance.");
        for (int number : range) {
            time1 = System.nanoTime();
            for (int i = 0; i < number; i++) {
                yaml.load(doc);
            }
            time2 = System.nanoTime();
            duration = ((time2 - time1) / 1000000) / (float) number;
            System.out.println("Duration for r=" + number + " was " + duration + " ms/load.");
            assertTrue("duration=" + duration, duration < 5);
        }

        System.out.println("\nMany instances.");
        for (int number : range) {
            time1 = System.nanoTime();
            for (int i = 0; i < number; i++) {
                yaml = new Yaml(new Loader(new Constructor(Invoice.class)));
                yaml.load(doc);
            }
            time2 = System.nanoTime();
            duration = ((time2 - time1) / 1000000) / (float) number;
            System.out.println("Duration for r=" + number + " was " + duration + " ms/load.");
            assertTrue("duration=" + duration, duration < 5);
        }
    }
}