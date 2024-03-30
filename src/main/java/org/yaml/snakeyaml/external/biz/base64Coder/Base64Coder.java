// Copyright 2003-2010 Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland
// www.source-code.biz, www.inventec.ch/chdh
//
// This module is multi-licensed and may be used under the terms
// of any of the following licenses:
//
// EPL, Eclipse Public License, V1.0 or later, http://www.eclipse.org/legal
// LGPL, GNU Lesser General Public License, V2.1 or later, http://www.gnu.org/licenses/lgpl.html
// GPL, GNU General Public License, V2 or later, http://www.gnu.org/licenses/gpl.html
// AL, Apache License, V2.0 or later, http://www.apache.org/licenses
// BSD, BSD License, http://www.opensource.org/licenses/bsd-license.php
//
// Please contact the author if you need another license.
// This module is provided "as is", without warranties of any kind.

package org.yaml.snakeyaml.external.biz.base64Coder;

import java.beans.Encoder;

/**
 * A Base64 encoder/decoder.
 *
 * <p>
 * This class is used to encode and decode data in Base64 format as described in RFC 1521.
 *
 * <p>
 * Project home page: <a href="http://www.source-code.biz/base64coder/java/">www.
 * source-code.biz/base64coder/java</a><br>
 * Author: Christian d'Heureuse, Inventec Informatik AG, Zurich, Switzerland<br>
 * Multi-licensed: EPL / LGPL / GPL / AL / BSD.
 */
public class Base64Coder {

  // The line separator string of the operating system.
  private static final String systemLineSeparator = System.getProperty("line.separator");

  // Mapping table from 6-bit nibbles to Base64 characters.
  private static final char[] map1 = new char[64];

  static {
    int i = 0;
    for (char c = 'A'; c <= 'Z'; c++) {
      map1[i++] = c;
    }
    for (char c = 'a'; c <= 'z'; c++) {
      map1[i++] = c;
    }
    for (char c = '0'; c <= '9'; c++) {
      map1[i++] = c;
    }
    map1[i++] = '+';
    map1[i++] = '/';
  }

  // Mapping table from Base64 characters to 6-bit nibbles.
  private static final byte[] map2 = new byte[128];

  static {
    for (int i = 0; i < map2.length; i++) {
      map2[i] = -1;
    }
    for (int i = 0; i < 64; i++) {
      map2[map1[i]] = (byte) i;
    }
  }

  /**
   * Encodes a string into Base64 format. No blanks or line breaks are inserted.
   *
   * @param s A String to be encoded.
   * @return A String containing the Base64 encoded data.
   */
  public static String encodeString(String s) {
    return new String(Base64Encoder.encode(s.getBytes()));
  }

  /**
   * Encodes a byte array into Base 64 format and breaks the output into lines of 76 characters.
   * This method is compatible with <code>sun.misc.BASE64Encoder.encodeBuffer(byte[])</code>.
   *
   * @param in An array containing the data bytes to be encoded.
   * @return A String containing the Base64 encoded data, broken into lines.
   */
  public static String encodeLines(byte[] in) {
    return Base64Encoder.encodeLines(in, 0, in.length, 76, systemLineSeparator);
  }

  /**
   * Encodes a byte array into Base 64 format and breaks the output into lines.
   *
   * @param in An array containing the data bytes to be encoded.
   * @param iOff Offset of the first byte in <code>in</code> to be processed.
   * @param iLen Number of bytes to be processed in <code>in</code>, starting at <code>iOff</code>.
   * @param lineLen Line length for the output data. Should be a multiple of 4.
   * @param lineSeparator The line separator to be used to separate the output lines.
   * @return A String containing the Base64 encoded data, broken into lines.
   */
  public static String encodeLines(byte[] in, int iOff, int iLen, int lineLen,
      String lineSeparator) {
    return Base64Encoder.encodeLines(in, iOff, iLen, lineLen, lineSeparator);
  }

  /**
   * Encodes a byte array into Base64 format. No blanks or line breaks are inserted in the output.
   *
   * @param in An array containing the data bytes to be encoded.
   * @return A character array containing the Base64 encoded data.
   */
  public static char[] encode(byte[] in) {
    return Base64Encoder.encode(in, 0, in.length);
  }

  /**
   * Encodes a byte array into Base64 format. No blanks or line breaks are inserted in the output.
   *
   * @param in An array containing the data bytes to be encoded.
   * @param iLen Number of bytes to process in <code>in</code>.
   * @return A character array containing the Base64 encoded data.
   */
  public static char[] encode(byte[] in, int iLen) {
    return Base64Encoder.encode(in, 0, iLen);
  }

  /**
   * Encodes a byte array into Base64 format. No blanks or line breaks are inserted in the output.
   *
   * @param in An array containing the data bytes to be encoded.
   * @param iOff Offset of the first byte in <code>in</code> to be processed.
   * @param iLen Number of bytes to process in <code>in</code>, starting at <code>iOff</code>.
   * @return A character array containing the Base64 encoded data.
   */
  public static char[] encode(byte[] in, int iOff, int iLen) {
    return Base64Encoder.encode(in, iOff, iLen);
  }

  /**
   * Decodes a string from Base64 format. No blanks or line breaks are allowed within the Base64
   * encoded input data.
   *
   * @param s A Base64 String to be decoded.
   * @return A String containing the decoded data.
   * @throws IllegalArgumentException If the input is not valid Base64 encoded data.
   */
  public static String decodeString(String s) {
    return new String(decode(s));
  }

  /**
   * Decodes a byte array from Base64 format and ignores line separators, tabs and blanks. CR, LF,
   * Tab and Space characters are ignored in the input data. This method is compatible with
   * <code>sun.misc.BASE64Decoder.decodeBuffer(String)</code>.
   *
   * @param s A Base64 String to be decoded.
   * @return An array containing the decoded data bytes.
   * @throws IllegalArgumentException If the input is not valid Base64 encoded data.
   */
  public static byte[] decodeLines(String s) {
    char[] buf = new char[s.length()];
    int validCharIndex = 0;
    for (int currentIndex = 0; currentIndex < s.length(); currentIndex++) {
      char c = s.charAt(currentIndex);
      if (c != ' ' && c != '\r' && c != '\n' && c != '\t') {
        buf[validCharIndex++] = c;
      }
    }
    return decode(buf, 0, validCharIndex);
  }

  /**
   * Decodes a byte array from Base64 format. No blanks or line breaks are allowed within the Base64
   * encoded input data.
   *
   * @param s A Base64 String to be decoded.
   * @return An array containing the decoded data bytes.
   * @throws IllegalArgumentException If the input is not valid Base64 encoded data.
   */
  public static byte[] decode(String s) {
    return decode(s.toCharArray());
  }

  /**
   * Decodes a byte array from Base64 format. No blanks or line breaks are allowed within the Base64
   * encoded input data.
   *
   * @param in A character array containing the Base64 encoded data.
   * @return An array containing the decoded data bytes.
   * @throws IllegalArgumentException If the input is not valid Base64 encoded data.
   */
  public static byte[] decode(char[] in) {
    return decode(in, 0, in.length);
  }

  /**
   * Decodes a byte array from Base64 format. No blanks or line breaks are allowed within the Base64
   * encoded input data.
   *
   * @param in A character array containing the Base64 encoded data.
   * @param iOff Offset of the first character in <code>in</code> to be processed.
   * @param iLen Number of characters to process in <code>in</code>, starting at <code>iOff</code>.
   * @return An array containing the decoded data bytes.
   * @throws IllegalArgumentException If the input is not valid Base64 encoded data.
   */
  public static byte[] decode(char[] in, int iOff, int iLen) {
    if (iLen % 4 != 0) {
      throw new IllegalArgumentException(
          "Length of Base64 encoded input string is not a multiple of 4.");
    }
    while (iLen > 0 && in[iOff + iLen - 1] == '=') {
      iLen--;
    }
    int oLen = (iLen * 3) / 4;
    byte[] out = new byte[oLen];
    int ip = iOff;
    int iEnd = iOff + iLen;
    int op = 0;
    while (ip < iEnd) {
      int i0 = in[ip++];
      int i1 = in[ip++];
      int i2 = ip < iEnd ? in[ip++] : 'A';
      int i3 = ip < iEnd ? in[ip++] : 'A';
      checkIllegalArgument(i0, i1, i2, i3);
      int b0 = map2[i0];
      int b1 = map2[i1];
      int b2 = map2[i2];
      int b3 = map2[i3];
      checkIllegalArgument(b0, b1, b2, b3);
      int o0 = (b0 << 2) | (b1 >>> 4);
      int o1 = ((b1 & 0xf) << 4) | (b2 >>> 2);
      int o2 = ((b2 & 3) << 6) | b3;
      out[op++] = (byte) o0;
      if (op < oLen) {
        out[op++] = (byte) o1;
      }
      if (op < oLen) {
        out[op++] = (byte) o2;
      }
    }
    return out;
  }

  private static void checkIllegalArgument(int... characters) {
    for (int character : characters) {
      if (isIllegalCharacter(character)) {
        throw new IllegalArgumentException("Illegal character in Base64 encoded data.");
      }
    }
  }

  private static boolean isIllegalCharacter(int asciiChar) {
    return asciiChar > 127 || asciiChar < 0;
  }

  // Dummy constructor.
  private Base64Coder() {}

} // end class Base64Coder
