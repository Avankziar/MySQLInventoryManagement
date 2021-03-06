package main.java.me.avankziar.mim.spigot.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.player.PlayerHarvestBlockEvent;

import main.java.me.avankziar.mim.spigot.MIM;
import main.java.me.avankziar.mim.spigot.objects.SyncTask.RunType;
import main.java.me.avankziar.mim.spigot.objects.SyncType;

public class PlayerHarvestBlockListener extends BaseListener
{
	public PlayerHarvestBlockListener(MIM plugin)
	{
		super(plugin, BaseListener.Type.PLAYER_HARVESTBLOCK);
	}
	
	@EventHandler (priority = EventPriority.LOWEST)
	public void onPlayerHarvestBlock(PlayerHarvestBlockEvent event)
	{
		if(event.isCancelled())
		{
			return;
		}
		if(!plugin.getConfigHandler().isEventEnabled(this.bType.getName(), event.getHarvestedBlock().getWorld()))
		{
			return;
		}
		Player player = event.getPlayer();
		doSync(player, SyncType.INVENTORY, RunType.SAVE);
	}
}