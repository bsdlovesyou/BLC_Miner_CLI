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

/* 
 * SubmitterClass.java
 * Submits solution to the server
 */

package net.mohatu.bloocoin.miner;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Random;

import org.apache.commons.codec.digest.DigestUtils;

public class BCRegister implements Runnable
{
	boolean submitted = false;

	@Override
	public void run()
	{
		generateData();
	}
	
	private void generateData()
	{
		Random r = new Random();
		String addr = DigestUtils.sha1Hex((Utility.randomString(5)+r.nextInt(Integer.MAX_VALUE)).toString()).toString();
		String key = DigestUtils.sha1Hex((Utility.randomString(5)+r.nextInt(Integer.MAX_VALUE)).toString()).toString();
		System.out.println("Addr: " + addr + "\nKey: "+key);
		register(addr, key);
	}

	private void register(String addr, String key)
	{
		try
		{
			String result = new String();
			Socket sock = new Socket(Program.BLOO_URL, Program.BLOO_PORT);
			String command = "{\"cmd\":\"register" + "\",\"addr\":\"" + addr
					+ "\",\"pwd\":\"" + key + "\"}";
			DataInputStream is = new DataInputStream(sock.getInputStream());
			DataOutputStream os = new DataOutputStream(sock.getOutputStream());
			os.write(command.getBytes());
			os.flush();

			BufferedReader in = new BufferedReader(new InputStreamReader(is));
			String inputLine;
			while ((inputLine = in.readLine()) != null)
			{
				result += inputLine;
			}

			is.close();
			os.close();
			sock.close();
			System.out.println(result);
			if (result.contains("\"success\": true"))
			{
				System.out.println("Registration successful: "+addr);
				saveBloostamp(addr, key);
			}
			else if (result.contains("\"success\": false"))
			{
				System.out.println("Result: Failed");
				System.exit(0);
			}
		}
		catch (UnknownHostException e)
		{
			System.out.println("Error: Unknown host.");
		}
		catch (IOException e)
		{
			System.out.println("Error: Network error.");
		}
	}
	
	private void saveBloostamp(String address, String key)
	{
		try
		{
			PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter("bloostamp")));
			out.print(address+":"+key);
			out.close();
		}
		catch(IOException e)
		{
			System.out.println("Saving of bloostamp file failed, check permissions.");
		}
	}
}
