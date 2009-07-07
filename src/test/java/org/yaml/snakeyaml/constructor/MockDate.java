/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.util.Date;

public class MockDate extends Date {

    private static final long serialVersionUID = 621384692653658062L;

    public MockDate(long date) {
        throw new RuntimeException("Test error.");
    }
}
