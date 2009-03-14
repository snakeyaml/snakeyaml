/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.resolver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.nodes.NodeId;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class Resolver {
    private static final String DEFAULT_SCALAR_TAG = "tag:yaml.org,2002:str";
    private static final String DEFAULT_SEQUENCE_TAG = "tag:yaml.org,2002:seq";
    private static final String DEFAULT_MAPPING_TAG = "tag:yaml.org,2002:map";
    private static final Pattern BOOL = Pattern
            .compile("^(?:yes|Yes|YES|no|No|NO|true|True|TRUE|false|False|FALSE|on|On|ON|off|Off|OFF)$");
    private static final Pattern FLOAT = Pattern
            .compile("^(?:[-+]?(?:[0-9][0-9_]*)\\.[0-9_]*(?:[eE][-+][0-9]+)?|[-+]?(?:[0-9][0-9_]*)?\\.[0-9_]+(?:[eE][-+][0-9]+)?|[-+]?[0-9][0-9_]*(?::[0-5]?[0-9])+\\.[0-9_]*|[-+]?\\.(?:inf|Inf|INF)|\\.(?:nan|NaN|NAN))$");
    private static final Pattern INT = Pattern
            .compile("^(?:[-+]?0b[0-1_]+|[-+]?0[0-7_]+|[-+]?(?:0|[1-9][0-9_]*)|[-+]?0x[0-9a-fA-F_]+|[-+]?[1-9][0-9_]*(?::[0-5]?[0-9])+)$");
    private static final Pattern MERGE = Pattern.compile("^(?:<<)$");
    private static final Pattern NULL = Pattern.compile("^(?:~|null|Null|NULL| )$");
    private static final Pattern EMPTY = Pattern.compile("^$");
    private static final Pattern TIMESTAMP = Pattern
            .compile("^(?:[0-9][0-9][0-9][0-9]-[0-9][0-9]-[0-9][0-9]|[0-9][0-9][0-9][0-9]-[0-9][0-9]?-[0-9][0-9]?(?:[Tt]|[ \t]+)[0-9][0-9]?:[0-9][0-9]:[0-9][0-9](?:\\.[0-9]*)?(?:[ \t]*(?:Z|[-+][0-9][0-9]?(?::[0-9][0-9])?))?)$");
    private static final Pattern VALUE = Pattern.compile("^(?:=)$");
    private static final Pattern YAML = Pattern.compile("^(?:!|&|\\*)$");

    private Map<Character, List<ResolverTuple>> yamlImplicitResolvers = new HashMap<Character, List<ResolverTuple>>();

    public Resolver() {
        addImplicitResolver("tag:yaml.org,2002:bool", BOOL, "yYnNtTfFoO");
        addImplicitResolver("tag:yaml.org,2002:float", FLOAT, "-+0123456789.");
        addImplicitResolver("tag:yaml.org,2002:int", INT, "-+0123456789");
        addImplicitResolver("tag:yaml.org,2002:merge", MERGE, "<");
        addImplicitResolver("tag:yaml.org,2002:null", NULL, "~nN\0");
        addImplicitResolver("tag:yaml.org,2002:null", EMPTY, null);
        addImplicitResolver("tag:yaml.org,2002:timestamp", TIMESTAMP, "0123456789");
        addImplicitResolver("tag:yaml.org,2002:value", VALUE, "=");
        // The following implicit resolver is only for documentation purposes.
        // It cannot work
        // because plain scalars cannot start with '!', '&', or '*'.
        addImplicitResolver("tag:yaml.org,2002:yaml", YAML, "!&*");
    }

    public void addImplicitResolver(String tag, Pattern regexp, String first) {
        if (first == null) {
            List<ResolverTuple> curr = yamlImplicitResolvers.get(null);
            if (curr == null) {
                curr = new LinkedList<ResolverTuple>();
                yamlImplicitResolvers.put(null, curr);
            }
            curr.add(new ResolverTuple(tag, regexp));
        } else {
            char[] chrs = first.toCharArray();
            for (int i = 0, j = chrs.length; i < j; i++) {
                Character theC = new Character(chrs[i]);
                if (theC == 0) {
                    // special case: for null
                    theC = null;
                }
                List<ResolverTuple> curr = yamlImplicitResolvers.get(theC);
                if (curr == null) {
                    curr = new LinkedList<ResolverTuple>();
                    yamlImplicitResolvers.put(theC, curr);
                }
                curr.add(new ResolverTuple(tag, regexp));
            }
        }
    }

    public String resolve(NodeId kind, String value, boolean implicit) {
        if (kind == NodeId.scalar && implicit) {
            List<ResolverTuple> resolvers = null;
            if ("".equals(value)) {
                resolvers = yamlImplicitResolvers.get('\0');
            } else {
                resolvers = yamlImplicitResolvers.get(value.charAt(0));
            }
            if (resolvers != null) {
                for (ResolverTuple v : resolvers) {
                    String tag = v.getTag();
                    Pattern regexp = v.getRegexp();
                    if (regexp.matcher(value).matches()) {
                        return tag;
                    }
                }
            }
            if (yamlImplicitResolvers.containsKey(null)) {
                for (ResolverTuple v : yamlImplicitResolvers.get(null)) {
                    String tag = v.getTag();
                    Pattern regexp = v.getRegexp();
                    if (regexp.matcher(value).matches()) {
                        return tag;
                    }
                }
            }
        }
        switch (kind) {
        case scalar:
            return DEFAULT_SCALAR_TAG;
        case sequence:
            return DEFAULT_SEQUENCE_TAG;
        default:
            return DEFAULT_MAPPING_TAG;
        }
    }
}