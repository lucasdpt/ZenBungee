package fr.zencrafting.bungee.event;

import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.ServerSwitchEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ServerSwitch implements Listener {

    @EventHandler
    public void onServerSwitch(ServerSwitchEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();
        proxiedPlayer.setTabHeader(new TextComponent("§a§lZen§2§lCrafting"), new TextComponent("§7Serveur: §6"+proxiedPlayer.getServer().getInfo().getName()));
    }

}
