package com.bankwel.j3d.raytracing.plugins;

import com.bankwel.j3d.raytracing.model.Constant;

/**
 * 
 * @author yuyuzhao
 * @since 2016-5-24
 *
 */
public class MathUtils {

	public static float max(float... args) {
		float max = args[0];
		for (int i = 1; i < args.length; i++)
			max = Math.max(max, args[i]);
		return max;
	}

	public static float min(float... args) {
		float min = args[0];
		for (int i = 1; i < args.length; i++)
			min = Math.min(min, args[i]);
		return min;
	}

	public static float abs(float f) {
		return Math.abs(f);
	}

	public static float sqrt(float f) {
		return (float) Math.sqrt(f);
	}

	public static float pow(float f, float r) {
		return (float) Math.pow(f, r);
	}

	public static boolean equalsZero(float f) {
		return f > -Constant.ZERO_APPROX && f < Constant.ZERO_APPROX;
	}

	public static boolean greaterThanZero(float f) {
		return f >= Constant.ZERO_APPROX;
	}

	public static boolean lessThanZero(float f) {
		return f <= -Constant.ZERO_APPROX;
	}

}
