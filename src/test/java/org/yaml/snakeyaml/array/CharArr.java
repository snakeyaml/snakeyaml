package org.yaml.snakeyaml.array;

import java.util.Arrays;

public class CharArr {
	private char[] chars;
	
	public CharArr () {}
	
	public CharArr ( char[] chars ) {
		this.chars = chars;
	}
	
	public String toString() {
		return Arrays.toString(chars);
	}
	
	public char[] getChars() {
		return chars;
	}

	public void setChars(char[] chars) {
		this.chars = chars;
	}
}
