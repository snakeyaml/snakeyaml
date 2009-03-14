/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.parser;

import org.yaml.snakeyaml.error.Mark;
import org.yaml.snakeyaml.error.MarkedYAMLException;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class ParserException extends MarkedYAMLException {
    private static final long serialVersionUID = -2349253802798398038L;

    public ParserException(String context, Mark contextMark, String problem, Mark problemMark) {
        super(context, contextMark, problem, problemMark, null);
    }
}
