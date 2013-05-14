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

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Socket;

public class BCWallet implements Runnable
{
	private String address;
	private String key;
	
	public BCWallet(String address, String key)
	{
		this.address = address;
		this.key = key;
	}

	@Override
	public void run()
	{
		getCoins(address, key);
	}

	public void getCoins(String address, String key)
	{
		try
		{
			String result = new String();
			Socket sock = new Socket(Program.BLOO_URL, Program.BLOO_PORT);
			String command = "{\"cmd\":\"my_coins\",\"addr\":\""
					+ Program.ADDRESS + "\",\"pwd\":\"" + key
					+ "\"}";
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
			String coins = result.split("t\": ")[1];
			coins = coins.split("}")[0];
			Program.updateBLC(Integer.parseInt(coins));
		}
		catch (MalformedURLException murle)
		{
			System.out.println("The URL seems to be malformed.");
		}
		catch (IOException e)
		{
			System.out.println("There seems to be a problem connecting. Server down?");
		}
		getTotal();
	}
	
	private void getTotal()
	{
		try
		{
			String result = new String();
			Socket sock = new Socket(Program.BLOO_URL, Program.BLOO_PORT);
			String command = "{\"cmd\":\"total_coins\"}";
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
			String[] amount = result.split("\"amount\": ");
			amount=amount[1].split("}");
			System.out.println("Total BLC: "+amount[0]);
			if(Program.getHelp()==true)
			{
				System.out.println("All data loaded, type \"help\" for help");
				Program.setHelp(false);
			}
		}
		catch (MalformedURLException murle)
		{
			System.out.println("The URL seems to be malformed.");
		}
		catch (IOException e)
		{
			System.out.println("There seems to be a problem connecting. Server down?");
		}
	}
}