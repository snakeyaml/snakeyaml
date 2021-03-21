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
import org.yaml.snakeyaml.reader.StreamReader;
import org.yaml.snakeyaml.scanner.Scanner;
import org.yaml.snakeyaml.scanner.ScannerImpl;
import org.yaml.snakeyaml.tokens.CommentToken;
import org.yaml.snakeyaml.tokens.ScalarToken;
import org.yaml.snakeyaml.tokens.Token;
import org.yaml.snakeyaml.tokens.Token.ID;

import java.io.StringReader;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ScannerWithCommentEnabledTest {

    private void assertTokensEqual(List<ID> expected, Scanner sut) {
        assertTokensEqual(expected, null, sut);
    }

    private void printToken(Token token) {
        String value;
        switch (token.getTokenId()) {
            case Scalar:
                value = "(value='" + ((ScalarToken) token).getValue() + "')";
                break;
            case Comment:
                CommentToken commentToken = (CommentToken) token;
                value = "(type='" + commentToken.getCommentType() + ", value='" + commentToken.getValue() + "')";
                break;
            default:
                value = "";
                break;
        }
        //System.out.println(token.getTokenId().name() + value);
    }

    private void assertTokenEquals(Iterator<ID> expectedIdIterator, Iterator<String> expectedScalarValueIterator,
                                   Token token) {
        printToken(token);
        assertTrue(expectedIdIterator.hasNext());
        ID expectedValue = expectedIdIterator.next();
        assertSame(expectedValue, token.getTokenId());
        if (expectedScalarValueIterator != null && token.getTokenId() == ID.Scalar) {
            assertEquals(expectedScalarValueIterator.next(), ((ScalarToken) token).getValue());
        }
    }

    private void assertTokensEqual(List<ID> expectedList, List<String> expectedScalarValueList, Scanner sut) {
        Iterator<ID> expectedIterator = expectedList.iterator();
        Iterator<String> expectedScalarValueIterator = expectedScalarValueList == null ? null
                : expectedScalarValueList.iterator();
        while (!sut.checkToken(Token.ID.StreamEnd)) {
            Token token = sut.getToken();
            assertTokenEquals(expectedIterator, expectedScalarValueIterator, token);
        }
        Token token = sut.peekToken();
        assertTokenEquals(expectedIterator, expectedScalarValueIterator, token);
        assertFalse(expectedIterator.hasNext());
    }

    private Scanner constructScanner(String input) {
        return new ScannerImpl(new StreamReader(new StringReader(input))).setEmitComments(true);
    }

    @Test
    public void testEmitComments() {
        ScannerImpl sutWithOutComments = new ScannerImpl(new StreamReader(new StringReader(""))).setEmitComments(false);
        assertFalse(sutWithOutComments.isEmitComments());
        ScannerImpl sutWithComments = new ScannerImpl(new StreamReader(new StringReader(""))).setEmitComments(true);
        assertTrue(sutWithComments.isEmitComments());
    }

    @Test
    public void testEmpty() {
        List<ID> expected = Arrays.asList(ID.StreamStart, ID.StreamEnd);

        Scanner sut = constructScanner("");

        assertTokensEqual(expected, sut);
    }

    @Test
    public void testOnlyCommentLines() {
        List<ID> expected = Arrays.asList(ID.StreamStart, //
                ID.Comment, //
                ID.Comment, //
                ID.StreamEnd);

        Scanner sut = constructScanner("" + //
                "# This stream contains no\n" + //
                "# documents, only comments.");

        assertTokensEqual(expected, sut);
    }

    @Test
    public void testCommentEndingALine() {
        List<ID> expected = Arrays.asList(ID.StreamStart, //
                ID.BlockMappingStart, //
                ID.Key, ID.Scalar, ID.Value, ID.Comment, //
                ID.Scalar, //
                ID.BlockEnd, //
                ID.StreamEnd);
        List<String> expectedScalarValue = Arrays.asList(//
                "key", "value");

        Scanner sut = constructScanner("" + //
                "key: # Comment\n" + //
                "  value\n");

        assertTokensEqual(expected, expectedScalarValue, sut);
    }

    @Test
    public void testMultiLineComment() {
        List<ID> expected = Arrays.asList(ID.StreamStart, //
                ID.BlockMappingStart, //
                ID.Key, ID.Scalar, ID.Value, ID.Comment, ID.Comment, //
                ID.Scalar, //
                ID.Comment, //
                ID.BlockEnd, //
                ID.StreamEnd);
        List<String> expectedScalarValue = Arrays.asList(//
                "key", "value");

        Scanner sut = constructScanner("" + //
                "key: # Comment\n" + //
                "     # lines\n" + //
                "  value\n" + //
                "\n");

        assertTokensEqual(expected, expectedScalarValue, sut);
    }

    @Test
    public void testBlankLine() {
        List<ID> expected = Arrays.asList(ID.StreamStart, //
                ID.Comment, //
                ID.StreamEnd);

        Scanner sut = constructScanner("" + //
                "\n");

        assertTokensEqual(expected, sut);
    }

    @Test
    public void testBlankLineComments() {
        List<ID> expected = Arrays.asList(ID.StreamStart, //
                ID.Comment, //
                ID.BlockMappingStart, //
                ID.Key, ID.Scalar, ID.Value, ID.Scalar, ID.Comment, //
                ID.Comment, //
                ID.Comment, //
                ID.BlockEnd, //
                ID.StreamEnd);

        Scanner sut = constructScanner("" + //
                "\n" + //
                "abc: def # commment\n" + //
                "\n" + //
                "\n");

        assertTokensEqual(expected, sut);
    }

    @Test
    public void test_blockScalar_replaceNLwithSpaces_singleNLatEnd() {
        List<ID> expected = Arrays.asList(//
                ID.StreamStart, //
                ID.BlockMappingStart, //
                ID.Key, ID.Scalar, ID.Value, ID.Comment, //
                ID.Scalar, //
                ID.BlockEnd, //
                ID.StreamEnd //
        );
        List<String> expectedScalarValue = Arrays.asList(//
                "abc", "def hij\n");

        Scanner sut = constructScanner("abc: > # Comment\n    def\n    hij\n\n");

        //printTokens(sut);
        assertTokensEqual(expected, expectedScalarValue, sut);
    }

    @Test
    public void test_blockScalar_replaceNLwithSpaces_noNLatEnd() {
        List<ID> expected = Arrays.asList(//
                ID.StreamStart, //
                ID.BlockMappingStart, //
                ID.Key, ID.Scalar, ID.Value, ID.Comment, ID.Scalar, //
                ID.BlockEnd, //
                ID.StreamEnd //
        );
        List<String> expectedScalarValue = Arrays.asList(//
                "abc", "def hij");

        Scanner sut = constructScanner("abc: >- # Comment\n    def\n    hij\n\n");

        assertTokensEqual(expected, expectedScalarValue, sut);
    }

    @Test
    public void test_blockScalar_replaceNLwithSpaces_allNLatEnd() {
        List<ID> expected = Arrays.asList(//
                ID.StreamStart, //
                ID.BlockMappingStart, //
                ID.Key, ID.Scalar, ID.Value, ID.Comment, ID.Scalar, //
                ID.Comment, //
                ID.BlockEnd, //
                ID.StreamEnd //
        );
        List<String> expectedScalarValue = Arrays.asList(//
                "abc", "def hij\n\n");

        Scanner sut = constructScanner("abc: >+ # Comment\n    def\n    hij\n\n");

        assertTokensEqual(expected, expectedScalarValue, sut);
    }

    @Test
    public void test_blockScalar_keepNL_singleNLatEnd() {
        List<ID> expected = Arrays.asList(//
                ID.StreamStart, //
                ID.BlockMappingStart, //
                ID.Key, ID.Scalar, ID.Value, ID.Comment, ID.Scalar, //
                ID.BlockEnd, //
                ID.StreamEnd //
        );
        List<String> expectedScalarValue = Arrays.asList(//
                "abc", "def\nhij\n");

        Scanner sut = constructScanner("abc: | # Comment\n    def\n    hij\n\n");

        assertTokensEqual(expected, expectedScalarValue, sut);
    }

    @Test
    public void test_blockScalar_keepNL_noNLatEnd() {
        List<ID> expected = Arrays.asList(//
                ID.StreamStart, //
                ID.BlockMappingStart, //
                ID.Key, ID.Scalar, ID.Value, ID.Comment, ID.Scalar, //
                ID.BlockEnd, //
                ID.StreamEnd //
        );
        List<String> expectedScalarValue = Arrays.asList(//
                "abc", "def\nhij");

        Scanner sut = constructScanner("abc: |- # Comment\n    def\n    hij\n\n");

        assertTokensEqual(expected, expectedScalarValue, sut);
    }

    @Test
    public void test_blockScalar_keepNL_allNLatEnd() {
        List<ID> expected = Arrays.asList(//
                ID.StreamStart, //
                ID.BlockMappingStart, //
                ID.Key, ID.Scalar, ID.Value, ID.Comment, ID.Scalar, //
                ID.Comment, //
                ID.BlockEnd, //
                ID.StreamEnd //
        );
        List<String> expectedScalarValue = Arrays.asList(//
                "abc", "def\nhij\n\n");

        Scanner sut = constructScanner("abc: |+ # Comment\n    def\n    hij\n\n");

        assertTokensEqual(expected, expectedScalarValue, sut);
    }

    @Test
    public void testDirectiveLineEndComment() {
        List<ID> expected = Arrays.asList(//
                ID.StreamStart, //
                ID.Directive, //
                ID.Comment, //
                ID.StreamEnd //
        );

        Scanner sut = constructScanner("%YAML 1.1 #Comment\n");

        assertTokensEqual(expected, sut);
    }
}
