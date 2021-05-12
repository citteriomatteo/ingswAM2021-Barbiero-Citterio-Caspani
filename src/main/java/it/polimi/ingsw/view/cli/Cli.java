package it.polimi.ingsw.view.cli;

import it.polimi.ingsw.model.essentials.FaithPoint;
import it.polimi.ingsw.model.essentials.PhysicalResource;
import it.polimi.ingsw.model.essentials.ResType;
import it.polimi.ingsw.model.essentials.Resource;

import java.awt.*;


public class Cli
{

    public Cli(){
        drawTitle();
    }

    public void drawTitle(){
        StringBuilder str = new StringBuilder();

        str.append(ColorCli.YELLOW_BOLD).append("                        __  __               _____   _______   ______   _____     _____                   \n" +
                "                       |  \\/  |     /\\      / ____| |__   __| |  ____| |  __ \\   / ____|                  \n" +
                "                       | \\  / |    /  \\    | (___      | |    | |__    | |__) | | (___                    \n" +
                "                       | |\\/| |   / /\\ \\    \\___ \\     | |    |  __|   |  _  /   \\___ \\                   \n" +
                "                       | |  | |  / ____ \\   ____) |    | |    | |____  | | \\ \\   ____) |                  \n" +
                "                       |_|  |_| /_/    \\_\\ |_____/_    |_|___ |______| |_|  \\_\\ |_____/                   \n" +
                "                                              / __ \\  |  ____|                                            \n" +
                "                                             | |  | | | |__                                               \n" +
                "                                             | |  | | |  __|                                              \n" +
                "                                             | |__| | | |                                                 \n" +
                "        _____    ______   _   _              _\\____/  |_|__    _____              _   _    _____   ______ \n" +
                "       |  __ \\  |  ____| | \\ | |     /\\     |_   _|  / ____|  / ____|     /\\     | \\ | |  / ____| |  ____|\n" +
                "       | |__) | | |__    |  \\| |    /  \\      | |   | (___   | (___      /  \\    |  \\| | | |      | |__   \n" +
                "       |  _  /  |  __|   | . ` |   / /\\ \\     | |    \\___ \\   \\___ \\    / /\\ \\   | . ` | | |      |  __|  \n" +
                "       | | \\ \\  | |____  | |\\  |  / ____ \\   _| |_   ____) |  ____) |  / ____ \\  | |\\  | | |____  | |____ \n" +
                "       |_|  \\_\\ |______| |_| \\_| /_/    \\_\\ |_____| |_____/  |_____/  /_/    \\_\\ |_| \\_|  \\_____| |______|\n" +
                "                                                                                                          \n" +
                "                                                                                                          ");
        System.out.println(str);
    }

    public void drawResource(PhysicalResource resource){
        StringBuilder str = new StringBuilder();

        if(resource.getType() == ResType.COIN);
            //str.append(ColorCli.RED).append(Graphics2D.drawRect(10,10,10,10));

    }





    public static void main(String args[]){
        Cli cli = new Cli();
    }

}
