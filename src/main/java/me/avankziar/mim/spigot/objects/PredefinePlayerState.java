package main.java.me.avankziar.mim.spigot.objects;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Map.Entry;
import java.util.logging.Level;

import org.bukkit.GameMode;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.EntityCategory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;

import main.java.me.avankziar.mim.spigot.MIM;
import main.java.me.avankziar.mim.spigot.database.MysqlHandable;
import main.java.me.avankziar.mim.spigot.database.MysqlHandler;
import main.java.me.avankziar.mim.spigot.gui.GUI.PersistentType;

public class PredefinePlayerState implements MysqlHandable
{	
	private int id;
	private String synchroKey; //Key to synchro on different server & worlds
	private GameMode gameMode; //Second "key"
	private String stateName; //The Name of the prefine player State
	//Inventory
	private ItemStack[] inventoryContents; //getStorageContents() - Returns hotbar & normal slots
	//PlayerInventory
	private ItemStack[] armorContents; //getArmorContents()
	private ItemStack offHand; //getItemInOffHand
	//HumanEntity
	private ItemStack[] enderchestContents; //getEnderChest#getStorageContents()
	private int foodLevel; //getFoodLevel
	private float saturation; //getSaturation
	private int saturatedRegenRate; //getSaturatedRegenRate
	private int unsaturatedRegenRate; //getUnsaturatedRegenRate
	private int starvationRate; //getStarvationRate()
	private float exhaustion; //getExhaustion
	private LinkedHashMap<Attribute, Double> attributes; //getAttribute
	private double health; //getHealth || Only set if MaxHealth is setted!
	private double absorptionAmount; //getAbsorptionAmount
	private float expTowardsNextLevel; //Player#getExp
	private int expLevel; //getLevel
	private float walkSpeed; //getWalkSpeed
	private float flySpeed; //getFlySpeed
	private int fireTicks; //getFireTicks
	private int freezeTicks; //getFreezeTicks
	private boolean glowing; //isGlowing
	private boolean gravity; //hasGravity
	private ArrayList<PotionEffect> activeEffects; //getActivePotionEffects
	private EntityCategory entityCategory; //getCategory || Interessant f??r Schadensverz. BANN etc.
	//LivingEntity
	private int arrowsInBody; //getArrowsInBody
	private int maximumAir; //getMaximumAir
	private int remainingAir; //getRemainingAir
	//Nameable
	private String customName;
	//PersistentDataHolder
	private ArrayList<PersistentData> persistentData;	
	//NamespaceKey(Namespace, Key), PersistentType, value);
	
	
	public PredefinePlayerState(){}
	
	public PredefinePlayerState(int id, String synchroKey, GameMode gameMode, String stateName, 
			ItemStack[] inventoryContents, ItemStack[] armorContents, ItemStack offHand, 
			ItemStack[] enderchestContents, int foodLevel, float saturation, int saturatedRegenRate, int unsaturatedRegenRate, 
			int starvationRate, float exhaustion, LinkedHashMap<Attribute, Double> attributes, double health, 
			double absorptionAmount, float expTowardsNextLevel, int expLevel, float walkSpeed,
			float flySpeed, int fireTicks, int freezeTicks, boolean glowing, boolean gravity, ArrayList<PotionEffect> activeEffects,
			EntityCategory entityCategory, 
			int arrowsInBody, int maximumAir, int remainingAir,
			String customName,
			ArrayList<PersistentData> persistentData)
	{
		setId(id);
		setSynchroKey(synchroKey);
		setGameMode(gameMode);
		setStateName(stateName);
		setInventoryStorageContents(inventoryContents);
		setArmorContents(armorContents);
		setOffHand(offHand);
		setEnderchestContents(enderchestContents);
		setFoodLevel(foodLevel);
		setSaturation(saturation);
		setSaturatedRegenRate(unsaturatedRegenRate);
		setUnsaturatedRegenRate(unsaturatedRegenRate);
		setStarvationRate(starvationRate);
		setExhaustion(exhaustion);
		setAttributes(attributes);
		setHealth(health);
		setAbsorptionAmount(absorptionAmount);
		setExpTowardsNextLevel(expTowardsNextLevel);
		setExpLevel(expLevel);
		setWalkSpeed(walkSpeed);
		setFlySpeed(flySpeed);
		setFireTicks(fireTicks);
		setFreezeTicks(freezeTicks);
		setGlowing(glowing);
		setGravity(gravity);
		setActiveEffects(activeEffects);
		setEntityCategory(entityCategory);
		setArrowsInBody(arrowsInBody);
		setMaximumAir(maximumAir);
		setRemainingAir(remainingAir);
		setCustomName(customName);
		setPersistentData(persistentData);
	}
	
	public PredefinePlayerState(int id, String synchroKey, GameMode gameMode, String stateName, 
			String inventoryContents, String armorContents, String offHand, 
			String enderchestContents, int foodLevel, float saturation, int saturatedRegenRate, int unsaturatedRegenRate, 
			int starvationRate, float exhaustion, String attributes, double health, 
			double absorptionAmount, float expTowardsNextLevel, int expLevel, float walkSpeed,
			float flySpeed, int fireTicks, int freezeTicks, boolean glowing, boolean gravity, String activeEffects,
			EntityCategory entityCategory, 
			int arrowsInBody, int maximumAir, int remainingAir,
			String customName,
			String persistentData)
	{
		setId(id);
		setSynchroKey(synchroKey);
		setGameMode(gameMode);
		setStateName(stateName);
		MIM plugin = MIM.getPlugin();
		ArrayList<ItemStack> invc = new ArrayList<>();
		for(Object o : plugin.getBase64Api().fromBase64Array(inventoryContents))
		{
			invc.add((ItemStack) o);
		}
		setInventoryStorageContents(invc.toArray(new ItemStack[invc.size()]));
		ArrayList<ItemStack> arc = new ArrayList<>();
		for(Object o : plugin.getBase64Api().fromBase64Array(armorContents))
		{
			arc.add((ItemStack) o);
		}
		setArmorContents(arc.toArray(new ItemStack[arc.size()]));
		setOffHand((ItemStack) plugin.getBase64Api().fromBase64(offHand));
		ArrayList<ItemStack> ecc = new ArrayList<>();
		for(Object o : plugin.getBase64Api().fromBase64Array(enderchestContents))
		{
			ecc.add((ItemStack) o);
		}
		setEnderchestContents(ecc.toArray(new ItemStack[ecc.size()]));
		setFoodLevel(foodLevel);
		setSaturation(saturation);
		setSaturatedRegenRate(unsaturatedRegenRate);
		setUnsaturatedRegenRate(unsaturatedRegenRate);
		setStarvationRate(starvationRate);
		setExhaustion(exhaustion);
		LinkedHashMap<Attribute, Double> at = new LinkedHashMap<>();
		for(String s : attributes.split("@"))
		{
			String[] a = s.split(";");
			Attribute att = Attribute.valueOf(a[0]);
			Double d = Double.parseDouble(a[1]);
			at.put(att, d);
		}
		setAttributes(at);
		setHealth(health);
		setAbsorptionAmount(absorptionAmount);
		setExpTowardsNextLevel(expTowardsNextLevel);
		setExpLevel(expLevel);
		setWalkSpeed(walkSpeed);
		setFlySpeed(flySpeed);
		setFireTicks(fireTicks);
		setFreezeTicks(freezeTicks);
		setGlowing(glowing);
		setGravity(gravity);
		ArrayList<PotionEffect> pe = new ArrayList<>();
		for(Object o : plugin.getBase64Api().fromBase64Array(activeEffects))
		{
			pe.add((PotionEffect) o);
		}
		setActiveEffects(pe);
		setEntityCategory(entityCategory);
		setArrowsInBody(arrowsInBody);
		setMaximumAir(maximumAir);
		setRemainingAir(remainingAir);
		setCustomName(customName);
		ArrayList<PersistentData> pd = new ArrayList<>();
		for(String s : persistentData.split("@"))
		{
			String[] p = s.split(";");
			String namespaced = p[0];
			String key = p[1];
			PersistentType pt = PersistentType.valueOf(p[2]);
			String v = p[3];
			pd.add(new PersistentData(namespaced, key, pt, v));
		}
		setPersistentData(pd);
	}

	public String getSynchroKey()
	{
		return synchroKey;
	}

	public void setSynchroKey(String synchroKey)
	{
		this.synchroKey = synchroKey;
	}

	public GameMode getGameMode()
	{
		return gameMode;
	}

	public void setGameMode(GameMode gameMode)
	{
		this.gameMode = gameMode;
	}

	public int getId()
	{
		return id;
	}

	public void setId(int id)
	{
		this.id = id;
	}

	public String getStateName()
	{
		return stateName;
	}

	public void setStateName(String stateName)
	{
		this.stateName = stateName;
	}

	public ItemStack[] getInventoryStorageContents()
	{
		return inventoryContents;
	}

	public void setInventoryStorageContents(ItemStack[] inventoryContents)
	{
		this.inventoryContents = inventoryContents;
	}

	public ItemStack[] getArmorContents()
	{
		return armorContents;
	}

	public void setArmorContents(ItemStack[] armorContents)
	{
		this.armorContents = armorContents;
	}

	public ItemStack getOffHand()
	{
		return offHand;
	}

	public void setOffHand(ItemStack offHand)
	{
		this.offHand = offHand;
	}

	public ItemStack[] getEnderchestContents()
	{
		return enderchestContents;
	}

	public void setEnderchestContents(ItemStack[] enderchestContents)
	{
		this.enderchestContents = enderchestContents;
	}

	public int getFoodLevel()
	{
		return foodLevel;
	}

	public void setFoodLevel(int foodLevel)
	{
		this.foodLevel = foodLevel;
	}

	public float getSaturation()
	{
		return saturation;
	}

	public void setSaturation(float saturation)
	{
		this.saturation = saturation;
	}

	public int getSaturatedRegenRate()
	{
		return saturatedRegenRate;
	}

	public void setSaturatedRegenRate(int saturatedRegenRate)
	{
		this.saturatedRegenRate = saturatedRegenRate;
	}

	public int getUnsaturatedRegenRate()
	{
		return unsaturatedRegenRate;
	}

	public void setUnsaturatedRegenRate(int unsaturatedRegenRate)
	{
		this.unsaturatedRegenRate = unsaturatedRegenRate;
	}

	public int getStarvationRate()
	{
		return starvationRate;
	}

	public void setStarvationRate(int starvationRate)
	{
		this.starvationRate = starvationRate;
	}

	public float getExhaustion()
	{
		return exhaustion;
	}

	public void setExhaustion(float exhaustion)
	{
		this.exhaustion = exhaustion;
	}

	public LinkedHashMap<Attribute, Double> getAttributes()
	{
		return attributes;
	}

	public void setAttributes(LinkedHashMap<Attribute, Double> attributes)
	{
		this.attributes = attributes;
	}

	public double getHealth()
	{
		return health;
	}

	public void setHealth(double health)
	{
		this.health = health;
	}

	public double getAbsorptionAmount()
	{
		return absorptionAmount;
	}

	public void setAbsorptionAmount(double absorptionAmount)
	{
		this.absorptionAmount = absorptionAmount;
	}

	public float getExpTowardsNextLevel()
	{
		return expTowardsNextLevel;
	}

	public void setExpTowardsNextLevel(float expTowardsNextLevel)
	{
		this.expTowardsNextLevel = expTowardsNextLevel;
	}

	public int getExpLevel()
	{
		return expLevel;
	}

	public void setExpLevel(int expLevel)
	{
		this.expLevel = expLevel;
	}

	public float getWalkSpeed()
	{
		return walkSpeed;
	}

	public void setWalkSpeed(float walkSpeed)
	{
		this.walkSpeed = walkSpeed;
	}

	public float getFlySpeed()
	{
		return flySpeed;
	}

	public void setFlySpeed(float flySpeed)
	{
		this.flySpeed = flySpeed;
	}

	public int getFireTicks()
	{
		return fireTicks;
	}

	public void setFireTicks(int fireTicks)
	{
		this.fireTicks = fireTicks;
	}

	public int getFreezeTicks()
	{
		return freezeTicks;
	}

	public void setFreezeTicks(int freezeTicks)
	{
		this.freezeTicks = freezeTicks;
	}

	public boolean isGlowing()
	{
		return glowing;
	}

	public void setGlowing(boolean glowing)
	{
		this.glowing = glowing;
	}

	public boolean isGravity()
	{
		return gravity;
	}

	public void setGravity(boolean gravity)
	{
		this.gravity = gravity;
	}

	public ArrayList<PotionEffect> getActiveEffects()
	{
		return activeEffects;
	}

	public void setActiveEffects(ArrayList<PotionEffect> activeEffects)
	{
		this.activeEffects = activeEffects;
	}

	public EntityCategory getEntityCategory()
	{
		return entityCategory;
	}

	public void setEntityCategory(EntityCategory entityCategory)
	{
		this.entityCategory = entityCategory;
	}

	public int getArrowsInBody()
	{
		return arrowsInBody;
	}

	public void setArrowsInBody(int arrowsInBody)
	{
		this.arrowsInBody = arrowsInBody;
	}

	public int getMaximumAir()
	{
		return maximumAir;
	}

	public void setMaximumAir(int maximumAir)
	{
		this.maximumAir = maximumAir;
	}

	public int getRemainingAir()
	{
		return remainingAir;
	}

	public void setRemainingAir(int remainingAir)
	{
		this.remainingAir = remainingAir;
	}

	public String getCustomName()
	{
		return customName;
	}

	public void setCustomName(String customName)
	{
		this.customName = customName;
	}

	public ArrayList<PersistentData> getPersistentData()
	{
		return persistentData;
	}

	public void setPersistentData(ArrayList<PersistentData> persistentData)
	{
		this.persistentData = persistentData;
	}

	@Override
	public boolean create(Connection conn, String tablename)
	{
		try
		{
			String sql = "INSERT INTO `" + tablename
					+ "`(`state_name`,"
					+ " `synchro_key`, `game_mode`,"
					+ " `inventory_content`, `armor_content`, `off_hand`, `enderchest_content`,"
					+ " `food_level`, `saturation`, `saturated_regen_rate`, `unsaturated_regen_rate`,"
					+ " `starvation_rate`, `exhaustion`, `attributes`, `health`, `absorption_amount`,"
					+ " `exp_towards_next_level`, `exp_level`,"
					+ " `walk_speed`, `fly_speed`, `fire_ticks`, `freeze_ticks`, `glowing`, `gravity`,"
					+ " `potion_effects`, `entity_category`, `arrows_in_body`, `maximum_air`, `remaining_air`, `custom_name`,"
					+ " `persistent_data`) " 
					+ "VALUES(?, "
					+ "?, ?, "
					+ "?, ?, ?, ?, "
					+ "?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, "
					+ "?, ?, "
					+ "?, ?, ?, ?, ?, ?, "
					+ "?, ?, ?, ?, ?, ?, "
					+ "?)";
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getStateName());
	        ps.setString(2, getSynchroKey());
	        ps.setString(3, getGameMode().toString());
	        ps.setString(4, MIM.getPlugin().getBase64Api().toBase64Array(getInventoryStorageContents()));
	        ps.setString(5, MIM.getPlugin().getBase64Api().toBase64Array(getArmorContents()));
	        ps.setString(6, MIM.getPlugin().getBase64Api().toBase64(getOffHand()));
	        ps.setString(7, MIM.getPlugin().getBase64Api().toBase64Array(getEnderchestContents()));
	        ps.setInt(8, getFoodLevel());
	        ps.setFloat(9, getSaturation());
	        ps.setInt(10, getSaturatedRegenRate());
	        ps.setInt(11, getUnsaturatedRegenRate());
	        ps.setInt(12, getStarvationRate());
	        ps.setFloat(13, getExhaustion());
	        StringBuilder at = new StringBuilder();
	        for(Entry<Attribute, Double> e : getAttributes().entrySet())
	        {
	        	at.append(e.getKey().toString()+";"+e.getValue().doubleValue()+"@");
	        }
	        ps.setString(14, at.toString());
	        ps.setDouble(15, getHealth());
	        ps.setDouble(16, getAbsorptionAmount());
	        ps.setFloat(17, getExpTowardsNextLevel());
	        ps.setInt(18, getExpLevel());
	        ps.setFloat(19, getWalkSpeed());
	        ps.setFloat(20, getFlySpeed());
	        ps.setInt(21, getFireTicks());
	        ps.setInt(22, getFreezeTicks());
	        ps.setBoolean(23, isGlowing());
	        ps.setBoolean(24, isGravity());
	        ps.setString(25, MIM.getPlugin().getBase64Api().toBase64Array(getActiveEffects().toArray(new PotionEffect[getActiveEffects().size()])));
	        ps.setString(26, getEntityCategory().toString());
	        ps.setInt(27, getArrowsInBody());
	        ps.setInt(28, getMaximumAir());
	        ps.setInt(29, getRemainingAir());
	        ps.setString(30, getCustomName());
	        StringBuilder pd = new StringBuilder();
	        for(PersistentData per : getPersistentData())
	        {
	        	pd.append(per.getNamespaced()+";"+per.getKey()+";"+per.getPersistentType().toString()+";"+per.getPersistentValue()+"@");
	        }
	        ps.setString(31, pd.toString());
	        
	        int i = ps.executeUpdate();
	        MysqlHandler.addRows(MysqlHandler.QueryType.INSERT, i);
	        return true;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not create a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public boolean update(Connection conn, String tablename, String whereColumn, Object... whereObject)
	{
		try 
		{
			String sql = "UPDATE `" + tablename
				+ "` SET `state_name` = ?,"
				+ " `synchro_key` = ?, `game_mode` = ?,"
				+ " `inventory_content` = ?, `armor_content` = ?, `off_hand` = ?, `enderchest_content` = ?,"
				+ " `food_level` = ?, `saturation` = ?, `saturated_regen_rate` = ?, `unsaturated_regen_rate` = ?,"
				+ " `starvation_rate` = ?, `exhaustion` = ?, `attributes` = ?, `health` = ?, `absorption_amount` = ?,"
				+ " `exp_towards_next_level` = ?, `exp_level` = ?,"
				+ " `walk_speed` = ?, `fly_speed` = ?, `fire_ticks` = ?, `freeze_ticks` = ?, `glowing` = ?, `gravity` = ?,"
				+ " `potion_effects` = ?, `entity_category` = ?, `arrows_in_body` = ?, `maximum_air` = ?, `remaining_air` = ?, `custom_name` = ?,"
				+ " `persistent_data` = ?" 
				+ " WHERE "+whereColumn;
			PreparedStatement ps = conn.prepareStatement(sql);
	        ps.setString(1, getStateName());
	        ps.setString(2, getSynchroKey());
	        ps.setString(3, getGameMode().toString());
	        
	        ps.setString(4, MIM.getPlugin().getBase64Api().toBase64Array(getInventoryStorageContents()));
	        ps.setString(5, MIM.getPlugin().getBase64Api().toBase64Array(getArmorContents()));
	        ps.setString(6, MIM.getPlugin().getBase64Api().toBase64(getOffHand()));
	        ps.setString(7, MIM.getPlugin().getBase64Api().toBase64Array(getEnderchestContents()));
	        
	        ps.setInt(8, getFoodLevel());
	        ps.setFloat(9, getSaturation());
	        ps.setInt(10, getSaturatedRegenRate());
	        ps.setInt(11, getUnsaturatedRegenRate());
	        
	        ps.setInt(12, getStarvationRate());
	        ps.setFloat(13, getExhaustion());
	        StringBuilder at = new StringBuilder();
	        for(Entry<Attribute, Double> e : getAttributes().entrySet())
	        {
	        	at.append(e.getKey().toString()+";"+e.getValue().doubleValue()+"@");
	        }
	        ps.setString(14, at.toString());
	        ps.setDouble(15, getHealth());
	        ps.setDouble(16, getAbsorptionAmount());
	        
	        ps.setFloat(17, getExpTowardsNextLevel());
	        ps.setInt(18, getExpLevel());
	        
	        ps.setFloat(19, getWalkSpeed());
	        ps.setFloat(20, getFlySpeed());
	        ps.setInt(21, getFireTicks());
	        ps.setInt(22, getFreezeTicks());
	        ps.setBoolean(23, isGlowing());
	        ps.setBoolean(24, isGravity());
	        
	        ps.setString(25, MIM.getPlugin().getBase64Api().toBase64Array(getActiveEffects().toArray(new PotionEffect[getActiveEffects().size()])));
	        ps.setString(26, getEntityCategory().toString());
	        ps.setInt(27, getArrowsInBody());
	        ps.setInt(28, getMaximumAir());
	        ps.setInt(29, getRemainingAir());
	        ps.setString(30, getCustomName());
	        StringBuilder pd = new StringBuilder();
	        for(PersistentData per : getPersistentData())
	        {
	        	pd.append(per.getNamespaced()+";"+per.getKey()+";"+per.getPersistentType().toString()+";"+per.getPersistentValue()+"@");
	        }
	        ps.setString(31, pd.toString());
			int i = 32;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}			
			int u = ps.executeUpdate();
			MysqlHandler.addRows(MysqlHandler.QueryType.UPDATE, u);
			return true;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not update a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return false;
	}

	@Override
	public ArrayList<Object> get(Connection conn, String tablename, String orderby, String limit, String whereColumn, Object... whereObject)
	{
		try
		{
			String sql = "SELECT * FROM `" + tablename
				+ "` WHERE "+whereColumn+" ORDER BY "+orderby+limit;
			PreparedStatement ps = conn.prepareStatement(sql);
			int i = 1;
			for(Object o : whereObject)
			{
				ps.setObject(i, o);
				i++;
			}
			
			ResultSet rs = ps.executeQuery();
			MysqlHandler.addRows(MysqlHandler.QueryType.READ, rs.getMetaData().getColumnCount());
			ArrayList<Object> al = new ArrayList<>();
			while (rs.next()) 
			{
				al.add(new PredefinePlayerState(
						rs.getInt("id"),
						rs.getString("synchro_key"),
						GameMode.valueOf(rs.getString("game_mode")),
						rs.getString("state_name"),
						rs.getString("inventory_content"),
						rs.getString("armor_content"),
						rs.getString("off_hand"),
						rs.getString("enderchest_content"),
						rs.getInt("food_level"),
						rs.getFloat("saturation"),
						rs.getInt("saturated_regen_rate"),
						rs.getInt("unsaturated_regen_rate"),
						rs.getInt("starvation_rate"),
						rs.getFloat("exhaustion"),
						rs.getString("attributes"),
						rs.getDouble("health"),
						rs.getDouble("absorption_amount"),
						rs.getFloat("exp_towards_next_level"),
						rs.getInt("exp_level"),
						rs.getFloat("walk_speed"),
						rs.getFloat("fly_speed"),
						rs.getInt("fire_ticks"),
						rs.getInt("freeze_ticks"),
						rs.getBoolean("glowing"),
						rs.getBoolean("gravity"),
						rs.getString("potion_effects"),
						EntityCategory.valueOf(rs.getString("entity_category")),
						rs.getInt("arrows_in_body"),
						rs.getInt("maximum_air"),
						rs.getInt("remaining_air"),
						rs.getString("custom_name"),
						rs.getString("persistent_data")
						));
			}
			return al;
		} catch (SQLException e)
		{
			this.log(Level.WARNING, "SQLException! Could not get a "+this.getClass().getSimpleName()+" Object!", e);
		}
		return new ArrayList<>();
	}
	
	public static ArrayList<PlayerData> convert(ArrayList<Object> arrayList)
	{
		ArrayList<PlayerData> l = new ArrayList<>();
		for(Object o : arrayList)
		{
			if(o instanceof PlayerData)
			{
				l.add((PlayerData) o);
			}
		}
		return l;
	}	
}