package com.maxx.keyspawner.events;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Sign;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.maxx.keyspawner.main.KeySpawner;
import com.maxx.keyspawner.util.Util;

@SuppressWarnings("deprecation")
public class SignEvent implements Listener {
	private KeySpawner plugin;

	public SignEvent(KeySpawner plugin) {
		this.plugin = plugin;
	}

	@EventHandler
	public void onSignChange(SignChangeEvent e) {
		Player p = e.getPlayer();
		if (e.getLine(0).equalsIgnoreCase("[spawner achat]")) {
			if (p.hasPermission("keyspawner.sign.buy")) {
				if (e.getLine(1).length() == 0) {
					Util.sendMsg(p, "&cVous devez entrer le nom d'une entité sur la 2e ligne");
					return;
				}
				if (e.getLine(2).length() == 0) {
					Util.sendMsg(p, "&cVous devez entrer le nombre de spawner sur la 3e ligne");
					return;
				}
				if (e.getLine(3).length() == 0) {
					Util.sendMsg(p, "&cVous devez entrer le prix sur la 4e ligne");
					return;
				}
				try {
					EntityType.valueOf(e.getLine(1).toUpperCase());
				} catch (IllegalArgumentException ex) {
					Util.sendMsg(p, KeySpawner.instance.getLangManager().getInvalidEntity().replaceAll("%ENTITY%",
							e.getLine(1)));
					return;
				}
				e.setLine(0, Util.colorize("[Achat spawner]"));
				e.setLine(1, Util.colorize("&4" + e.getLine(1)));
				e.setLine(2, Util.colorize("&4" + e.getLine(2)));
				e.setLine(3, Util.colorize("&6Prix: &a" + e.getLine(3)));
			}
		}

		if (e.getLine(0).equalsIgnoreCase("[key achat]")) {
			if (p.hasPermission("keyspawner.sign.buy")) {
				if (e.getLine(1).length() == 0) {
					Util.sendMsg(p, "&cVous devez entrer le prix d'une clé à spawner sur la deuxième ligne");
					return;
				}
				e.setLine(0, Util.colorize("[Achat]"));
				e.setLine(3, Util.colorize("&6Prix: &a" + e.getLine(1)));
				e.setLine(1, Util.colorize("&4Clé spawner"));
			}
		}
	}

	@EventHandler
	public void SignClick(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase(Util.colorize("[Achat]"))
						&& sign.getLine(3).startsWith(Util.colorize("&6Prix: &a"))) {
					int price = Integer.valueOf(sign.getLine(3).replace(Util.colorize("&6Prix: &a"), ""));
					if (plugin.econ.getBalance(p.getName()) > price) {
						plugin.econ.withdrawPlayer(p.getName(), price);
						Util.sendMsg(p, KeySpawner.instance.getLangManager().getBoughtKeyspawner());
						p.getInventory().addItem(KeySpawner.instance.itemKey);
						p.updateInventory();
					} else {
						Util.sendMsg(p, "&cVous n'avez pas assez d'argent pour acheter ceci!");
						return;
					}
				}
			}
		}

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK) {
			if (e.getClickedBlock().getState() instanceof Sign) {
				Sign sign = (Sign) e.getClickedBlock().getState();
				if (sign.getLine(0).equalsIgnoreCase(Util.colorize("[Spawner | Achat]"))
						|| sign.getLine(0).equalsIgnoreCase(Util.colorize("[Achat spawner]"))) {
					int price = Integer.valueOf(sign.getLine(3).replace(Util.colorize("&6Prix: &a"), ""));
					EntityType entType = EntityType
							.valueOf(sign.getLine(1).replace(Util.colorize("&4"), "").toUpperCase());
					if (plugin.econ.getBalance(p.getName()) > price) {
						plugin.econ.withdrawPlayer(p.getName(), price);
						Util.sendMsg(p, KeySpawner.instance.getLangManager().getBoughtSpawner().replaceAll("%ENTITY%",
								entType.name()));
						p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10, -5000);
						ItemStack spawner = new ItemStack(Material.MOB_SPAWNER, 1);
						ItemMeta spawnerMeta = spawner.getItemMeta();
						spawnerMeta.setDisplayName(Util.colorize("&5Spawner de " + entType.name()));
						spawner.setItemMeta(spawnerMeta);
						p.getInventory().addItem(spawner);
						p.updateInventory();
						Util.logToFile("PANNEAU DE VENTE", p.getName(), spawnerMeta.getDisplayName());
					} else {
						Util.sendMsg(p, "&cVous n'avez pas assez d'argent pour acheter ceci!");
						return;
					}
				}
			}
		}
	}
}