/**
 * Copyright (c) 2008-2010, http://code.google.com/p/snakeyaml/
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
    private final Mode mode;

    public enum Mode {
        /**
         * Store context with a Mark to have a better error message. Loader
         * works 40% slower and it consumes much more memory (default=false)
         */
        CONTEXT_MARK(1),
        /**
         * Disable implicit types when JavaBean is loaded (default=true). When
         * this is present then USE_IMPLICIT_TYPES is ignored.
         */
        DYNAMIC_IMPLICIT_TYPES(2),
        /**
         * When DYNAMIC_IMPLICIT_TYPES is 'false' defines whether to apply the
         * regular expressions. When implicit types are not used all the scalars
         * are Strings. Enable this when JavaBean has a property which is a
         * generic collections like Map<String, Integer>
         */
        USE_IMPLICIT_TYPES(4),
        /**
         * Enable compact format for JavaBeans
         */
        COMPACT_FORMAT(8);

        private final int mask;

        private Mode(int mask) {
            this.mask = mask;
        }

        public boolean hasMode(Mode mode) {
            return (mode.mask & this.mask) == mode.mask;
        }
    }

    public LoaderOptions(Mode mode) {
        this.mode = mode;
    }

    public LoaderOptions() {
        this(Mode.DYNAMIC_IMPLICIT_TYPES);
    }

    public boolean hasMode(Mode mode) {
        return this.mode.hasMode(mode);
    }
}
