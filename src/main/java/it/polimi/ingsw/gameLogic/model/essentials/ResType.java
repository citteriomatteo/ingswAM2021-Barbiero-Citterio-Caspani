package it.polimi.ingsw.gameLogic.model.essentials;

import javafx.scene.image.Image;

import java.util.Objects;

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

    /**
     * Private constructor, it links every instance of the enumeration class with the correct image
     * @param path the path where to find the image to take
     */
    ResType(String path) {
        image = new Image(Objects.requireNonNull(ResType.class.getResourceAsStream("/it/polimi/ingsw/view/GUI/" + path)));
    }

    /**
     * Returns the image that corresponds to the resource type (for GUI purposes)
     * @return the Image object
     */
    public Image asImage(){
        return image;
    }

    /**
     * This method returns the res type that corresponds to an image object (for GUI).
     * @param image the image
     * @return the res type
     */
    public static ResType valueOfImage(Image image){
        for (ResType type : ResType.values())
            if(type.asImage().equals(image))
                return type;

        return null;
    }
}
