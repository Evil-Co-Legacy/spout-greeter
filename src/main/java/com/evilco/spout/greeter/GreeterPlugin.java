/*
* This file is part of SpoutGreeter.
*
* Copyright (C) 2013 Evil-Co <http://www.evil-co.com>
* SpoutGreeter is licensed under the GNU Lesser General Public License.
*
* SpoutGreeter is free software: you can redistribute it and/or modify
* it under the terms of the GNU Lesser General Public License as published by
* the Free Software Foundation, either version 3 of the License, or
* (at your option) any later version.
*
* SpoutPlugin is distributed in the hope that it will be useful,
* but WITHOUT ANY WARRANTY; without even the implied warranty of
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
* GNU Lesser General Public License for more details.
*
* You should have received a copy of the GNU Lesser General Public License
* along with this program. If not, see <http://www.gnu.org/licenses/>.
*/
package com.evilco.spout.greeter;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.logging.Level;

import org.apache.commons.io.IOUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.plugin.java.JavaPlugin;
import org.getspout.spoutapi.SpoutManager;
import org.getspout.spoutapi.event.spout.SpoutCraftEnableEvent;

/**
 * Basic Greeter Plugin
 * @author			Johannes Donath <johannesd@evil-co.com>
 * @copyright		(C) 2013 Evil-Co <http://www.evil-co.com>
 * @license			GNU Lesser General Public License <http://www.gnu.org/licenses/lgpl.txt>
 * @package			com.evilco.spout.greeter
 */
public class GreeterPlugin extends JavaPlugin implements Listener {

	/**
	 * Defines whether the plugin version has been extracted.
	 */
	public static final boolean VERSION_FOUND;
	
	/**
	 * Defines the version string.
	 */
	public static final String VERSION_STRING;
	
	/**
	 * Stores the greeting.
	 */
	protected String greeting = "ERROR ERROR ERROR ERROR";
	
	/**
	 * Stores the player list.
	 */
	protected List<String> playerList = new Vector<String>();
	
	/**
	 * Static init.
	 */
	static {
		boolean versionFound = false;
		String versionString = "0.0.0";
		
		Package p = GreeterPlugin.class.getPackage();
		if (p.getImplementationVersion() != null) {
			versionString = p.getImplementationVersion();
			versionFound = true;
		}
		
		VERSION_FOUND = versionFound;
		VERSION_STRING = versionString;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onEnable()
	 */
	public void onEnable() {
		this.getLogger().info("Enabling Spout Greeter plugin " + (VERSION_FOUND ? "v" + VERSION_STRING : "(unknown version)") + " ...");
		
		// create data dir
		this.getDataFolder().mkdirs();
		
		// extract default greeting
		if (!this.getGreetingFile().exists()) this.extractDefaultFile(this.getGreetingFile(), "/defaults/greeting.txt");
		
		// read database
		this.readDatabase();
		
		// read greeting
		try {
			this.greeting = new Scanner(this.getGreetingFile()).useDelimiter("\\Z").next();
		} catch (FileNotFoundException ex) {
			this.getLogger().log(Level.SEVERE, "Cannot read greeting template!", ex);
			this.getServer().getPluginManager().disablePlugin(this);
			return;
		}
		
		// register events
		this.getServer().getPluginManager().registerEvents(this, this);
		
		// register pre-cache files
		SpoutManager.getFileManager().addToPreLoginCache(this, GreeterPlugin.class.getResourceAsStream("/gui/background.png"), "greeter-background.png");
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.bukkit.plugin.java.JavaPlugin#onDisable()
	 */
	public void onDisable() {
		this.getLogger().info("Disabling Spout Greeter plugin " + (VERSION_FOUND ? "v" + VERSION_STRING : "(unknown version)") + " ...");
		
		// store database
		this.saveDatabase();
	}
	
	/**
	 * Extracts a default file.
	 * @param target
	 * @param input
	 */
	public void extractDefaultFile(File target, String input) {
		this.getLogger().info("Extracting " + input + " from plugin container ...");
		
		// create streams
		InputStream in = null;
		OutputStream out = null;
		
		try {
			// get stream
			in = GreeterPlugin.class.getResourceAsStream(input);
			
			// create output file
			target.createNewFile();
			
			// get output stream
			out = new FileOutputStream(target);
			
			// copy
			IOUtils.copy(in, out);
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Cannot extract default resource \"" + input + "\" from plugin jar!", ex);
		} finally {
			try {
				in.close();
			} catch (Exception ex) { } // ignore
			
			try {
				out.close();
			} catch (Exception ex) { } // ignore
		}
	}
	
	/**
	 * Returns the player database file.
	 * @return
	 */
	public File getDatabaseFile() {
		return (new File(this.getDataFolder(), "players.dat"));
	}
	
	/**
	 * Returns the current greeting.
	 * @return
	 */
	public String getGreeting() {
		return this.greeting;
	}
	
	/**
	 * Returns the greeting file object.
	 * @return
	 */
	public File getGreetingFile() {
		return (new File(this.getDataFolder(), "greeting.txt"));
	}
	
	/**
	 * Returns the player database.
	 */
	public List<String> getPlayerList() {
		return this.playerList;
	}
	
	/**
	 * Reads the database.
	 */
	public void readDatabase() {
		FileInputStream file = null;
		BufferedReader in = null;
		String line;
		
		try {
			file = new FileInputStream(new File(this.getDataFolder(), "players.dat"));
			in = new BufferedReader(new InputStreamReader(file, Charset.forName("UTF-8")));
			
			while((line = in.readLine()) != null) {
				// ignore comment lines
				if (line.startsWith("#")) continue;
				
				// add to playerlist
				this.playerList.add(line);
			}
		} catch (IOException ex) {
			this.getLogger().warning("Corrupt or non-existant database detected. Running on empty user database!");
		} finally {
			try {
				in.close();
			} catch (Exception ex) { } // ignore
			
			try {
				file.close();
			} catch (Exception ex) { } // ignore
		}
	}
	
	/**
	 * Saves the database.
	 */
	public void saveDatabase() {
		FileOutputStream file = null;
		BufferedWriter out = null;
		
		try {
			file = new FileOutputStream(new File(this.getDataFolder(), "players.dat"));
			out = new BufferedWriter(new OutputStreamWriter(file, Charset.forName("UTF-8")));
			
			// add header
			out.write(
					"####################################################\n" +
					"#					PLAYER DATABASE					#\n" +
					"####################################################\n" +
					"# Do not edit this file manually! It will get		#\n" +
					"# overwritten on the next plugin cycle!			#\n" +
					"#													#\n" +
					"# You may delete this file if you want to show		#\n" +
					"# the greeting to all users again though.			#\n" +
					"####################################################\n"
			);
			
			// write players
			for(String player : this.playerList) {
				out.write(player + "\n");
			}
			
			// flush streams
			out.flush();
			file.flush();
		} catch (IOException ex) {
			this.getLogger().log(Level.SEVERE, "Cannot write player database!", ex);
		} finally {
			try {
				file.close();
			} catch (Exception ex) { } // ignore
			
			try {
				out.close();
			} catch (Exception ex) { } // ignore
		}
	}
	
	/**
	 * Handles player joins.
	 * @param event
	 */
	@EventHandler(priority = EventPriority.LOWEST)
	public void onSpoutCraftEnable(SpoutCraftEnableEvent event) {
		// check database
		if (this.getPlayerList().contains(event.getPlayer().getName())) return;
		
		// show screen
		this.getServer().getScheduler().scheduleSyncDelayedTask(this, new DelayedSpoutGreeting(this, event.getPlayer()));
		// event.getPlayer().getMainScreen().attachPopupScreen(new ServerGreetingScreen(this, event.getPlayer()));
	}
}
