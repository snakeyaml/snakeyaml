/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.error;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class YAMLException extends RuntimeException {
    private static final long serialVersionUID = -4738336175050337570L;

    public YAMLException(String message) {
        super(message);
    }

    public YAMLException(Throwable cause) {
        super(cause);
    }
}
