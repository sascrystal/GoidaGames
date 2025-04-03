package io.github.some_example_name;

import static io.github.some_example_name.GameScreen.viewport;

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

    public static CellMap[][] generateAct1(Player player){
        int wight =(int) (Math.random()*10) + 9;
        int height = 6;
        CellMap[][] map = new CellMap[height][wight];
        int center = height/2;
        map[center][0] = new EmptyCell(0, (viewport.getWorldHeight()/2)-100);
        for(int i = 1; i<wight; i++){
            map[center][i] = new EmptyCell(map[center][i-1].bounds.getX()+200,(viewport.getWorldHeight()/2)-100);
        }
        player.setCellX(0);
        player.setCellY(center);
        map[center][0].setPlayerIn(true);
        map[center][wight-1] = new ExitCell(1,map[center][wight-1].getBounds());
        int MiniBoss1 = (int) (Math.random()*2) + 3;
        map[center+1][MiniBoss1] = new EmptyCell(map[center][MiniBoss1].getBounds().getX(),map[center][MiniBoss1].getBounds().getY()+200);
        int MiniBoss2 = (int) (Math.random()*2) + MiniBoss1+3;
        map[center+1][MiniBoss2] = new EmptyCell(map[center][MiniBoss2].getBounds().getX(),map[center][MiniBoss2].getBounds().getY()+200);
        return  map;
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

class ExitCell extends CellMap{
    private final int act;

    public ExitCell(int act,Rectangle bounds) {
        this.act = act;
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        this.bounds = bounds;
    }

    @Override
    public void action(MapScreen map) {

    }
}

