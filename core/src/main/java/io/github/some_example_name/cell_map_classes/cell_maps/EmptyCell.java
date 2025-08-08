package io.github.some_example_name.cell_map_classes.cell_maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.screens.MapScreen;

public class EmptyCell extends CellMap {
    public EmptyCell(float x, float y) {
        int cellWight = 150;
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        bounds = new Rectangle(x, y, cellWight, cellWight);
    }

    public EmptyCell(CellMap cellMap, String direction) {
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        int cellWight = 150;
        switch (direction) {
            case "Up":
                bounds = new Rectangle(cellMap.getBounds().getX(), cellMap.getBounds().getY() + cellWight, cellWight, cellWight);
                break;
            case "Down":
                bounds = new Rectangle(cellMap.getBounds().getX(), cellMap.getBounds().getY() - cellWight, cellWight, cellWight);
                break;
            case "Left":
                bounds = new Rectangle(cellMap.bounds.getX() - cellWight, cellMap.getBounds().getY(), cellWight, cellWight);
                break;
            case "Right":
                bounds = new Rectangle(cellMap.bounds.getX() + cellWight, cellMap.getBounds().getY(), cellWight, cellWight);
                break;
            default:
                Gdx.app.log("E", "misdirection when creating a cell");
                bounds = new Rectangle(0, 0, 150, 150);
                break;
        }
    }


    public void action(MapScreen map) {
    }
}
