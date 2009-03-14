/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.pyyaml;

import org.yaml.snakeyaml.error.YAMLException;

public class CanonicalException extends YAMLException {

    private static final long serialVersionUID = -6489045150083747626L;

    public CanonicalException(String message) {
        super(message);
    }

    public CanonicalException(Throwable cause) {
        super(cause);
    }
}
