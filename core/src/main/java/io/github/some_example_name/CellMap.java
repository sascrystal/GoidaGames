package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

public abstract class CellMap {
    protected Rectangle bounds;
    protected Texture texture ;
    protected boolean isPlayerIn = false, isAvailable = true;

    abstract public  void  action(MapScreen map);

    public void draw(SpriteBatch batch){
        batch.draw(texture,bounds.getX(),bounds.getY());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public void setPlayerIn(boolean playerIn) {
        isPlayerIn = playerIn;
    }

    public boolean isPlayerIn() {
        return isPlayerIn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}

class  EmptyCell extends CellMap {
    public EmptyCell(float x, float y) {
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        bounds = new Rectangle(x,y,100,100);
    }


    public void action(MapScreen map) {}
}

class FightCell extends CellMap{
    private final Stage stage;
    public FightCell(float x, float y, Stage stage) {
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        bounds = new Rectangle(x,y,100,100);
        this.stage = stage;
    }

    @Override
    public void action(MapScreen map) {
        isAvailable = false;
        stage.stageAction(map);
    }
}

