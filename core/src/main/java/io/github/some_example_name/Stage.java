package io.github.some_example_name;

import com.badlogic.gdx.Gdx;

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

