package io.github.some_example_name.cell_map_classes.cell_maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.Main;
import io.github.some_example_name.screens.FirstScreen;
import io.github.some_example_name.screens.MapScreen;

public class ExitCell extends CellMap {
    private final int act;
    public ExitCell(int act, CellMap cell) {
        this.act = act;
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        this.bounds = cell.bounds;
    }

    public ExitCell(int act, Rectangle bounds) {
        this.act = act;
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        this.bounds = bounds;
    }

    @Override
    public void action(MapScreen map) {
        map.dispose();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new FirstScreen());
    }
}
