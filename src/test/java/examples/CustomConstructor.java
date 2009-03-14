/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package examples;

import java.util.ArrayList;
import java.util.List;

import org.yaml.snakeyaml.Invoice;
import org.yaml.snakeyaml.constructor.Constructor;

public class CustomConstructor extends Constructor {

    public CustomConstructor() {
        super(Invoice.class);
    }

    @Override
    protected List<Object> createDefaultList(int initSize) {
        return new ArrayList<Object>(initSize);
    }
}