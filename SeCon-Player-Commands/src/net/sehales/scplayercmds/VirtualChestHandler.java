// package net.sehales.scplayercmds;
//
// import java.io.IOException;
// import java.sql.SQLDataException;
// import java.util.HashMap;
// import java.util.Map;
//
// import net.sehales.secon.SeCon;
// import net.sehales.secon.exception.DatabaseException;
// import net.sehales.secon.obj.DatabaseResult;
// import net.sehales.secon.obj.ItemStackInfo;
//
// import org.bukkit.Bukkit;
// import org.bukkit.inventory.Inventory;
// import org.bukkit.inventory.ItemStack;
//
// public class VirtualChestHandler {
//
// private Map<String, Inventory> vchests = new HashMap<String, Inventory>();
//
// private Map<Inventory, String> openChests = new HashMap<Inventory, String>();
// private PlayerCmdCollection pc;
//
// VirtualChestHandler(PlayerCmdCollection pc) {
// this.pc = pc;
// }
//
// public void addOpenInv(Inventory inv, String owner) {
// if (!isOpen(inv))
// openChests.put(inv, owner);
// }
//
// private Inventory createChest(String playerName) {
// Inventory chest = Bukkit.createInventory(null, 54,
// SeCon.getAPI().getChatUtils().formatMessage(pc.getConf().getString("virtualchest-name")));
// vchests.put(playerName, chest);
// load(playerName);
// return chest;
// }
//
// public String getOpenInvOwner(Inventory inv) {
// return openChests.get(inv);
// }
//
// public String getTableName() {
// try {
// return
// SeCon.getAPI().getDatabaseUtils().getDefault().tableName("virtualchests");
// } catch (DatabaseException e) {
// e.printStackTrace();
// return "unable to get table-name!";
// }
// }
//
// public Inventory getVirtualChest(String playerName) {
// if (vchests.containsKey(playerName))
// return vchests.get(playerName);
// else
// return createChest(playerName);
// }
//
// void init() throws DatabaseException {
// SeCon.getAPI().getDatabaseUtils().getDefault().write("CREATE TABLE IF NOT EXISTS "
// + getTableName() + "(" +
//
// "`id` INT AUTO_INCREMENT," +
//
// "`name` VARCHAR(16) NOT NULL ," +
//
// "`data` VARCHAR(20000) NULL ," +
//
// "PRIMARY KEY (`id`, `name`) ," +
//
// "UNIQUE INDEX `id_UNIQUE` (`id` ASC) ," +
//
// "UNIQUE INDEX `name_UNIQUE` (`name` ASC) ) " +
//
// "ENGINE = MyISAM " +
//
// "DEFAULT CHARACTER SET utf8 COLLATE utf8_general_ci;");
// }
//
// public boolean isOpen(Inventory inv) {
// return openChests.containsKey(inv);
// }
//
// @SuppressWarnings("unchecked")
// public void load(String playerName) {
// try {
// String serialized = loadFromDb(playerName);
// if (serialized != null) {
// HashMap<Integer, ItemStackInfo> tmpInfo = (HashMap<Integer, ItemStackInfo>)
// SeCon.getAPI().getObjectUtils().deserializeFromString(serialized);
// Inventory chest = getVirtualChest(playerName);
//
// for (int i : tmpInfo.keySet())
// chest.setItem(i, tmpInfo.get(i).createItemStack());
// }
// } catch (IOException e) {
// SeCon.getAPI().getLogger().severe("Player-Cmd-Collection",
// "Cannot load virtual chest! name:" + playerName + " error:" +
// e.getMessage());
// e.printStackTrace();
// } catch (ClassNotFoundException e) {
// e.printStackTrace();
// }
// }
//
// public String loadFromDb(String playerName) {
// try {
// DatabaseResult result =
// SeCon.getAPI().getDatabaseUtils().getDefault().readEnhanced("SELECT * FROM "
// + getTableName() + " WHERE name = ?", playerName);
// if (result != null && result.hasRows())
// try {
// return result.getString(0, "data");
// } catch (SQLDataException e) {
// return null;
// }
// else
// SeCon.getAPI().getDatabaseUtils().getDefault().write("INSERT INTO " +
// getTableName() + "(`name`, `data`) VALUES (?, ?);", playerName, "");
// } catch (DatabaseException e) {
// e.printStackTrace();
// }
// return null;
// }
//
// public void remove(String playerName) {
// save(playerName);
// vchests.remove(playerName);
// }
//
// public void removeOpenInv(Inventory inv) {
// if (isOpen(inv))
// openChests.remove(inv);
// }
//
// public void save(String playerName) {
// Inventory inv = vchests.get(playerName);
// HashMap<Integer, ItemStackInfo> tmpInfo = new HashMap<Integer,
// ItemStackInfo>();
// for (int i = 0; i < 54; i++) {
// ItemStack item = inv.getItem(i);
// if (item != null)
// tmpInfo.put(i, ItemStackInfo.createStackInfo(item));
// }
// try {
// saveToDb(playerName,
// SeCon.getAPI().getObjectUtils().serializeToString(tmpInfo));
// } catch (Exception e) {
// SeCon.getAPI().getLogger().severe("Player-Cmd-Collection",
// "Cannot save virtual chest! name:" + playerName + " error:" +
// e.getMessage());
// e.printStackTrace();
// }
// }
//
// public void saveAll() {
// for (String s : vchests.keySet())
// save(s);
// }
//
// private void saveToDb(String playerName, String data) {
// try {
// SeCon.getAPI().getDatabaseUtils().getDefault().write("UPDATE " +
// getTableName() + " SET `data` = ? WHERE `name` = '" + playerName + "';",
// data);
// } catch (DatabaseException e) {
// SeCon.getAPI().getLogger().severe("Player-Cmd-Collection",
// "Cannot save virtual chest! name:" + playerName + " error:" +
// e.getMessage());
// e.printStackTrace();
// }
// }
// }
