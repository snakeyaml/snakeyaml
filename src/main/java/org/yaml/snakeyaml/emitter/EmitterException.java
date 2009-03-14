/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.emitter;

import org.yaml.snakeyaml.error.YAMLException;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class EmitterException extends YAMLException {
    private static final long serialVersionUID = -8280070025452995908L;

    public EmitterException(String msg) {
        super(msg);
    }
}
