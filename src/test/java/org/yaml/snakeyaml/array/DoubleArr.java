package org.yaml.snakeyaml.array;

import java.util.Arrays;

public class DoubleArr {
	private double[] doubles;
	
	public DoubleArr () {}
	
	public DoubleArr ( double[] doubles ) {
		this.doubles = doubles;
	}
	
	public String toString() {
		return Arrays.toString(doubles);
	}

	public double[] getDoubles() {
		return doubles;
	}

	public void setDoubles(double[] doubles) {
		this.doubles = doubles;
	}
}
