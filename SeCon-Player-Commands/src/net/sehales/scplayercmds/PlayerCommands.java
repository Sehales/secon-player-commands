package net.sehales.scplayercmds;

import java.util.Arrays;

import net.sehales.secon.SeCon;
import net.sehales.secon.addon.SeConAddonManager;
import net.sehales.secon.addon.SeConCommand;
import net.sehales.secon.annotations.SeConCommandHandler;
import net.sehales.secon.config.LanguageHelper;
import net.sehales.secon.exception.CommandNotFoundException;
import net.sehales.secon.player.SeConPlayer;
import net.sehales.secon.utils.ChatUtils;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

public class PlayerCommands {

	private PlayerCmdCollection pc;
	private ChatUtils           chat = SeCon.getAPI().getChatUtils();
	private PCUtils             utils;

	PlayerCommands(PlayerCmdCollection pc, PCUtils utils) {
		this.pc = pc;
		this.utils = utils;
	}

	@SeConCommandHandler(name = "chat", help = "<darkaqua>chat as another player;<darkaqua>/chat [player] Hello!", permission = "secon.command.chat", aliases = "chatas,forcechat")
	public void onChatCommand(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
				return;
			}
			p.chat(chat.getStringOfArray(args, 1));
		} else
			chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "checkvisibility", help = "<darkaqua>check the invisiblity state of a player;<darkaqua>usage: /checkvisibility [player]", permission = "secon.command.checkvisibility", aliases = "isvisible,isinvisible,ishidden")
	public void onCheckVisibilityCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
				return;
			}

			if (pc.getInvManager().isHidden(p))
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("invisiblity.not-visible-msg").replace("<player>", p.getName()));
			else
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("invisiblity.not-invisible-msg").replace("<player>", p.getName()));
		}
	}

	@SeConCommandHandler(name = "closeview", help = "<darkaqua>close the inventory (view) of a player;<darkaqua>usage: /closeview [player]", permission = "secon.command.closeview", aliases = "closeinventory")
	public void onCloseViewCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
				return;
			}
			p.closeInventory();
		} else
			chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "enchantmenttable", help = "<darkaqua>open an enchantmenttable for you or for another player;<darkaqua>usage: /enchantmenttable [player]", additionalPerms = "other:secon.command.enchantmenttable.other", permission = "secon.command.enchantmenttable", aliases = "et,etable,enchanttable")
	public void onEnchantmentTableCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}
				p.openEnchanting(null, true);
			}

		} else if (sender instanceof Player)
			((Player) sender).getPlayer().openEnchanting(null, true);
	}

	@SeConCommandHandler(name = "enderchest", help = "<darkaqua>open your enderchest or the enderchest of another player;<darkaqua>usage: /enderchest [player]", additionalPerms = "other:secon.command.enderchest.other", permission = "secon.command.enderchest", aliases = "ec")
	public void onEnderchestCmd(Player sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}
				sender.openInventory(p.getEnderChest());
			}

		} else
			sender.openInventory(sender.getEnderChest());
	}

	@SeConCommandHandler(name = "feed", help = "<darkaqua>feed yourself or another player;<darkaqua>usage: /feed [player]", additionalPerms = "other:secon.command.feed.other", permission = "secon.command.feed")
	public void onFeedCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}
				p.setFoodLevel(20);
				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("feed.fed-msg").replace("<sender>", sender.getName()));
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("feed.sender-fed-msg").replace("<player>", p.getName()));
			}
		} else if (sender instanceof Player) {
			Player p = ((Player) sender).getPlayer();
			p.setSaturation(20);
			chat.sendFormattedMessage(p, pc.getLanguageInfoNode("feed.fed-msg").replace("<sender>", sender.getName()));
		}
	}

	@SeConCommandHandler(name = "flymode", help = "<darkaqua>enable or disable flymode for you or another player;<darkaqua>usage: /flymode [player]", additionalPerms = "other:secon.command.flymode.other", permission = "secon.command.flymode", aliases = "fly,togglefly")
	public void onFlymodeCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}
				utils.changeFlyMode(sender, p);
			}
		} else if (sender instanceof Player) {
			Player p = ((Player) sender).getPlayer();
			utils.changeFlyMode(p, p);
		}
	}

	@SeConCommandHandler(name = "flyspeed", help = "<darkaqua>modify your flyspeed or the flyspeed of another player;<darkaqua>usage: /flyspeed [player] 10", additionalPerms = "other:secon.command.flyspeed.other", permission = "secon.command.flyspeed", aliases = "fspeed")
	public void onFlySpeedCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (args.length > 1) {
				if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
					Player p = Bukkit.getPlayer(args[0]);
					if (p == null) {
						chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
						return;
					}
					float f = utils.parseSpeedValue(args[1]);
					p.setFlySpeed(f);

					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("flyspeed.changed").replace("<player>", p.getName()).replace("<value>", args[1]));
				}
			} else if (sender instanceof Player) {
				Player p = ((Player) sender).getPlayer();
				float f = utils.parseSpeedValue(args[0]);
				p.setFlySpeed(f);

				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("flyspeed.changed").replace("<player>", p.getName()).replace("<value>", args[0]));
			}
		} else
			chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "gamemode", help = "<darkaqua>change the gamemode of yourself or another player;<darkaqua>usage: /gamemode [player] [survival|creative|adventure|0|1|2];<darkaqua>/gamemode [survival|creative|...]", additionalPerms = "other:secon.command.gamemode.other", permission = "secon.command.gamemode", aliases = "gm")
	public void onGameModeCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			Player p;
			String value;
			if (args.length > 1) {
				if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
					p = Bukkit.getPlayer(args[0]);
					if (p == null) {
						chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
						return;
					}
					value = args[1];
					try {
						GameMode mode = utils.parseGameMode(value);
						utils.setGameMode(mode, sender, p);
					} catch (IllegalArgumentException e) {
						chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);

					}
				}
			} else if (sender instanceof Player) {
				p = ((Player) sender).getPlayer();
				value = args[0];
				try {
					GameMode mode = utils.parseGameMode(value);
					utils.setGameMode(mode, p, p);
				} catch (IllegalArgumentException e) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
				}
			}
		} else if (sender instanceof Player) {
			Player p = ((Player) sender).getPlayer();
			switch (p.getGameMode()) {
				case SURVIVAL: {
					utils.setGameMode(GameMode.CREATIVE, p, p);
					break;
				}
				default: {
					utils.setGameMode(GameMode.SURVIVAL, p, p);
					break;
				}
			}
		}
	}

	@SeConCommandHandler(name = "heal", help = "<darkaqua>heal yourself or another player;<darkaqua>usage: /heal [player]", additionalPerms = "other:secon.command.heal.other", permission = "secon.command.heal")
	public void onHealCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}
				p.setHealth(p.getMaxHealth());
				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("heal.healed-msg").replace("<sender>", sender.getName()));
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("heal.sender-healed-msg").replace("<player>", sender.getName()));
			}
		} else if (sender instanceof Player) {
			Player p = ((Player) sender).getPlayer();
			p.setHealth(p.getMaxHealth());
			chat.sendFormattedMessage(p, pc.getLanguageInfoNode("heal.healed-msg").replace("<sender>", sender.getName()));
		}
	}

	@SeConCommandHandler(name = "hide", help = "<darkaqua>make yourself or another player invisible;<darkaqua>usage: /hide [player]", permission = "secon.command.hide", additionalPerms = "other:secon.command.hide.other")
	public void onHideCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}

				if (pc.getInvManager().hide(p)) {
					chat.sendFormattedMessage(p, pc.getLanguageInfoNode("invisiblity.invisible-msg").replace("<sender>", sender.getName()));
					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("invisiblity.sender-invisible-msg").replace("<player>", sender.getName()));
				} else
					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("invisiblity.not-visible-msg").replace("<player>", p.getName()));
			}
		} else if (sender instanceof Player) {
			Player p = ((Player) sender).getPlayer();
			if (pc.getInvManager().hide(p))
				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("invisiblity.invisible-msg").replace("<sender>", sender.getName()));
			else
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("invisiblity.not-visible-msg").replace("<player>", p.getName()));
		}
	}

	@SeConCommandHandler(name = "invisibility", help = "<darkaqua>make yourself or another player invisible;<darkaqua>usage: /invisibility [player]", aliases = "inv,vanish", permission = "secon.command.invisibility", additionalPerms = "other:secon.command.invisibility.other")
	public void onInvisibilityCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}

				if (pc.getInvManager().toggleInvisibility(p)) {
					chat.sendFormattedMessage(p, pc.getLanguageInfoNode("invisiblity.invisible-msg").replace("<sender>", sender.getName()));
					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("invisiblity.sender-invisible-msg").replace("<player>", sender.getName()));
				} else {
					chat.sendFormattedMessage(p, pc.getLanguageInfoNode("invisiblity.visible-msg").replace("<sender>", sender.getName()));
					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("invisiblity.sender-visible-msg").replace("<player>", sender.getName()));
				}
			}
		} else if (sender instanceof Player) {
			Player p = ((Player) sender).getPlayer();
			if (pc.getInvManager().toggleInvisibility(p))
				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("invisiblity.invisible-msg").replace("<sender>", sender.getName()));
			else
				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("invisiblity.visible-msg").replace("<sender>", sender.getName()));
		}
	}

	@SeConCommandHandler(name = "mute", help = "<darkaqua>mute another player;<darkaqua>usage: /mute [player]", permission = "secon.command.mute")
	public void onMuteCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
				return;
			}

			SeConPlayer scp = SeCon.getAPI().getPlayerManager().getPlayer(args[0]);

			if (scp.hasData("muted"))
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("mute.already-muted").replace("<player>", p.getName()));
			else {
				scp.setData("muted", true);
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("mute.sender-muted-msg").replace("<player>", p.getName()));
				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("mute.muted-msg").replace("<sender>", sender.getName()));
			}
		}
	}

	@SeConCommandHandler(name = "nick", help = "<darkaqua>change your name or the name of another player;<darkaqua>usage: /nick [player] newName", additionalPerms = "other:secon.command.nick.other", permission = "secon.command.nick", aliases = "playername")
	public void onNickCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (args.length > 1) {
				if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
					Player p = Bukkit.getPlayer(args[0]);
					if (p == null) {
						chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
						return;
					}
					String name = chat.formatMessage(args[1]);
					if (name.length() > 16) {
						chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("nick.too-long"));
						return;
					}
					p.setDisplayName(name);
					p.setPlayerListName(name);

					SeConPlayer scp = SeCon.getAPI().getPlayerManager().getPlayer(p.getName());
					scp.setData("listname", name);
					scp.setData("displayname", name);

					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("nick.sender-changed-msg").replace("<player>", p.getName()).replace("<nick>", p.getDisplayName()));
					chat.sendFormattedMessage(p, pc.getLanguageInfoNode("nick.changed-msg").replace("<sender>", sender.getName()).replace("<nick>", p.getDisplayName()));
				}
			} else if (sender instanceof Player) {
				Player p = ((Player) sender).getPlayer();
				String name = chat.formatMessage(args[0]);
				if (name.length() > 16) {
					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("nick.too-long"));
					return;
				}
				p.setDisplayName(name);
				p.setPlayerListName(name);

				SeConPlayer scp = SeCon.getAPI().getPlayerManager().getPlayer(p.getName());
				scp.setData("listname", name);
				scp.setData("displayname", name);

				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("nick.changed-msg").replace("<sender>", p.getName()).replace("<nick>", p.getDisplayName()));
			}
		} else
			chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "resetnick", help = "<darkaqua>reset your name or the name of another player;<darkaqua>usage: /resetnick [player]", additionalPerms = "other:secon.command.resetnick.other", permission = "secon.command.resetnick", aliases = "resetname")
	public void onResetNickCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}
				String name = p.getName();
				p.setDisplayName(name);
				p.setPlayerListName(name);

				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("nick.sender-reset-msg").replace("<player>", p.getName()));
				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("nick.reset-msg").replace("<sender>", sender.getName()));
			}
		} else if (sender instanceof Player) {
			Player p = ((Player) sender).getPlayer();
			String name = p.getName();
			p.setDisplayName(name);
			p.setPlayerListName(name);

			chat.sendFormattedMessage(p, pc.getLanguageInfoNode("nick.reset-msg").replace("<sender>", p.getName()).replace("<nick>", p.getDisplayName()));
		}
	}

	@SeConCommandHandler(name = "show", help = "<darkaqua>make yourself or another player visible;<darkaqua>usage: /show [player]", permission = "secon.command.show", additionalPerms = "other:secon.command.show.other")
	public void onShowCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}

				if (pc.getInvManager().hide(p)) {
					chat.sendFormattedMessage(p, pc.getLanguageInfoNode("invisiblity.visible-msg").replace("<sender>", sender.getName()));
					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("invisiblity.sender-visible-msg").replace("<player>", sender.getName()));
				} else
					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("invisiblity.not-invisible-msg").replace("<player>", p.getName()));
			}
		} else if (sender instanceof Player) {
			Player p = ((Player) sender).getPlayer();
			if (pc.getInvManager().hide(p))
				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("invisiblity.visible-msg").replace("<sender>", sender.getName()));
			else
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("invisiblity.not-invisible-msg").replace("<player>", p.getName()));
		}
	}

	@SeConCommandHandler(name = "slay", help = "<darkaqua>kill another player or yourself;<darkaqua>usage: /slay [player]", permission = "secon.command.slay", additionalPerms = "other:secon.command.slay.other", aliases = "kill")
	public void onSlayCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}
				p.damage(Integer.MAX_VALUE);
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("slay.player-killed-msg").replace("<player>", p.getName()));
			}

		} else if (sender instanceof Player)
			((Player) sender).getPlayer().damage(Integer.MAX_VALUE);
	}

	@SeConCommandHandler(name = "sudo", help = "<darkaqua>execute a command as another player or as console;<darkaqua>usage: /sudo -u [player] say Hello!;<darkaqua>usage: /sudo say Hello!", additionalPerms = "console:secon.command.sudo.console,user:secon.command.sudo.user", permission = "secon.command.sudo", aliases = "executeas,exec,runas,force")
	public void onSudoCommand(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (args[0].equalsIgnoreCase("-u") && SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("user"), true)) {
				if (args.length > 2) {
					Player p = Bukkit.getPlayer(args[1]);
					if (p == null) {
						chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[1]));
						return;
					}
					SeConCommand command = null;
					if (SeConAddonManager.containsCommand(args[2]))
						try {
							command = SeConAddonManager.getCommand(args[2]);
						} catch (CommandNotFoundException e) {
						}
					else if (SeConAddonManager.containsCmdAlias(args[2]))
						try {
							command = SeConAddonManager.getCmdAlias(args[2]);
						} catch (CommandNotFoundException e1) {
						}
					if (command != null)
						command.execute(p, Arrays.copyOfRange(args, 3, args.length));
					else
						p.performCommand(chat.getStringOfArray(args, 2));
				} else
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
			} else if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("console"), true)) {
				SeConCommand command = null;
				if (SeConAddonManager.containsCommand(args[0]))
					try {
						command = SeConAddonManager.getCommand(args[0]);
					} catch (CommandNotFoundException e) {
					}
				if (SeConAddonManager.containsCmdAlias(args[0]))
					try {
						command = SeConAddonManager.getCmdAlias(args[0]);
					} catch (CommandNotFoundException e1) {
					}
				if (command != null)
					command.execute(Bukkit.getConsoleSender(), Arrays.copyOfRange(args, 1, args.length));
				else
					Bukkit.dispatchCommand(Bukkit.getConsoleSender(), chat.getStringOfArray(args, 0));
			}
		} else
			chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "unmute", help = "<darkaqua>unmute another player;<darkaqua>usage: /unmute [player]", permission = "secon.command.unmute")
	public void onUnmuteCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			Player p = Bukkit.getPlayer(args[0]);
			if (p == null) {
				chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
				return;
			}

			SeConPlayer scp = SeCon.getAPI().getPlayerManager().getPlayer(args[0]);

			if (scp.hasData("muted")) {
				scp.removeData("muted");
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("mute.sender-unmuted-msg").replace("<player>", p.getName()));
				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("mute.unmuted-msg").replace("<sender>", sender.getName()));
			} else
				chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("mute.not-muted").replace("<player>", p.getName()));
		}
	}

	//	@SeConCommandHandler(name = "virtualchest", help = "<darkaqua>open your virtual double chest or the virtual double chest of another player;<darkaqua>usage: /virtualchest [player]", aliases = "vchest", additionalPerms = "other:secon.command.virtualchest.other", permission = "secon.command.virtualchest", type = CommandType.PLAYER)
	public void onVirtualChestCmd(Player player, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(player, cmd.getPermission("other"), true)) {
				String name = args[0];
				if (name.length() < 16) {
					Inventory inv = pc.getVirtualChestHandler().getVirtualChest(name);
					player.openInventory(inv);
					pc.getVirtualChestHandler().addOpenInv(inv, name);
				}
			}
		} else
			player.openInventory(pc.getVirtualChestHandler().getVirtualChest(player.getName()));
	}

	@SeConCommandHandler(name = "walkspeed", help = "<darkaqua>modify your walkspeed or the walkspeed of another player;<darkaqua>usage: /walkspeed [player] 10", additionalPerms = "other:secon.command.walkspeed.other", permission = "secon.command.walkspeed", aliases = "wspeed")
	public void onWalkSpeedCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (args.length > 1) {
				if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
					Player p = Bukkit.getPlayer(args[0]);
					if (p == null) {
						chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
						return;
					}
					float f = utils.parseSpeedValue(args[1]);
					p.setWalkSpeed(f);

					chat.sendFormattedMessage(sender, pc.getLanguageInfoNode("walkspeed.changed").replace("<player>", p.getName()).replace("<value>", args[1]));
				}
			} else if (sender instanceof Player) {
				Player p = ((Player) sender).getPlayer();
				float f = utils.parseSpeedValue(args[0]);
				p.setWalkSpeed(f);

				chat.sendFormattedMessage(p, pc.getLanguageInfoNode("walkspeed.changed").replace("<player>", p.getName()).replace("<value>", args[0]));
			}
		} else
			chat.sendFormattedMessage(sender, LanguageHelper.INFO_WRONG_ARGUMENTS);
	}

	@SeConCommandHandler(name = "workbench", help = "<darkaqua>open a workbench for you or another player;<darkaqua>usage: /workbench [player]", additionalPerms = "other:secon.command.workbench.other", permission = "secon.command.workbench", aliases = "wb,openworkbench")
	public void onWorkbenchCmd(CommandSender sender, SeConCommand cmd, String[] args) {
		if (args.length > 0) {
			if (SeCon.getAPI().getSeConUtils().hasPermission(sender, cmd.getPermission("other"), true)) {
				Player p = Bukkit.getPlayer(args[0]);
				if (p == null) {
					chat.sendFormattedMessage(sender, LanguageHelper.INFO_PLAYER_NOT_EXIST.replace("<player>", args[0]));
					return;
				}
				p.openWorkbench(null, true);
			}

		} else if (sender instanceof Player)
			((Player) sender).getPlayer().openWorkbench(null, true);
	}
}
