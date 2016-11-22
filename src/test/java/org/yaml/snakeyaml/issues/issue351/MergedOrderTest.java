package org.yaml.snakeyaml.issues.issue351;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.junit.Test;
import org.yaml.snakeyaml.Yaml;

public class MergedOrderTest {

    @SuppressWarnings("rawtypes")
    @Test
    public void mergedLinkedMapOrder() throws IOException {
        Yaml yaml = new Yaml();
        InputStream inputStream = MergedOrderTest.class
                .getResourceAsStream("/issues/issue351_1.yaml");
        Map<?, ?> bean = yaml.loadAs(inputStream, Map.class);

        Object first = bean.get("prize_cashBack_fixture");
        Object second = bean.get("prize_cashBack_sendEmail_fixture");

        assertThat(first, instanceOf(Map.class));
        assertThat(second, instanceOf(Map.class));

        assertArrayEquals(((Map) first).entrySet().toArray(), ((Map) second).entrySet().toArray());

        inputStream.close();
    }
}
