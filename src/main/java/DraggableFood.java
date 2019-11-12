
// https://stackoverflow.com/questions/27080039/proper-way-to-move-a-javafx8-node-around

import javafx.scene.Cursor;
import javafx.scene.image.Image;

class DraggableFood extends javafx.scene.image.ImageView {
    private double mouseX ;
    private double mouseY ;

    DraggableFood(Image image) {
        super(image);

        setOnMousePressed(event -> {
            mouseX = event.getSceneX() ;
            mouseY = event.getSceneY() ;
            setCursor(Cursor.MOVE);
        });

        setOnMouseDragged(event -> {
            double X = event.getSceneX() - mouseX ;
            double Y = event.getSceneY() - mouseY ;
            relocate(getLayoutX() + X, getLayoutY() + Y);
            mouseX = event.getSceneX() ;
            mouseY = event.getSceneY() ;
        });

        setOnMouseReleased(event -> setCursor(Cursor.HAND));
    }

}

