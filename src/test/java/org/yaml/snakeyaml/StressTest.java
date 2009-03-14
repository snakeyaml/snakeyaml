/*
 * See LICENSE file in distribution for copyright and licensing information.
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
        long time1 = System.currentTimeMillis();
        new Yaml(new Loader(new Constructor(Invoice.class)));
        long time2 = System.currentTimeMillis();
        float duration = time2 - time1;
        System.out.println("Init was " + duration + " ms.");

        Yaml yaml = new Yaml(new Loader(new Constructor(Invoice.class)));
        time1 = System.currentTimeMillis();
        yaml.load(doc);
        time2 = System.currentTimeMillis();
        duration = time2 - time1;
        System.out.println("\nSingle load was " + duration + " ms.");

        yaml = new Yaml(new Loader(new Constructor(Invoice.class)));
        int[] range = new int[] { 1000, 2000 };
        System.out.println("\nOne instance.");
        for (int number : range) {
            time1 = System.currentTimeMillis();
            for (int i = 0; i < number; i++) {
                yaml.load(doc);
            }
            time2 = System.currentTimeMillis();
            duration = (time2 - time1) / (float) number;
            System.out.println("Duration for r=" + number + " was " + duration + " ms/load.");
            assertTrue("duration=" + duration, duration < 5);
        }

        System.out.println("\nMany instances.");
        for (int number : range) {
            time1 = System.currentTimeMillis();
            for (int i = 0; i < number; i++) {
                yaml = new Yaml(new Loader(new Constructor(Invoice.class)));
                yaml.load(doc);
            }
            time2 = System.currentTimeMillis();
            duration = (time2 - time1) / (float) number;
            System.out.println("Duration for r=" + number + " was " + duration + " ms/load.");
            assertTrue("duration=" + duration, duration < 5);
        }
    }
}