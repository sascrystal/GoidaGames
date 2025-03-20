package io.github.some_example_name;

import com.badlogic.gdx.Gdx;

public class Stage {
    public static void BeginnerLevelBegin(Player player){
        Enemy[] enemies = new Enemy[3];
        enemies[0] = new EnemyGhost();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemies,player));
    }
    public static void HamsterCombat(Player player){
        Enemy[] enemies = new Enemy[3];
        enemies[0] = new EnemyHamster();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemies,player));
    }
    public static void levelGambler(Player player){
        Enemy[] enemies = new Enemy[3];
        enemies[0] = new EnemyGambler();
        enemies[1] = new EnemyHamster();

        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemies,player));
    }
    public static void levelProgrammer(Player player){
        Enemy[] enemies = new Enemy[3];
        enemies[0] = new EnemyProgrammer();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new GameScreen(enemies,player));
    }
}

