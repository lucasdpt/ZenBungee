package fr.zencrafting.bungee.server;

import fr.zencrafting.bungee.ZenBungee;
import fr.zencrafting.common.Server;
import fr.zencrafting.common.ServerType;
import fr.zencrafting.common.tech.rabbit.RabbitConnection;
import lombok.Getter;
import net.md_5.bungee.api.config.ServerInfo;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

public class ServersManager {

    private RabbitConnection rabbitConnection;
    private PacketsHandler packetsHandler;
    @Getter
    public HashMap<String, Server> serversList;

    public ServersManager(RabbitConnection rabbitConnection) throws IOException {
        this.rabbitConnection = rabbitConnection;
        this.packetsHandler = new PacketsHandler(this, rabbitConnection);
        this.serversList = new HashMap<String, Server>();

        this.packetsHandler.handlePlayersPackets();
        this.packetsHandler.handleServersPackets();
    }

    public ArrayList<Server> getOnlineLobbies() {
        ArrayList<Server> onlineLobbies = new ArrayList<>();
        for (Server server : serversList.values()) {
            if (server.getServerType().equals(ServerType.LOBBY) && server.isOnline()) {
                onlineLobbies.add(server);
            }
        }
        return onlineLobbies;
    }

    public ServerInfo getRandomLobby() {
        ArrayList<Server> onlineLobbies = getOnlineLobbies();

        for (Server server : onlineLobbies) {
            ServerInfo serverInfo = ZenBungee.getInstance().getProxy().getServerInfo(server.getName());
            if (serverInfo == null) {
                return null;
            }
            if (serverInfo.getPlayers().size() < 20) {
                return serverInfo;
            }
        }
        return ZenBungee.getInstance().getProxy().getServerInfo(onlineLobbies.get(0).getName());
    }

}
