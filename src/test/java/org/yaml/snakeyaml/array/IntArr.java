package org.yaml.snakeyaml.array;

import java.util.Arrays;

public class IntArr {
	private int[] ints;
	
	public IntArr () {}
	
	public IntArr ( int[] ints ) {
		this.ints = ints;
	}

	public String toString() {
		return Arrays.toString(ints);
	}
	
	public int[] getInts() {
		return ints;
	}

	public void setInts(int[] ints) {
		this.ints = ints;
	}
}
