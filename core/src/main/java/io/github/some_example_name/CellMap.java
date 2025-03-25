package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

abstract class CellMap {
    protected Rectangle bounds;
    protected Texture texture ;
    protected boolean isPlayerIn = false;

    abstract public  void  action(Player player);
    public void draw(SpriteBatch batch){
        batch.draw(texture,bounds.getX(),bounds.getY());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public Texture getTexture() {
        return texture;
    }


    public void setPlayerIn(boolean playerIn) {
        isPlayerIn = playerIn;
    }

    public boolean isPlayerIn() {
        return isPlayerIn;
    }
}

class  EmptyCell extends CellMap {
    public EmptyCell() {
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        bounds = new Rectangle(0,0,100,100);
    }

    public EmptyCell(float x, float y) {
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        bounds = new Rectangle(x,y,100,100);

    }


    public void action(Player player) {}
}

class VoidCell extends EmptyCell {
    public VoidCell() {
        texture = new Texture(Gdx.files.internal("cell/voidCell.png"));
        bounds = new Rectangle(0,0,100,100);
    }

    public VoidCell(float x, float y) {
        texture = new Texture(Gdx.files.internal("cell/voidCell.png"));
        bounds = new Rectangle(x,y,100,100);
    }
}
class FightCell extends CellMap{
    private Stage stage;
    public FightCell(float x, float y, Stage stage) {
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        bounds = new Rectangle(x,y,100,100);
        this.stage = stage;
    }

    @Override
    public void action(Player player) {
        stage.stageAction(player);

    }
}

