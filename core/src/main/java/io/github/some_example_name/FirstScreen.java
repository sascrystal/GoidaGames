package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

public class FirstScreen implements Screen {
    private SpriteBatch batch;
    private StretchViewport viewport;
    private final Texture BG_image;
    private Texture startButtonTexture;
    private Texture continueButtonTexture;
    private Rectangle startButtonBounds;
    private Rectangle continueButtonBounds;
    private BitmapFont font;
    private float backgroundX1;
    private float backgroundX2;
    private Music backgroundMusic, backgroundNoiseMenu;

    public FirstScreen() {
        BG_image = new Texture(Gdx.files.internal("menu/BG_1.png"));
        backgroundX1 = 0;
        backgroundX2 = BG_image.getWidth();
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        viewport = new StretchViewport(1920, 1080);
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.getCamera().update();
        viewport.apply();

        startButtonTexture = new Texture(Gdx.files.internal("menu/NG.png"));
        continueButtonTexture = new Texture(Gdx.files.internal("menu/CT.png"));

        // Инициализация границ кнопок
        float buttonWidth = startButtonTexture.getWidth();
        float buttonHeight = startButtonTexture.getHeight();
        startButtonBounds = new Rectangle(800, 250, buttonWidth - 35, buttonHeight - 35);
        continueButtonBounds = new Rectangle(800, 1, buttonWidth - 35, buttonHeight - 35);

        font = new BitmapFont();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/mainMenuMusic.mp3"));
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.7f);
        backgroundMusic.play();

        backgroundNoiseMenu = Gdx.audio.newMusic(Gdx.files.internal("music/backgroundNoiseMenu.mp3"));
        backgroundNoiseMenu.setLooping(true);
        backgroundNoiseMenu.setVolume(0.5f);
        backgroundNoiseMenu.play();
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        // Обновление позиций фона
        float speed = 1f;
        backgroundX1 -= speed;
        backgroundX2 -= speed;

        if (backgroundX1 <= -BG_image.getWidth()) {
            backgroundX1 = BG_image.getWidth();
        }
        if (backgroundX2 <= -BG_image.getWidth()) {
            backgroundX2 = BG_image.getWidth();
        }

        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(BG_image, backgroundX1, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.draw(BG_image, backgroundX2, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.draw(startButtonTexture, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width - 30, startButtonBounds.height - 30);
        batch.draw(continueButtonTexture, continueButtonBounds.x, continueButtonBounds.y, continueButtonBounds.width - 30, continueButtonBounds.height - 30);
        batch.end();

        input();
    }

    private void input() {
        if (Gdx.input.isTouched()) {
            Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPosition);

            if (startButtonBounds.contains(touchPosition.x, touchPosition.y)) {
                System.out.println("Start button pressed!");
                backgroundMusic.stop();
                dispose();
                Player player = new CharacterKnight();
                CellMap[][] map = CellMap.generateAct1(player);
                MapScreen act1 = new MapScreen(player, map);
                ((Main) Gdx.app.getApplicationListener()).setScreen(act1);
            } else if (continueButtonBounds.contains(touchPosition.x, touchPosition.y)) {
                System.out.println("Continue button pressed!");
                backgroundMusic.stop();
                dispose();
                Player player = new CharacterKnight();
                // Логика продолжения игры (если требуется)
                DialogueScreen dialogueScreen = new DialogueScreen();
                ((Main) Gdx.app.getApplicationListener()).setScreen(dialogueScreen);
            }
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
        batch.dispose();
        BG_image.dispose();
        startButtonTexture.dispose();
        continueButtonTexture.dispose();
        font.dispose();
        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }
        if (backgroundNoiseMenu != null) {
            backgroundNoiseMenu.stop();
            backgroundNoiseMenu.dispose();
        }
    }
}
