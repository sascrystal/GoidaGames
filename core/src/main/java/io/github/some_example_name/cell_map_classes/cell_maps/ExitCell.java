package io.github.some_example_name.cell_map_classes.cell_maps;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.Main;
import io.github.some_example_name.screens.FirstScreen;
import io.github.some_example_name.screens.MapScreen;

public class ExitCell extends CellMap {
    private final int act;
    private Sprite door;
    private static final float SCALE_FOR_DOOR = 0.15f;
    public ExitCell(int act, CellMap cell) {
        this.act = act;
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        this.bounds = cell.bounds;
        Texture textureDoor = new Texture("map_screen/door.png");
        door = new Sprite(textureDoor);
        door.setSize(door.getWidth()*SCALE_FOR_DOOR, door.getHeight()*SCALE_FOR_DOOR);
        door.setPosition(cell.getBounds().getX()+cell.getBounds().getWidth()/2-door.getWidth()/2, cell.getBounds().getY()+cell.getBounds().getHeight()/2-door.getHeight()/2);
    }

    public ExitCell(int act, Rectangle bounds) {
        this.act = act;
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        this.bounds = bounds;
        Texture textureDoor = new Texture("map_screen/door.png");
        door = new Sprite(textureDoor);
        door.setSize(door.getWidth()*SCALE_FOR_DOOR, door.getHeight()*SCALE_FOR_DOOR);
        door.setPosition(bounds.getX()+bounds.getWidth()/2-door.getWidth()/2, bounds.getY()+bounds.getHeight()/2-door.getHeight()/2);
    }

    @Override
    public void draw(SpriteBatch batch, float delta) {
        super.draw(batch, delta);
        door.draw(batch);

    }

    @Override
    public void action(MapScreen map) {
        map.dispose();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new FirstScreen());
    }
}
