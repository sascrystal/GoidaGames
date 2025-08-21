package io.github.some_example_name.cell_map_classes.cell_maps;

import static io.github.some_example_name.screens.GameScreen.viewport;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;

import io.github.some_example_name.cell_map_classes.events.DialogEvent;
import io.github.some_example_name.cell_map_classes.stage.Stage;
import io.github.some_example_name.player.Player;
import io.github.some_example_name.screens.MapScreen;

public abstract class CellMap {
    protected Rectangle bounds;
    protected Texture texture;
    protected boolean isPlayerIn = false, isAvailable = true;


    public static CellMap[][] generateAct1(Player player) {
        Random random = new Random();
        random.nextInt();
        int wight = generateWight(random);
        int height = 7;
        CellMap[][] map = new CellMap[height][wight];
        generateMainLine(map);
        setPlayer(map, player);
        generateMiniBossesAct1(map, random);
        generateTailOfAct1(map);
        generateSideBranches(map, random);
        return map;
    }

    private static int generateWight(Random random) {
        int min = 10;
        int max = 16;
        return random.nextInt(max - min) + min;
    }

    private static void setPlayer(CellMap[][] map, Player player) {
        int center = map.length / 2;
        player.setCellX(0);
        player.setCellY(center);
        player.setxOnScreen(map[center][0]);
        player.setyOnScreen(map[center][0]);
        map[center][0].setPlayerIn(true);
    }
    private static void setPlayer(CellMap cellMap, Player player,int x, int y) {
        player.setCellX(x);
        player.setCellY(y);
        player.setxOnScreen(cellMap);
        player.setyOnScreen(cellMap);
        cellMap.setPlayerIn(true);
    }

    private static void generateMainLine(CellMap[][] map) {
        int center = map.length / 2;
        int cellWight = 150;
        map[center][0] = new EmptyCell(0, (viewport.getWorldHeight() / 2) - (float) cellWight / 2);
        for (int i = 1; i < map[center].length; i++) {
            map[center][i] = new EmptyCell(map[center][i - 1].bounds.getX() + cellWight,
                (viewport.getWorldHeight() / 2) - (float) cellWight / 2);
        }
    }

    private static void generateMiniBossesAct1(CellMap[][] map, Random random) { //TODO: разобраться почему тут math.random()*2 и сделать Stage.generateFightMiniBossesAct1()
        int center = map.length / 2;
        int MiniBoss1 = (int) (Math.random() * 2) + 3;
        map[center][MiniBoss1] = new FightCell(Stage.generateFightAct1(), map[center][MiniBoss1]);
        int MiniBoss2 = (int) (Math.random() * 2) + MiniBoss1 + 3;
        map[center][MiniBoss2] = new FightCell(Stage.generateFightAct1(), map[center][MiniBoss2]);
    }

    private static void generateTailOfAct1(CellMap[][] map) {
        int center = map.length / 2;
        int wight = map[center].length;

        map[center][wight - 1] = new ExitCell(1, map[center][wight - 1].getBounds());
        map[center][wight - 2] = new FightCell(Stage.generateFightAct1(), map[center][wight - 2]);
        //TODO: заменить потом на Stage.generateFightBossesAct1()
    }

    private static void generateSideBranches(CellMap[][] map, Random random) {
        int center = map.length / 2;
        for (int i = 0; !(map[center][i] instanceof ExitCell); i++) {
            if (map[center][i] instanceof FightCell) {
                continue;
            }
            generateUpBranch(map, i, random);
            generateDownBranch(map, i, random);
        }
    }

    private static void generateUpBranch(CellMap[][] map, int index, Random random) {
        int center = map.length / 2;
        int max = 4;
        int rand = random.nextInt(max);
        for (int i = 1; i <= rand; i++) {
            map[center - i][index] = new EmptyCell(map[center - i + 1][index].getBounds().getX(),
                map[center - i + 1][index].getBounds().getY() + map[center - i + 1][index].getBounds().getHeight());
            fillCell(map, center - i, index, random);
        }
    }

    private static void generateDownBranch(CellMap[][] map, int index, Random random) {
        int center = map.length / 2;
        int max = 4;
        int rand = random.nextInt(max);
        for (int i = 1; i <= rand; i++) {
            map[center + i][index] = new EmptyCell(map[center + i - 1][index].getBounds().getX(),
                map[center + i - 1][index].getBounds().getY() - map[center + i - 1][index].getBounds().getHeight());
            fillCell(map, center + i, index, random);
        }
    }

    private static void fillCell(CellMap[][] map, int i, int j, Random random) {
        int max = 4;
        int randomFill = random.nextInt(max);
        if (randomFill == 2) {
            map[i][j] = new FightCell(Stage.generateFightAct1(), map[i][j]);
        } else if (randomFill == 1) {
            map[i][j] = new EventCell(DialogEvent.generateEventAct1(), map[i][j]);
        }
    }

    public static CellMap[][] createTrainingAct(Player player) {
        CellMap[][] trainingMap = new CellMap[4][13];
        player.setCellX(0);
        player.setCellY(1);
        int cellWight = 150;
        float center = (viewport.getWorldHeight() / 2) - (float) cellWight / 2;
        trainingMap[1][0] = new EmptyCell(0, center);
        trainingMap[1][1] = new FightCellTutorial(Stage.generateFightAct1(), new EmptyCell(trainingMap[1][0], "Right"),"dialogues/tutorial_dialog_box_GameScreen.json");
        trainingMap[1][2] = new EmptyCell(trainingMap[1][1], "Right");
        trainingMap[1][3] = new FightCell(Stage.generateFightAct1(), new EmptyCell(trainingMap[1][2], "Right"));
        trainingMap[1][4] = new EmptyCell(trainingMap[1][3], "Right");
        trainingMap[1][5] = new EventCell(DialogEvent.generateEventAct1(), new EmptyCell(trainingMap[1][4], "Right"));
        trainingMap[1][6] = new FightCell(Stage.generateFightAct1(), new EmptyCell(trainingMap[1][5], "Right"));
        trainingMap[0][6] = new EmptyCell(trainingMap[1][6], "Up");
        trainingMap[2][6] = new EmptyCell(trainingMap[1][6], "Down");
        trainingMap[0][7] = new EmptyCell(trainingMap[0][6], "Right");
        trainingMap[0][8] = new EventCell(DialogEvent.generateEventAct1(), new EmptyCell(trainingMap[0][7], "Right"));
        trainingMap[0][9] = new EmptyCell(trainingMap[0][8], "Right");
        trainingMap[0][10] = new FightCell(Stage.generateFightAct1(), new EmptyCell(trainingMap[0][9], "Right"));
        trainingMap[0][4] = new EventCell(DialogEvent.generateEventAct1(), new EmptyCell(trainingMap[1][4], "Up"));
        trainingMap[2][4] = new FightCell(Stage.generateFightAct1(), new EmptyCell(trainingMap[1][4], "Down"));
        trainingMap[3][4] = new EmptyCell(trainingMap[2][4], "Down");
        trainingMap[3][5] = new EventCell(DialogEvent.generateEventAct1(), new EmptyCell(trainingMap[3][4], "Right"));
        trainingMap[3][6] = new FightCell(Stage.generateFightAct1(), new EmptyCell(trainingMap[3][5], "Right"));
        trainingMap[3][7] = new EmptyCell(trainingMap[3][6], "Right");
        trainingMap[3][8] = new EventCell(DialogEvent.generateEventAct1(), new EmptyCell(trainingMap[3][7], "Right"));
        trainingMap[3][9] = new EmptyCell(trainingMap[3][8], "Right");
        trainingMap[3][10] = new EventCell(DialogEvent.generateEventAct1(), new EmptyCell(trainingMap[3][9], "Right"));
        trainingMap[2][10] = new EmptyCell(trainingMap[3][10], "Up");
        trainingMap[1][10] = new EmptyCell(trainingMap[2][10], "Up");
        trainingMap[1][11] = new EmptyCell(trainingMap[1][10], "Right");
        trainingMap[1][12] = new FightCell(Stage.generateFightAct1(), new EmptyCell(trainingMap[1][11], "Right"));
        setPlayer(trainingMap[1][0], player,0,1);
        return trainingMap;
    }

    abstract public void action(MapScreen map);

    public void draw(SpriteBatch batch, float delta) {
        batch.draw(texture, bounds.getX(), bounds.getY(), bounds.getWidth(), bounds.getHeight());
    }

    public Rectangle getBounds() {
        return bounds;
    }

    public boolean isPlayerIn() {
        return isPlayerIn;
    }

    public void setPlayerIn(boolean playerIn) {
        isPlayerIn = playerIn;
    }

    public boolean isAvailable() {
        return isAvailable;
    }
}


