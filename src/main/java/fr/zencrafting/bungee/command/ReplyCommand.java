package fr.zencrafting.bungee.command;

import lombok.Getter;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;

import java.util.HashMap;

public class ReplyCommand extends Command {

    @Getter
    private static HashMap<ProxiedPlayer, ProxiedPlayer> conversationList = new HashMap<ProxiedPlayer, ProxiedPlayer>();

    public ReplyCommand() {
        super("reply", null, "r");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
            if(getConversationList().containsKey(proxiedPlayer)) {
                if(args.length == 0) {
                    proxiedPlayer.sendMessage(new TextComponent(ChatColor.GRAY+"Aide "+ChatColor.DARK_GRAY+">> "+ChatColor.WHITE+"/reply <message>"));
                } else {
                    StringBuilder bc = new StringBuilder();
                    for(String part : args) {
                        bc.append(part + " ");
                    }
                    String message = bc.toString();
                    ProxiedPlayer proxiedTarget = getConversationList().get(proxiedPlayer);
                    if(proxiedTarget != null) {
                        proxiedPlayer.sendMessage(new TextComponent(ChatColor.GOLD+"Moi "+ChatColor.GRAY+"-> "+ChatColor.WHITE+proxiedTarget.getName()+" "+ChatColor.DARK_GRAY+">> "+ChatColor.WHITE+message));
                        proxiedTarget.sendMessage(new TextComponent(ChatColor.WHITE+proxiedPlayer.getName()+" "+ChatColor.GRAY+"-> "+ChatColor.GOLD+"Moi "+ChatColor.DARK_GRAY+">> "+ChatColor.WHITE+message));
                    } else {
                        proxiedPlayer.sendMessage(new TextComponent(ChatColor.GRAY+"Aide "+ChatColor.DARK_GRAY+">> "+ChatColor.RED+args[0]+" n'est plus en ligne."));
                    }
                    if(ReplyCommand.getConversationList().containsKey(proxiedPlayer)) {
                        ReplyCommand.getConversationList().remove(proxiedPlayer);
                    }
                }
            } else {
                proxiedPlayer.sendMessage(new TextComponent(ChatColor.GRAY+"Aide "+ChatColor.DARK_GRAY+">> "+ChatColor.WHITE+"Vous n'avez personne à qui répondre."));
            }
        } else {
            sender.sendMessage(new TextComponent(ChatColor.GRAY+"Aide "+ChatColor.DARK_GRAY+">> "+ChatColor.RED+"Cette action est réservée aux joueurs."));
        }

    }

}
