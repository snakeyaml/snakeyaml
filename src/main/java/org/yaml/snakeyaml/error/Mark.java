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
package org.yaml.snakeyaml.error;

import org.yaml.snakeyaml.scanner.Constant;

import java.io.Serializable;

/**
 * It's just a record and its only use is producing nice error messages. Parser
 * does not use it for any other purposes.
 */
public final class Mark implements Serializable {
    private String name;
    private int line;
    private int column;
    private char[] buffer;
    private int pointer;

    public Mark(String name, int line, int column, char[] buffer, int pointer) {
        super();
        this.name = name;
        this.line = line;
        this.column = column;
        this.buffer = buffer;
        this.pointer = pointer;
    }

    private boolean isLineBreak(int c) {
        return Constant.NULL_OR_LINEBR.has(c);
    }

    public String get_snippet(int indent, int max_length) {
        if (buffer == null) {
            return null;
        }
        float half = max_length / 2 - 1;
        int start = pointer;
        String head = "";
        while ((start > 0) && !isLineBreak(Character.codePointAt(buffer, start - 1))) {
            start -= 1;
            if (pointer - start > half) {
                head = " ... ";
                start += 5;
                break;
            }
        }
        String tail = "";
        int end = pointer;
        while ((end < buffer.length) && !isLineBreak(Character.codePointAt(buffer, end))) {
            end += 1;
            if (end - pointer > half) {
                tail = " ... ";
                end -= 5;
                break;
            }
        }

        StringBuilder result = new StringBuilder();
        for (int i = 0; i < indent; i++) {
            result.append(" ");
        }
        result.append(head);
        result.append(buffer, start, (end - start));
        result.append(tail);
        result.append("\n");
        for (int i = 0; i < indent + pointer - start + head.length(); i++) {
            result.append(" ");
        }
        result.append("^");
        return result.toString();
    }

    public String get_snippet() {
        return get_snippet(4, 75);
    }

    @Override
    public String toString() {
        String snippet = get_snippet();
        StringBuilder builder = new StringBuilder(" in ");
        builder.append(name);
        builder.append(", line ");
        builder.append(line + 1);
        builder.append(", column ");
        builder.append(column + 1);
        builder.append(":\n");
        builder.append(snippet);
        return builder.toString();
    }

    public String getName() {
        return name;
    }

    /**
     * starts with 0
     * @return line number
     */
    public int getLine() {
        return line;
    }

    /**
     * starts with 0
     * @return column number
     */
    public int getColumn() {
        return column;
    }

}
