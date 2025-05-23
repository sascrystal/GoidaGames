package io.github.some_example_name.enemy_classes.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.math.Rectangle;

import io.github.some_example_name.buffs.modifier_buffs.Weakness;
import io.github.some_example_name.enemy_classes.enemy_moves.AttackEnemy;
import io.github.some_example_name.enemy_classes.enemy_moves.DebuffAttackEnemy;
import io.github.some_example_name.enemy_classes.enemy_moves.MoveEnemy;
import io.github.some_example_name.screens.GameScreen;

public class SkeletonHalberd extends Enemy {
    public SkeletonHalberd() {
        TextureAtlas frames = new TextureAtlas(Gdx.files.internal("enemies/skeleton.atlas"));
        animation = new Animation<>(0.2f,
            frames.findRegions("Skeleton Idle"),
            Animation.PlayMode.LOOP);
        stateTime = 0f;
        Texture texture = new Texture(Gdx.files.internal("enemies/skeleton.png"));

        int countFrames  = 12;
        float width = (float) texture.getWidth() /countFrames;
        float height = texture.getHeight();
        float scale = 7;
        bounds = new Rectangle(centerOfGameScreen(width,scale),
            GameScreen.viewport.getWorldHeight() / 3,
            width*scale,
            height*scale);

        health = 30; // Установка здоровья
        moveList = new MoveEnemy[3];// Установка массива возможностей enemy
        moveList[0] = new AttackEnemy(6);
        moveList[1] = new AttackEnemy(8);
        moveList[2] = new DebuffAttackEnemy(3, new Weakness());
    }
}
