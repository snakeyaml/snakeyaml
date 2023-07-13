/**
 * Copyright (c) 2008, SnakeYAML
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package org.yaml.snakeyaml.issues.issue1065;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.Iterator;
import org.junit.Test;
import org.yaml.snakeyaml.LoaderOptions;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.error.YAMLException;

/**
 * https://bitbucket.org/snakeyaml/snakeyaml/issues/1065
 */
public class DocumentSizeLimitTest {

  /**
   * The document start '---\n' is added to the first document
   */
  @Test
  public void testFirstLoadManyDocuments() {
    LoaderOptions options = new LoaderOptions();
    options.setCodePointLimit(8);
    Yaml yaml = new Yaml(options);
    String doc = "---\nfoo\n---\nbar\n";
    Iterator<Object> iter1 = yaml.loadAll(doc).iterator();
    assertEquals("foo", iter1.next());
    assertEquals("bar", iter1.next());
    assertFalse(iter1.hasNext());
    // exceed the limit
    options.setCodePointLimit(8 - 1);
    yaml = new Yaml(options);
    Iterator<Object> iter2 = yaml.loadAll(doc).iterator();
    assertEquals("foo", iter2.next());
    try {
      iter2.next();
    } catch (YAMLException e) {
      assertEquals("The incoming YAML document exceeds the limit: 4 code points.", e.getMessage());
    }
  }

  /**
   * The document start '---\n' is added to the non-first documents.
   */
  @Test
  public void testLastLoadManyDocuments() {
    LoaderOptions options = new LoaderOptions();
    String secondDocument = "---\nbar\n";
    int limit = secondDocument.length();
    options.setCodePointLimit(limit);
    Yaml yaml = new Yaml(options);
    String complete = "foo\n" + secondDocument;
    Iterator<Object> iter1 = yaml.loadAll(complete).iterator();
    assertEquals("foo", iter1.next());
    assertEquals("bar", iter1.next());
    assertFalse(iter1.hasNext());
    // exceed the limit
    options.setCodePointLimit(limit - 1);
    yaml = new Yaml(options);
    Iterator<Object> iter2 = yaml.loadAll(complete).iterator();
    assertEquals("foo", iter2.next());
    try {
      iter2.next();
    } catch (YAMLException e) {
      assertEquals("The incoming YAML document exceeds the limit: 4 code points.", e.getMessage());
    }
  }

  @Test
  public void testLoadDocuments() {
    String doc1 = "document: this is document one\n";
    String doc2 = "document: this is document 2\n";
    String doc3 = "document: this is document three\n";
    String input = doc1 + "---\n" + doc2 + "---\n" + doc3;

    assertTrue("Test1. All should load, all docs are less than total input size.",
        dumpAllDocs(input, input.length()));

    assertTrue("Test2. All should load, all docs are less than total input size - 1.",
        dumpAllDocs(input, input.length() - 1));

    // TODO is should be without +4 (for ---\n)
    assertTrue("Test3. All should load, all docs are less or equal to doc3 size.",
        dumpAllDocs(input, doc3.length() + 1));

    // TODO is should be with +3, to be 1 less than ---\n
    assertFalse("Test4. Should fail to load at 3rd doc, it is longer than doc3 size -1.",
        dumpAllDocs(input, doc3.length()));
  }

  private boolean dumpAllDocs(String input, int codePointLimit) {
    LoaderOptions loaderOpts = new LoaderOptions();
    loaderOpts.setCodePointLimit(codePointLimit);
    Yaml yaml = new Yaml(loaderOpts);
    Iterator<Object> docs = yaml.loadAll(input).iterator();
    for (int ndx = 1; ndx <= 3; ndx++) {
      try {
        Object doc = docs.next();
        // System.out.println("doc " + ndx + " loaded: " + doc);
      } catch (Exception e) {
        // System.out.println("doc " + ndx + " failed: " + e.getMessage());
        return false;
      }
    }
    return true;
  }
}
