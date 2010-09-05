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
    /**
     * Store context with a Mark to have a better error message. Loader works
     * 40% slower and it consumes much more memory (default=false)
     */
    private boolean withMarkContext = false;
    private ImplicitMode implicitMode = ImplicitMode.ALWAYS_IMPLICIT_TYPES;

    public enum ImplicitMode {
        /**
         * Disable implicit types when JavaBean is loaded (default).
         */
        DYNAMIC_IMPLICIT_TYPES,
        /**
         * Enable this when JavaBean has a property which is a generic
         * collections like Map<String, Integer>
         */
        ALWAYS_IMPLICIT_TYPES,
        /**
         * When implicit types are not used all the scalars are Strings.
         */
        NEVER_IMPLICIT_TYPES;
    }

    public boolean isWithMarkContext() {
        return withMarkContext;
    }

    public void setWithMarkContext(boolean useContextMark) {
        this.withMarkContext = useContextMark;
    }

    public ImplicitMode getImplicitMode() {
        return implicitMode;
    }

    public void setImplicitMode(ImplicitMode implicitMode) {
        this.implicitMode = implicitMode;
    }
}
