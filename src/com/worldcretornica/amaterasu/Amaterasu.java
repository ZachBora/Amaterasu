package com.worldcretornica.amaterasu;

import java.util.HashSet;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import com.nijiko.permissions.PermissionHandler;
import com.nijikokun.bukkit.Permissions.Permissions;

public class Amaterasu extends JavaPlugin {
	
	public final JutsuListener jutsulistener = new JutsuListener(this);
	
	public final Set<Player> jutsuplayers = new HashSet<Player>();
	public final Set<Player> sneakingjutsuplayers = new HashSet<Player>();
	
	public final Logger logger = Logger.getLogger("Minecraft");
	
	public String pdfdescription;
	private String pdfversion;
	
	// Permissions
    public PermissionHandler permissions;
    boolean permissions3;
	
	@Override
	public void onDisable() {
		this.logger.info(pdfdescription + " disabled.");
		
	}

	@Override
	public void onEnable() {
		PluginManager pm = getServer().getPluginManager();
		pm.registerEvent(Event.Type.PLAYER_TOGGLE_SNEAK, this.jutsulistener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_ANIMATION, this.jutsulistener, Event.Priority.Normal, this);
		pm.registerEvent(Event.Type.PLAYER_MOVE, this.jutsulistener, Event.Priority.Normal, this);
		
		PluginDescriptionFile pdfFile = this.getDescription();
		pdfdescription = pdfFile.getName();
		pdfversion = pdfFile.getVersion();
	
		setupPermissions();
		
		this.logger.info(pdfdescription + " version " + pdfversion + " is enabled!");
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command command,	String label, String[] args)
	{
		if (label.equalsIgnoreCase("amaterasu"))
		{
			if (sender instanceof Player)
			{
				if (!this.checkPermissions((Player) sender, "Amaterasu.fire"))
				{
					sender.sendMessage("[" + pdfdescription + "] Permissions Denied");
				}else{
					toggleJutsuPlayer((Player) sender);
				}
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
	
	
	public boolean isJutsuPlayer(Player player)
	{
		return jutsuplayers.contains(player);
	}
	
	public boolean isSneakingJutsuPlayers(Player player)
	{
		return sneakingjutsuplayers.contains(player);
	}
	
	public void toggleJutsuPlayer(Player player)
	{
		if (isJutsuPlayer(player))
		{
			jutsuplayers.remove(player);
			player.sendMessage(ChatColor.RED + "Amaterasu Disabled.");
		}else{
			jutsuplayers.add(player);
			player.sendMessage(ChatColor.RED + "Amaterasu Enabled. Press SNEAK (shift) to set ablaze!");
		}
	}
	
	public void toggleSneakingJutsuPlayer(Player player, boolean enable)
	{
		if (isSneakingJutsuPlayers(player) && !enable)
		{
			sneakingjutsuplayers.remove(player);
		}else if (enable){
			sneakingjutsuplayers.add(player);
		}
	}
	
	private void setupPermissions() {
        if(permissions != null)
            return;
        
        Plugin permTest = this.getServer().getPluginManager().getPlugin("Permissions");
        
        // Check to see if Permissions exists
        if (permTest == null) {
        	logger.info("[" + pdfdescription + "] Permissions not found, using SuperPerms");
        	return;
        }
    	// Check if it's a bridge
    	if (permTest.getDescription().getVersion().startsWith("2.7.7")) {
    		logger.info("[" + pdfdescription + "] Found Permissions Bridge. Using SuperPerms");
    		return;
    	}
    	
    	// We're using Permissions
    	permissions = ((Permissions) permTest).getHandler();
    	// Check for Permissions 3
    	permissions3 = permTest.getDescription().getVersion().startsWith("3");
    	logger.info("[" + pdfdescription + "] Permissions " + permTest.getDescription().getVersion() + " found");
    }
	
	public Boolean checkPermissions(Player player, String node) {
    	// Permissions
        if (this.permissions != null) {
            if (this.permissions.has(player, node))
                return true;
        // SuperPerms
        } else if (player.hasPermission(node)) {
              return true;
        } else if (player.isOp()) {
            return true;
        }
        return false;
    }


}
