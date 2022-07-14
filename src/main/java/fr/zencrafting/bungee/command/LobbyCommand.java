package fr.zencrafting.bungee.command;

import fr.zencrafting.bungee.ZenBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

public class LobbyCommand extends Command {

    private ZenBungee main;

    public LobbyCommand(ZenBungee main) {
        super("lobby", null, "hub");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
            ServerInfo lobbyInfo = main.getServersManager().getRandomLobby();
            if(lobbyInfo.equals(proxiedPlayer.getServer().getInfo())) {
                proxiedPlayer.sendMessage(new TextComponent(ChatColor.GRAY+""+ChatColor.ITALIC+"Vous êtes déjà sur un lobby."));
            } else {
                proxiedPlayer.connect(lobbyInfo);
            }
        } else {
            sender.sendMessage(new TextComponent("Commande réservée aux joueurs"));
        }
    }

}
