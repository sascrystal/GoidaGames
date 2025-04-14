package io.github.some_example_name.cell_map_classes.stage;

import com.badlogic.gdx.Gdx;

import io.github.some_example_name.Main;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.enemy_classes.enemies.EnemyGambler;
import io.github.some_example_name.enemy_classes.enemies.EnemyHamster;
import io.github.some_example_name.screens.GameScreen;
import io.github.some_example_name.screens.MapScreen;

public class Stage {
    private final Enemy[] enemies;

    public Stage(Enemy[] enemies) {
        this.enemies = enemies;
    }

    public void stageAction(MapScreen map){
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemies,map));
    }

    public static Stage generateFightAct1(){
        Stage[] STAGES = new Stage[2];

        Enemy[] enemies = new Enemy[3];
        enemies[0] = new EnemyGambler();
        STAGES[0] = new Stage(enemies);

        enemies = new Enemy[3];
        enemies[0] = new EnemyHamster();
        STAGES[1] = new Stage(enemies);

        //выше делаем стейджи

        int random = (int) (Math.random()* STAGES.length);
        return  STAGES[random];
    }



}

