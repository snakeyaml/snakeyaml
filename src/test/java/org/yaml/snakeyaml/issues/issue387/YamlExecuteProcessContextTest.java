/**
 * Copyright (c) 2008, SnakeYAML
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
package org.yaml.snakeyaml.issues.issue387;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Collection;
import java.util.Set;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

public class YamlExecuteProcessContextTest {

    public static enum ExecuteProcessConstants {
        EXECUTE_ID, EXECUTE_STATUS_START, EXECUTE_STATUS_DONE
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class YamlExecuteProcessContext {
        private String executionID;
        private String schemaName;
        private String username;
        private String hostname;
        private String sql;
        private Collection<YamlExecuteProcessUnit> unitStatuses;
        private Long startTimeMillis;
    }

    @NoArgsConstructor
    @AllArgsConstructor
    @Data
    public static class YamlExecuteProcessUnit {
        private String unitID;
        private ExecuteProcessConstants status;
    }

    @Test
    public void parameterizedCollectionTest() {
        String marshal = "unitStatuses: !!set\n" +
                "  ? status: EXECUTE_STATUS_DONE\n" +
                "    unitID: '159917166'\n" +
                "  : null\n";
        Constructor constructor = new Constructor(YamlExecuteProcessContext.class);
        YamlExecuteProcessContext unmarshal = new Yaml(constructor).loadAs(marshal,
                YamlExecuteProcessContext.class);

        assertThat(unmarshal.getUnitStatuses(), instanceOf(Set.class));

        for (YamlExecuteProcessUnit unit : unmarshal.getUnitStatuses()) {
            assertEquals(unit.getStatus(), ExecuteProcessConstants.EXECUTE_STATUS_DONE);
        }
    }
}
