package fr.zencrafting.bungee.event;

import net.md_5.bungee.api.ServerPing;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.event.ProxyPingEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class ProxyPing implements Listener {

    @EventHandler
    public void onProxyPing(ProxyPingEvent event) {
        ServerPing sp = event.getResponse();

        String line1 = "§a§lZen§2§lCrafting §7- §6Mini-jeux §7et §bSurvie §c[1.8-1.19]";
        String line2 = "§dOuverture prochainement...";
        sp.setDescriptionComponent(new TextComponent(line1+"\n"+line2));

        event.setResponse(sp);
    }

}
