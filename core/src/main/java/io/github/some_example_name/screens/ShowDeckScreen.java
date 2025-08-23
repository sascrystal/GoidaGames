package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.input.GestureDetector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.List;

import io.github.some_example_name.Main;
import io.github.some_example_name.cards.PlayingCard;

public class ShowDeckScreen implements Screen, GestureDetector.GestureListener {
    private static final float MODIFIER_FOR_CARD_NANE = 0.61f, MODIFIER_FOR_CARD_DESCRIPTION = 0.75F;
    private static final float LAYOUT_HEIGHT_FOR_NAME_CARDS = (float) 10 / 225;
    private final List<PlayingCard> deck;
    private final Screen nextScreen;
    private Texture buttonBackTexture, background;
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
        showBackground();
        showFont();
    }

    private void showFont() {
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);

    }

    private void showBackground() {
        background = new Texture(Gdx.files.internal("backgrounds/background_map.jpg"));
    }


    private void batchConfiguration() {
        batch = new SpriteBatch();
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    private void gestureDetectorConfiguration() {
        GestureDetector gestureDetector = new GestureDetector(this);
        Gdx.input.setInputProcessor(gestureDetector);
    }


    private void screenMinMaxCalculate() {
        minX = viewport.getWorldWidth() / 2;
        maxX = deck.size() * (viewport.getWorldHeight() * ((float) deck.get(0).getTexture().getHeight() / deck.get(0).getTexture().getWidth()) + 20) - 20;
    }

    private void viewportConfiguration() {
        viewport = new StretchViewport(2080, deck.get(0).getTexture().getHeight());
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.getCamera().update();
        viewport.apply();
    }

    private void showButtonBack() {

        buttonBackTexture = new Texture(Gdx.files.internal("buttons/back_button.png"));
        float width = 100;
        float height = 100;
        buttonBackRectangle = new Rectangle(
            viewport.getWorldWidth() - width - 100,
            viewport.getWorldHeight() - height - 100,
            100, 50);
    }

    @Override
    public void render(float delta) {
        updateCamera();
        draw();
    }

    private void updateCamera() {
        viewport.getCamera().update();
        batch.setProjectionMatrix(viewport.getCamera().combined);
    }

    private void draw() {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        backgroundDraw();
        deckDraw();
        buttonBackDraw();
        batch.end();
    }

    private void backgroundDraw() {
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getScreenHeight());
    }

    private void buttonBackDraw() {
        buttonBackRectangle.x = viewport.getCamera().position.x + viewport.getWorldWidth() / 2 - 100;
        buttonBackRectangle.y = viewport.getCamera().position.y + viewport.getWorldHeight() / 2 - 50;
        batch.draw(buttonBackTexture, buttonBackRectangle.x, buttonBackRectangle.y,
            buttonBackRectangle.width, buttonBackRectangle.height);
    }

    private void deckDraw() {
        int indent = 0;
        float beginPositionRowX = 0;
        float beginPositionRowY = 0;
        float width = viewport.getWorldHeight() * ((float) deck.get(0).getTexture().getHeight() / deck.get(0).getTexture().getWidth());
        for (int i = 0; i < deck.size(); i++) {
            float x = beginPositionRowX + (indent + width) * i;
            batch.draw(
                deck.get(i).getTexture(),
                x,
                beginPositionRowY,
                width,
                viewport.getWorldHeight());
            font.setColor(Color.WHITE);

            font.getData().setScale(1.0f);
            font.draw(
                batch,
                String.valueOf(deck.get(i).getCost()),
                x + 70,
                beginPositionRowY + viewport.getWorldHeight() - 10);

            font.getData().setScale(2.0f);
            font.setColor(Color.WHITE);

            float widthText = width * MODIFIER_FOR_CARD_NANE;
            GlyphLayout layout = new GlyphLayout(font, deck.get(i).getName(), Color.WHITE, widthText, Align.center, true);
            float xText = x + width / 2 - widthText / 2;
            float y = beginPositionRowY + viewport.getWorldHeight() * 0.52f;
            while (layout.height > viewport.getWorldHeight() * LAYOUT_HEIGHT_FOR_NAME_CARDS) {
                font.getData().setScale(font.getScaleX() * 0.999f);
                layout.setText(font, deck.get(i).getName(), Color.WHITE, widthText, Align.center, true);
            }
            font.draw(batch, layout, xText, y);
            font.setColor(Color.WHITE);
            font.getData().setScale(0.7f);

            widthText = width * MODIFIER_FOR_CARD_DESCRIPTION;
            xText = x + width / 2 - widthText / 2;
            y = beginPositionRowY + viewport.getWorldHeight() * 0.40f;
            layout.setText(font, deck.get(i).getDescription(), Color.WHITE, widthText, Align.center, true);
            while (layout.height > viewport.getWorldHeight() * 0.32f) {
                font.getData().setScale(font.getScaleX() * 0.999f);
                layout.setText(font, deck.get(i).getDescription(), Color.WHITE, widthText, Align.center, true);
            }
            font.draw(batch, layout, xText, y);
            font.setColor(Color.WHITE);
            font.getData().setScale(1.0f);

        }
    }


    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
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
        Vector2 touchPosition = new Vector2(x, y);
        viewport.unproject(touchPosition);
        buttonBackInput(touchPosition);
        return true;
    }

    private void buttonBackInput(Vector2 touchPosition) {
        if (buttonBackRectangle.contains(touchPosition)) {


            ((Main) Gdx.app.getApplicationListener()).setScreen(nextScreen);

        }
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
        targetX = MathUtils.clamp(targetX, minX, maxX);
        viewport.getCamera().position.set(targetX, viewport.getCamera().position.y, 0);
        viewport.getCamera().update();
        return true;
    }

    @Override
    public boolean pan(float x, float y, float deltaX, float deltaY) {
        float speed = 1f;
        float newX = viewport.getCamera().position.x - deltaX * speed;
        newX = MathUtils.clamp(newX, minX, maxX);
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
