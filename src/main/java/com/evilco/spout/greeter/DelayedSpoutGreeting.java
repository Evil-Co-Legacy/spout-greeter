/**
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

import java.util.logging.Level;

import org.getspout.spoutapi.player.SpoutPlayer;

import com.evilco.spout.greeter.ui.ServerGreetingScreen;

/**
 * @author			Johannes Donath <johannesd@evil-co.com>
 * @copyright		(C) 2013 Evil-Co <http://www.evil-co.com>
 * @license			GNU Lesser General Public License <http://www.gnu.org/licenses/lgpl.txt>
 * @package			com.evilco.spout.greeter
 */
public class DelayedSpoutGreeting implements Runnable {

	/**
	 * Stores the player.
	 */
	SpoutPlayer player;
	
	/**
	 * Stores the parent plugin.
	 */
	GreeterPlugin plugin;
	
	/**
	 * @param greeterPlugin
	 * @param player
	 */
	public DelayedSpoutGreeting(GreeterPlugin greeterPlugin, SpoutPlayer player) {
		this.plugin = greeterPlugin;
		this.player = player;
	}

	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// check for offline players (this may happen so we check it)
		if (!this.player.isOnline()) return;
		
		// hang up thread until caching finished
		if (!this.player.isPreCachingComplete()) {
			this.plugin.getServer().getScheduler().scheduleSyncDelayedTask(this.plugin, this, 10);
			return;
		}
		
		// show screen
		try {
			this.player.getMainScreen().attachPopupScreen(new ServerGreetingScreen(this.plugin, this.player));
		} catch (Exception e) {
			this.plugin.getLogger().log(Level.WARNING, "A problem occured while showing the greet screen to player " + this.player.getName() + ".", e);
		}
	}

}
