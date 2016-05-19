package com.maxx.keyspawner.events;

import org.bukkit.block.Biome;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Skeleton;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.CreatureSpawnEvent;

public class SpawnEvent implements Listener {
	@EventHandler
	public void onCreatureSpawn(CreatureSpawnEvent event) {
		if (!event.getSpawnReason().equals(CreatureSpawnEvent.SpawnReason.SPAWNER)) {
			return;
		}
		if (!event.getEntityType().equals(EntityType.SKELETON)) {
			return;
		}
		Skeleton skelly = (Skeleton) event.getEntity();
		if (!skelly.getLocation().getBlock().getBiome().equals(Biome.HELL)) {
			return;
		}
		if (skelly.getSkeletonType().equals(Skeleton.SkeletonType.WITHER)) {
			skelly.setSkeletonType(Skeleton.SkeletonType.NORMAL);
		}
	}
}
