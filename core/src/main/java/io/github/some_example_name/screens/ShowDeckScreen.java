package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.List;

import io.github.some_example_name.Main;
import io.github.some_example_name.cards.PlayingCard;
import io.github.some_example_name.player.Player;

public class ShowDeckScreen implements Screen {
    private List<PlayingCard> deck;
    private Screen nextScreen;
    private Texture buttonBackTexture;
    private Rectangle buttonBackRectangle;
    private StretchViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;

    public ShowDeckScreen(List<PlayingCard> deck, Screen nextScreen) {
        this.deck = deck;
        this.nextScreen = nextScreen;
    }

    @Override
    public void show() {

        viewportConfiguration();
        batch = new SpriteBatch();
        batch.setProjectionMatrix(viewport.getCamera().combined);
        showButtonBack();


    }
    private void viewportConfiguration(){
        int indent = 20;
        viewport = new StretchViewport(2080, deck.get(0).getTexture().getHeight());
        viewport.getCamera().position.set(viewport.getWorldWidth()/2,viewport.getWorldHeight()/2, 0);
        viewport.getCamera().update();
        viewport.apply();
    }
    private void showButtonBack(){
        float width = 50;
        float height = 50;
        buttonBackRectangle = new Rectangle(
            viewport.getWorldWidth()-width-100,
            viewport.getWorldHeight()-height,
            width,height);
        buttonBackTexture = new Texture(Gdx.files.internal("HUD/attak.png"));
    }

    @Override
    public void render(float delta) {
        draw(delta);
        //logic();
        input();
    }
    private void draw(float delta){
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        deckDraw();
        buttonBackDraw();
        batch.end();
    }
    private void buttonBackDraw(){
        batch.draw(buttonBackTexture,buttonBackRectangle.x,buttonBackRectangle.y,
            buttonBackRectangle.width,buttonBackRectangle.height);
    }
    private void deckDraw(){
        int indent = 20;
        float beginPositionRowX = 0;
        float beginPositionRowY = 0;
        float width = viewport.getWorldHeight()*((float) deck.get(0).getTexture().getHeight() /deck.get(0).getTexture().getWidth());
        float height;
        for (int i = 0; i<deck.size(); i++){
            batch.draw(deck.get(i).getTexture(), beginPositionRowX +(indent+width)*i,beginPositionRowY,width,viewport.getWorldHeight());
        }
    }
    private void input(){
        if(Gdx.input.isTouched()){
            Vector2 touchPosition = new Vector2(Gdx.input.getX(),Gdx.input.getY());
            viewport.unproject(touchPosition);
            buttonBackInput(touchPosition);
        }
    }
    private void  buttonBackInput(Vector2 touchPosition){
        if(buttonBackRectangle.contains(touchPosition)){
            ((Main) Gdx.app.getApplicationListener()).setScreen(nextScreen);
        }
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width,height);
    }


    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
