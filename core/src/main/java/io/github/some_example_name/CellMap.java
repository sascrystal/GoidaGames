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
        int wight = generateWight();
        int height = 7;
        CellMap[][] map = new CellMap[height][wight];

        generateMainLine(map);
        setPlayer(map,player);
        generateMiniBossesAct1(map);
        generateTailOfAct1(map);
        generateSideBranches(map);

        return  map;
    }

    private static int generateWight(){
        int min = 10;
        int max = 16;
        return (int) (Math.random()*(max-min+1)) + min;
    }

    private static void setPlayer(CellMap[][] map, Player player){
        int center = map.length/2;
        player.setCellX(0);
        player.setCellY(center);
        map[center][0].setPlayerIn(true);
    }

    private static void generateMainLine(CellMap[][] map){
        int center = map.length/2;
        int cellWight = 150;
        map[center][0] = new EmptyCell(0, (viewport.getWorldHeight()/2)- (float) cellWight /2);
        for(int i = 1; i<map[center].length; i++){
            map[center][i] = new EmptyCell(map[center][i-1].bounds.getX()+cellWight,
                (viewport.getWorldHeight()/2)-(float) cellWight /2);
        }
    }

    private static void generateMiniBossesAct1(CellMap[][] map){
        int center = map.length/2;
        int MiniBoss1 = (int) (Math.random()*2) + 3;
        map[center][MiniBoss1] = new FightCell(Stage.generateFightAct1(),map[center][MiniBoss1]);
        int MiniBoss2 = (int) (Math.random()*2) + MiniBoss1+3;
        map[center][MiniBoss2] = new FightCell(Stage.generateFightAct1(),map[center][MiniBoss2]);
    }

    private static void generateTailOfAct1(CellMap[][] map){
        int center = map.length/2;
        int wight = map[center].length;

        map[center][wight-1] = new ExitCell(1,map[center][wight-1].getBounds());
        map[center][wight-2] = new FightCell(Stage.generateFightAct1(), map[center][wight-2]);
        //заменить потом на Stage.generateFightBossesAct1()
    }

    private static void generateSideBranches(CellMap[][] map){
        int center = map.length/2;
        for(int i = 0; !(map[center][i] instanceof ExitCell); i++){
            if(map[center][i] instanceof FightCell){
                continue;
            }
            generateUpBranch(map, i);
            generateDownBranch(map,i);
        }
    }

    private static void generateUpBranch(CellMap[][] map, int index){
        int center = map.length/2;
        int min = 0;
        int max = 4;
        int random = (int)(Math.random()*max-min) +min;
        for(int i = 1; i<=random; i++){
            map[center-i][index] = new EmptyCell(map[center-i+1][index].getBounds().getX(),
                map[center-i+1][index].getBounds().getY()+map[center-i+1][index].getBounds().getHeight());
            fillCell(map,center-i,index);
        }
    }

    private static void generateDownBranch(CellMap[][] map, int index){
        int center = map.length/2;
        int min = 0;
        int max = 4;
        int random = (int)(Math.random()*max-min) +min;
        for(int i = 1; i<=random; i++){
            map[center+i][index] = new EmptyCell(map[center+i-1][index].getBounds().getX(),
                map[center+i-1][index].getBounds().getY()-map[center+i-1][index].getBounds().getHeight());
            fillCell(map,center+i,index);
        }
    }

    private static void fillCell(CellMap[][] map, int i, int j){
        int min = 0;
        int max = 3;
        int randomFill = (int)(Math.random()*max-min)+ min;
        if(randomFill == 2){
            map[i][j] = new FightCell(Stage.generateFightAct1(),map[i][j]);
        }
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
    private static final Animation<TextureRegion> ANIMATION_MARK;

    static {
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
        int cellWight = 150;
        super.draw(batch,elapsedTime);
        if(isAvailable){
            TextureRegion currentFrame = ANIMATION_MARK.getKeyFrame(elapsedTime, true);
            batch.draw(currentFrame,
                bounds.getX()+ (float) cellWight /2 -32,
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

