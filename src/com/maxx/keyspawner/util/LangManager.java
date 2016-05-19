package com.maxx.keyspawner.util;

import com.maxx.keyspawner.main.KeySpawner;

public class LangManager {
	private String usageKeySpawner;
	private String keySent;
	private String keyReceived;
	private String userNotOnline;
	private String noPermission;
	private String listEntities;
	private String usageSpawner;
	private String notPlayer;
	private String chooseAnother;
	private String blacklistedWorld;
	private String blacklistedEntity;
	private String invalidEntity;
	private String gotSpawner;
	private String spawnerName;
	private String lostYourKey;
	private String boughtSpawner;
	private String boughtKeyspawner;

	public void setup(KeySpawner plugin) {
		setUsageKeySpawner(plugin.getLangConfig().getString("msg.usage"));
		setKeySent(plugin.getLangConfig().getString("msg.keysent"));
		setKeyReceived(plugin.getLangConfig().getString("msg.receivekey"));
		setUserNotOnline(plugin.getLangConfig().getString("msg.usernotonline"));
		setNoPermission(plugin.getLangConfig().getString("msg.noperm"));
		setListEntities(plugin.getLangConfig().getString("msg.list-entities"));
		setUsageSpawner(plugin.getLangConfig().getString("msg.usagespawner"));
		setNotPlayer(plugin.getLangConfig().getString("msg.notplayer"));
		setChooseAnother(plugin.getLangConfig().getString("msg.chooseanother"));
		setBlacklistedWorld(plugin.getLangConfig().getString("msg.blacklistedworld"));
		setBlacklistedEntity(plugin.getLangConfig().getString("msg.blacklistedentity"));
		setInvalidEntity(plugin.getLangConfig().getString("msg.invalidentity"));
		setGotSpawner(plugin.getLangConfig().getString("msg.gotspawner"));
		setSpawnerName(plugin.getLangConfig().getString("msg.spawnername"));
		setLostYourKey(plugin.getLangConfig().getString("msg.lostyourkey"));
		setBoughtSpawner(plugin.getLangConfig().getString("msg.boughtspawner"));
		setBoughtKeyspawner(plugin.getLangConfig().getString("msg.boughtkeyspawner"));
	}

	public String getUsageKeySpawner() {
		return usageKeySpawner;
	}

	public void setUsageKeySpawner(String usageKeySpawner) {
		this.usageKeySpawner = Util.colorize(usageKeySpawner);
	}

	public String getBoughtSpawner() {
		return boughtSpawner;
	}

	public void setBoughtSpawner(String boughtSpawner) {
		this.boughtSpawner = Util.colorize(boughtSpawner);
	}

	public String getKeySent() {
		return keySent;
	}

	public void setKeySent(String keySent) {
		this.keySent = Util.colorize(keySent);
	}

	public String getKeyReceived() {
		return keyReceived;
	}

	public void setKeyReceived(String keyReceived) {
		this.keyReceived = Util.colorize(keyReceived);
	}

	public String getUserNotOnline() {
		return userNotOnline;
	}

	public void setUserNotOnline(String userNotOnline) {
		this.userNotOnline = Util.colorize(userNotOnline);
	}

	public String getNoPermission() {
		return noPermission;
	}

	public void setNoPermission(String noPermission) {
		this.noPermission = Util.colorize(noPermission);
	}

	public String getListEntities() {
		return listEntities;
	}

	public void setListEntities(String listEntities) {
		this.listEntities = Util.colorize(listEntities);
	}

	public String getUsageSpawner() {
		return usageSpawner;
	}

	public void setUsageSpawner(String usageSpawner) {
		this.usageSpawner = Util.colorize(usageSpawner);
	}

	public String getNotPlayer() {
		return notPlayer;
	}

	public void setNotPlayer(String notPlayer) {
		this.notPlayer = Util.colorize(notPlayer);
	}

	public String getChooseAnother() {
		return chooseAnother;
	}

	public void setChooseAnother(String chooseAnother) {
		this.chooseAnother = Util.colorize(chooseAnother);
	}

	public String getBlacklistedWorld() {
		return blacklistedWorld;
	}

	public void setBlacklistedWorld(String blacklistedWorld) {
		this.blacklistedWorld = Util.colorize(blacklistedWorld);
	}

	public String getBlacklistedEntity() {
		return blacklistedEntity;
	}

	public void setBlacklistedEntity(String blacklistedEntity) {
		this.blacklistedEntity = Util.colorize(blacklistedEntity);
	}

	public String getInvalidEntity() {
		return invalidEntity;
	}

	public void setInvalidEntity(String invalidEntity) {
		this.invalidEntity = Util.colorize(invalidEntity);
	}

	public String getGotSpawner() {
		return gotSpawner;
	}

	public void setGotSpawner(String gotSpawner) {
		this.gotSpawner = Util.colorize(gotSpawner);
	}

	public String getSpawnerName() {
		return spawnerName;
	}

	public void setSpawnerName(String spawnerName) {
		this.spawnerName = Util.colorize(spawnerName);
	}

	public String getLostYourKey() {
		return lostYourKey;
	}

	public void setLostYourKey(String lostYourKey) {
		this.lostYourKey = Util.colorize(lostYourKey);
	}

	public String getStartingOfSpawner() {
		String[] start = getSpawnerName().split(" ");
		String startingWords = "";
		for (String s : start) {
			if (s.equalsIgnoreCase("%ENTITY%"))
				break;
			if (startingWords.isEmpty()) {
				startingWords = s;
			} else {
				startingWords = startingWords + " " + s;
			}
		}
		return startingWords + " ";
	}

	public String getBoughtKeyspawner() {
		return boughtKeyspawner;
	}

	public void setBoughtKeyspawner(String boughtKeyspawner) {
		this.boughtKeyspawner = Util.colorize(boughtKeyspawner);
	}
}
