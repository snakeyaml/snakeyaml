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
