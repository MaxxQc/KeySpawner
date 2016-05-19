package com.maxx.keyspawner.util;

import org.bukkit.Bukkit;

public class ConsoleMessage {
	private static final String prefix = "&a[KeySpawner] ";
	private static final String warning = "&c[Warning] ";
	private static final String severe = "&e[Severe] ";
	private static final String debug = "&b[Debug] ";
	private static final String info = "&5[Info] ";

	public static void sendWarning(String msg) {
		Bukkit.getConsoleSender().sendMessage(Util.colorize(prefix + warning + "&f" + msg));
	}

	public static void sendSevere(String msg) {
		Bukkit.getConsoleSender().sendMessage(Util.colorize(prefix + severe + "&f" + msg));
	}

	public static void sendDebug(String msg) {
		Bukkit.getConsoleSender().sendMessage(Util.colorize(prefix + debug + "&f" + msg));
	}

	public static void sendInfo(String msg) {
		Bukkit.getConsoleSender().sendMessage(Util.colorize(prefix + info + "&f" + msg));
	}
}