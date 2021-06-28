package it.polimi.ingsw;

import static it.polimi.ingsw.jsonUtilities.Preferences.*;
import static it.polimi.ingsw.network.client.Client.getClient;
import static it.polimi.ingsw.network.client.LocalClient.getLocalClient;
import static it.polimi.ingsw.view.ClientController.getClientController;

/**
 * Main class for starting client, it can handle different arguments:
 * --local sets starts a match played locally
 * --ip/IP/address [address] sets the address to find the server
 * --port [num] sets the port on which the server is listening
 * --cli/CLI chose to start a CLI
 * --gui/GUI chose to start a GUI
 *
 * If an argument is missing, takes it from the json file Preferences.json
 */
public class ClientApp {

    public static void main(String[] args) {
        String hostName = "127.0.0.1";
        int portNumber = 2500;
        boolean online = true;
        boolean cliChoice = false;
        boolean foundIP = false, foundPort = false, foundView = false;
        for (int i = 0; i < args.length; i++) {
            switch(args[i]){
                case "--local":
                    online = false;
                    break;
                case "--ip":
                case "--IP":
                case "--address":
                    online = true;
                    if(i+1 != args.length && !args[i+1].startsWith("-")){
                        hostName = args[i+1];
                        foundIP = true;
                    }
                    else
                        foundIP = false;
                    break;
                case "--port":
                    if(i+1 != args.length && !args[i+1].startsWith("-")){
                        try {
                            portNumber = Integer.parseInt(args[i+1]);
                            foundPort = true;
                        }catch (NumberFormatException e){
                            foundPort = false;
                        }
                    }
                    else
                        foundPort = false;
                    break;
                case "--cli":
                case "--CLI":
                    cliChoice = true;
                    foundView = true;
                    break;
                case "--gui":
                case "--GUI":
                    cliChoice = false;
                    foundView = true;
                    break;
            }
        }

        if(online) {

            if (!foundIP)
                hostName = readHostFromJSON();
            if (!foundPort)
                portNumber = readPortFromJSON();
            if (!foundView)
                cliChoice = readViewFromJSON();

            getClient().setSocket(hostName, portNumber);

            getClient().heartbeat();

            new Thread(() -> getClient().startClient()).start();

            getClient().setView(cliChoice);
        }
        else{

            getClientController().setIsLocal();

            getLocalClient().setView(cliChoice);
        }


        //getClient().terminateConnection();
    }
}
