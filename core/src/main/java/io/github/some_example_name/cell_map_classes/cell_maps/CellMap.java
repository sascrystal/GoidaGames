package io.github.some_example_name.cell_map_classes.cell_maps;

import static io.github.some_example_name.screens.GameScreen.viewport;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import io.github.some_example_name.cell_map_classes.events.DialogEvent;
import io.github.some_example_name.screens.MapScreen;

import io.github.some_example_name.cell_map_classes.stage.Stage;
import io.github.some_example_name.player.Player;

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
        Random random = new Random();

        random.nextInt();
        int wight = generateWight(random);
        int height = 7;
        CellMap[][] map = new CellMap[height][wight];

        generateMainLine(map);
        setPlayer(map,player);
        generateMiniBossesAct1(map,random);
        generateTailOfAct1(map);
        generateSideBranches(map,random);

        return  map;
    }

    private static int generateWight(Random random){
        int min = 10;
        int max = 16;
        return  random.nextInt(max-min)+min;
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

    private static void generateMiniBossesAct1(CellMap[][] map,Random random){ //TODO: разобраться почему тут math.random()*2 и сделать Stage.generateFightMiniBossesAct1()
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
        //TODO: заменить потом на Stage.generateFightBossesAct1()
    }

    private static void generateSideBranches(CellMap[][] map, Random random){
        int center = map.length/2;
        for(int i = 0; !(map[center][i] instanceof ExitCell); i++){
            if(map[center][i] instanceof FightCell){
                continue;
            }
            generateUpBranch(map, i,random);
            generateDownBranch(map,i,random);
        }
    }

    private static void generateUpBranch(CellMap[][] map, int index,Random random){
        int center = map.length/2;
        int max = 4;
        int rand = random.nextInt(max);
        for(int i = 1; i<=rand; i++){
            map[center-i][index] = new EmptyCell(map[center-i+1][index].getBounds().getX(),
                map[center-i+1][index].getBounds().getY()+map[center-i+1][index].getBounds().getHeight());
            fillCell(map,center-i,index,random);
        }
    }

    private static void generateDownBranch(CellMap[][] map, int index,Random random){
        int center = map.length/2;
        int max = 4;
        int rand = random.nextInt(max);
        for(int i = 1; i<=rand; i++){
            map[center+i][index] = new EmptyCell(map[center+i-1][index].getBounds().getX(),
                map[center+i-1][index].getBounds().getY()-map[center+i-1][index].getBounds().getHeight());
            fillCell(map,center+i,index,random);
        }
    }

    private static void fillCell(CellMap[][] map, int i, int j,Random random){
        int max = 4;
        int randomFill =random.nextInt(max);
        if(randomFill == 2){
            map[i][j] = new FightCell(Stage.generateFightAct1(),map[i][j]);
        } else if(randomFill == 1){
            map[i][j] = new EventCell(DialogEvent.generateEventAct1(),map[i][j]);
        }
    }
}


