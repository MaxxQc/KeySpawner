package com.maxx.keyspawner.util;

import java.io.File;
import java.io.IOException;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.maxx.keyspawner.main.KeySpawner;

public class Util {
	public static void sendMsg(CommandSender sender, String message) {
		if (sender == null) {
			return;
		}
		Player p = null;
		if (sender instanceof Player) {
			p = (Player) sender;
		}
		if (p == null) {
			ConsoleMessage.sendInfo(message);
		} else {
			p.sendMessage(colorize(message));
		}
	}

	public static String colorize(String string) {
		if (string == null) {
			return null;
		}
		return string.replaceAll("&([0-9a-z])", "§$1");
	}

	public static void logToFile(String admin, String player, String type) {
		File f = new File(KeySpawner.instance.getDataFolder() + File.separator + "spawner-logs.txt");
		try {
			f.createNewFile();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
		Logger logger = Logger.getLogger("MyLog");
		FileHandler fh;

		try {
			fh = new FileHandler(f.getAbsolutePath());
			logger.addHandler(fh);
			SimpleFormatter formatter = new SimpleFormatter();
			fh.setFormatter(formatter);
			logger.info(admin + " a donné un " + type + " à " + player);
			fh.close();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
