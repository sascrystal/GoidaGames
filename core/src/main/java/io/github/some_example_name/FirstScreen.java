package io.github.some_example_name;

import static io.github.some_example_name.GameScreen.viewport;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Music;

public class FirstScreen implements Screen {
    private SpriteBatch batch;
    private final Texture BG_image;
    private Texture startButtonTexture;
    private Texture continueButtonTexture;
    private Rectangle startButtonBounds;
    private Rectangle continueButtonBounds;
    private BitmapFont font;
    private float backgroundX1; // Первая позиция фона
    private float backgroundX2; // Вторая позиция фона
    private Music backgroundMusic, backgroundNoiseMenu;

    private float screenWidth;
    private float screenHeight;

    // Конструктор
    public FirstScreen() {
        BG_image = new Texture(Gdx.files.internal("menu/BG_1.png"));
        backgroundX1 = 0;
        backgroundX2 = BG_image.getWidth(); // Начальная позиция второго фона
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        startButtonTexture = new Texture(Gdx.files.internal("menu/NG.png")); // Замените на ваш путь
        continueButtonTexture = new Texture(Gdx.files.internal("menu/CT.png")); // Замените на ваш путь

        // Установка начальных размеров экрана
        screenWidth = Gdx.graphics.getWidth();
        screenHeight = Gdx.graphics.getHeight();

        // Установите размеры и позиции кнопок с учетом масштабирования
        float buttonWidth = startButtonTexture.getWidth();
        float buttonHeight = startButtonTexture.getHeight();

        // Используем коэффициенты масштабирования
        float scaleX = screenWidth / 1920f; // Предполагаемое разрешение 1920x1080
        float scaleY = screenHeight / 1080f;

        startButtonBounds = new Rectangle(800 * scaleX, 250 * scaleY, (buttonWidth - 35) * scaleX, (buttonHeight - 35) * scaleY);
        continueButtonBounds = new Rectangle(800 * scaleX, 1 * scaleY, (buttonWidth - 35) * scaleX, (buttonHeight - 35) * scaleY);

        font = new BitmapFont(); // Инициализация шрифта
        GlyphLayout layout = new GlyphLayout();


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
        // Скорость прокрутки
        float speed = 1f;
        backgroundX1 -= speed;
        backgroundX2 -= speed;

        // Проверка, нужно ли сбросить позицию
        if (backgroundX1 <= -BG_image.getWidth()) {
            backgroundX1 = BG_image.getWidth();
        }
        if (backgroundX2 <= -BG_image.getWidth()) {
            backgroundX2 = BG_image.getWidth();
        }

        // Отрисовка фона
        batch.begin();
        batch.draw(BG_image, backgroundX1, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Рисуем первый фон
        batch.draw(BG_image, backgroundX2, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight()); // Рисуем второй фон

        // Отрисовка кнопок с учетом масштабирования
        batch.draw(startButtonTexture, startButtonBounds.x, startButtonBounds.y, startButtonBounds.width - 30, startButtonBounds.height - 30);
        batch.draw(continueButtonTexture, continueButtonBounds.x, continueButtonBounds.y, continueButtonBounds.width - 30, continueButtonBounds.height - 30);

        batch.end();

        // Обработка нажатия на кнопки
        if (Gdx.input.isTouched()) {
            int screenX = Gdx.input.getX();
            int screenY = Gdx.graphics.getHeight() - Gdx.input.getY(); // Коррекция координат

            if (startButtonBounds.contains(screenX, screenY)) {
                System.out.println("Start button pressed!");
                // Переход на экран игры
                backgroundMusic.stop();
                dispose();

                //test

                Player player = new CharacterKnight();
                CellMap[][] map =  new CellMap[3][3];
                map[0][0] = new EmptyCell(0, viewport.getScreenY());
                Enemy[] enemies = new Enemy[3];
                enemies[0] = new EnemyGambler();
                Stage stage = new Stage(enemies);
                map[0][1] = new FightCell(map[0][0].getBounds().getX() + map[0][0].texture.getWidth(), 0, stage);
                map[0][0].setPlayerIn(true);

                //test

                MapScreen act1  = new MapScreen(player, map);
                ((Main) Gdx.app.getApplicationListener()).setScreen(act1);
            } else if (continueButtonBounds.contains(screenX, screenY)) {
                System.out.println("Continue button pressed!");
                backgroundMusic.stop();
                dispose();
                Player gay = new CharacterKnight();
                // Логика продолжения игры (если требуется)
                backgroundMusic.stop();
                dispose();
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        // Обновляем размеры экрана и пересчитываем позиции кнопок
        screenWidth = width;
        screenHeight = height;

        float scaleX = screenWidth / 1920f; // Предполагаемое разрешение 1920x1080
        float scaleY = screenHeight / 1080f;

        startButtonBounds.setPosition(800 * scaleX, 300 * scaleY);
        continueButtonBounds.setPosition(800 * scaleX, 70 * scaleY);
        startButtonBounds.setSize(startButtonTexture.getWidth() * scaleX, startButtonTexture.getHeight() * scaleY);
        continueButtonBounds.setSize(continueButtonTexture.getWidth() * scaleX, continueButtonTexture.getHeight() * scaleY);
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

        // Освобождение ресурсов фоновой музыки
        if (backgroundMusic != null) {
            backgroundMusic.stop(); // Останавливаем музыку
            backgroundMusic.dispose(); // Освобождаем ресурсы
        }

        if (backgroundNoiseMenu != null) {
            backgroundNoiseMenu.stop(); // Останавливаем музыку
            backgroundNoiseMenu.dispose(); // Освобождаем ресурсы
        }
    }
}
