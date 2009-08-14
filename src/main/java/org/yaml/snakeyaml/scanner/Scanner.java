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
package org.yaml.snakeyaml.scanner;

import java.util.List;

import org.yaml.snakeyaml.tokens.Token;

/**
 * Produce <code>Token<code>s.
 * 
 * @see <a href="http://pyyaml.org/wiki/PyYAML">PyYAML</a> for more information
 */
public interface Scanner {
    /**
     * Check if the next token is one of the given types.
     */
    boolean checkToken(List<Class<? extends Token>> choices);

    /**
     * Convenience method to avoid List creation
     */
    boolean checkToken(Class<? extends Token> choice);

    /**
     * Return the next token, but do not delete it from the queue.
     */
    Token peekToken();

    /**
     * Return the next token.
     */
    Token getToken();

}
