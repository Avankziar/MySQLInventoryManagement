package main.java.me.avankziar.mim.spigot.handler;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.NamespacedKey;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.potion.PotionEffect;

import main.java.me.avankziar.mim.spigot.MIM;
import main.java.me.avankziar.mim.spigot.database.MysqlHandler;
import main.java.me.avankziar.mim.spigot.gui.GUI.PersistentType;
import main.java.me.avankziar.mim.spigot.objects.PersistentData;
import main.java.me.avankziar.mim.spigot.objects.PlayerData;
import main.java.me.avankziar.mim.spigot.objects.SyncType;

public class PlayerDataHandler
{
	public static ArrayList<Attribute> attributeList = new ArrayList<>();
	static
	{
		for(Attribute a : new ArrayList<Attribute>(EnumSet.allOf(Attribute.class)))
		{
			if(a != Attribute.HORSE_JUMP_STRENGTH && a != Attribute.ZOMBIE_SPAWN_REINFORCEMENTS)
			{
				attributeList.add(a);
			}
		}
	}
	
	public static ArrayList<PersistentData> getPersitentData(Player player)
	{
		ArrayList<PersistentData> list = new ArrayList<>();
		PersistentDataContainer pdc = player.getPersistentDataContainer();
		for(NamespacedKey n : pdc.getKeys())
		{
			for(PersistentType a : new ArrayList<PersistentType>(EnumSet.allOf(PersistentType.class)))
			{
				PersistentDataType<?, ?> pdt = null;
				switch(a)
				{
				default:
					break;
				case BYTE:
					pdt = PersistentDataType.BYTE;
					if(pdc.has(n, pdt))
					{
						list.add(new PersistentData(n.getNamespace(), n.getKey(), a, String.valueOf((Byte) pdc.get(n, pdt))));
					}
					break;
				case DOUBLE:
					pdt = PersistentDataType.DOUBLE;
					if(pdc.has(n, pdt))
					{
						list.add(new PersistentData(n.getNamespace(), n.getKey(), a, String.valueOf((double) pdc.get(n, pdt))));
					}
					break;
				case FLOAT:
					pdt = PersistentDataType.FLOAT;
					break;
				case INTEGER:
					pdt = PersistentDataType.INTEGER;
					break;
				case LONG:
					pdt = PersistentDataType.LONG;
					break;
				case LONG_ARRAY:
					pdt = PersistentDataType.LONG_ARRAY;
					break;
				case SHORT:
					pdt = PersistentDataType.SHORT;
					break;
				case STRING:
					pdt = PersistentDataType.STRING;
					break;
				}
				
			}
		}
		return list;
	}
	
	@SuppressWarnings("deprecation")
	private static void setPersitentData(Player player, ArrayList<PersistentData> list)
	{
		PersistentDataContainer pdc = player.getPersistentDataContainer();
		for(PersistentData pd : list)
		{
			NamespacedKey n = new NamespacedKey(pd.getNamespaced(), pd.getKey());
			switch(pd.getPersistentType())
			{
			default:
				break;
			case BYTE:
				try
				{
					pdc.set(n, PersistentDataType.BYTE, Byte.valueOf(pd.getPersistentValue()));
				} catch(Exception e){}
				break;
			case DOUBLE:
				try
				{
					pdc.set(n, PersistentDataType.DOUBLE, Double.valueOf(pd.getPersistentValue()));
				} catch(Exception e){}
				break;
			case FLOAT:
				try
				{
					pdc.set(n, PersistentDataType.FLOAT, Float.valueOf(pd.getPersistentValue()));
				} catch(Exception e){}
				break;
			case INTEGER:
				try
				{
					pdc.set(n, PersistentDataType.INTEGER, Integer.valueOf(pd.getPersistentValue()));
				} catch(Exception e){}
				break;
			case LONG:
				try
				{
					pdc.set(n, PersistentDataType.LONG, Long.valueOf(pd.getPersistentValue()));
				} catch(Exception e){}
				break;
			case SHORT:
				try
				{
					pdc.set(n, PersistentDataType.SHORT, Short.valueOf(pd.getPersistentValue()));
				} catch(Exception e){}
				break;
			case STRING:
				try
				{
					pdc.set(n, PersistentDataType.STRING, String.valueOf(pd.getPersistentValue()));
				} catch(Exception e){}
				break;
			}
		}
		return;
	}
	
	/**
	 * Saves the playerData in the mysql. Async usage!
	 * @param player
	 */
	public static void save(SyncType syncType, final Player player)
	{
		String synchroKey = MIM.getPlugin().getConfigHandler().getSynchroKey(player);
		PlayerData pd = (PlayerData) MIM.getPlugin().getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA,
				"`player_uuid` = ? AND `synchro_key` = ?",
				player.getUniqueId().toString(), synchroKey);
		LinkedHashMap<Attribute, Double> attributes = new LinkedHashMap<>();
		for(Attribute at : attributeList)
		{
			if(player.getAttribute(at) != null)
			{
				attributes.put(at, player.getAttribute(at).getBaseValue());
			}
		}
		ArrayList<PotionEffect> pe = new ArrayList<>();
		for(PotionEffect eff : player.getActivePotionEffects())
		{
			pe.add(eff);
		}
		if(pd != null)
		{
			if(syncType != SyncType.FULL)
			{
				save(syncType, player, pd, attributes, pe);
				return;
			}
			pd.setName(player.getName());
			pd.setInventoryStorageContents(player.getInventory().getStorageContents());
			pd.setArmorContents(player.getInventory().getArmorContents());
			pd.setOffHand(player.getInventory().getItemInOffHand());
			pd.setEnderchestContents(player.getEnderChest().getContents());
			pd.setFoodLevel(player.getFoodLevel());
			pd.setSaturation(player.getSaturation());
			pd.setSaturatedRegenRate(player.getSaturatedRegenRate());
			pd.setUnsaturatedRegenRate(player.getUnsaturatedRegenRate());
			pd.setStarvationRate(player.getStarvationRate());
			pd.setExhaustion(player.getExhaustion());
			pd.setAttributes(attributes);
			pd.setHealth(player.getHealth());
			pd.setAbsorptionAmount(player.getAbsorptionAmount());
			pd.setExpTowardsNextLevel(player.getExp());
			pd.setExpLevel(player.getLevel());
			pd.setTotalExperience(player.getTotalExperience());
			pd.setWalkSpeed(player.getWalkSpeed());
			pd.setFlySpeed(player.getFlySpeed());
			pd.setFireTicks(player.getFireTicks());
			pd.setFreezeTicks(player.getFreezeTicks());
			pd.setFlying(player.getAllowFlight());
			pd.setGlowing(player.isGlowing());
			pd.setGravity(player.hasGravity());
			pd.setInvisible(player.isInvisible());
			pd.setInvulnerable(MIM.getPlugin().getYamlHandler().getConfig().getBoolean("SaveOption.Invulnerable", false) ? player.isInvulnerable() : false);
			pd.setActiveEffects(pe);
			pd.setEntityCategory(player.getCategory());
			pd.setArrowsInBody(player.getArrowsInBody());
			pd.setMaximumAir(player.getMaximumAir());
			pd.setRemainingAir(player.getRemainingAir());
			pd.setCustomName(player.getCustomName());
			pd.setPersistentData(getPersitentData(player));
			MIM.getPlugin().getMysqlHandler().updateData(MysqlHandler.Type.PLAYERDATA, pd,
					"`player_uuid` = ? AND `synchro_key` = ?",
				player.getUniqueId().toString(), synchroKey);
		} else
		{
			pd = new PlayerData(0, synchroKey, player.getUniqueId(), player.getName(),
					player.getInventory().getStorageContents(), player.getInventory().getArmorContents(),
					player.getInventory().getItemInOffHand(), player.getEnderChest().getContents(),
					player.getFoodLevel(), player.getSaturation(), player.getSaturatedRegenRate(),
					player.getUnsaturatedRegenRate(), player.getStarvationRate(), player.getExhaustion(),
					attributes, player.getHealth(), player.getAbsorptionAmount(), 
					player.getExp(), player.getLevel(), player.getTotalExperience(), 
					player.getWalkSpeed(), player.getFlySpeed(), player.getFireTicks(), player.getFreezeTicks(),
					player.getAllowFlight(), player.isGlowing(), player.hasGravity(), player.isInvisible(), player.isInvulnerable(),
					pe, player.getCategory(), player.getArrowsInBody(), 
					player.getMaximumAir(), player.getRemainingAir(), player.getCustomName(), getPersitentData(player),
					MIM.getPlugin().getConfigHandler().getDefaultClearToggle(), player.getGameMode());
			MIM.getPlugin().getMysqlHandler().create(MysqlHandler.Type.PLAYERDATA, pd);
		}
	}
	
	private static void save(SyncType syncType, Player player, PlayerData pd, LinkedHashMap<Attribute, Double> attributes,
			ArrayList<PotionEffect> pe)
	{
		switch(syncType)
		{
		default:
			return;
		case ATTRIBUTE:
			try (Connection conn = MIM.getPlugin().getMysqlSetup().getConnection();)
			{
				String sql = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
					+ "` SET `food_level` = ?, `saturation` = ?, `saturated_regen_rate` = ?,"
					+ " `unsaturated_regen_rate` = ?, `starvation_rate` = ?, `exhaustion` = ?,"
					+ " `attributes` = ?, `health` = ?, `absorption_amount` = ?,"
					+ " `walk_speed` = ?, `fly_speed` = ?, `fire_ticks` = ?, `freeze_ticks` = ?,"
					+ " `flying` = ?, `glowing` = ?, `gravity` = ?, `invisible` = ?, `invulnerable` = ?,"
					+ " `entity_category` = ?, `arrows_in_body` = ?, `maximum_air` = ?,"
					+ " `remaining_air` = ?, `custom_name` = ?, `last_game_mode` = ?"
					+ " WHERE `synchro_key` = ? AND `player_uuid` = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
		        ps.setInt(1, player.getFoodLevel());
		        ps.setFloat(2, player.getSaturation());
		        ps.setInt(3, player.getSaturatedRegenRate());
		        ps.setInt(4, player.getUnsaturatedRegenRate());
		        ps.setInt(5,player.getStarvationRate());
		        ps.setFloat(6, player.getExhaustion());
		        StringBuilder at = new StringBuilder();
		        for(Entry<Attribute, Double> e : attributes.entrySet())
		        {
		        	at.append(e.getKey().toString()+";"+e.getValue().doubleValue()+"@");
		        }
		        ps.setString(7, at.toString());
		        ps.setDouble(8, player.getHealth());
		        ps.setDouble(9, player.getAbsorptionAmount());
		        ps.setFloat(10, player.getWalkSpeed());
		        ps.setFloat(11, player.getFlySpeed());
		        ps.setInt(12, player.getFireTicks());
		        ps.setInt(13, player.getFreezeTicks());
		        ps.setBoolean(14, player.getAllowFlight());
		        ps.setBoolean(15, player.isGlowing());
		        ps.setBoolean(16, player.hasGravity());
		        ps.setBoolean(17, player.isInvisible());
		        ps.setBoolean(18, MIM.getPlugin().getYamlHandler().getConfig().getBoolean("SaveOption.Invulnerable", false) ? player.isInvulnerable() : false);
		        ps.setString(19, player.getCategory().toString());
		        ps.setInt(20, player.getArrowsInBody());
		        ps.setInt(21, player.getMaximumAir());
		        ps.setInt(22, player.getRemainingAir());
		        ps.setString(23, player.getGameMode().toString());
		        
		        ps.setString(24, pd.getSynchroKey());
		        ps.setString(25, player.getUniqueId().toString());		
				int u = ps.executeUpdate();
				MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			} catch (SQLException e)
			{
				MIM.log.log(Level.WARNING, "SQLException! Could not update a PlayerData Object!", e);
			}
			return;
		case EXP:
			try (Connection conn = MIM.getPlugin().getMysqlSetup().getConnection();)
			{
				String sql = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
					+ "` SET `exp_towards_next_level` = ?, `exp_level` = ?, `total_experience` = ?"
					+ " WHERE `synchro_key` = ? AND `player_uuid` = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
		        ps.setFloat(1, player.getExp());
		        ps.setInt(2, player.getLevel());
		        ps.setInt(3, player.getTotalExperience());
		        
		        ps.setString(4, pd.getSynchroKey());
		        ps.setString(5, player.getUniqueId().toString());		
				int u = ps.executeUpdate();
				MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			} catch (SQLException e)
			{
				MIM.log.log(Level.WARNING, "SQLException! Could not update a PlayerData Object!", e);
			}
			return;
		case INVENTORY:
			try (Connection conn = MIM.getPlugin().getMysqlSetup().getConnection();)
			{
				String sql = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
					+ "` SET `inventory_content` = ?, `armor_content` = ?, `off_hand` = ?, `enderchest_content` = ?"
					+ " WHERE `synchro_key` = ? AND `player_uuid` = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
		        ps.setString(1, MIM.getPlugin().getBase64Provider().toBase64Array(player.getInventory().getStorageContents()));
		        ps.setString(2, MIM.getPlugin().getBase64Provider().toBase64Array(player.getInventory().getArmorContents()));
		        ps.setString(3, MIM.getPlugin().getBase64Provider().toBase64(player.getInventory().getItemInOffHand()));
		        ps.setString(4, MIM.getPlugin().getBase64Provider().toBase64Array(player.getEnderChest().getContents()));
		        
		        ps.setString(5, pd.getSynchroKey());
		        ps.setString(6, player.getUniqueId().toString());		
				int u = ps.executeUpdate();
				MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			} catch (SQLException e)
			{
				MIM.log.log(Level.WARNING, "SQLException! Could not update a PlayerData Object!", e);
			}
			return;
		case INV_ARMOR:
			try (Connection conn = MIM.getPlugin().getMysqlSetup().getConnection();)
			{
				String sql = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
					+ "` SET `armor_content` = ?"
					+ " WHERE `synchro_key` = ? AND `player_uuid` = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
		        ps.setString(1, MIM.getPlugin().getBase64Provider().toBase64Array(player.getInventory().getArmorContents()));
		        
		        ps.setString(2, pd.getSynchroKey());
		        ps.setString(3, player.getUniqueId().toString());		
				int u = ps.executeUpdate();
				MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			} catch (SQLException e)
			{
				MIM.log.log(Level.WARNING, "SQLException! Could not update a PlayerData Object!", e);
			}
			return;
		case INV_ENDERCHEST:
			try (Connection conn = MIM.getPlugin().getMysqlSetup().getConnection();)
			{
				String sql = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
					+ "` SET `enderchest_content` = ?"
					+ " WHERE `synchro_key` = ? AND `player_uuid` = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
		        ps.setString(1, MIM.getPlugin().getBase64Provider().toBase64Array(player.getEnderChest().getContents()));
		        
		        ps.setString(2, pd.getSynchroKey());
		        ps.setString(3, player.getUniqueId().toString());		
				int u = ps.executeUpdate();
				MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			} catch (SQLException e)
			{
				MIM.log.log(Level.WARNING, "SQLException! Could not update a PlayerData Object!", e);
			}
			return;
		case INV_OFFHAND:
			try (Connection conn = MIM.getPlugin().getMysqlSetup().getConnection();)
			{
				String sql = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
					+ "` SET `off_hand` = ?"
					+ " WHERE `synchro_key` = ? AND `player_uuid` = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
		        ps.setString(1, MIM.getPlugin().getBase64Provider().toBase64(player.getInventory().getItemInOffHand()));
		        
		        ps.setString(2, pd.getSynchroKey());
		        ps.setString(3, player.getUniqueId().toString());		
				int u = ps.executeUpdate();
				MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			} catch (SQLException e)
			{
				MIM.log.log(Level.WARNING, "SQLException! Could not update a PlayerData Object!", e);
			}
			return;
		case INV_ONLY:
			try (Connection conn = MIM.getPlugin().getMysqlSetup().getConnection();)
			{
				String sql = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
					+ "` SET `inventory_content` = ?"
					+ " WHERE `synchro_key` = ? AND `player_uuid` = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
		        ps.setString(1, MIM.getPlugin().getBase64Provider().toBase64Array(player.getInventory().getStorageContents()));
		        
		        ps.setString(2, pd.getSynchroKey());
		        ps.setString(3, player.getUniqueId().toString());		
				int u = ps.executeUpdate();
				MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			} catch (SQLException e)
			{
				MIM.log.log(Level.WARNING, "SQLException! Could not update a PlayerData Object!", e);
			}
			return;
		case EFFECT:
			try (Connection conn = MIM.getPlugin().getMysqlSetup().getConnection();)
			{
				String sql = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
					+ "` SET `potion_effects` = ?"
					+ " WHERE `synchro_key` = ? AND `player_uuid` = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
		        ps.setString(1, MIM.getPlugin().getBase64Provider().toBase64Array(pe.toArray(
		        		new PotionEffect[pd.getActiveEffects().size()])));
		        
		        ps.setString(2, pd.getSynchroKey());
		        ps.setString(3, player.getUniqueId().toString());		
				int u = ps.executeUpdate();
				MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			} catch (SQLException e)
			{
				MIM.log.log(Level.WARNING, "SQLException! Could not update a PlayerData Object!", e);
			}
			return;
		case PERSITENTDATA:
			try (Connection conn = MIM.getPlugin().getMysqlSetup().getConnection();)
			{
				String sql = "UPDATE `" + MysqlHandler.Type.PLAYERDATA.getValue()
					+ "` SET `persistent_data` = ?"
					+ " WHERE `synchro_key` = ? AND `player_uuid` = ?";
				PreparedStatement ps = conn.prepareStatement(sql);
		        StringBuilder pds = new StringBuilder();
		        for(PersistentData per : getPersitentData(player))
		        {
		        	pds.append(per.getNamespaced()+";"+per.getKey()+";"+per.getPersistentType().toString()+";"+per.getPersistentValue()+"@");
		        }
		        ps.setString(1, pds.toString());
		        
		        ps.setString(2, pd.getSynchroKey());
		        ps.setString(3, player.getUniqueId().toString());		
				int u = ps.executeUpdate();
				MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			} catch (SQLException e)
			{
				MIM.log.log(Level.WARNING, "SQLException! Could not update a PlayerData Object!", e);
			}
			return;
		}
	}
	
	/**
	 * Load the playerData on the player. Always sync
	 * @param player
	 */
	public static void load(SyncType syncType, Player player)
	{
		String synchroKey = MIM.getPlugin().getConfigHandler().getSynchroKey(player);
		PlayerData pd = (PlayerData) MIM.getPlugin().getMysqlHandler().getData(MysqlHandler.Type.PLAYERDATA,
				"`player_uuid` = ? AND `synchro_key` = ?",
				player.getUniqueId().toString(), synchroKey);
		if(pd == null)
		{
			save(SyncType.FULL, player);
			return;
		}
		if(syncType != SyncType.FULL)
		{
			load(syncType, player, pd);
			return;
		}
		try
		{
			player.getInventory().setStorageContents(pd.getInventoryStorageContents());
			player.getInventory().setArmorContents(pd.getArmorContents());
			player.getInventory().setItemInOffHand(pd.getOffHand());
			player.getEnderChest().setStorageContents(pd.getEnderchestContents());
		} catch(Exception e){}
		player.setFoodLevel(pd.getFoodLevel());
		player.setSaturation(pd.getSaturation());
		player.setSaturatedRegenRate(pd.getSaturatedRegenRate());
		player.setUnsaturatedRegenRate(pd.getUnsaturatedRegenRate());
		player.setStarvationRate(pd.getStarvationRate());
		player.setExhaustion(pd.getExhaustion());
		for(Attribute a : attributeList)
		{
			if(pd.getAttributes().containsKey(a))
			{
				player.getAttribute(a).setBaseValue(pd.getAttributes().get(a));
			}
		}
		player.setHealth(pd.getHealth());
		player.setAbsorptionAmount(pd.getAbsorptionAmount());
		player.setExp(pd.getExpTowardsNextLevel());
		player.setLevel(pd.getExpLevel());
		player.setTotalExperience(pd.getTotalExperience());
		player.setWalkSpeed(pd.getWalkSpeed());
		player.setFlySpeed(pd.getFlySpeed());
		player.setFireTicks(pd.getFireTicks());
		player.setFreezeTicks(pd.getFreezeTicks());
		player.setAllowFlight(pd.isFlying());
		player.setGlowing(pd.isGlowing());
		player.setGravity(pd.isGravity());
		player.setInvisible(pd.isInvisible());
		player.setInvulnerable(pd.isInvulnerable());
		for(PotionEffect pe : pd.getActiveEffects())
		{
			player.addPotionEffect(pe);
		}
		player.setArrowsInBody(pd.getArrowsInBody());
		player.setMaximumAir(pd.getMaximumAir());
		player.setRemainingAir(pd.getRemainingAir());
		if(new ConfigHandler(MIM.getPlugin()).isOverrideGameMode(player.getWorld(), false))
		{
			player.setGameMode(new ConfigHandler(MIM.getPlugin()).getOverrideGameMode(player.getWorld(), false));
		} else
		{
			player.setGameMode(pd.getLastGameMode());
		}
		setPersitentData(player, pd.getPersistentData());
	}
	
	private static void load(SyncType syncType, Player player, PlayerData pd)
	{
		switch(syncType)
		{
		default:
			return;
		case ATTRIBUTE:
			player.setFoodLevel(pd.getFoodLevel());
			player.setSaturation(pd.getSaturation());
			player.setSaturatedRegenRate(pd.getSaturatedRegenRate());
			player.setUnsaturatedRegenRate(pd.getUnsaturatedRegenRate());
			player.setStarvationRate(pd.getStarvationRate());
			player.setExhaustion(pd.getExhaustion());
			for(Attribute a : attributeList)
			{
				if(pd.getAttributes().containsKey(a))
				{
					player.getAttribute(a).setBaseValue(pd.getAttributes().get(a));
				}
			}
			player.setHealth(pd.getHealth());
			player.setAbsorptionAmount(pd.getAbsorptionAmount());
			player.setWalkSpeed(pd.getWalkSpeed());
			player.setFlySpeed(pd.getFlySpeed());
			player.setFireTicks(pd.getFireTicks());
			player.setFreezeTicks(pd.getFreezeTicks());
			player.setAllowFlight(pd.isFlying());
			player.setGlowing(pd.isGlowing());
			player.setGravity(pd.isGravity());
			player.setInvisible(pd.isInvisible());
			player.setInvulnerable(pd.isInvulnerable());
			player.setArrowsInBody(pd.getArrowsInBody());
			player.setMaximumAir(pd.getMaximumAir());
			player.setRemainingAir(pd.getRemainingAir());
			if(new ConfigHandler(MIM.getPlugin()).isOverrideGameMode(player.getWorld(), false))
			{
				player.setGameMode(new ConfigHandler(MIM.getPlugin()).getOverrideGameMode(player.getWorld(), false));
			} else
			{
				player.setGameMode(pd.getLastGameMode());
			}
			return;
		case EXP:
			player.setExp(pd.getExpTowardsNextLevel());
			player.setLevel(pd.getExpLevel());
			player.setTotalExperience(pd.getTotalExperience());
			return;
		case INVENTORY:
			player.getInventory().setStorageContents(pd.getInventoryStorageContents());
			player.getInventory().setArmorContents(pd.getArmorContents());
			player.getInventory().setItemInOffHand(pd.getOffHand());
			player.getEnderChest().setStorageContents(pd.getEnderchestContents());
			return;
		case EFFECT:
			for(PotionEffect pe : pd.getActiveEffects())
			{
				player.addPotionEffect(pe);
			}
			return;
		case PERSITENTDATA:
			setPersitentData(player, pd.getPersistentData());
			return;
		}
	}
}
