package ch.trikeyyy.commands;

import ch.trikeyyy.friendmanager.FriendManager;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.ArrayList;

/**
 * Created by trikeyyy on 01.11.2015.
 * Date: 07.11.2015
 * Time: 18:26
 */
public class Friend extends Command {

    public Friend(String name) {
        super(name);
    }

    private String prefix = "§8[§6FreundeSystem§8] ";

    @SuppressWarnings("deprecation")
    @Override
    public void execute(CommandSender sender, String[] args) {

        if(sender instanceof ProxiedPlayer) {

            ProxiedPlayer p = (ProxiedPlayer) sender;

            if(args.length == 1) {

                if(args[0].equalsIgnoreCase("help")) {

                    /*
                     * Friend-Help
                     */

                    p.sendMessage("§7-----------[§6Freunde§7]-----------");
                    p.sendMessage("§6/friend add <name> §3Freund hinzufügen");
                    p.sendMessage("§6/friend accept <name> §3Anfrage akzeptieren");
                    p.sendMessage("§6/friend remove <name> §3Freund entfernen");
                    p.sendMessage("§6/friend join <name> §3Freund folgen");
                    p.sendMessage("§6/friend list §3Alle Freunde anzeigen");
                    p.sendMessage("§6/friend help §3Zeigt diese Hilfeseite an");

                } else if(args[0].equalsIgnoreCase("list")) {

                    //Get his friends and add them into List
                    ArrayList<String> list = FriendManager.getFriends(p.getName());

                    if(list.size() == 0) {

                        p.sendMessage(prefix + " §cDu hast keine Freunde :(");

                    } else {

                        //Create both Online and Offline Playerlist
                        ArrayList<String> online = new ArrayList<String>();
                        ArrayList<String> offline = new ArrayList<String>();

                        for(String name : list) {

                            if(ProxyServer.getInstance().getPlayer(name) != null) {

                                //Add online friends
                                online.add(name);

                            } else {

                                //Add offline friends
                                offline.add(name);

                            }

                        }

                        p.sendMessage(prefix + " §aDu hast §6" + list.size() + " §aFreunde, davon §6" + online.size() + " §aOnline und §6" + offline.size() + " §aOffline!");

                        p.sendMessage("§6Online:");
                        if(online.size() == 0) {

                            p.sendMessage(" §3-");

                        } else {

                            for(String name : online) {

                                p.sendMessage(" §a" + name);

                            }

                        }

                        p.sendMessage("§6Offline:");
                        if(offline.size() == 0) {

                            p.sendMessage(" §3-");

                        } else {

                            for(String name : offline) {

                                p.sendMessage(" §7" + name);

                            }

                        }

                    }

                } else {

                    p.sendMessage(prefix + " §cBenutze /friend help");

                }

            } else if(args.length == 2) {

                if(args[0].equalsIgnoreCase("add")) {

                    if(!args[1].equalsIgnoreCase(p.getName())) {

                        if(ProxyServer.getInstance().getPlayer(args[1]) != null) {

                            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[1]);

                            if(!FriendManager.alreadyFriends(p.getName(), player.getName())) {

                                if(FriendManager.bereitsAngefragtWorden(p, player)) {

                                    p.sendMessage(prefix + " §cDu wurdest von diesem Spieler bereits angefragt! Mache /friend accept " + player.getName());

                                } else if(FriendManager.hatBereitsAngefragt(p, player)) {

                                    p.sendMessage(prefix + " §cDu hast diesen Spieler bereits angefragt!");

                                } else {

                                    //Send Friendrequest
                                    p.sendMessage(prefix + " §aEs wurde eine Anfrage an §6" + player.getName() + " §agesendet");
                                    player.sendMessage(prefix + " §aDer Spieler §6" + p.getName() + " §awill mit dir befreundet sein: Mache §6/friend accept " + p.getName() + " §aum seine Anfrage anzunehmen!");
                                    FriendManager.anfragen.put(p.getUUID().toString(), player.getUUID().toString());

                                }

                            } else {

                                p.sendMessage(prefix + " §cDu bist bereits mit diesem Spieler befreundet");

                            }

                        } else {

                            p.sendMessage(prefix + " §cDieser Spieler ist nicht online!");

                        }

                    } else {

                        p.sendMessage(prefix + " §cDu kannst dir selbst keine Anfragen schicken!");

                    }

                } else if(args[0].equalsIgnoreCase("accept")) {

                    if(ProxyServer.getInstance().getPlayer(args[1]) != null) {

                        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[1]);

                        if(FriendManager.bereitsAngefragtWorden(p, player)) {

                            //Insert Data including both Players
                            FriendManager.createFreundschaft(p, player);
                            p.sendMessage(prefix + " §aDu bist nun mit §6" + player.getName() + " §abefreundet :D");
                            player.sendMessage(prefix + " §aDu bist nun mit §6" + p.getName() + " §abefreundet :D");
                            FriendManager.anfragen.remove(p.getUUID().toString());
                            FriendManager.anfragen.remove(player.getUUID().toString());

                        } else {

                            p.sendMessage(prefix + " §cDu wurdest noch nicht von diesem Spieler angefragt!");

                        }

                    } else {

                        p.sendMessage(prefix + " §cDu kannst leider noch keine Spieler accepten, die nicht online sind :(");

                    }

                } else if(args[0].equalsIgnoreCase("remove")) {

                    if(FriendManager.alreadyFriends(p.getName(), args[1])) {

                        //Delete Friendship-Data
                        FriendManager.deleteFreundschaft(p.getName(), args[1]);
                        p.sendMessage(prefix + " §cDu hast die Freundschaft mit §6" + args[1] + " §caufgelöst!");

                        if(ProxyServer.getInstance().getPlayer(args[1]) != null) {

                            ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[1]);
                            player.sendMessage(prefix + " §6" + p.getName() + " §chat die Freundschaft mit dir aufgelöst!");

                        }

                    } else {

                        p.sendMessage(prefix + " §cDu bist mit diesem Spieler nicht befreundet!");

                    }

                } else if(args[0].equalsIgnoreCase("join")) {

                    if(ProxyServer.getInstance().getPlayer(args[1]) != null) {

                        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[1]);

                        if(FriendManager.alreadyFriends(p.getUniqueId().toString(), player.getUniqueId().toString())) {

                            //Get Serverinfo and connect to Friends Server
                            ServerInfo sf = player.getServer().getInfo();
                            p.connect(sf);

                        } else {

                            p.sendMessage(prefix + " §cDu bist mit diesem Spieler nicht befreundet!");

                        }

                    } else {

                        p.sendMessage(prefix + " §cDieser Spieler ist nicht online!");

                    }

                } else if(args[0].equalsIgnoreCase("msg")) {

                    if(ProxyServer.getInstance().getPlayer(args[1]) != null) {

                        ProxiedPlayer player = ProxyServer.getInstance().getPlayer(args[1]);

                        if(FriendManager.alreadyFriends(p.getUniqueId().toString(), player.getUniqueId().toString())) {

                            String msg = "";

                            //Build MSG-String
                            for(int i = 1; i < args.length; i++) {
                                msg = msg + " " + args[i];
                            }

                            player.sendMessage(ChatColor.GREEN + msg);

                        } else {

                            p.sendMessage(prefix + " §cDu bist mit diesem Spieler nicht befreundet!");

                        }

                    } else {

                        p.sendMessage(prefix + " §cBenutze /friend help");

                    }

                } else {

                    p.sendMessage(prefix + " §cBenutze /friend help");

                }

            } else {

                p.sendMessage(prefix + " §cBenutze /friend help");

            }

        } else {

            System.out.println("[Freunde] Du musst ein Spieler sein um das zu machen!");

        }

    }

}
