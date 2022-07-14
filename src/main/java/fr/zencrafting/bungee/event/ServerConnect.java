package fr.zencrafting.bungee.event;

import fr.zencrafting.bungee.ZenBungee;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerConnect implements Listener {

    private ZenBungee main;

    public ServerConnect(ZenBungee main) {
        this.main = main;
    }

    @EventHandler
    public void onServerConnect(ServerConnectEvent event) {
        if (event.getTarget().equals(main.getProxy().getServerInfo("default"))) {
            event.setTarget(main.getServersManager().getRandomLobby());
        }
    }

}
