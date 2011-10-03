package com.worldcretornica.amaterasu;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerAnimationEvent;
import org.bukkit.event.player.PlayerAnimationType;
import org.bukkit.event.player.PlayerListener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerToggleSneakEvent;

public class JutsuListener extends PlayerListener {

	public static Amaterasu plugin;
	
	public JutsuListener(Amaterasu instance)
	{
		plugin = instance;
	}
	
	
	@Override
	public void onPlayerMove(PlayerMoveEvent event) {
		
		Player player = event.getPlayer();
		
		if(plugin.isJutsuPlayer(player) && plugin.isSneakingJutsuPlayers(player))
		{
			//First arg of getTargetBlock = transparent blocks (null = air only), 2nd arg = maxdistance
			Block block = player.getTargetBlock(null, 25);
			
			setBlockOnFire(block, player);
		}
	}
	
	@Override
	public void onPlayerToggleSneak(PlayerToggleSneakEvent event) {
		
		Player player = event.getPlayer();
		
		if(plugin.isJutsuPlayer(player))
		{
			if (event.isSneaking())
			{
				plugin.toggleSneakingJutsuPlayer(player, true);
				
				//First arg of getTargetBlock = transparent blocks (null = air only), 2nd arg = maxdistance
				Block block = player.getTargetBlock(null, 25);
				
				
				setBlockOnFire(block, player);
			}else{
				plugin.toggleSneakingJutsuPlayer(player, false);
			}
		}
	}
	
	public void setBlockOnFire(Block block, Player player)
	{
		Location blockloc = block.getLocation();
		Location playerloc = player.getLocation();
		World world = block.getWorld();
		
		Location fireloc = new Location(world, blockloc.getBlockX(), blockloc.getBlockY() + 1, blockloc.getBlockZ());
		
		if (!(world.getBlockAt(fireloc).getType() == Material.AIR))
		{
			fireloc.subtract(0, 1, 0);
			
			int Xdiff = blockloc.getBlockX() - playerloc.getBlockX();
			int Zdiff = blockloc.getBlockZ() - playerloc.getBlockZ();
			
			if (Math.abs(Xdiff) > Math.abs(Zdiff))
			{
				if (Xdiff > 0)
				{
					fireloc = fireloc.subtract(1, 0, 0);
				}else{
					fireloc = fireloc.subtract(-1, 0, 0);
				}
			}else{
				if (Zdiff > 0)
				{
					fireloc = fireloc.subtract(0, 0, 1);
				}else{
					fireloc = fireloc.subtract(0, 0, -1);
				}
			}
		}
		
		//player.sendMessage("Player : " + playerloc.getBlockX() + "," + playerloc.getBlockY() + "," + playerloc.getBlockZ());
		//player.sendMessage("Bloc : " + blockloc.getBlockX() + "," + blockloc.getBlockY() + "," + blockloc.getBlockZ());
		//player.sendMessage("Fire : " + fireloc.getBlockX() + "," + fireloc.getBlockY() + "," + fireloc.getBlockZ());
		
		world.getBlockAt(fireloc).setType(Material.FIRE);
		
		//if plugin.getServer().getWorld(player.getWorld().getName()).getBlockAt(fireloc)
	}
	
}
