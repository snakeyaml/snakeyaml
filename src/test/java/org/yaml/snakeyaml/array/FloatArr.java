package org.yaml.snakeyaml.array;

import java.util.Arrays;

public class FloatArr {
	private float[] floats;
	
	public FloatArr () {}
	
	public FloatArr ( float[] floats ) {
		this.floats = floats;
	}

	public String toString() {
		return Arrays.toString(floats);
	}
	
	public float[] getFloats() {
		return floats;
	}

	public void setFloats(float[] floats) {
		this.floats = floats;
	}
}
