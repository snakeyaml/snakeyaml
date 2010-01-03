/**
 * Copyright (c) 2008-2009 Andrey Somov
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.yaml.snakeyaml.nodes;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class Tags {
    public static final String PREFIX = "tag:yaml.org,2002:";
    public static final String MAP = PREFIX + "map";
    public static final String SEQ = PREFIX + "seq";
    public static final String STR = PREFIX + "str";
    public static final String NULL = PREFIX + "null";
    public static final String BOOL = PREFIX + "bool";
    public static final String TIMESTAMP = PREFIX + "timestamp";
    public static final String FLOAT = PREFIX + "float";
    public static final String INT = PREFIX + "int";
    public static final String BINARY = PREFIX + "binary";
    public static final String OMAP = PREFIX + "omap";
    public static final String PAIRS = PREFIX + "pairs";
    public static final String SET = PREFIX + "set";
    public static final String MERGE = PREFIX + "merge";
    public static final String VALUE = PREFIX + "value";
    public static final String YAML = PREFIX + "yaml";

    public static String getGlobalTagForClass(Class<? extends Object> clazz) {
        return PREFIX + clazz.getName();
    }

    // TODO is it the proper place for the code ?
    // TODO should we define the preference which one is created when the
    // runtime class is not known ?
    /**
     * Get list of classes which are constructed out of the same tag
     * 
     * @param tag
     *            - the tag with more then one matching Java class
     * @return
     */
    @SuppressWarnings("unchecked")
    public static List<Class> getCompatibleForTag(String tag) {
        List<Class> result = new ArrayList<Class>(3);
        if (TIMESTAMP.equals(tag)) {
            result.add(Date.class);
            result.add(java.sql.Date.class);
            result.add(Timestamp.class);
        } else if (FLOAT.equals(tag)) {
            result.add(Double.class);
            result.add(Float.class);
            result.add(BigDecimal.class);
        } else if (INT.equals(tag)) {
            result.add(Integer.class);
            result.add(Long.class);
            result.add(BigInteger.class);
        }
        return result;
    }
}
