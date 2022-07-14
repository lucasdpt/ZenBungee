package fr.zencrafting.bungee.server;

import com.rabbitmq.client.DeliverCallback;
import fr.zencrafting.bungee.ZenBungee;
import fr.zencrafting.common.Server;
import fr.zencrafting.common.ServerType;
import fr.zencrafting.common.packets.Packets;
import fr.zencrafting.common.tech.rabbit.RabbitConnection;
import fr.zencrafting.common.tech.rabbit.ZenQueues;
import fr.zencrafting.utils.ZenLogger;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class PacketsHandler {

    public ServersManager serversManager;
    public RabbitConnection rabbitConnection;

    public PacketsHandler(ServersManager serversManager, RabbitConnection rabbitConnection) {
        this.serversManager = serversManager;
        this.rabbitConnection = rabbitConnection;
    }

    public void handlePlayersPackets() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] args = message.split("@");
            if (args.length < 2)
                return;
            ZenLogger.log("[RABBITMQ] [PLAYERS] RECEIVED PACKET: "+message);
        };
        rabbitConnection.getChannel().basicConsume(ZenQueues.PLAYERS.name(), true, deliverCallback, consumerTag -> { });
    }

    public void handleServersPackets() throws IOException {
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
            String[] args = message.split("@");
            if (args.length < 2)
                return;
            ZenLogger.log("[RABBITMQ] [SERVERS] RECEIVED PACKET: "+message);
            if (args[0].equalsIgnoreCase(Packets.SERVER_START.name()) && args.length == 2) { // SERVER_START@serverName
                String serverName = args[1];
                Server server = serversManager.getServersList().get(serverName);
                if (server == null) {
                    ZenLogger.log(ZenLogger.Level.WARNING, "[RABBITMQ] [SERVERS] SERVER " + serverName + " NOT FOUND !");
                    return;
                }
                server.setOnline(true);
            }
            if (args[0].equalsIgnoreCase(Packets.SERVER_STOP.name()) && args.length == 2) { // SERVER_STOP@serverName
                String serverName = args[1];
                Server server = serversManager.getServersList().get(serverName);
                if (server == null) {
                    ZenLogger.log(ZenLogger.Level.WARNING, "[RABBITMQ] [SERVERS] SERVER " + serverName + " NOT FOUND !");
                    return;
                }
                server.setOnline(false);
            }
            if (args[0].equalsIgnoreCase(Packets.SERVER_REGISTER.name()) && args.length == 5) { // 0@SERVER_REGISTER@serverName@serverHost@serverPort@serverTypeId;
                String serverName = args[1];
                String serverHost = args[2];
                int serverPort = Integer.valueOf(args[3]);
                ServerType serverType = ServerType.getTypeById(Integer.valueOf(args[4]));

                if (serverName.equalsIgnoreCase(System.getenv("SERVER_NAME")))
                    return;
                Server server = new Server(serverName, serverHost, serverPort, serverType, true, "null");
                serversManager.getServersList().put(serverName, server);

                InetSocketAddress inetSocketAddress = new InetSocketAddress(server.getHost(), server.getPort());
                ServerInfo serverInfo = ZenBungee.getInstance().getProxy().constructServerInfo(server.getName(), inetSocketAddress, server.getName(), false);
                ZenBungee.getInstance().getProxy().getServers().put(serverName, serverInfo);
            }
            if (args[0].equalsIgnoreCase(Packets.SERVER_UNREGISTER.name()) && args.length == 2) { // SERVER_UNREGISTER@serverName
                String serverName = args[1];
                Server server = serversManager.getServersList().get(serverName);
                if (server == null) {
                    ZenLogger.log(ZenLogger.Level.WARNING, "[RABBITMQ] [SERVERS] SERVER " + serverName + " NOT FOUND !");
                    return;
                }
                serversManager.getServersList().remove(serverName);
                ZenBungee.getInstance().getProxy().getServers().remove(serverName);
            }
        };
        rabbitConnection.getChannel().basicConsume(ZenQueues.BUNGEECORD.name(), true, deliverCallback, consumerTag -> { });
    }

}
