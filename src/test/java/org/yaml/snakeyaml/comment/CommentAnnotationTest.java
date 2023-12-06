package org.yaml.snakeyaml.comment;

import org.junit.Test;
import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.comments.Comment;

import static org.junit.Assert.assertEquals;

public class CommentAnnotationTest {
    public static class FieldComment {
        @Comment("watch out")
        public String yes = "no";
    }

    public static class MethodComment {
        private String value = "no";
        @Comment("watch out")
        public String getYes() {
            return value;
        }
        public void setYes(String value) {
            this.value = value;
        }
    }

    @Test
    public void commentAnnotationWorksForField() {
        // GIVEN
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setProcessComments(true);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        Yaml yaml = new Yaml(dumperOptions);

        // WHEN
        String result = yaml.dumpAsMap(new FieldComment());

        // THEN
        assertEquals("# watch out\n'yes': 'no'", result.trim());
    }

    @Test
    public void commentAnnotationWorksForMethod() {
        // GIVEN
        DumperOptions dumperOptions = new DumperOptions();
        dumperOptions.setProcessComments(true);
        dumperOptions.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        dumperOptions.setDefaultScalarStyle(DumperOptions.ScalarStyle.PLAIN);
        Yaml yaml = new Yaml(dumperOptions);

        // WHEN
        String result = yaml.dumpAsMap(new MethodComment());

        // THEN
        assertEquals("# watch out\n'yes': 'no'", result.trim());
    }
}
