package fr.zencrafting.bungee;

import com.rabbitmq.client.DeliverCallback;
import fr.zencrafting.bungee.command.LobbyCommand;
import fr.zencrafting.bungee.command.MessageCommand;
import fr.zencrafting.bungee.command.ReplyCommand;
import fr.zencrafting.bungee.event.PostLogin;
import fr.zencrafting.bungee.event.ProxyPing;
import fr.zencrafting.bungee.event.ServerConnect;
import fr.zencrafting.bungee.event.ServerSwitch;
import fr.zencrafting.bungee.server.ServersManager;
import fr.zencrafting.common.packets.Packets;
import fr.zencrafting.common.packets.RabbitPackets;
import fr.zencrafting.common.tech.rabbit.RabbitConnection;
import fr.zencrafting.common.tech.rabbit.ZenQueues;
import fr.zencrafting.utils.ZenLogger;
import lombok.Getter;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.api.plugin.PluginManager;

import java.io.IOException;

public class ZenBungee extends Plugin {

    @Getter private static ZenBungee instance;
    private RabbitConnection rabbitConnection;
    @Getter private ServersManager serversManager;

    @Override
    public void onEnable() {
        instance = this;
        this.rabbitConnection = new RabbitConnection();
        if (!this.rabbitConnection.isConnected()) {
            ZenLogger.log(ZenLogger.Level.SEVERE, "Initialization of RabbitConnection failed");
            return;
        }

        PluginManager pluginManager = getProxy().getPluginManager();
        pluginManager.registerListener(this, new PostLogin(this));
        pluginManager.registerListener(this, new ProxyPing());
        pluginManager.registerListener(this, new ServerConnect(this));
        pluginManager.registerListener(this, new ServerSwitch());

        pluginManager.registerCommand(this, new LobbyCommand((this)));
        pluginManager.registerCommand(this, new MessageCommand(this));
        pluginManager.registerCommand(this, new ReplyCommand());

        try {
            this.serversManager = new ServersManager(this.rabbitConnection);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String packet = Packets.SERVER_START+"@"+"BungeeCord01";
        RabbitPackets.send(packet, this.rabbitConnection, ZenQueues.HARMONY.name());
    }

}
