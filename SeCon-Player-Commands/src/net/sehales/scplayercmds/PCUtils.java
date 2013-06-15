package net.sehales.scplayercmds;

import net.sehales.secon.SeCon;
import net.sehales.secon.utils.ChatUtils;

import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class PCUtils {

	private PlayerCmdCollection pc;

	private ChatUtils           chat = SeCon.getAPI().getChatUtils();

	PCUtils(PlayerCmdCollection pc) {
		this.pc = pc;
	}

	/**
	 * change the ability to fly of a player
	 * 
	 * @param player
	 * @param senderName
	 */
	public void changeFlyMode(CommandSender sender, Player target) {
		if (target.getAllowFlight()) {
			target.setAllowFlight(false);
			target.setFlying(false);
			chat.sendFormattedMessage(target, pc.getLanguageInfoNode("flymode.disabled").replace("<sender>", sender.getName()));
			if (!sender.getName().equals(target.getName()))
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("flymode.sender-msg-disabled").replace("<player>", target.getName()));
		} else if (!target.getAllowFlight()) {
			target.setAllowFlight(true);
			target.setFlying(true);
			chat.sendFormattedMessage(target, pc.getLanguageInfoNode("flymode.enabled").replace("<sender>", sender.getName()));
			if (!sender.getName().equals(target.getName()))
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("flymode.sender-msg-enabled").replace("<player>", target.getName()));
		}
	}

	/**
	 * convert a string into 0, 1 or 2 survival/0 to 0 creative/1 to 1 adventure/2 to 2
	 * 
	 * @param option
	 * @return 0 to 2 for the corresponding GameMode number or -1 if the string does not contains a mode
	 */
	public int convertGameModeStringToInt(String option) {
		int value = -1;
		if (option.equalsIgnoreCase("survival") || option.equalsIgnoreCase("0") || option.equalsIgnoreCase("s"))
			value = 0;
		else if (option.equalsIgnoreCase("creative") || option.equalsIgnoreCase("1") || option.equalsIgnoreCase("c"))
			value = 1;
		else if (option.equalsIgnoreCase("adventure") || option.equalsIgnoreCase("2") || option.equalsIgnoreCase("a"))
			value = 2;
		return value;
	}

	/**
	 * convert a GameMode number into the GameMode enum
	 * 
	 * @param value
	 * @return
	 * @throws IllegalArgumentException
	 */
	public GameMode parseGameMode(int value) throws IllegalArgumentException {
		switch (value) {
			case 0: {
				return GameMode.SURVIVAL;
			}
			case 1: {
				return GameMode.CREATIVE;
			}
			case 2: {
				return GameMode.ADVENTURE;
			}
			default: {
				throw new IllegalArgumentException("GameMode cannot be " + value);
			}
		}
	}

	/**
	 * convert a GameMode containing string into the corresponding GameMode
	 * 
	 * @param value
	 * @return
	 */
	public GameMode parseGameMode(String value) {
		return parseGameMode(convertGameModeStringToInt(value));
	}

	public float parseSpeedValue(String option) {
		int i = Integer.parseInt(option);
		switch (i) {
			case 0: {
				return 0.0f;
			}
			case 1: {
				return 0.1f;
			}
			case 2: {
				return 0.2f;
			}
			case 3: {
				return 0.3f;
			}
			case 4: {
				return 0.4f;
			}
			case 5: {
				return 0.5f;
			}
			case 6: {
				return 0.6f;
			}
			case 7: {
				return 0.7f;
			}
			case 8: {
				return 0.8f;
			}
			case 9: {
				return 0.9f;
			}
			case 10: {
				return 1.0f;
			}
			case -1: {
				return -0.1f;
			}
			case -2: {
				return -0.2f;
			}
			case -3: {
				return -0.3f;
			}
			case -4: {
				return -0.4f;
			}
			case -5: {
				return -0.5f;
			}
			case -6: {
				return -0.6f;
			}
			case -7: {
				return -0.7f;
			}
			case -8: {
				return -0.8f;
			}
			case -9: {
				return -0.9f;
			}
			case -10: {
				return -1.0f;
			}
		}
		return 1.0f;
	}

	/**
	 * set the GameMode of the given player and inform the player
	 * 
	 * @param mode
	 * @param player
	 * @param senderName
	 */
	public void setGameMode(GameMode mode, CommandSender sender, Player target) {
		switch (mode) {
			case SURVIVAL: {
				target.setGameMode(mode);
				chat.sendFormattedMessage(target, pc.getLanguageInfoNode("gamemode.survival").replace("<sender>", sender.getName()));
				if (!sender.getName().equals(target.getName()))
					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("gamemode.sender-msg-survival").replace("<player>", target.getName()));
				break;
			}
			case CREATIVE: {
				target.setGameMode(mode);
				chat.sendFormattedMessage(target, pc.getLanguageInfoNode("gamemode.creative").replace("<sender>", sender.getName()));
				if (!sender.getName().equals(target.getName()))
					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("gamemode.sender-msg-creative").replace("<player>", target.getName()));
				break;
			}
			case ADVENTURE: {
				target.setGameMode(mode);
				chat.sendFormattedMessage(target, pc.getLanguageInfoNode("gamemode.adventure").replace("<sender>", sender.getName()));
				if (!sender.getName().equals(target.getName()))
					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("gamemode.sender-msg-adventure").replace("<player>", target.getName()));
				break;
			}
		}
	}

}
