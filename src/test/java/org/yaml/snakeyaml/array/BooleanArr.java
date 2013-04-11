package org.yaml.snakeyaml.array;

import java.util.Arrays;

public class BooleanArr {
	private boolean[] bools;
	
	public BooleanArr () {}
	
	public BooleanArr ( boolean[] bools ) {
		this.bools = bools;
	}
	
	public String toString() {
		return Arrays.toString(bools);
	}
	
	public boolean[] getBools() {
		return bools;
	}

	public void setBools(boolean[] bools) {
		this.bools = bools;
	}
}
