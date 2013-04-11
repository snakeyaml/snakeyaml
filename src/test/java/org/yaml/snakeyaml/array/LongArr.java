package org.yaml.snakeyaml.array;

import java.util.Arrays;

public class LongArr {
	private long[] longs;

	public LongArr () {}
	
	public LongArr ( long[] longs ) {
		this.longs = longs;
	}
	
	public String toString() {
		return Arrays.toString(longs);
	}

	public long[] getLongs() {
		return longs;
	}

	public void setLongs(long[] longs) {
		this.longs = longs;
	}
}
