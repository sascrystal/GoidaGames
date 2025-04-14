package io.github.some_example_name.cell_map_classes.cell_maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.screens.MapScreen;

public class EmptyCell extends CellMap {
    public EmptyCell(float x, float y) {
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        bounds = new Rectangle(x, y, 150, 150);
    }


    public void action(MapScreen map) {
    }
}
