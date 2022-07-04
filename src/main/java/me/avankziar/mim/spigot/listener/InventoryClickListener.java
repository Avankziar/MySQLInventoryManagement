package main.java.me.avankziar.mim.spigot.listener;

import org.bukkit.Material;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;

import main.java.me.avankziar.mim.spigot.permission.Bypass;

public class InventoryClickListener implements Listener
{	
	@EventHandler (priority = EventPriority.HIGH)
	public void onInventoryClick(InventoryClickEvent event)
	{
		Player player = (Player) event.getWhoClicked();
		if(!player.hasPermission(Bypass.get(Bypass.Permission.SHULKER_OPEN_IN_INVENTORY))
				|| event.getClick() != ClickType.RIGHT
				|| InventoryCloseListener.executorToShulkerSlot.containsKey(player.getUniqueId())
				|| event.getClickedInventory() == null 
				|| event.getCurrentItem() == null
				|| !isShulker(event.getCurrentItem().getType()))
		{
			return;
		}
		int slot = event.getSlot();
		final ItemStack is = event.getCurrentItem();
		if(!(is.getItemMeta() instanceof BlockStateMeta))
		{
			return;
		}
		BlockStateMeta im = (BlockStateMeta) is.getItemMeta();
        if(!(im.getBlockState() instanceof ShulkerBox))
        {
        	return;
        }
        ShulkerBox shulker = (ShulkerBox) im.getBlockState();
		if(InventoryCloseListener.inExternInventory(player.getUniqueId()))
		{
			InventoryCloseListener.openShulkerInInventory(player,
	        		null, null,
	        		slot, is, shulker, player.getName(),
	        		im.hasDisplayName() ? im.getDisplayName() : null);			
		} else if(!InventoryCloseListener.inExternInventory(player.getUniqueId()))
		{
	        InventoryCloseListener.openShulkerInInventory(player,
	        		player.getUniqueId(), player.getInventory(),
	        		slot, is, shulker, player.getName(),
	        		im.hasDisplayName() ? im.getDisplayName() : null);
		}
	}
	
	public static boolean isShulker(Material mat)
	{
		switch(mat)
		{
		case SHULKER_BOX:
		case BLACK_SHULKER_BOX:
		case BLUE_SHULKER_BOX:
		case BROWN_SHULKER_BOX:
		case CYAN_SHULKER_BOX:
		case GRAY_SHULKER_BOX:
		case GREEN_SHULKER_BOX:
		case LIGHT_BLUE_SHULKER_BOX:
		case LIGHT_GRAY_SHULKER_BOX:
		case LIME_SHULKER_BOX:
		case MAGENTA_SHULKER_BOX:
		case ORANGE_SHULKER_BOX:
		case PINK_SHULKER_BOX:
		case PURPLE_SHULKER_BOX:
		case RED_SHULKER_BOX:
		case WHITE_SHULKER_BOX:
		case YELLOW_SHULKER_BOX:
			return true;
		default:
			break;
		}
		return false;
	}
}