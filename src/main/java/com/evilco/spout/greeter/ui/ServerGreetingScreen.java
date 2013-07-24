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
package com.evilco.spout.greeter.ui;

import org.getspout.spoutapi.gui.GenericLabel;
import org.getspout.spoutapi.gui.GenericPopup;
import org.getspout.spoutapi.gui.GenericTexture;
import org.getspout.spoutapi.gui.RenderPriority;
import org.getspout.spoutapi.player.SpoutPlayer;

import com.evilco.spout.greeter.GreeterPlugin;

/**
 * Basic Greeter Plugin
 * @author			Johannes Donath <johannesd@evil-co.com>
 * @copyright		(C) 2013 Evil-Co <http://www.evil-co.com>
 * @license			GNU Lesser General Public License <http://www.gnu.org/licenses/lgpl.txt>
 * @package			com.evilco.spout.greeter
 */
public class ServerGreetingScreen extends GenericPopup {

	/**
	 * Stores the parent player.
	 */
	protected SpoutPlayer player;
	
	/**
	 * Stores the greeter plugin instance.
	 */
	protected GreeterPlugin plugin;
	
	/**
	 * @param greeterPlugin
	 * @param player
	 */
	public ServerGreetingScreen(GreeterPlugin greeterPlugin, SpoutPlayer player) {
		// store variables
		this.plugin = greeterPlugin;
		this.player = player;
		
		// build UI
		this.buildUI();
	}
	
	/**
	 * Builds the UI.
	 */
	protected void buildUI() {
		int screenWidth = player.getMainScreen().getWidth();
		int screenHeight = player.getMainScreen().getHeight();
		int x = (screenWidth / 2 - 170);
		int y = (screenHeight / 2 - 100);
		
		// set screen settings
	    this.setTransparent(true);
		
		// create GUI
	    GenericTexture background = new GenericTexture();
		
		GenericLabel text = new GenericLabel(this.plugin.getGreeting());
		CloseButton button = new CloseButton(this.plugin);
		
		// set properties
		background.setUrl("greeter-background.png");
		background.setX(x).setY(y);
		background.setFixed(false);
		background.setWidth(340).setHeight(200);
		background.setPriority(RenderPriority.Highest);
		
		text.setX(x + 10).setY(y + 20);
		text.setWidth(320).setHeight(136);
		text.setDirty(true);
		text.setPriority(RenderPriority.High);
		
		button.setX(x + 10).setY(y + 185);
		button.setWidth(25).setHeight(10);
		button.setPriority(RenderPriority.High);
		
		// attach
		this.attachWidget(this.plugin, background);
		this.attachWidget(this.plugin, text);
		this.attachWidget(this.plugin, button);
	}
}
