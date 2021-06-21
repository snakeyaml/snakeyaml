/**
 * Copyright (c) 2008, http://www.snakeyaml.org
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
package org.yaml.snakeyaml.comment;

import org.junit.Test;
import org.yaml.snakeyaml.comments.CommentLine;
import org.yaml.snakeyaml.composer.Composer;
import org.yaml.snakeyaml.constructor.SafeConstructor;
import org.yaml.snakeyaml.nodes.MappingNode;
import org.yaml.snakeyaml.nodes.Node;
import org.yaml.snakeyaml.nodes.NodeTuple;
import org.yaml.snakeyaml.nodes.ScalarNode;
import org.yaml.snakeyaml.nodes.SequenceNode;
import org.yaml.snakeyaml.parser.ParserImpl;
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.resolver.Resolver;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class ComposerWithCommentEnabledTest {
    private boolean DEBUG = false;

    private void printBlockComment(Node node, int level, PrintStream out) {
        if (node.getBlockComments() != null) {
            List<CommentLine> blockComments = node.getBlockComments();
            for (int i = 0; i < blockComments.size(); i++) {
                printWithIndent("Block Comment", level, out);
            }
        }
    }

    private void printEndComment(Node node, int level, PrintStream out) {
        if (node.getEndComments() != null) {
            List<CommentLine> endComments = node.getEndComments();
            for (int i = 0; i < endComments.size(); i++) {
                printWithIndent("End Comment", level, out);
            }
        }
    }

    private void printInLineComment(Node node, int level, PrintStream out) {
        if (node.getInLineComments() != null) {
            List<CommentLine> inLineComments = node.getInLineComments();
            for (int i = 0; i < inLineComments.size(); i++) {
                printWithIndent("InLine Comment", level + 1, out);
            }
        }
    }

    private void printWithIndent(String line, int level, PrintStream out) {
        for (int ix = 0; ix < level; ix++) {
            out.print("    ");
        }
        out.print(line);
        out.print("\n");
    }

    private void printNodeInternal(Node node, int level, PrintStream out) {

        if (node instanceof MappingNode) {
            MappingNode mappingNode = (MappingNode) node;
            printBlockComment(mappingNode, level, out);
            printWithIndent(mappingNode.getClass().getSimpleName(), level, out);
            for (NodeTuple childNodeTuple : mappingNode.getValue()) {
                printWithIndent("Tuple", level + 1, out);
                printNodeInternal(childNodeTuple.getKeyNode(), level + 2, out);
                printNodeInternal(childNodeTuple.getValueNode(), level + 2, out);
            }
            printInLineComment(mappingNode, level, out);
            printEndComment(mappingNode, level, out);

        } else if (node instanceof SequenceNode) {
            SequenceNode sequenceNode = (SequenceNode) node;
            printBlockComment(sequenceNode, level, out);
            printWithIndent(sequenceNode.getClass().getSimpleName(), level, out);
            for (Node childNode : sequenceNode.getValue()) {
                printNodeInternal(childNode, level + 1, out);
            }
            printInLineComment(sequenceNode, level, out);
            printEndComment(sequenceNode, level, out);

        } else if (node instanceof ScalarNode) {
            ScalarNode scalarNode = (ScalarNode) node;
            printBlockComment(scalarNode, level, out);
            printWithIndent(scalarNode.getClass().getSimpleName() + ": " + scalarNode.getValue(), level, out);
            printInLineComment(scalarNode, level, out);
            printEndComment(scalarNode, level, out);

        } else {
            printBlockComment(node, level, out);
            printWithIndent(node.getClass().getSimpleName(), level, out);
            printInLineComment(node, level, out);
            printEndComment(node, level, out);
        }
    }

    private void printNodeList(List<Node> nodeList) {
        if (DEBUG) {
            System.out.println("BEGIN");
            boolean first = true;
            for (Node node : nodeList) {
                if (first) {
                    first = false;
                } else {
                    System.out.println("---");
                }
                printNodeInternal(node, 1, System.out);
            }
            System.out.println("DONE\n");
        }
    }

    private List<Node> getNodeList(Composer composer) {
        List<Node> nodeList = new ArrayList<>();
        while (composer.checkNode()) {
            nodeList.add(composer.getNode());
        }
        return nodeList;
    }

    private void assertNodesEqual(String[] expected, List<Node> nodeList) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        boolean first = true;
        try (PrintStream out = new PrintStream(baos)) {
            for (Node node : nodeList) {
                if (first) {
                    first = false;
                } else {
                    out.print("---\n");
                }
                printNodeInternal(node, 0, out);
            }
        }
        String actualString = baos.toString();
        String[] actuals = actualString.split("\n");
        for (int ix = 0; ix < Math.min(expected.length, actuals.length); ix++) {
            assertEquals(expected[ix], actuals[ix]);
        }
        assertEquals(expected.length, actuals.length);
    }

    public Composer newComposerWithCommentsEnabled(String data) {
        return new Composer(new ParserImpl(new StreamReader(data), true), new Resolver());
    }

    @Test
    public void testEmpty() {
        String data = "";
        String[] expected = new String[]{ //
                "" //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testParseWithOnlyComment() {
        String data = "# Comment";
        String[] expected = new String[]{ //
                "Block Comment", //
                "MappingNode", //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testCommentEndingALine() {
        String data = "" + //
                "key: # Comment\n" + //
                "  value\n";

        String[] expected = new String[]{ //
                "MappingNode", //
                "    Tuple", //
                "        ScalarNode: key", //
                "            InLine Comment", //
                "        ScalarNode: value" //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testMultiLineComment() {
        String data = "" + //
                "key: # Comment\n" + //
                "     # lines\n" + //
                "  value\n" + //
                "\n";

        String[] expected = new String[]{ //
                "MappingNode", //
                "    Tuple", //
                "        ScalarNode: key", //
                "            InLine Comment", //
                "            InLine Comment", //
                "        ScalarNode: value", //
                "End Comment" //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testBlankLine() {
        String data = "" + //
                "\n";

        String[] expected = new String[]{ //
                "Block Comment", //
                "MappingNode", //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testBlankLineComments() {
        String data = "" + //
                "\n" + //
                "abc: def # commment\n" + //
                "\n" + //
                "\n";

        String[] expected = new String[]{ //
                "MappingNode", //
                "    Tuple", //
                "        Block Comment", //
                "        ScalarNode: abc", //
                "        ScalarNode: def", //
                "            InLine Comment", //
                "End Comment", //
                "End Comment", //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void test_blockScalar() {
        String data = "" + //
                "abc: > # Comment\n" + //
                "    def\n" + //
                "    hij\n" + //
                "\n";

        String[] expected = new String[]{ //
                "MappingNode", //
                "    Tuple", //
                "        ScalarNode: abc", //
                "            InLine Comment", //
                "        ScalarNode: def hij" //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testDirectiveLineEndComment() {
        String data = "%YAML 1.1 #Comment\n";

        String[] expected = new String[]{ //
                "" //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testSequence() {
        String data = "" + //
                "# Comment\n" + //
                "list: # InlineComment1\n" + //
                "# Block Comment\n" + //
                "- item # InlineComment2\n" + //
                "# Comment\n";

        String[] expected = new String[]{ //
                "MappingNode", //
                "    Tuple", //
                "        Block Comment", //
                "        ScalarNode: list", //
                "            InLine Comment", //
                "        SequenceNode", //
                "            Block Comment", //
                "            ScalarNode: item", //
                "                InLine Comment", //
                "End Comment" //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testAllComments1() throws Exception {
        String data = "" + //
                "# Block Comment1\n" + //
                "# Block Comment2\n" + //
                "key: # Inline Comment1a\n" + //
                "     # Inline Comment1b\n" + //
                "  # Block Comment3a\n" + //
                "  # Block Comment3b\n" + //
                "  value # Inline Comment2\n" + //
                "# Block Comment4\n" + //
                "list: # InlineComment3a\n" + //
                "      # InlineComment3b\n" + //
                "# Block Comment5\n" + //
                "- item1 # InlineComment4\n" + //
                "- item2: [ value2a, value2b ] # InlineComment5\n" + //
                "- item3: { key3a: [ value3a1, value3a2 ], key3b: value3b } # InlineComment6\n" + //
                "# Block Comment6\n" + //
                "---\n" + //
                "# Block Comment7\n" + //
                "";

        String[] expected = new String[]{ //
                "MappingNode", //
                "    Tuple", //
                "        Block Comment", //
                "        Block Comment", //
                "        ScalarNode: key", //
                "            InLine Comment", //
                "            InLine Comment", //
                "        Block Comment", //
                "        Block Comment", //
                "        ScalarNode: value", //
                "            InLine Comment", //
                "    Tuple", //
                "        Block Comment", //
                "        ScalarNode: list", //
                "            InLine Comment", //
                "            InLine Comment", //
                "        SequenceNode", //
                "            Block Comment", //
                "            ScalarNode: item1", //
                "                InLine Comment", //
                "            MappingNode", //
                "                Tuple", //
                "                    ScalarNode: item2", //
                "                    SequenceNode", //
                "                        ScalarNode: value2a", //
                "                        ScalarNode: value2b", //
                "                        InLine Comment", //
                "            MappingNode", //
                "                Tuple", //
                "                    ScalarNode: item3", //
                "                    MappingNode", //
                "                        Tuple", //
                "                            ScalarNode: key3a", //
                "                            SequenceNode", //
                "                                ScalarNode: value3a1", //
                "                                ScalarNode: value3a2", //
                "                        Tuple", //
                "                            ScalarNode: key3b", //
                "                            ScalarNode: value3b", //
                "                        InLine Comment", //
                "End Comment", //
                "---", //
                "Block Comment", //
                "ScalarNode: ", // This is an empty scalar created as this is an empty document
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testAllComments2() throws Exception {
        String data = "" + //
                "# Block Comment1\n" + //
                "# Block Comment2\n" + //
                "- item1 # Inline Comment1a\n" + //
                "        # Inline Comment1b\n" + //
                "# Block Comment3a\n" + //
                "# Block Comment3b\n" + //
                "- item2: value # Inline Comment2\n" + //
                "# Block Comment4\n" + //
                "";

        String[] expected = new String[]{ //
                "SequenceNode", //
                "    Block Comment", //
                "    Block Comment", //
                "    ScalarNode: item1", //
                "        InLine Comment", //
                "        InLine Comment", //
                "    MappingNode", //
                "        Tuple", //
                "            Block Comment", //
                "            Block Comment", //
                "            ScalarNode: item2", //
                "            ScalarNode: value", //
                "                InLine Comment", //
                "End Comment", //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testAllComments3() throws Exception {
        String data = "" + //
                "# Block Comment1\n" + //
                "[ item1, item2: value2, {item3: value3} ] # Inline Comment1\n" + //
                "# Block Comment2\n" + //
                "";

        String[] expected = new String[]{ //
                "Block Comment", //
                "SequenceNode", //
                "    ScalarNode: item1", //
                "    MappingNode", //
                "        Tuple", //
                "            ScalarNode: item2", //
                "            ScalarNode: value2", //
                "    MappingNode", //
                "        Tuple", //
                "            ScalarNode: item3", //
                "            ScalarNode: value3", //
                "    InLine Comment", //
                "End Comment", //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = getNodeList(sut);

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testGetSingleNode() {
        String data = "" + //
                "\n" + //
                "abc: def # commment\n" + //
                "\n" + //
                "\n";
        String[] expected = new String[]{ //
                "MappingNode", //
                "    Tuple", //
                "        Block Comment",
                "        ScalarNode: abc", //
                "        ScalarNode: def", //
                "            InLine Comment", //
                "End Comment", //
                "End Comment", //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = Arrays.asList(sut.getSingleNode());

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    @Test
    public void testGetSingleNodeHeaderComment() {
        String data = "" + //
                "\n" + //
                "# Block Comment1\n" + //
                "# Block Comment2\n" + //
                "abc: def # commment\n" + //
                "\n" + //
                "\n";
        String[] expected = new String[]{ //
                "MappingNode", //
                "    Tuple", //
                "        Block Comment", //
                "        Block Comment", //
                "        Block Comment", //
                "        ScalarNode: abc", //
                "        ScalarNode: def", //
                "            InLine Comment", //
                "End Comment", //
                "End Comment", //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = Arrays.asList(sut.getSingleNode());

        printNodeList(result);
        assertNodesEqual(expected, result);
    }

    private static class TestConstructor extends SafeConstructor {
    }

    @Test
    public void testBaseConstructorGetData() {
        String data = "" + //
                "\n" + //
                "abc: def # commment\n" + //
                "\n" + //
                "\n";

        TestConstructor sut = new TestConstructor();
        sut.setComposer(newComposerWithCommentsEnabled(data));
        Object result = sut.getData();
        assertTrue(result instanceof LinkedHashMap);
        @SuppressWarnings("unchecked")
        LinkedHashMap<String, Object> map = (LinkedHashMap<String, Object>) result;
        assertEquals(1, map.size());
        assertEquals(map.get("abc"), "def");
    }

    @Test
    public void testEmptyEntryInMap() {
        String data =
                "userProps:\n" + //
                "#password\n"+ //
                "pass: mySecret\n";
        String[] expected = new String[]{ //
                "MappingNode", //
                "    Tuple", //
                "        ScalarNode: userProps", //
                "        ScalarNode: ", //
                "    Tuple", //
                "        Block Comment", //
                "        ScalarNode: pass", //
                "        ScalarNode: mySecret", //
        };

        Composer sut = newComposerWithCommentsEnabled(data);
        List<Node> result = Arrays.asList(sut.getSingleNode());

        printNodeList(result);
        assertNodesEqual(expected, result);
    }
}
