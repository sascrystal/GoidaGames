package io.github.some_example_name.cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;


public abstract class PlayingCard {
    protected  String name,description;
    protected int cost;
    protected boolean burnable = false, ethereal = false;
    protected  Texture texture = new Texture(Gdx.files.internal("cards/noDataCard.png"));
    protected Sound soundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/Zaglushka.wav"));
    protected TextureAtlas frames = new TextureAtlas(Gdx.files.internal("animationCards/gravity.atlas"));
    protected Animation<TextureRegion> effect = new Animation<>(1/15F,
        frames.findRegions("Gravity-Sheet"),
        Animation.PlayMode.NORMAL);


    public  void cardAction(Enemy x, Player y, int index){
        y.useManaForCard(this);
        soundEffect.play(0.7f);

    }

    public void drawAnimation(float time, Batch batch,Enemy enemy){
        TextureRegion currentFrame = effect.getKeyFrame(time, false);
        batch.draw(currentFrame, enemy.getBounds().x,enemy.getBounds().y,
            enemy.getBounds().width, enemy.getBounds().height);
    }

    public void draw(SpriteBatch batch, float x, float y, float width, float height){
        batch.draw(texture, x,y,width,height);
    }
    public void  draw(SpriteBatch batch, float x, float y){
        batch.draw(texture, x,y);
    }

    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public  Texture getTexture() {
        return texture;
    }

    public  String getDescription() {
        return description;
    }

    public boolean isBurnable() {
        return burnable;
    }

    public boolean isEthereal() {
        return ethereal;
    }

    public Animation<TextureRegion> getEffect() {
        return effect;
    }
}

//CardAttackList





