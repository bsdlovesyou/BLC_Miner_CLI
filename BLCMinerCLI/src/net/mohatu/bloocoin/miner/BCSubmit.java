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
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.net.UnknownHostException;

public class BCSubmit implements Runnable
{

	String hash = "";
	String solution = "";
	String addr = "";
	String key = "";
	boolean submitted = false;

	public BCSubmit(String hash, String solution)
	{
		this.hash = hash;
		this.solution = solution;
	}

	@Override
	public void run()
	{
		while (!submitted)
		{
			boolean sawException = false;
			try
			{
				System.out.println("Submitting " + solution);
				submit(hash, solution);
				Thread.sleep(5000);
			}
			catch (InterruptedException e)
			{
				sawException = true;
			}
			if (sawException)
			{
				Thread.currentThread().interrupt();
			}
		}
	}

	private void submit(String hash, String solution)
	{
		try
		{
			String result = new String();
			Socket sock = new Socket(Program.BLOO_URL, Program.BLOO_PORT);
			String command = "{\"cmd\":\"check" + "\",\"winning_string\":\""
					+ solution + "\",\"winning_hash\":\"" + hash
					+ "\",\"addr\":\"" + Program.ADDRESS + "\"}";
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
			if (result.contains("\"success\": true"))
			{
				submitted = true;
				System.out.println(solution + " submitted");
			}
			else if (result.contains("\"success\": false"))
			{
				submitted = true;
				System.out.println("Submission of " + solution + " failed, already exists!");
			}
		}
		catch (UnknownHostException e)
		{
			System.out.println("Submission of " + solution + " failed, unknown host!");
		}
		catch (IOException e)
		{
			System.out.println("Submission of " + solution + " failed, connection failed!");
		}
		//Thread gc = new Thread(new BCWallet());
		//gc.start();
	}
}
