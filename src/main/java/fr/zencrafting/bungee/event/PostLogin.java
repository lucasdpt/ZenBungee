package fr.zencrafting.bungee.event;

import fr.zencrafting.bungee.ZenBungee;
import net.md_5.bungee.api.Title;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;

public class PostLogin implements Listener {

    private ZenBungee main;

    public PostLogin(ZenBungee main) {
        this.main = main;
    }

    @EventHandler
    public void onPostLogin(PostLoginEvent event) {
        ProxiedPlayer proxiedPlayer = event.getPlayer();
        proxiedPlayer.setReconnectServer(main.getServersManager().getRandomLobby());
        Title title = main.getProxy().createTitle();
        title.title(new TextComponent("§a§lZen§2§lCrafting"));
        title.subTitle(new TextComponent("§6Mini-Jeux §7- §bSurvie"));
        proxiedPlayer.sendTitle(title);
    }

}
