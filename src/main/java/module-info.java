module it.polimi.ingsw {
    requires javafx.controls;
    requires javafx.fxml;
    requires gson;
    requires gson.extras;
    requires annotations;
    requires java.sql;

    // TODO :Continue export all the packages and opens all the packages


    opens it.polimi.ingsw.controller;
    opens it.polimi.ingsw.exceptions;
    opens it.polimi.ingsw.jsonUtilities;

    opens it.polimi.ingsw.model.match;
    opens it.polimi.ingsw.model.match.player;
    opens it.polimi.ingsw.model.match.player.personalBoard;
    opens it.polimi.ingsw.model.match.player.personalBoard.faithPath;
    opens it.polimi.ingsw.model.match.player.personalBoard.warehouse;
    opens it.polimi.ingsw.model.match.market;
    opens it.polimi.ingsw.model.match.token;
    opens it.polimi.ingsw.model.essentials;
    opens it.polimi.ingsw.model.essentials.leader;

    opens it.polimi.ingsw.network.client;
    opens it.polimi.ingsw.network.message;
    opens it.polimi.ingsw.network.message.ctosmessage;
    opens it.polimi.ingsw.network.message.stocmessage;
    opens it.polimi.ingsw.network.server;
    opens it.polimi.ingsw.observer;
    opens it.polimi.ingsw.view;
    opens it.polimi.ingsw.view.GUI;
    opens it.polimi.ingsw.view.CLI;
    opens it.polimi.ingsw.view.observer;
    opens it.polimi.ingsw.view.lightmodel;


    exports it.polimi.ingsw.controller;
    exports it.polimi.ingsw.exceptions;
    exports it.polimi.ingsw.jsonUtilities;

    exports it.polimi.ingsw.model.match;
    exports it.polimi.ingsw.model.match.player;
    exports it.polimi.ingsw.model.match.player.personalBoard;
    exports it.polimi.ingsw.model.match.player.personalBoard.faithPath;
    exports it.polimi.ingsw.model.match.player.personalBoard.warehouse;
    exports it.polimi.ingsw.model.match.market;
    exports it.polimi.ingsw.model.match.token;
    exports it.polimi.ingsw.model.essentials;
    exports it.polimi.ingsw.model.essentials.leader;

    exports it.polimi.ingsw.network.client;
    exports it.polimi.ingsw.network.message;
    exports it.polimi.ingsw.network.message.ctosmessage;
    exports it.polimi.ingsw.network.message.stocmessage;
    exports it.polimi.ingsw.network.server;
    exports it.polimi.ingsw.observer;
    exports it.polimi.ingsw.view;
    exports it.polimi.ingsw.view.GUI;
    exports it.polimi.ingsw.view.CLI;
    exports it.polimi.ingsw.view.observer;
    exports it.polimi.ingsw.view.lightmodel;

}