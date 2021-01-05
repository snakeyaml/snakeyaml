package org.yaml.snakeyaml.util;

public class EnumUtils {

    /**
     * Looks for an enumeration constant that matches the string without being case sensitive
     * @param enumeration class of the enumeration
     * @param enumConstantName enumerated constant name
     * @param <T> enumeration type
     * @return an enumeration type that matches the name of the enumerated constant
     */
    public static <T extends Enum<?>> T findEnumWithIgnoreCase(Class<T> enumeration, String enumConstantName) {
        for (T enumConstant : enumeration.getEnumConstants()) {
            if (enumConstant.name().compareToIgnoreCase(enumConstantName) == 0) {
                return enumConstant;
            }
        }
        return null;
    }
}
