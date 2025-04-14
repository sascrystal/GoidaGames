package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.ImageButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.github.some_example_name.Main;
import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.player.CharacterKnight;
import io.github.some_example_name.player.Player;

public class FirstScreen implements Screen {
    private SpriteBatch batch;
    private StretchViewport viewport;
    private Stage stage;
    private final Texture BG_image;
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
        viewport = new StretchViewport(2400, 1080);
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.apply();

        // Инициализация Stage
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage); // Передача ввода Stage

        // Создание кнопок
        Texture startButtonTexture = new Texture(Gdx.files.internal("menu/NG.png"));
        Texture continueButtonTexture = new Texture(Gdx.files.internal("menu/CT.png"));

        ImageButton startButton = new ImageButton(new TextureRegionDrawable(startButtonTexture));
        ImageButton continueButton = new ImageButton(new TextureRegionDrawable(continueButtonTexture));

        // Позиционирование кнопок
        startButton.setPosition(800, 350);
        continueButton.setPosition(800, 100); // Изменено, чтобы кнопки не пересекались

        // Масштабирование кнопок (если нужно уменьшить размер)
        startButton.setSize(startButton.getWidth() * 0.9f, startButton.getHeight() * 0.9f);
        continueButton.setSize(continueButton.getWidth() * 0.9f, continueButton.getHeight() * 0.9f);

        // Обработчики событий для кнопок
        startButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Start button pressed!");
                backgroundMusic.stop();
                dispose();
                Player player = new CharacterKnight();
                CellMap[][] map = CellMap.generateAct1(player);
                MapScreen act1 = new MapScreen(player, map);
                ((Main) Gdx.app.getApplicationListener()).setScreen(act1);
            }
        });

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Continue button pressed!");
                backgroundMusic.stop();
                dispose();
                Player player = new CharacterKnight();
                DialogueScreen dialogueScreen = new DialogueScreen();
                ((Main) Gdx.app.getApplicationListener()).setScreen(dialogueScreen);
            }
        });

        // Добавление кнопок на сцену
        stage.addActor(startButton);
        stage.addActor(continueButton);

        // Инициализация музыки
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

        // Рендеринг фона
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(BG_image, backgroundX1, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.draw(BG_image, backgroundX2, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.end();

        // Обновление и рендеринг Stage
        stage.act(delta);
        stage.draw();
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height);
        stage.getViewport().update(width, height, true);
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
        stage.dispose();
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
