package io.github.some_example_name.enemy_classes.enemy_moves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public abstract class MoveEnemy {
    protected TextureAtlas frames = new TextureAtlas(Gdx.files.internal("enemyMove/questionMark.atlas"));
    protected Animation<TextureRegion> animation = new Animation<>(1 / 3F,
        frames.findRegions("questionMark"), Animation.PlayMode.LOOP);


    public abstract void enemyAction(Enemy x, Player y);

    public abstract String showNumericalValue(Enemy x, Player y);


    public void draw(Batch batch, BitmapFont font, float elapsedTime, Enemy enemy, Player player) {

        font.draw(batch, String.valueOf(showNumericalValue(enemy, player)),
            (enemy.getBounds().getX() + enemy.getBounds().getWidth() + 50),
            enemy.getBounds().getY() + enemy.getBounds().getHeight() + 100);

        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame,
            enemy.getBounds().getX() + enemy.getBounds().getWidth() - 40,
            enemy.getBounds().getY() + enemy.getBounds().getHeight() + 30,
            100, 130);
    }
}



