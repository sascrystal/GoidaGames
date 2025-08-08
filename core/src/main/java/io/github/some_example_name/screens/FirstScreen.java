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
    private static final float NEW_GAME_BUTTON_SCALING = 0.48f, CONTINUE_GAME_BUTTON_SCALING = 0.5f,
        TUTORIAL_BUTTON_SCALING = 0.5f, LOGO_SCALING = 0.8f;
    private static final float INDENT_BUTTONS = 10, STARTING_POSITION = 200;
    private static final float AMPLITUDE_SHAKING = 30, SPEED_SHAKING = 2f, STEP_VALUE = 0.3f;
    private Texture BG_image;
    private Texture logoTexture;
    private SpriteBatch batch;
    private StretchViewport viewport;
    private Stage stage;
    private Music backgroundMusic, backgroundNoiseMenu;
    private float logoStep = 0, logoShakingDelta = 0, logoELapsedTime = 0;

    @Override
    public void show() {
        batch = new SpriteBatch();
        viewport = new StretchViewport(2400, 1080);
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.apply();

        // Инициализация Stage
        stage = new Stage(viewport, batch);
        Gdx.input.setInputProcessor(stage); // Передача ввода Stage
        BG_image = new Texture(Gdx.files.internal("menu/menu_background.png"));
        logoTexture = new Texture(Gdx.files.internal("menu/goblin_cards_logo.png"));

        // Создание кнопок
        Texture startButtonTexture = new Texture(Gdx.files.internal("menu/new_game_button.png"));
        Texture continueButtonTexture = new Texture(Gdx.files.internal("menu/continue_button_unavailable.png"));
        Texture tutorialButtonTexture = new Texture(Gdx.files.internal("menu/tutorial_button.png"));

        ImageButton startButton = new ImageButton(new TextureRegionDrawable(startButtonTexture));
        ImageButton continueButton = new ImageButton(new TextureRegionDrawable(continueButtonTexture));
        ImageButton tutorialButton = new ImageButton(new TextureRegionDrawable(tutorialButtonTexture));


        startButton.setSize(startButtonTexture.getWidth() * NEW_GAME_BUTTON_SCALING, startButtonTexture.getHeight() * NEW_GAME_BUTTON_SCALING);
        continueButton.setSize(continueButtonTexture.getWidth() * CONTINUE_GAME_BUTTON_SCALING, continueButtonTexture.getHeight() * CONTINUE_GAME_BUTTON_SCALING);
        tutorialButton.setSize(tutorialButtonTexture.getWidth() * TUTORIAL_BUTTON_SCALING, tutorialButtonTexture.getHeight() * TUTORIAL_BUTTON_SCALING);


        // Позиционирование кнопок

        continueButton.setPosition(INDENT_BUTTONS, STARTING_POSITION);
        tutorialButton.setPosition(INDENT_BUTTONS, continueButton.getY() + continueButton.getHeight());
        startButton.setPosition(INDENT_BUTTONS, tutorialButton.getY() + tutorialButton.getHeight());

        // Масштабирование кнопок (если нужно уменьшить размер)


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
                DialogueScreen dialogueScreen = new DialogueScreen("intro", act1);
                ((Main) Gdx.app.getApplicationListener()).setScreen(dialogueScreen);
            }
        });

        continueButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {//TODO: сделать продолжение игры
            }
        });

        tutorialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("Continue button pressed!");
                backgroundMusic.stop();
                dispose();
                Player player = new CharacterKnight();
                CellMap[][] map = CellMap.createTrainingAct(player);
                MapScreen training = new MapScreen(player, map);
                ((Main) Gdx.app.getApplicationListener()).setScreen(training);
            }
        });

        // Добавление кнопок на сцену
        stage.addActor(startButton);
        stage.addActor(continueButton);
        stage.addActor(tutorialButton);

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

        // Рендеринг фона
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        batch.draw(BG_image, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        logoDraw(delta);
        batch.end();

        // Обновление и рендеринг Stage
        stage.act(delta);
        stage.draw();
    }

    private void logoDraw(float delta) {
        batch.draw(logoTexture, (viewport.getWorldWidth() / 2) + 10 - (float) logoTexture.getWidth() / 2, 600 - logoShakingDelta * AMPLITUDE_SHAKING,
            logoTexture.getWidth() * LOGO_SCALING, logoTexture.getHeight() * LOGO_SCALING);
        logoStep += (delta) * SPEED_SHAKING;
        if (logoStep >= STEP_VALUE) {
            logoELapsedTime += logoStep;
            logoShakingDelta = (float) Math.abs(Math.sin(logoELapsedTime));
            logoStep = 0;
        }

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
