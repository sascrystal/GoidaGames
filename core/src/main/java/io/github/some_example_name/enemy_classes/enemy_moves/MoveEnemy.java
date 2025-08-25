package io.github.some_example_name.enemy_classes.enemy_moves;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
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


    public void draw(Batch batch, BitmapFont font, float elapsedTime, Enemy enemy, Player player,float x ,float y) {



        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame,
            x,
            y);
        GlyphLayout glyphLayout = new GlyphLayout(font,String.valueOf(showNumericalValue(enemy, player)));
        font.draw(batch, glyphLayout,x+currentFrame.getRegionWidth(),y+ (float) currentFrame.getRegionHeight() /2 + glyphLayout.height/2);
    }
}



