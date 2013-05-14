package net.mohatu.bloocoin.miner;

import java.util.Random;

public class Utility
{
	private static Random randomGen = new Random();

	public static String randomString(int length)
	{
		String chars = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789";
		int limit = length;
		StringBuffer buf = new StringBuffer();

		for (int i = 0; i < limit; i++) {
			buf.append(chars.charAt(randomGen.nextInt(chars.length())));
		}
		return buf.toString();
	}
}
