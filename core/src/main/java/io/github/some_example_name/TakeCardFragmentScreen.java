package io.github.some_example_name;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class TakeCardFragmentScreen implements Screen {
    private final PlayingCard[] givingCards;
    private Rectangle[] givingCardsBoxes;
    private Texture buttonSkip;
    private Rectangle buttonSkipBox;
    private StretchViewport viewport;
    private MapScreen map;

    public TakeCardFragmentScreen(PlayingCard[] givingCards, MapScreen map) {
        this.givingCards = givingCards;
        this.map = map;
    }

    @Override
    public void show() {
        viewportConfiguration();
        buttonSkipBoxShow();
        givingCardsShow();

    }
    private void viewportConfiguration(){
        viewport = new StretchViewport(2400, 1080);
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.getCamera().update();
        viewport.apply();
    }

    private void buttonSkipBoxShow(){
        buttonSkip = new Texture("cell/emptyCell.png");
        buttonSkipBox = new Rectangle(1200,100,200,200);
    }

    private void givingCardsShow(){
        givingCardsBoxes = new Rectangle[3];
        float wightCard = 500;
        float heightCard = 1000;
        givingCardsBoxes[0] = new Rectangle(1200, 400, wightCard, heightCard);
        givingCardsBoxes[1] = new Rectangle(givingCardsBoxes[0].getX() +givingCardsBoxes[0].getWidth() +15,
            400, wightCard,heightCard);
        givingCardsBoxes[2] = new Rectangle(givingCardsBoxes[0].getX() -wightCard*2 +15,
            400, wightCard,heightCard);
    }



    @Override
    public void render(float delta) {

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
