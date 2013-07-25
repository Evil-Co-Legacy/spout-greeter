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
package com.evilco.spout.greeter.ui;

import org.getspout.spoutapi.event.screen.ButtonClickEvent;
import org.getspout.spoutapi.gui.GenericButton;

import com.evilco.spout.greeter.GreeterPlugin;

/**
 * @author			Johannes Donath <johannesd@evil-co.com>
 * @copyright		(C) 2013 Evil-Co <http://www.evil-co.com>
 * @license			GNU Lesser General Public License <http://www.gnu.org/licenses/lgpl.txt>
 * @package			com.evilco.spout.greeter
 */
public class CloseButton extends GenericButton {

	/**
	 * Stores the parent plugin instance.
	 */
	protected GreeterPlugin plugin;
	
	/**
	 * Constructs the close button.
	 */
	public CloseButton(GreeterPlugin plugin) {
		super("Close");
		
		this.plugin = plugin;
	}
	
	/*
	 * (non-Javadoc)
	 * @see org.getspout.spoutapi.gui.GenericButton#onButtonClick(org.getspout.spoutapi.event.screen.ButtonClickEvent)
	 */
	public void onButtonClick(ButtonClickEvent event) {
		// send close window command
		event.getPlayer().getMainScreen().getActivePopup().close();
		
		// store in database
		this.plugin.getPlayerList().add(event.getPlayer().getName());
		this.plugin.saveDatabase();
	}
}
