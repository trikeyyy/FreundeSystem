package ch.trikeyyy;

import ch.trikeyyy.commands.Friend;
import ch.trikeyyy.mysql.MySql;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

/**
 * Created by trikeyyy on 01.11.2015.
 * Date: 07.11.2015
 * Time: 18:15
 */
public class FreundeSystem extends Plugin {

    @Override
    public void onEnable() {

        System.out.println("[FreundeSystem] Plugin aktiviert");
        setup();

    }

    @Override
    public void onDisable() {

        System.out.println("[FreundeSystem] Plugin deaktiviert");

    }

    public void setup() {

        MySql.connect();
        registerCommands();

    }

    public void registerCommands() {

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new Friend("friend"));

    }

}
