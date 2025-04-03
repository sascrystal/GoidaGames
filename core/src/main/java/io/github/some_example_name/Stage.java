package io.github.some_example_name;

import com.badlogic.gdx.Gdx;

public class Stage {
    Enemy[] enemies;

    public Stage(Enemy[] enemies) {
        this.enemies = enemies;
    }

    public void stageAction(MapScreen map){
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemies,map));
    }



}

