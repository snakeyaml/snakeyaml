package org.yaml.snakeyaml.array;

import java.util.Arrays;

public class ByteArr {

	private byte[] bytes;
	
	public ByteArr(){}
	
	public ByteArr ( byte[] bytes ) {
		this.bytes = bytes;
	}
	
	public String toString() {
		return Arrays.toString(bytes);
	}

	public byte[] getBytes() {
		return bytes;
	}

	public void setBytes(byte[] bytes) {
		this.bytes = bytes;
	}
}
