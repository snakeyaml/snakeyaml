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
package org.yaml.snakeyaml.helpers;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.commons.lang.Validate;
import org.springframework.core.io.ClassPathResource;

/**
 * @since 2016-06-06
 * @author kibertoad
 *
 */
public class FileTestHelper {

    private FileTestHelper() {
    }

    /**
     * Provide an InputStream, wrapped into BufferedInputStream, from a file on
     * classpath
     * 
     * @param path
     *            - path to the file that will be the source for the InputStream
     * @return
     */
    public static InputStream getInputStreamFromClasspath(String path) {
        Validate.notEmpty(path, "Path cannot be empty.");
        ClassPathResource classpathResource = new ClassPathResource(path);
        try {
            Validate.isTrue(classpathResource.contentLength() > 0, "Content cannot be empty");
            return new BufferedInputStream(classpathResource.getInputStream());
        } catch (IOException e) {
            throw new IllegalStateException(e);
        }
    }

}
