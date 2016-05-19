package com.maxx.keyspawner.events;

import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.massivecraft.factions.entity.BoardColl;
import com.massivecraft.factions.entity.Faction;
import com.massivecraft.factions.entity.MConf;
import com.massivecraft.factions.entity.MPerm;
import com.massivecraft.factions.entity.MPlayer;
import com.massivecraft.massivecore.ps.PS;
import com.maxx.keyspawner.main.KeySpawner;
import com.maxx.keyspawner.util.Util;

public class InteractEventWithFaction implements Listener {
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onInteract(PlayerInteractEvent e) {
		Player p = e.getPlayer();
		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && p.getItemInHand() != null
				&& p.getItemInHand().getType() == KeySpawner.instance.getItemMat()
				&& p.getItemInHand().getItemMeta().getDisplayName().equals(KeySpawner.instance.getItemName())
				&& canPlayerBuildAt(p, PS.valueOf(e.getClickedBlock()), true)) {
			if (e.getClickedBlock().getType() == Material.MOB_SPAWNER && e.getClickedBlock().getType() != null) {
				if (KeySpawner.instance.getBlacklistedWorlds().size() > 0) {
					for (String worldName : KeySpawner.instance.getBlacklistedWorlds()) {
						if (worldName.equals(e.getClickedBlock().getWorld().getName())) {
							e.setCancelled(true);
							Util.sendMsg(p, KeySpawner.instance.getLangManager().getBlacklistedWorld());
							return;
						}
					}
				}
				if (KeySpawner.instance.getBlacklistedEntities().size() > 0) {
					for (String ent : KeySpawner.instance.getBlacklistedEntities()) {
						CreatureSpawner spawner = (CreatureSpawner) e.getClickedBlock().getState();
						if (spawner.getCreatureTypeName().equalsIgnoreCase(ent)) {
							e.setCancelled(true);
							Util.sendMsg(p, KeySpawner.instance.getLangManager().getBlacklistedEntity());
							return;
						}
					}
				}
				e.setCancelled(true);

				ItemStack is = p.getItemInHand();
				if (is.getAmount() > 1)
					is.setAmount(is.getAmount() - 1);
				else
					p.setItemInHand(null);
				p.updateInventory();

				Util.sendMsg(p, KeySpawner.instance.getLangManager().getLostYourKey());
				p.playSound(p.getLocation(), Sound.ORB_PICKUP, 10, -5000);
				ItemStack spawner = new ItemStack(Material.MOB_SPAWNER, 1);
				ItemMeta spawnerMeta = spawner.getItemMeta();
				spawnerMeta.setDisplayName(Util.colorize(KeySpawner.instance.getLangManager().getStartingOfSpawner()
						+ ((CreatureSpawner) e.getClickedBlock().getState()).getSpawnedType().name().toUpperCase()));
				spawner.setItemMeta(spawnerMeta);
				e.getClickedBlock().getWorld().getBlockAt(e.getClickedBlock().getLocation()).setType(Material.AIR);

				if (p.getInventory().contains(spawner)) {
					p.getInventory().addItem(spawner);
					p.updateInventory();
					return;
				}

				if (p.getInventory().firstEmpty() == -1) {
					p.getWorld().dropItem(p.getLocation().add(0, 1, 0), spawner);
				} else {
					int invSlot = p.getInventory().firstEmpty();
					p.getInventory().setItem(invSlot, spawner);
				}
				p.updateInventory();
			} else {
				e.setCancelled(true);
			}
		}

		if (e.getAction() == Action.RIGHT_CLICK_BLOCK && p.getItemInHand() != null
				&& p.getItemInHand().getType() == Material.MONSTER_EGG
				&& e.getClickedBlock().getType() == Material.MOB_SPAWNER && e.getClickedBlock().getType() != null) {
			if (KeySpawner.instance.isEggsEnabled()) {
				e.setCancelled(true);
				EntityType ent = EntityType.fromId(p.getItemInHand().getDurability());
				BlockState state = e.getClickedBlock().getState();
				CreatureSpawner spawner = (CreatureSpawner) state;
				spawner.setSpawnedType(ent);
				spawner.update();
			} else {
				e.setCancelled(true);
			}
		}
	}

	public static boolean canPlayerBuildAt(Object senderObject, PS ps, boolean verboose) {
		MPlayer mplayer = MPlayer.get(senderObject);
		if (mplayer == null)
			return false;
		String name = mplayer.getName();
		if (MConf.get().playersWhoBypassAllProtection.contains(name))
			return true;
		if (mplayer.isUsingAdminMode())
			return true;
		if (!MPerm.getPermBuild().has(mplayer, ps, false) && MPerm.getPermPainbuild().has(mplayer, ps, false)) {
			if (verboose) {
				Faction hostFaction = BoardColl.get().getFactionAt(ps);
				mplayer.msg("<b>It is painful to build in the territory of %s<b>.", hostFaction.describeTo(mplayer));
				Player player = mplayer.getPlayer();
				if (player != null) {
					player.damage(MConf.get().actionDeniedPainAmount);
				}
			}
			return true;
		}
		return MPerm.getPermBuild().has(mplayer, ps, verboose);
	}
}