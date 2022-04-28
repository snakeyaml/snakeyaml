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
package org.yaml.snakeyaml;

public class LoaderOptions {

    private boolean allowDuplicateKeys = true;
    private boolean wrappedToRootException = false;
    private int maxAliasesForCollections = 50; //to prevent YAML at https://en.wikipedia.org/wiki/Billion_laughs_attack
    private boolean allowRecursiveKeys = false;
    private boolean processComments = false;
    private boolean enumCaseSensitive = true;
    private int nestingDepthLimit = 50;

    public boolean isAllowDuplicateKeys() {
        return allowDuplicateKeys;
    }

    /**
     * Allow/Reject duplicate map keys in the YAML file.
     *
     * Default is to allow.
     *
     * YAML 1.1 is slightly vague around duplicate entries in the YAML file. The
     * best reference is <a href="http://www.yaml.org/spec/1.1/#id862121">
     * 3.2.1.3. Nodes Comparison</a> where it hints that a duplicate map key is
     * an error.
     *
     * For future reference, YAML spec 1.2 is clear. The keys MUST be unique.
     * <a href="http://www.yaml.org/spec/1.2/spec.html#id2759572">1.3. Relation
     * to JSON</a>
     * @param allowDuplicateKeys false to reject duplicate mapping keys
     */
    public void setAllowDuplicateKeys(boolean allowDuplicateKeys) {
        this.allowDuplicateKeys = allowDuplicateKeys;
    }

    public boolean isWrappedToRootException() {
        return wrappedToRootException;
    }

    /**
     * Wrap runtime exception to YAMLException during parsing or leave them as they are
     *
     * Default is to leave original exceptions
     *
     * @param wrappedToRootException - true to convert runtime exception to YAMLException
     */
    public void setWrappedToRootException(boolean wrappedToRootException) {
        this.wrappedToRootException = wrappedToRootException;
    }

    public int getMaxAliasesForCollections() {
        return maxAliasesForCollections;
    }

    /**
     * Restrict the amount of aliases for collections (sequences and mappings)
     * to avoid https://en.wikipedia.org/wiki/Billion_laughs_attack
     * @param maxAliasesForCollections set max allowed value (50 by default)
     */
    public void setMaxAliasesForCollections(int maxAliasesForCollections) {
    	this.maxAliasesForCollections = maxAliasesForCollections;
    }

    /**
     * Allow recursive keys for mappings. By default, it is not allowed.
     * This setting only prevents the case when the key is the value. If the key is only a part of the value
     * (the value is a sequence or a mapping) then this case is not recognized and always allowed.
     * @param allowRecursiveKeys - false to disable recursive keys
     */
    public void setAllowRecursiveKeys(boolean allowRecursiveKeys) {
    	this.allowRecursiveKeys = allowRecursiveKeys;
    }

    public boolean getAllowRecursiveKeys() {
        return allowRecursiveKeys;
    }

    /**
     * Set the comment processing. By default, comments are ignored.
     *
     * @param processComments <code>true</code> to process; <code>false</code> to ignore
     */
    public void setProcessComments(boolean processComments) {
        this.processComments = processComments;
    }

    public boolean isProcessComments() {
        return processComments;
    }

    public boolean isEnumCaseSensitive() {
        return enumCaseSensitive;
    }

    /**
     * Disables or enables case sensitivity during construct enum constant from string value
     * Default is false.
     *
     * @param enumCaseSensitive - true to set enum case sensitive, false the reverse
     */
    public void setEnumCaseSensitive(boolean enumCaseSensitive) {
        this.enumCaseSensitive = enumCaseSensitive;
    }

    public int getNestingDepthLimit() {
        return nestingDepthLimit;
    }

    /**
     * Set max depth of nested collections. When the limit is exceeded an exception is thrown.
     * Aliases/Anchors are not counted.
     * This is to prevent a DoS attack
     * @param nestingDepthLimit - depth to be accepted (50 by default)
     */
    public void setNestingDepthLimit(int nestingDepthLimit) {
        this.nestingDepthLimit = nestingDepthLimit;
    }
}
