package io.github.some_example_name;

import com.badlogic.gdx.Gdx;

public class Stage {
    public static void BeginnerLevelBegin(Player player){
        Enemy enemy;
        enemy = new EnemyGhost();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemy,player));
    }
    public static void HamsterCombat(Player player){
        Enemy enemy;
        enemy = new EnemyHamster();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemy,player));
    }
    public static void levelGambler(Player player){
        Enemy enemy = new EnemyGambler();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemy,player));
    }
    public static void levelProgrammer(Player player){
        Enemy enemy = new EnemyProgrammer();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemy,player));
    }
}

