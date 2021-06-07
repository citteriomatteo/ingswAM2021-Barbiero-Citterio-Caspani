package it.polimi.ingsw.model.essentials;

import javafx.scene.image.Image;

/**
 * This enumeration represents the possible types of resources in the game
 */
public enum ResType {
    UNKNOWN("images/black.png"),
    STONE("images/punchBoard/stone.png"),
    SHIELD("images/punchBoard/shield.png"),
    SERVANT("images/punchBoard/servant.png"),
    COIN("images/punchBoard/coin.png");

    private final Image image;

    ResType(String s) {
        image = new Image(ResType.class.getResourceAsStream("/it/polimi/ingsw/view/GUI/" + s));
    }

    public Image asImage(){
        return image;
    }

    public static ResType valueOfImage(Image image){
        for (ResType type : ResType.values())
            if(type.asImage().equals(image))
                return type;

        return null;
    }
}
