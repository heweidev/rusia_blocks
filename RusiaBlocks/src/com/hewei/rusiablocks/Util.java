package com.hewei.rusiablocks;

import java.util.Random;

public class Util {
	public static int roate(final float[] values) {
		float[] newValues = values.clone();
		float total = 0;
		
		for (int i = 0; i < values.length; i++) {
			total += values[i];
			newValues[i] = total;
		}
		
		Random rand = new Random();
		float val = rand.nextFloat() * total;
		
		for (int i = 0; i < values.length; i++) {
			if (val < newValues[i]) {
				return i;
			}
		}
		
		return 0;
	}
}
