/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.scanner;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class ScannerException extends MarkedYAMLException {

    private static final long serialVersionUID = 4782293188600445954L;

    public ScannerException(String context, Mark contextMark, String problem, Mark problemMark,
            String note) {
        super(context, contextMark, problem, problemMark, note);
    }

    public ScannerException(String context, Mark contextMark, String problem, Mark problemMark) {
        super(context, contextMark, problem, problemMark, null);
    }

}
