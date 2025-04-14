package io.github.some_example_name.enemy_classes.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;


import io.github.some_example_name.enemy_classes.enemy_moves.AttackEnemy;
import io.github.some_example_name.enemy_classes.enemy_moves.MoveEnemy;
import io.github.some_example_name.buffs.HamsterBuff;
import io.github.some_example_name.screens.GameScreen;

public class EnemyHamster extends Enemy {
    public EnemyHamster() {

        // Загрузка текстуры спрайт-листа
        TextureAtlas frames = new TextureAtlas(Gdx.files.internal("enemies/hamster.atlas"));
        animation = new Animation<>(0.3f,
            frames.findRegions("hmstr_sprite"),
            Animation.PlayMode.LOOP);

        Texture texture = new Texture(Gdx.files.internal("enemies/hamster.png"));

        int FRAMES = 3;
        // Установка границ
        bounds = new Rectangle(
            (float) (GameScreen.viewport.getWorldWidth() / 2.4),
            GameScreen.viewport.getWorldHeight() / 3,
            (float) (((double) texture.getWidth() / FRAMES) / 2.2),
            (float) (texture.getHeight() / 2.2));


        health = 70; // Установка здоровья
        moveList = new MoveEnemy[1];// Установка массива возможностей моба
        giveBuff(new HamsterBuff());

        moveList[0] = new AttackEnemy(4);
    }

}
