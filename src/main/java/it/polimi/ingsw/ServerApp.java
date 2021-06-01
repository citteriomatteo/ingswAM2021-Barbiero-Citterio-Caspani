package it.polimi.ingsw;

import it.polimi.ingsw.network.server.Server;

import static it.polimi.ingsw.jsonUtilities.Preferences.readPortFromJSON;

/**
 * Main class for starting Server, if there is an argument the server will use that port,
 * otherwise he will use the default one saved in the json file
 */
public class ServerApp {

    public static void main(String[] args) {
        int portNumber;
        if (args.length==1)
            portNumber = Integer.parseInt(args[0]);
        else
            portNumber = readPortFromJSON();

        Server gameServer = new Server(portNumber);
        gameServer.startServer();
    }
}
