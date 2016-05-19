package com.maxx.keyspawner.main;

import java.util.ArrayList;
import java.util.List;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import com.massivecraft.factions.Factions;
import com.maxx.keyspawner.events.InteractEventWithFaction;
import com.maxx.keyspawner.events.InteractEventWithoutFaction;
import com.maxx.keyspawner.events.PlaceEvent;
import com.maxx.keyspawner.events.SignEvent;
import com.maxx.keyspawner.events.SpawnEvent;
import com.maxx.keyspawner.util.ConsoleMessage;
import com.maxx.keyspawner.util.LangManager;
import com.maxx.keyspawner.util.SimpleConfig;
import com.maxx.keyspawner.util.SimpleConfigManager;
import com.maxx.keyspawner.util.Util;

import net.milkbowl.vault.economy.Economy;

public class KeySpawner extends JavaPlugin implements Listener {
	public static KeySpawner instance;
	private SimpleConfigManager manager;
	private SimpleConfig config, langConfig;
	public ItemStack itemKey;
	public Material itemMat;
	public String itemName;
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> blacklistedWorlds = new ArrayList();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> blacklistedEntities = new ArrayList();
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public List<String> blacklistedEntitiesSpawner = new ArrayList();
	private LangManager langManager;
	public Economy econ = null;
	private boolean eggsEnabled = false;

	public void onEnable() {
		instance = this;
		getServer().getPluginManager().registerEvents(new PlaceEvent(), this);
		this.manager = new SimpleConfigManager(this);
		this.config = manager.getNewConfig("config.yml");
		this.langConfig = manager.getNewConfig("lang.yml");
		if (this.config.contains("blacklist.worlds")) {
			for (Object obj : this.config.getList("blacklist.worlds"))
				this.blacklistedWorlds.add(obj.toString());
		} else {
			this.blacklistedWorlds.add("example");
		}
		if (this.config.contains("blacklist.entities")) {
			for (Object obj : this.config.getList("blacklist.entities"))
				this.blacklistedEntities.add(obj.toString());
		} else {
			this.blacklistedEntities.add("GHAST");
		}
		if (this.config.contains("blacklist.spawners")) {
			for (Object obj : this.config.getList("blacklist.spawners"))
				this.blacklistedEntitiesSpawner.add(obj.toString());
		} else {
			this.blacklistedEntitiesSpawner.add("GHAST");
		}
		setupConfig();
		setupLangConfig();
		this.itemMat = Material.valueOf(this.config.getString("item.material"));
		this.itemName = Util.colorize(this.config.getString("item.name"));
		this.itemKey = new ItemStack(itemMat, 1);
		ItemMeta itemKeyMeta = itemKey.getItemMeta();
		itemKeyMeta.setDisplayName(itemName);
		this.itemKey.setItemMeta(itemKeyMeta);

		Factions f2 = (com.massivecraft.factions.Factions) Bukkit.getPluginManager().getPlugin("Factions");
		if (f2 != null) {
			getServer().getPluginManager().registerEvents(new InteractEventWithFaction(), this);
		} else {
			getServer().getPluginManager().registerEvents(new InteractEventWithoutFaction(), this);
		}

		if (getConfig().getBoolean("options.disablewithernether"))
			getServer().getPluginManager().registerEvents(new SpawnEvent(), this);
		this.langManager = new LangManager();
		this.langManager.setup(this);
		if (!setupEconomy()) {
			ConsoleMessage.sendInfo(
					"Vous n'allez pas pouvoir utiliser des panneaux d'achat, aucun plugin d'economie n'a été trouvé");
		} else {
			getServer().getPluginManager().registerEvents(new SignEvent(this), this);
		}
		if (this.config.contains("options.enablemonstereggs"))
			this.eggsEnabled = this.config.getBoolean("options.enablemonstereggs");
	}

	private boolean setupEconomy() {
		if (getServer().getPluginManager().getPlugin("Vault") == null) {
			return false;
		}
		RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
		if (rsp == null) {
			return false;
		}
		econ = rsp.getProvider();
		return econ != null;
	}

	private void setupConfig() {
		if (!this.config.contains("item.material"))
			this.config.set("item.material", "TRIPWIRE_HOOK");
		if (!this.config.contains("item.name"))
			this.config.set("item.name", "&6Clé à spawner");
		if (!this.config.contains("blacklist.worlds"))
			this.config.set("blacklist.worlds", this.blacklistedWorlds);
		if (!this.config.contains("blacklist.entities"))
			this.config.set("blacklist.entities", this.blacklistedEntities);
		if (!this.config.contains("blacklist.spawners"))
			this.config.set("blacklist.spawners", this.blacklistedEntitiesSpawner);
		if (!this.config.contains("options.enablemonstereggs"))
			this.config.set("options.enablemonstereggs", true);
		if (!this.config.contains("options.disablewithernether"))
			this.config.set("options.disablewithernether", false);
		if (!this.config.contains("options.disableskeletonspawnernether"))
			this.config.set("options.disableskeletonspawnernether", false);
		this.config.saveConfig();
	}

	private void setupLangConfig() {
		if (!this.langConfig.contains("msg.usagekeyspawner"))
			this.langConfig.set("msg.usage", "&fUsage: &a/keyspawner <joueur>");
		if (!this.langConfig.contains("msg.keysent"))
			this.langConfig.set("msg.keysent", "&aLe joueur &b%PLAYER%&a a bien reçu sa clé!");
		if (!this.langConfig.contains("msg.receivekey"))
			this.langConfig.set("msg.receivekey",
					"&aVous avez reçu une clé à spawner!\nUtilisez-la pour récupérer un spawner.");
		if (!this.langConfig.contains("msg.usernotonline"))
			this.langConfig.set("msg.usernotonline", "&cLe joueur &b%PLAYER%&c n'est pas en ligne!");
		if (!this.langConfig.contains("msg.noperm"))
			this.langConfig.set("msg.noperm", "&cVous n'avez pas le droit de faire ceci.");
		if (!this.langConfig.contains("msg.list-entities"))
			this.langConfig.set("msg.list-entities", "&fListe de toutes les entités: &b");
		if (!this.langConfig.contains("msg.usagespawner"))
			this.langConfig.set("msg.usagespawner", "&fUsage: &a/spawner <entité> [joueur]");
		if (!this.langConfig.contains("msg.notplayer"))
			this.langConfig.set("msg.notplayer",
					"&cVous devez être un joueur pour faire ceci!\n&cUsage pour donner un spawner à un joueur: /spawner <entité> <joueur>");
		if (!this.langConfig.contains("msg.chooseanother"))
			this.langConfig.set("msg.chooseanother",
					"&cCette entité est interdite!\nVeuillez en sélectionner une autre.");
		if (!this.langConfig.contains("msg.invalidentity"))
			this.langConfig.set("msg.invalidentity", "&cL'entité &b%ENTITY%&c nest pas valide!");
		if (!this.langConfig.contains("msg.gotspawner"))
			this.langConfig.set("msg.gotspawner", "&aVous avez reçu un spawner de &b%ENTITY%&a!");
		if (!this.langConfig.contains("msg.blacklistedworld"))
			this.langConfig.set("msg.blacklistedworld", "&cVous ne pouvez pas faire ceci dans ce monde!");
		if (!this.langConfig.contains("msg.blacklistedentity"))
			this.langConfig.set("msg.blacklistedentity", "&cVous ne pouvez pas faire ceci avec ce type de spawner!");
		if (!this.langConfig.contains("msg.lostyourkey"))
			this.langConfig.set("msg.lostyourkey", "&aVous avez récupéré un spawner et votre clé s'est volatisée!");
		if (!this.langConfig.contains("msg.spawnername"))
			this.langConfig.set("msg.spawnername", "&5Spawner de %ENTITY%");
		if (!this.langConfig.contains("msg.boughtspawner"))
			this.langConfig.set("msg.boughtspawner", "&aVous avez acheté un spawner de &b%ENTITY%&a!");
		if (!this.langConfig.contains("msg.boughtkeyspawner"))
			this.langConfig.set("msg.boughtkeyspawner", "&aVous avez acheté une &bclé à spawner&a!");
		this.langConfig.saveConfig();
	}

	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (cmd.getName().equalsIgnoreCase("keyspawner")) {
			if (sender.hasPermission("keyspawner.give")) {
				if (args.length < 1) {
					Util.sendMsg(sender, getLangManager().getUsageKeySpawner());
					return true;
				}

				Player target = Bukkit.getPlayer(args[0]);
				if (target != null) {
					if (target != sender)
						Util.sendMsg(sender, getLangManager().getKeySent().replace("%PLAYER%", target.getName()));
					Util.sendMsg(target, getLangManager().getKeyReceived());

					if (target.getInventory().contains(itemKey)) {
						target.getInventory().addItem(itemKey);
						target.updateInventory();
						return true;
					}

					if (target.getInventory().firstEmpty() == -1) {
						target.getWorld().dropItem(target.getLocation().add(0, 1, 0), itemKey);
					} else {
						int invSlot = target.getInventory().firstEmpty();
						target.getInventory().setItem(invSlot, itemKey);
						target.updateInventory();
					}
					return true;
				} else {
					Util.sendMsg(sender, getLangManager().getUserNotOnline().replace("%PLAYER%", args[0]));
					return true;
				}
			} else {
				Util.sendMsg(sender, getLangManager().getNoPermission());
				return true;
			}
		}
		if (cmd.getName().equalsIgnoreCase("spawner")) {
			if (sender.hasPermission("keyspawner.spawner")) {
				if (args.length == 0) {
					String list = "";
					for (EntityType ent : EntityType.values()) {
						if (list.equalsIgnoreCase("")) {
							list = "&b" + ent.name();
						}
						list = list + "&f, &b" + ent.name();
						// TODO color from config
					}
					Util.sendMsg(sender, getLangManager().getListEntities() + list);
					Util.sendMsg(sender, getLangManager().getUsageSpawner());
					return true;
				}

				if (args.length == 1) {
					if (!(sender instanceof Player)) {
						Util.sendMsg(sender, getLangManager().getNotPlayer());
						return true;
					}
					EntityType entType = EntityType.valueOf(args[0].toUpperCase());
					if (this.blacklistedEntitiesSpawner.size() > 0) {
						for (String ent : this.blacklistedEntitiesSpawner) {
							if (entType.name().equalsIgnoreCase(ent)) {
								Util.sendMsg(sender, getLangManager().getChooseAnother());
								return true;
							}
						}
					}
					if (entType == null) {
						Util.sendMsg(sender, getLangManager().getInvalidEntity().replaceAll("%ENTITY%", args[0]));
						return true;
					}
					Player p = (Player) sender;
					Util.sendMsg(p, getLangManager().getGotSpawner().replaceAll("%ENTITY%", entType.name()));
					p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10, -5000);
					ItemStack spawner = new ItemStack(Material.MOB_SPAWNER, 1);
					ItemMeta spawnerMeta = spawner.getItemMeta();
					spawnerMeta
							.setDisplayName(getLangManager().getSpawnerName().replaceAll("%ENTITY%", entType.name()));
					spawner.setItemMeta(spawnerMeta);

					if (p.getInventory().contains(spawner)) {
						p.getInventory().addItem(spawner);
						p.updateInventory();
						return true;
					}

					if (p.getInventory().firstEmpty() == -1) {
						p.getWorld().dropItem(p.getLocation().add(0, 1, 0), spawner);
					} else {
						int invSlot = p.getInventory().firstEmpty();
						p.getInventory().setItem(invSlot, spawner);
					}
					p.updateInventory();
					Util.logToFile(p.getName(), p.getName(), spawnerMeta.getDisplayName());
					return true;
				}

				if (args.length >= 2) {
					EntityType entType = EntityType.valueOf(args[0].toUpperCase());
					if (entType == null) {
						Util.sendMsg(sender, getLangManager().getInvalidEntity().replaceAll("%ENTITY%", args[0]));
						return true;
					}

					if (this.blacklistedEntitiesSpawner.size() > 0) {
						for (String ent : this.blacklistedEntitiesSpawner) {
							if (entType.name().equalsIgnoreCase(ent)) {
								Util.sendMsg(sender, getLangManager().getBlacklistedEntity());
								return true;
							}
						}
					}

					Player target = Bukkit.getPlayer(args[1]);
					if (target != null) {
						Util.sendMsg(target, getLangManager().getGotSpawner().replaceAll("%ENTITY%", entType.name()));
						target.playSound(target.getLocation(), Sound.ORB_PICKUP, 10, -5000);
						ItemStack spawner = new ItemStack(Material.MOB_SPAWNER, 1);
						ItemMeta spawnerMeta = spawner.getItemMeta();
						spawnerMeta.setDisplayName(
								getLangManager().getSpawnerName().replaceAll("%ENTITY%", entType.name()));
						spawner.setItemMeta(spawnerMeta);
						if (target.getInventory().firstEmpty() == -1) {
							target.getWorld().dropItem(target.getLocation().add(0, 1, 0), spawner);
						} else {
							int invSlot = target.getInventory().firstEmpty();
							target.getInventory().setItem(invSlot, spawner);
							target.updateInventory();
						}
						Util.logToFile(sender.getName(), target.getName(), spawnerMeta.getDisplayName());
						return true;
					} else {
						Util.sendMsg(sender, getLangManager().getUserNotOnline().replaceAll("%PLAYER%", args[1]));
						return true;
					}
				}
			} else {
				Util.sendMsg(sender, getLangManager().getNoPermission());
				return true;
			}
		}
		return true;
	}

	public Material getItemMat() {
		return itemMat;
	}

	public String getItemName() {
		return itemName;
	}

	public List<String> getBlacklistedWorlds() {
		return blacklistedWorlds;
	}

	public List<String> getBlacklistedEntities() {
		return blacklistedEntities;
	}

	public SimpleConfig getLangConfig() {
		return langConfig;
	}

	public LangManager getLangManager() {
		return langManager;
	}

	public boolean isEggsEnabled() {
		return eggsEnabled;
	}

	public void setEggsEnabled(boolean eggsEnabled) {
		this.eggsEnabled = eggsEnabled;
	}
}
