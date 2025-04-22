package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
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
        int countCardsInRow = 3;
        float worldHeight = (float) ((deck.get(0).getTexture().getHeight() + indent) * deck.size()) /countCardsInRow;
        viewport = new StretchViewport(2400, worldHeight);
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2,viewport.getWorldHeight() / 2 , 0);
        viewport.getCamera().update();
        viewport.apply();
    }
    private void showButtonBack(){
        float width = 100;
        float height = 100;
        buttonBackRectangle = new Rectangle(
            viewport.getWorldWidth()-width-40,
            viewport.getWorldHeight()-height-40,
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
        buttonBackDraw();
        deckDraw();
        batch.end();
    }
    private void buttonBackDraw(){
        batch.draw(buttonBackTexture,buttonBackRectangle.x,buttonBackRectangle.y,
            buttonBackRectangle.width,buttonBackRectangle.height);
    }
    private void deckDraw(){
        int indent = 20;
        int countCardsInRow = 10;
        float beginPositionRowX = 100;
        float beginPositionRowY = viewport.getWorldHeight()-deck.get(0).getTexture().getHeight();
        float differenceBetweenRows =  deck.get(0).getTexture().getHeight() + indent;
        int positionInRow = 1;
        int row = 0;
        for(int i = 0; i<deck.size(); i++){
            batch.draw(deck.get(i).getTexture(),(beginPositionRowX+indent)*positionInRow,beginPositionRowY+differenceBetweenRows*row);
            positionInRow ++;
            if(positionInRow > countCardsInRow){
                positionInRow = 0;
                row++;
            }
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

            nextScreen.show();
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
