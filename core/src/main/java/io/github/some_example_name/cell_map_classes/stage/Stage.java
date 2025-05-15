package io.github.some_example_name.cell_map_classes.stage;

import com.badlogic.gdx.Gdx;

import io.github.some_example_name.Main;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.enemy_classes.enemies.EnemyGambler;
import io.github.some_example_name.enemy_classes.enemies.EnemyHamster;
import io.github.some_example_name.enemy_classes.enemies.SkeletonHalberd;
import io.github.some_example_name.screens.GameScreen;
import io.github.some_example_name.screens.MapScreen;

public class Stage {
    private static final Stage[] STAGES;

    static {
        STAGES = new Stage[3];

        Enemy[] enemies = new Enemy[3];
        int score = 15;
        enemies[0] = new EnemyGambler();
        STAGES[0] = new Stage(enemies, score);


        score = 26;
        enemies = new Enemy[3];
        enemies[0] = new EnemyHamster();
        STAGES[1] = new Stage(enemies, score);
        enemies = new Enemy[3];

        score = 30;
        enemies[0] = new SkeletonHalberd();
        enemies[1] = new SkeletonHalberd();
        STAGES[2] = new Stage(enemies, score);
    }

    private final Enemy[] enemies;
    private final int score;

    public Stage(Enemy[] enemies, int score) {
        this.enemies = enemies;
        this.score = score;
    }

    public static Stage generateFightAct1() {
        int random = (int) (Math.random() * STAGES.length);
        Stage randomStage = STAGES[random];
        returnStageInPool(random);
        return randomStage;
    }

    private static void returnStageInPool(int index) {
        Enemy[] enemies = new Enemy[3];
        int score;
        switch (index) {
            case 0:
                score = 15;
                enemies[0] = new EnemyGambler();
                STAGES[0] = new Stage(enemies, score);
                break;
            case 1:
                score = 26;
                enemies = new Enemy[3];
                enemies[0] = new EnemyHamster();
                STAGES[1] = new Stage(enemies, score);
                break;
            case 2:
                score = 30;
                enemies[0] = new SkeletonHalberd();
                enemies[1] = new SkeletonHalberd();
                STAGES[2] = new Stage(enemies, score);
                break;
        }

    }

    public void stageAction(MapScreen map) {
        map.getPlayer().takeScore(this);
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemies, map));
    }

    public int getScore() {
        return score;
    }


}

