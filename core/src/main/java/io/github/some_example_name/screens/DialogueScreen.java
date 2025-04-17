package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music; // Импорт для работы с музыкой
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.XmlReader;

import io.github.some_example_name.Main;
import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.player.CharacterKnight;
import io.github.some_example_name.player.Player;

public class DialogueScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;
    private BitmapFont font;
    private Texture whitePixel;
    private GlyphLayout layout;
    private Array<String> dialoguePages;
    private Array<String> backgroundPaths; // Список путей к фонам
    private Array<String> musicPaths; // Список путей к музыке
    private Texture backgroundTexture; // Текущая фоновая текстура
    private Music currentMusic; // Текущая музыка
    private String currentText;
    private float textProgress;
    private float charDisplayTime = 0.01f;
    private float timeSinceLastChar;
    private boolean isAnimating;
    private boolean hasMoreText;
    private int currentPageIndex;

    // Переход фона
    private float backgroundAlpha = 1f;
    private float fadeDuration = 0.5f; // Длительность перехода
    private float fadeTimer = 0f;
    private boolean isFading = false;

    // Конфигурация
    private static final Color BACKGROUND_COLOR = new Color(0f, 0f, 0f, 0.7f);
    private static final float PADDING = 20f;
    private static final float TEXT_BOX_HEIGHT = 200f;
    private static final float TEXT_BOX_Y = 50f;
    private static final float FONT_SCALE = 1.6f;
    private Screen nextScreen;

    private final String dialogueId;

    public DialogueScreen(String dialogueId, Screen nextScreen) {
        this.dialogueId = dialogueId;
        this.backgroundPaths = new Array<>();
        this.musicPaths = new Array<>(); // Инициализация массива для музыки
        this.nextScreen = nextScreen;
    }


    @Override
    public void show() {
        initializeResources();
        setupInput();
        loadAndSetupDialogue();
    }

    private void initializeResources() {
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        layout = new GlyphLayout();

        font = new BitmapFont(
            Gdx.files.internal("fonts/font.fnt"),
            Gdx.files.internal("fonts/font.png"),
            false
        );
        font.getData().setScale(FONT_SCALE);

        whitePixel = createWhitePixelTexture();
    }

    private Texture createWhitePixelTexture() {
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        return texture;
    }

    private void setupInput() {
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleTap();
            }
        });
        Gdx.input.setInputProcessor(stage);
    }

    private Array<String> loadDialogueFromXml(String dialogueId) {
        Array<String> pages = new Array<>();
        backgroundPaths.clear();
        musicPaths.clear(); // Очищаем пути к музыке
        try {
            XmlReader reader = new XmlReader();
            XmlReader.Element root = reader.parse(Gdx.files.internal("dialogues/dialogues.xml"));
            for (XmlReader.Element dialogue : root.getChildrenByName("dialogue")) {
                if (dialogue.getAttribute("id").equals(dialogueId)) {
                    for (XmlReader.Element page : dialogue.getChildrenByName("page")) {
                        pages.add(page.getText());
                        // Загружаем путь к фону
                        String bgPath = page.get("background", null);
                        backgroundPaths.add(bgPath);
                        // Загружаем путь к музыке
                        String musicPath = page.get("music", null);
                        musicPaths.add(musicPath);
                    }
                    break;
                }
            }
        } catch (Exception e) {
            Gdx.app.error("DialogueScreen", "Error loading dialogues.xml", e);
        }
        return pages;
    }

    private void loadAndSetupDialogue() {
        dialoguePages = loadDialogueFromXml(dialogueId);
        currentText = "";
        textProgress = 0;
        timeSinceLastChar = 0;
        isAnimating = true;
        currentPageIndex = 0;
        hasMoreText = dialoguePages.size > 0;

        // Загружаем начальный фон и музыку
        loadBackgroundForPage(currentPageIndex);
        loadMusicForPage(currentPageIndex);
    }

    private void loadBackgroundForPage(int pageIndex) {
        // Освобождаем текущую текстуру
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
            backgroundTexture = null;
        }

        // Загружаем новый фон, если путь существует
        if (pageIndex < backgroundPaths.size && backgroundPaths.get(pageIndex) != null) {
            String bgPath = backgroundPaths.get(pageIndex);
            if (!bgPath.isEmpty() && Gdx.files.internal(bgPath).exists()) {
                try {
                    backgroundTexture = new Texture(Gdx.files.internal(bgPath));
                    // Запускаем эффект перехода
                    backgroundAlpha = 0f;
                    fadeTimer = 0f;
                    isFading = true;
                } catch (Exception e) {
                    Gdx.app.error("DialogueScreen", "Error loading background: " + bgPath, e);
                }
            }
        }
    }

    private void loadMusicForPage(int pageIndex) {
        // Проверяем, есть ли новый путь к музыке для текущей страницы
        String newMusicPath = (pageIndex < musicPaths.size && musicPaths.get(pageIndex) != null) ? musicPaths.get(pageIndex) : null;

        // Если новый путь отсутствует или пустой, продолжаем играть текущую музыку (если она есть)
        if (newMusicPath == null || newMusicPath.isEmpty()) {
            return; // Ничего не делаем, музыка продолжает играть
        }

        // Если текущая музыка уже играет и путь совпадает с новым, ничего не меняем
        if (currentMusic != null && pageIndex > 0 && musicPaths.get(pageIndex - 1) != null && musicPaths.get(pageIndex - 1).equals(newMusicPath)) {
            return; // Продолжаем играть текущую музыку
        }

        // Останавливаем и освобождаем текущую музыку, если она есть
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
            currentMusic = null;
        }

        // Загружаем новую музыку, если путь существует
        if (Gdx.files.internal(newMusicPath).exists()) {
            try {
                currentMusic = Gdx.audio.newMusic(Gdx.files.internal(newMusicPath));
                currentMusic.setLooping(true); // Зацикливаем музыку
                currentMusic.setVolume(0.5f); // Устанавливаем громкость
                currentMusic.play(); // Начинаем воспроизведение
            } catch (Exception e) {
                Gdx.app.error("DialogueScreen", "Error loading music: " + newMusicPath, e);
            }
        }
    }

    private void handleTap() {
        if (isAnimating) {
            completeCurrentAnimation();
            return;
        }
        advanceToNextPage();
        if (!hasMoreText) {
            setNextScreen();
        }



    }

    private void setNextScreen(){
        nextScreen.show();
        dispose();
        ((Main)Gdx.app.getApplicationListener()).setScreen(nextScreen);

    }

    private void completeCurrentAnimation() {
        if (currentPageIndex < dialoguePages.size) {
            currentText = dialoguePages.get(currentPageIndex);
            textProgress = currentText.length();
            isAnimating = false;
        }
    }

    private void advanceToNextPage() {
        currentPageIndex++;
        hasMoreText = currentPageIndex < dialoguePages.size;
        currentText = "";
        textProgress = 0;
        isAnimating = true;

        // Загружаем фон и музыку для новой страницы
        loadBackgroundForPage(currentPageIndex);
        loadMusicForPage(currentPageIndex);
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta) {
        updateTextAnimation(delta);
        updateBackgroundFade(delta);
        stage.act(delta);
    }

    private void updateTextAnimation(float delta) {
        if (!isAnimating || currentPageIndex >= dialoguePages.size) return;

        timeSinceLastChar += delta;
        if (timeSinceLastChar >= charDisplayTime) {
            timeSinceLastChar = 0;
            textProgress++;

            String targetText = dialoguePages.get(currentPageIndex);
            if (textProgress >= targetText.length()) {
                isAnimating = false;
                textProgress = targetText.length();
            }

            currentText = targetText.substring(0, Math.min((int) textProgress, targetText.length()));
        }
    }

    private void updateBackgroundFade(float delta) {
        if (isFading) {
            fadeTimer += delta;
            backgroundAlpha = fadeTimer / fadeDuration;
            if (fadeTimer >= fadeDuration) {
                backgroundAlpha = 1f;
                isFading = false;
            }
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        // Отрисовка фона с альфа-каналом
        if (backgroundTexture != null) {
            batch.setColor(1f, 1f, 1f, backgroundAlpha);
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.setColor(1f, 1f, 1f, 1f);
        }
        drawDialogueBackground();
        drawDialogueText();
        batch.end();

        stage.draw();
    }

    private void drawDialogueBackground() {
        batch.setColor(BACKGROUND_COLOR);
        batch.draw(whitePixel, 0, TEXT_BOX_Y, Gdx.graphics.getWidth(), TEXT_BOX_HEIGHT);
        batch.setColor(Color.WHITE);
    }

    private void drawDialogueText() {
        if (currentText == null || font == null) return;

        float x = PADDING;
        float y = TEXT_BOX_Y + TEXT_BOX_HEIGHT - PADDING;
        float width = Gdx.graphics.getWidth() - 2 * PADDING;

        float originalScale = font.getData().scaleX;
        layout.setText(font, currentText, Color.WHITE, width, Align.left, true);

        while (layout.height > TEXT_BOX_HEIGHT - 2 * PADDING && font.getData().scaleX > 0.5f) {
            font.getData().setScale(font.getData().scaleX * 0.9f);
            layout.setText(font, currentText, Color.WHITE, width, Align.left, true);
        }

        font.draw(batch, layout, x, y);
        font.getData().setScale(originalScale);
    }

    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override
    public void pause() {
        // Пауза музыки при сворачивании приложения
        if (currentMusic != null && currentMusic.isPlaying()) {
            currentMusic.pause();
        }
    }

    @Override
    public void resume() {
        // Возобновление музыки при возвращении в приложение
        if (currentMusic != null && !currentMusic.isPlaying()) {
            currentMusic.play();
        }
    }

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
        // Останавливаем музыку при скрытии экрана
        if (currentMusic != null) {
            currentMusic.stop();
        }
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (stage != null) stage.dispose();
        if (font != null) font.dispose();
        if (whitePixel != null) whitePixel.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
        if (currentMusic != null) {
            currentMusic.stop();
            currentMusic.dispose();
        }
        if (layout != null) layout.reset();
    }
}
