package com.maxx.keyspawner.events;

import org.bukkit.block.Biome;
import org.bukkit.block.BlockState;
import org.bukkit.block.CreatureSpawner;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import com.maxx.keyspawner.main.KeySpawner;

public class PlaceEvent implements Listener {
	@EventHandler
	public void onPlace(BlockPlaceEvent e) {
		if (e.getBlockPlaced() != null && e.getBlockPlaced().getState() != null
				&& e.getBlockPlaced().getState() instanceof CreatureSpawner && e.getPlayer().getItemInHand() != null
				&& e.getPlayer().getItemInHand().getItemMeta() != null
				&& e.getPlayer().getItemInHand().getItemMeta().getDisplayName()
						.startsWith(KeySpawner.instance.getLangManager().getSpawnerName().replace("%ENTITY%", ""))) {
			if (e.getBlock().getBiome().equals(Biome.HELL)
					&& KeySpawner.instance.getConfig().getBoolean("options.disableskeletonspawnernether")) {

			}
			BlockState state = e.getBlockPlaced().getState();
			CreatureSpawner spawner = (CreatureSpawner) state;
			spawner.setSpawnedType(EntityType.valueOf(e.getPlayer().getItemInHand().getItemMeta().getDisplayName()
					.replace(KeySpawner.instance.getLangManager().getStartingOfSpawner(), "")));
			spawner.update();
		}
	}
}
