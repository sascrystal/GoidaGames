package io.github.some_example_name;

import static io.github.some_example_name.GameScreen.viewport;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;

public abstract class CellMap {
    protected Rectangle bounds;
    protected Texture texture ;
    protected boolean isPlayerIn = false, isAvailable = true;

    abstract public  void  action(MapScreen map);

    public void draw(SpriteBatch batch,float delta){
        batch.draw(texture,bounds.getX(),bounds.getY(), bounds.getWidth(),bounds.getHeight());
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
        int height = 7;
        CellMap[][] map = new CellMap[height][wight];
        int center = height/2;
        map[center][0] = new EmptyCell(0, (viewport.getWorldHeight()/2)-75);
        for(int i = 1; i<wight; i++){
            map[center][i] = new EmptyCell(map[center][i-1].bounds.getX()+150,(viewport.getWorldHeight()/2)-75);
        }
        player.setCellX(0);
        player.setCellY(center);
        map[center][0].setPlayerIn(true);
        map[center][wight-1] = new ExitCell(1,map[center][wight-1].getBounds());
        int MiniBoss1 = (int) (Math.random()*2) + 3;
        map[center][MiniBoss1] = new FightCell(FightCell.STAGES[0],map[center][MiniBoss1]);
        int MiniBoss2 = (int) (Math.random()*2) + MiniBoss1+3;
        map[center][MiniBoss2] = new FightCell(FightCell.STAGES[0],map[center][MiniBoss2]);
        return  map;
    }
}

class  EmptyCell extends CellMap {
    public EmptyCell(float x, float y) {
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        bounds = new Rectangle(x,y,150,150);
    }


    public void action(MapScreen map) {}
}

class FightCell extends CellMap{
    private final Stage stage;
    public static final Stage[] STAGES;
    private static final Animation<TextureRegion> ANIMATION_MARK;

    static {
        STAGES = new Stage[1];
        Enemy[] enemies = new Enemy[3];
        enemies[0] = new EnemyGambler();
        STAGES[0] = new Stage(enemies);
        //Выше делать stage которые будут в игре

        TextureAtlas frames = new TextureAtlas(Gdx.files.internal("cell/mark.atlas"));
        ANIMATION_MARK = new Animation<>(0.1f,
            frames.findRegions("Mark"),
            Animation.PlayMode.LOOP);
    }

    public FightCell(float x, float y, Stage stage) {
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        bounds = new Rectangle(x,y,150,150);
        this.stage = stage;
    }

    public FightCell(Stage stage, CellMap cell) {
        this.stage = stage;
        texture = new Texture(Gdx.files.internal("cell/emptyCell.png"));
        this.bounds = cell.bounds;
    }

    @Override
    public void action(MapScreen map) {
        isAvailable = false;
        stage.stageAction(map);
    }

    @Override
    public void draw(SpriteBatch batch,float elapsedTime) {
        super.draw(batch,elapsedTime);
        if(isAvailable){
            TextureRegion currentFrame = ANIMATION_MARK.getKeyFrame(elapsedTime, true);
            batch.draw(currentFrame,
                bounds.getX()+75-32,
                bounds.getY());
        }
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

