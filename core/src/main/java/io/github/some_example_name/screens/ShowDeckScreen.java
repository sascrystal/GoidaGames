package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.input.GestureDetector;

import java.util.List;

import io.github.some_example_name.Main;
import io.github.some_example_name.cards.PlayingCard;

public class ShowDeckScreen implements Screen , GestureDetector.GestureListener {
    private final List<PlayingCard> deck;
    private final Screen nextScreen;
    private Texture buttonBackTexture;
    private Rectangle buttonBackRectangle;
    private StretchViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;
    private float maxX, minX;


    public ShowDeckScreen(List<PlayingCard> deck, Screen nextScreen) {
        this.deck = deck;
        this.nextScreen = nextScreen;
    }

    @Override
    public void show() {
        viewportConfiguration();
        batchConfiguration();
        screenMinMaxCalculate();
        gestureDetectorConfiguration();
        showButtonBack();
    }
    private void batchConfiguration(){
        batch = new SpriteBatch();
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }
    private void gestureDetectorConfiguration(){
        GestureDetector gestureDetector = new GestureDetector(this);
        Gdx.input.setInputProcessor(gestureDetector);
    }
    private void screenMinMaxCalculate(){
        minX = viewport.getWorldWidth() / 2;
        float totalDeckWidth = deck.size() * (viewport.getWorldHeight() * ((float)deck.get(0).getTexture().getHeight() / deck.get(0).getTexture().getWidth()) + 20) - 20;
        maxX = totalDeckWidth -viewport.getWorldWidth() / 2;
    }
    private void viewportConfiguration(){
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
        updateCamera();
        draw(delta);
    }
    private void updateCamera(){
        viewport.getCamera().update();
        batch.setProjectionMatrix(viewport.getCamera().combined);
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
        for (int i = 0; i<deck.size(); i++){
            deck.get(i).draw(batch,beginPositionRowX +(indent+width)*i,beginPositionRowY,
                width,viewport.getWorldHeight());
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

    @Override
    public boolean touchDown(float x, float y, int pointer, int button) {
        Vector2 touchPosition = new Vector2(x,y);
        viewport.unproject(touchPosition);
        if(buttonBackRectangle.contains(touchPosition)){
            ((Main) Gdx.app.getApplicationListener()).setScreen(nextScreen);
        }
        return true;
    }

    @Override
    public boolean tap(float x, float y, int count, int button) {
        return false;
    }

    @Override
    public boolean longPress(float x, float y) {
        return false;
    }

    @Override
    public boolean fling(float velocityX, float velocityY, int button) {
        float targetX = viewport.getCamera().position.x - velocityX * 0.001f;
        targetX = MathUtils.clamp(targetX,minX, maxX);
        viewport.getCamera().position.set(targetX, viewport.getCamera().position.y, 0);
        viewport.getCamera().update();
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        float speed = 1f;
        float newX = viewport.getCamera().position.x - deltaX * speed;
        newX = MathUtils.clamp(newX,minX, maxX);
        viewport.getCamera().position.set(newX, viewport.getCamera().position.y, 0);
        viewport.getCamera().update();
        return true;
    }
    @Override
    public boolean panStop(float x, float y, int pointer, int button) {
        return false;
    }

    @Override
    public boolean zoom(float initialDistance, float distance) {
        return false;
    }

    @Override
    public boolean pinch(Vector2 initialPointer1, Vector2 initialPointer2, Vector2 pointer1, Vector2 pointer2) {
        return false;
    }

    @Override
    public void pinchStop() {

    }
}
