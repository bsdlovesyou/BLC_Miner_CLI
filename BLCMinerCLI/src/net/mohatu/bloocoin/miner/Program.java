/*
 * Shitty code r us
 * Copyright (C) 2013 Mohatu.net
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>
 */

package net.mohatu.bloocoin.miner;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.Charset;
import java.util.Scanner;

public class Program
{
	public static boolean IS_MINING = false;
	public static long COUNTER = 0;
	public static String ADDRESS = "";
	public static String KEY = "";
	public static int SOLVED = 0;
	public static final String BLOO_URL = "server.bloocoin.org";
	public static final int BLOO_PORT = 3122;
	public static long START_TIME = System.nanoTime();
	public static int THREADS = 5;
	public static boolean SHOW_HELP = true;
	public static double KHS;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args)
	{
		ADDRESS = "6e727cc3a7j32a973uqd7iuq3h4p154l35hk5se9";
		KEY = "boo";
		System.out.println("https://github.com/bsdlovesyou/BLC_Miner_CLI");
		System.out.println("~~~~~bsd loves you~~~~~");
		System.out.println("Setting address to "+ADDRESS);
		System.out.println("Getting coin count.");
		setThreads((Runtime.getRuntime().availableProcessors() / 2) + 1);
		getCoins();
		mainLoop();
	}

	public static void mainLoop()
	{
		// main switch:case rae you rufcukgin mental?!?!?!!??!
		if (IS_MINING == false)//minin erryday
		{
			IS_MINING = true;
			START_TIME = System.nanoTime();
			Thread miner = new Thread(new BCMineThreadRunner());
			Thread khs = new Thread(new BCKhs());
			miner.start();
			khs.start();
			System.out.println("Mining started");
		}
		else
			System.out.println("Mining already!");
	}

	public static void displayHelp()
	{
		System.out.println("Available commands: help, mine, stop, setthreads x, exit, blc");
	}

	public static boolean getHelp()
	{
		return SHOW_HELP;
	}

	public static void setHelp(boolean b)
	{
		SHOW_HELP = false;
	}

	public static void updateCounter()
	{
		COUNTER++;
	}

	public static void updateKhs(double khss)
	{
		KHS = khss;
	}

	public static void setTime(int hour, int minute, int second)
	{
		String hourString, minuteString, secondString;
		hourString = Integer.toString(hour);
		minuteString = Integer.toString(minute);
		secondString = Integer.toString(second);

		if (hour < 10)
			hourString = "0" + hour;
		if (minute < 10)
			minuteString = "0" + minute;
		if (second < 10)
			secondString = "0" + second;
		
		System.out.println(hourString + ":" + minuteString + ":"
				+ secondString + "\tTried: " + COUNTER + "\tKh/s: "+KHS + "\tSolved:"+SOLVED);
	}

	public static void updateSolved(String solution)
	{
		System.out.println("Solved: " + ++SOLVED);
	}

	public static void updateBLC(int blc)
	{
		System.out.println("BLC: " + Integer.toString(blc));
	}

	private static void getCoins()
	{
		//Thread gc = new Thread(new BCWallet());
		//gc.start();
	}
	
	public static void setThreads(int thread)
	{
		THREADS = thread;
	}

	public static long getStartTime()
	{
		return START_TIME;
	}
}
