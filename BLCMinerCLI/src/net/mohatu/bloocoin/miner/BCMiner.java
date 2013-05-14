/*
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

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class BCMiner implements Runnable
{
	private String difficulty = "0000000";

	public BCMiner(int diff)
	{
		if(diff >= 7)
		{
			this.difficulty = "";
			for(int i=0; i < diff; i++)
			{
				this.difficulty += "0";
			}
		}
	}

	@Override
	public void run()
	{
		try
		{
			mine(difficulty);
		}
		catch (NoSuchAlgorithmException e)
		{
			System.out.println("No such algorithm: SHA-512");
		}
	}

	public void mine(String difficulty) throws NoSuchAlgorithmException
	{
		String currentString;
		while (Program.IS_MINING)
		{
			String startString = Utility.randomString(5);
			String hash = "";
			System.out.println("Starting: " + startString);
			for (int counter = 0; counter <= 100000000; counter++)
			{
				Program.updateCounter();
				currentString = startString + counter;
				hash = DigestUtils.sha512Hex(currentString);
				if(hash.startsWith(difficulty)){
					System.out.println("Success: " + currentString);
					Thread sub = new Thread(new BCSubmit(hash,
							currentString));
					sub.start();
					Program.updateSolved(currentString);
					try
					{
						PrintWriter out = new PrintWriter(new BufferedWriter(
								new FileWriter(System.getProperty("user.dir") + "/BLC_Solved.txt", true)));
						out.println(currentString);
						out.close();
					}
					catch (IOException e)
					{
						System.out.println("Could not save to BLC_Solved.txt, check permissions.");
					}
				}
				if (!Program.IS_MINING)
				{
					counter = 100000000;
					System.out.println("STOPPING");
				}
			}
		}
	}
	public void submit() {

	}

}
