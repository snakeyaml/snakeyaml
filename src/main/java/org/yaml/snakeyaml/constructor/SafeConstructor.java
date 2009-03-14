/*
 * See LICENSE file in distribution for copyright and licensing information.
 */
package org.yaml.snakeyaml.constructor;

import java.math.BigInteger;
import java.util.Calendar;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.yaml.snakeyaml.error.YAMLException;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.util.Base64Coder;

/**
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public class SafeConstructor extends BaseConstructor {

    public SafeConstructor() {
        this.yamlConstructors.put("tag:yaml.org,2002:null", new ConstuctYamlNull());
        this.yamlConstructors.put("tag:yaml.org,2002:bool", new ConstuctYamlBool());
        this.yamlConstructors.put("tag:yaml.org,2002:int", new ConstuctYamlInt());
        this.yamlConstructors.put("tag:yaml.org,2002:float", new ConstuctYamlFloat());
        this.yamlConstructors.put("tag:yaml.org,2002:binary", new ConstuctYamlBinary());
        this.yamlConstructors.put("tag:yaml.org,2002:timestamp", new ConstuctYamlTimestamp());
        this.yamlConstructors.put("tag:yaml.org,2002:omap", new ConstuctYamlOmap());
        this.yamlConstructors.put("tag:yaml.org,2002:pairs", new ConstuctYamlPairs());
        this.yamlConstructors.put("tag:yaml.org,2002:set", new ConstuctYamlSet());
        this.yamlConstructors.put("tag:yaml.org,2002:str", new ConstuctYamlStr());
        this.yamlConstructors.put("tag:yaml.org,2002:seq", new ConstuctYamlSeq());
        this.yamlConstructors.put("tag:yaml.org,2002:map", new ConstuctYamlMap());
        this.yamlConstructors.put(null, new ConstuctUndefined());
    }

    private void flattenMapping(MappingNode node) {
        List<Node[]> merge = new LinkedList<Node[]>();
        int index = 0;
        List<Node[]> nodeValue = (List<Node[]>) node.getValue();
        while (index < nodeValue.size()) {
            Node keyNode = nodeValue.get(index)[0];
            Node valueNode = nodeValue.get(index)[1];
            if (keyNode.getTag().equals("tag:yaml.org,2002:merge")) {
                nodeValue.remove(index);
                switch (valueNode.getNodeId()) {
                case mapping:
                    MappingNode mn = (MappingNode) valueNode;
                    flattenMapping(mn);
                    merge.addAll(mn.getValue());
                    break;
                case sequence:
                    List<List<Node[]>> submerge = new LinkedList<List<Node[]>>();
                    SequenceNode sn = (SequenceNode) valueNode;
                    List<Node> vals = sn.getValue();
                    for (Node subnode : vals) {
                        if (!(subnode instanceof MappingNode)) {
                            throw new ConstructorException("while constructing a mapping", node
                                    .getStartMark(), "expected a mapping for merging, but found "
                                    + subnode.getNodeId(), subnode.getStartMark());
                        }
                        MappingNode mnode = (MappingNode) subnode;
                        flattenMapping(mnode);
                        submerge.add(mnode.getValue());
                    }
                    Collections.reverse(submerge);
                    for (List<Node[]> value : submerge) {
                        merge.addAll(value);
                    }
                    break;
                default:
                    throw new ConstructorException("while constructing a mapping", node
                            .getStartMark(),
                            "expected a mapping or list of mappings for merging, but found "
                                    + valueNode.getNodeId(), valueNode.getStartMark());
                }
            } else if (keyNode.getTag().equals("tag:yaml.org,2002:value")) {
                keyNode.setTag("tag:yaml.org,2002:str");
                index++;
            } else {
                index++;
            }
        }
        if (!merge.isEmpty()) {
            merge.addAll(nodeValue);
            ((MappingNode) node).setValue(merge);
        }
    }

    protected Map<Object, Object> constructMapping(MappingNode node) {
        flattenMapping(node);
        return super.constructMapping(node);
    }

    private class ConstuctYamlNull implements Construct {
        public Object construct(Node node) {
            constructScalar((ScalarNode) node);
            return null;
        }
    }

    private final static Map<String, Boolean> BOOL_VALUES = new HashMap<String, Boolean>();
    static {
        BOOL_VALUES.put("yes", Boolean.TRUE);
        BOOL_VALUES.put("no", Boolean.FALSE);
        BOOL_VALUES.put("true", Boolean.TRUE);
        BOOL_VALUES.put("false", Boolean.FALSE);
        BOOL_VALUES.put("on", Boolean.TRUE);
        BOOL_VALUES.put("off", Boolean.FALSE);
    }

    private class ConstuctYamlBool implements Construct {
        public Object construct(Node node) {
            String val = (String) constructScalar((ScalarNode) node);
            return BOOL_VALUES.get(val.toLowerCase());
        }
    }

    private class ConstuctYamlInt implements Construct {
        public Object construct(Node node) {
            String value = constructScalar((ScalarNode) node).toString().replaceAll("_", "");
            int sign = +1;
            char first = value.charAt(0);
            if (first == '-') {
                sign = -1;
                value = value.substring(1);
            } else if (first == '+') {
                value = value.substring(1);
            }
            int base = 10;
            if (value.equals("0")) {
                return new Integer(0);
            } else if (value.startsWith("0b")) {
                value = value.substring(2);
                base = 2;
            } else if (value.startsWith("0x")) {
                value = value.substring(2);
                base = 16;
            } else if (value.startsWith("0")) {
                value = value.substring(1);
                base = 8;
            } else if (value.indexOf(':') != -1) {
                String[] digits = value.split(":");
                int bes = 1;
                int val = 0;
                for (int i = 0, j = digits.length; i < j; i++) {
                    val += (Long.parseLong(digits[(j - i) - 1]) * bes);
                    bes *= 60;
                }
                return createNumber(sign, String.valueOf(val), 10);
            } else {
                return createNumber(sign, value, 10);
            }
            return createNumber(sign, value, base);
        }
    }

    private Number createNumber(int sign, String number, int radix) {
        Number result;
        if (sign < 0) {
            number = "-" + number;
        }
        try {
            int integer = Integer.parseInt(number, radix);
            result = new Integer(integer);
        } catch (NumberFormatException e) {
            try {
                long longValue = Long.parseLong(number, radix);
                result = new Long(longValue);
            } catch (NumberFormatException e1) {
                result = new BigInteger(number, radix);
            }
        }
        return result;
    }

    private class ConstuctYamlFloat implements Construct {
        public Object construct(Node node) {
            String value = constructScalar((ScalarNode) node).toString().replaceAll("_", "");
            int sign = +1;
            char first = value.charAt(0);
            if (first == '-') {
                sign = -1;
                value = value.substring(1);
            } else if (first == '+') {
                value = value.substring(1);
            }
            String valLower = value.toLowerCase();
            if (valLower.equals(".inf")) {
                return new Double(sign == -1 ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY);
            } else if (valLower.equals(".nan")) {
                return new Double(Double.NaN);
            } else if (value.indexOf(':') != -1) {
                String[] digits = value.split(":");
                int bes = 1;
                double val = 0.0;
                for (int i = 0, j = digits.length; i < j; i++) {
                    val += (Double.parseDouble(digits[(j - i) - 1]) * bes);
                    bes *= 60;
                }
                return new Double(sign * val);
            } else {
                try {
                    Double d = Double.valueOf(value);
                    return new Double(d.doubleValue() * sign);
                } catch (NumberFormatException e) {
                    throw new YAMLException("Invalid number: '" + value + "'; in node " + node);
                }
            }
        }
    }

    private class ConstuctYamlBinary implements Construct {
        public Object construct(Node node) {
            byte[] decoded = Base64Coder.decode(constructScalar((ScalarNode) node).toString()
                    .toCharArray());
            return decoded;
        }
    }

    private final static Pattern TIMESTAMP_REGEXP = Pattern
            .compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)(?:(?:[Tt]|[ \t]+)([0-9][0-9]?):([0-9][0-9]):([0-9][0-9])(?:\\.([0-9]*))?(?:[ \t]*(?:Z|([-+][0-9][0-9]?)(?::([0-9][0-9])?)?))?)?$");
    private final static Pattern YMD_REGEXP = Pattern
            .compile("^([0-9][0-9][0-9][0-9])-([0-9][0-9]?)-([0-9][0-9]?)$");

    private class ConstuctYamlTimestamp implements Construct {
        public Object construct(Node node) {
            Matcher match = YMD_REGEXP.matcher((String) node.getValue());
            if (match.matches()) {
                String year_s = match.group(1);
                String month_s = match.group(2);
                String day_s = match.group(3);
                Calendar cal = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
                cal.clear();
                cal.set(Calendar.YEAR, Integer.parseInt(year_s));
                // Java's months are zero-based...
                cal.set(Calendar.MONTH, Integer.parseInt(month_s) - 1); // x
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day_s));
                return cal.getTime();
            } else {
                match = TIMESTAMP_REGEXP.matcher((String) node.getValue());
                if (!match.matches()) {
                    throw new YAMLException("Expected timestamp: " + node);
                }
                String year_s = match.group(1);
                String month_s = match.group(2);
                String day_s = match.group(3);
                String hour_s = match.group(4);
                String min_s = match.group(5);
                String sec_s = match.group(6);
                String fract_s = match.group(7);
                String timezoneh_s = match.group(8);
                String timezonem_s = match.group(9);

                int usec = 0;
                if (fract_s != null) {
                    usec = Integer.parseInt(fract_s);
                    if (usec != 0) {
                        while (10 * usec < 1000) {
                            usec *= 10;
                        }
                    }
                }
                Calendar cal = Calendar.getInstance();
                cal.set(Calendar.YEAR, Integer.parseInt(year_s));
                // Java's months are zero-based...
                cal.set(Calendar.MONTH, Integer.parseInt(month_s) - 1);
                cal.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day_s));
                cal.set(Calendar.HOUR_OF_DAY, Integer.parseInt(hour_s));
                cal.set(Calendar.MINUTE, Integer.parseInt(min_s));
                cal.set(Calendar.SECOND, Integer.parseInt(sec_s));
                cal.set(Calendar.MILLISECOND, usec);
                if (timezoneh_s != null || timezonem_s != null) {
                    int zone = 0;
                    int sign = +1;
                    if (timezoneh_s != null) {
                        if (timezoneh_s.startsWith("-")) {
                            sign = -1;
                        }
                        zone += Integer.parseInt(timezoneh_s.substring(1)) * 3600000;
                    }
                    if (timezonem_s != null) {
                        zone += Integer.parseInt(timezonem_s) * 60000;
                    }
                    cal.set(Calendar.ZONE_OFFSET, sign * zone);
                } else {
                    cal.setTimeZone(TimeZone.getTimeZone("UTC"));
                }
                return cal.getTime();
            }
        }
    }

    private class ConstuctYamlOmap implements Construct {
        public Object construct(Node node) {
            // Note: we do not check for duplicate keys, because it's too
            // CPU-expensive.
            Map<Object, Object> omap = new LinkedHashMap<Object, Object>();
            if (!(node instanceof SequenceNode)) {
                throw new ConstructorException("while constructing an ordered map", node
                        .getStartMark(), "expected a sequence, but found " + node.getNodeId(), node
                        .getStartMark());
            }
            SequenceNode snode = (SequenceNode) node;
            for (Node subnode : snode.getValue()) {
                if (!(subnode instanceof MappingNode)) {
                    throw new ConstructorException("while constructing an ordered map", node
                            .getStartMark(), "expected a mapping of length 1, but found "
                            + subnode.getNodeId(), subnode.getStartMark());
                }
                MappingNode mnode = (MappingNode) subnode;
                if (mnode.getValue().size() != 1) {
                    throw new ConstructorException("while constructing an ordered map", node
                            .getStartMark(), "expected a single mapping item, but found "
                            + mnode.getValue().size() + " items", mnode.getStartMark());
                }
                Node keyNode = mnode.getValue().get(0)[0];
                Node valueNode = mnode.getValue().get(0)[1];
                Object key = constructObject(keyNode);
                Object value = constructObject(valueNode);
                omap.put(key, value);
            }
            return omap;
        }
    }

    // Note: the same code as `construct_yaml_omap`.
    private class ConstuctYamlPairs implements Construct {
        public Object construct(Node node) {
            // Note: we do not check for duplicate keys, because it's too
            // CPU-expensive.
            List<Object[]> pairs = new LinkedList<Object[]>();
            if (!(node instanceof SequenceNode)) {
                throw new ConstructorException("while constructing pairs", node.getStartMark(),
                        "expected a sequence, but found " + node.getNodeId(), node.getStartMark());
            }
            SequenceNode snode = (SequenceNode) node;
            for (Node subnode : snode.getValue()) {
                if (!(subnode instanceof MappingNode)) {
                    throw new ConstructorException("while constructingpairs", node.getStartMark(),
                            "expected a mapping of length 1, but found " + subnode.getNodeId(),
                            subnode.getStartMark());
                }
                MappingNode mnode = (MappingNode) subnode;
                if (mnode.getValue().size() != 1) {
                    throw new ConstructorException("while constructing pairs", node.getStartMark(),
                            "expected a single mapping item, but found " + mnode.getValue().size()
                                    + " items", mnode.getStartMark());
                }
                Node keyNode = mnode.getValue().get(0)[0];
                Node valueNode = mnode.getValue().get(0)[1];
                Object key = constructObject(keyNode);
                Object value = constructObject(valueNode);
                pairs.add(new Object[] { key, value });
            }
            return pairs;
        }
    }

    private class ConstuctYamlSet implements Construct {
        public Object construct(Node node) {
            Map<Object, Object> value = constructMapping((MappingNode) node);
            return value.keySet();
        }
    }

    private class ConstuctYamlStr implements Construct {
        public Object construct(Node node) {
            String value = (String) constructScalar((ScalarNode) node);
            return value;
        }
    }

    private class ConstuctYamlSeq implements Construct {
        public Object construct(Node node) {
            return constructSequence((SequenceNode) node);
        }
    }

    private class ConstuctYamlMap implements Construct {
        public Object construct(Node node) {
            return constructMapping((MappingNode) node);
        }
    }

    private class ConstuctUndefined implements Construct {
        public Object construct(Node node) {
            throw new ConstructorException(null, null,
                    "could not determine a constructor for the tag " + node.getTag(), node
                            .getStartMark());
        }
    }
}
