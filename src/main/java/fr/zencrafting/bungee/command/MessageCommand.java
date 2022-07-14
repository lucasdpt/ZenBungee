package fr.zencrafting.bungee.command;

import fr.zencrafting.bungee.ZenBungee;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;

public class MessageCommand extends Command implements TabExecutor {

    private ZenBungee main;

    public MessageCommand(ZenBungee main) {
        super("message", null, "msg","tell");
        this.main = main;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(sender instanceof ProxiedPlayer) {
            ProxiedPlayer proxiedPlayer = (ProxiedPlayer) sender;
            if(args.length <= 1) {
                proxiedPlayer.sendMessage(new TextComponent(ChatColor.GRAY+"Aide "+ChatColor.DARK_GRAY+">> "+"/msg <joueur> <message>"));
            } else {
                StringBuilder bc = new StringBuilder();
                for(String part : args) {
                    if(!(part.equals(args[0]))) {
                        bc.append(part + " ");

                    }
                }
                String message = bc.toString();
                ProxiedPlayer proxiedTarget = main.getProxy().getPlayer(args[0]);
                if(proxiedTarget != null) {
                    proxiedPlayer.sendMessage(new TextComponent(ChatColor.GOLD+"Moi "+ ChatColor.GRAY+"-> "+ChatColor.WHITE+proxiedTarget.getName()+" "+ChatColor.DARK_GRAY+">> "+ChatColor.WHITE+message));
                    proxiedTarget.sendMessage(new TextComponent(ChatColor.WHITE+proxiedPlayer.getName()+" "+ChatColor.GRAY+"-> "+ChatColor.GOLD+"Moi "+ChatColor.DARK_GRAY+">> "+ChatColor.WHITE+message));
                    if(ReplyCommand.getConversationList().containsKey(proxiedPlayer)) {
                        ReplyCommand.getConversationList().remove(proxiedPlayer);
                    }
                    if(ReplyCommand.getConversationList().containsKey(proxiedTarget)) {
                        ReplyCommand.getConversationList().remove(proxiedTarget);
                    }
                    ReplyCommand.getConversationList().put(proxiedPlayer, proxiedTarget);
                    ReplyCommand.getConversationList().put(proxiedTarget, proxiedPlayer);
                } else {
                    proxiedPlayer.sendMessage(new TextComponent(ChatColor.GRAY+"Aide "+ChatColor.DARK_GRAY+">> "+ChatColor.RED+args[0]+" n'est pas en ligne."));
                }
            }
        } else {
            sender.sendMessage(new TextComponent(ChatColor.GRAY+"Aide "+ChatColor.DARK_GRAY+">> "+ChatColor.RED+"Cette action est réservée aux joueurs"));
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        ArrayList<String> completions = new ArrayList<String>();
        if(args.length == 0) {
            for(ProxiedPlayer proxiedPlayer : main.getProxy().getPlayers()) {
                completions.add(proxiedPlayer.getName());
            }
            return completions;
        } else if(args.length == 1) {
            for(ProxiedPlayer proxiedPlayer : main.getProxy().getPlayers()) {
                completions.add(proxiedPlayer.getName());
            }
            return completions;
        } else {
            return completions;
        }
    }

}
