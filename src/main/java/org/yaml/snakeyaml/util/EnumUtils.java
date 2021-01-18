package org.yaml.snakeyaml.util;

public class EnumUtils {

    /**
     * Looks for an enumeration constant that matches the string without being case sensitive
     *
     * @param enumType - the Class object of the enum type from which to return a constant
     * @param name     - the name of the constant to return
     * @param <T>      - the enum type whose constant is to be returned
     * @return the enum constant of the specified enum type with the specified name, insensitive to case
     * @throws IllegalArgumentException – if the specified enum type has no constant with the specified name, insensitive case
     */
    public static <T extends Enum<T>> T findEnumInsensitiveCase(Class<T> enumType, String name) {
        for (T constant : enumType.getEnumConstants()) {
            if (constant.name().compareToIgnoreCase(name) == 0) {
                return constant;
            }
        }
        throw new IllegalArgumentException("No enum constant " + enumType.getCanonicalName() + "." + name);
    }
}
