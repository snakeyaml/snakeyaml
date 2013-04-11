package org.yaml.snakeyaml.array;

import java.util.Arrays;

public class ShortArr {
	private short[] shorts;
	
	public ShortArr(){}
	
	public ShortArr ( short[] shorts ) {
		this.shorts = shorts;
	}
	
	public String toString() {
		return Arrays.toString(shorts);
	}

	public short[] getShorts() {
		return shorts;
	}

	public void setShorts(short[] shorts) {
		this.shorts = shorts;
	}
}
