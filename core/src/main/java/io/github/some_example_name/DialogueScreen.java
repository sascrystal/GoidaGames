package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
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
import com.badlogic.gdx.utils.viewport.ScreenViewport;

public class DialogueScreen implements Screen {
    private SpriteBatch batch;
    private Stage stage;
    private BitmapFont font;
    private Texture whitePixel;
    private GlyphLayout layout;

    private String fullText;
    private String currentText;
    private float textProgress;
    private float charDisplayTime = 0.01f;
    private float timeSinceLastChar;

    private boolean isAnimating;
    private boolean hasMoreText;
    private int currentPageStart;
    private int currentPageEnd;

    // Конфигурация
    private static final Color BACKGROUND_COLOR = new Color(0f, 0f, 0f, 0.7f);
    private static final float PADDING = 20f;
    private static final float TEXT_BOX_HEIGHT = 200f;
    private static final float TEXT_BOX_Y = 50f;
    private static final float FONT_SCALE = 1.6f;

    private static final String TEST_TEXT = "DDDD EGGS LLSLLSLSLLSLSL Это тестовый диалог для демонстрации работы системы. " +
        "Здесь будет достаточно много текста, чтобы он не поместился на один экран. " +
        "Мы должны реализовать механизм, который позволит разбивать текст на части " +
        "и отображать их последовательно при каждом нажатии. " +
        "Также текст должен появляться постепенно, символ за символом, " +
        "создавая эффект печати. Если игрок торопится, он может пропустить анимацию " +
        "и сразу увидеть весь текст текущей части. " +
        "Продолжаем наш длинный текст. Вот еще несколько предложений, чтобы " +
        "убедиться, что все работает правильно. " +
        "Последнее предложение тестового диалога.";

    public DialogueScreen() {
        // Конструктор пустой, инициализация перенесена в show()
    }

    @Override
    public void show() {
        initializeResources();
        setupInput();
        setupDialogue(TEST_TEXT);
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

    private void setupDialogue(String text) {
        fullText = text;
        currentText = "";
        textProgress = 0;
        timeSinceLastChar = 0;
        isAnimating = true;
        hasMoreText = true;
        currentPageStart = 0;
        currentPageEnd = 0;
        findNextPageBreak();
    }

    private void findNextPageBreak() {
        if (fullText == null) return;

        float maxWidth = Gdx.graphics.getWidth() - 2 * PADDING;
        float maxHeight = TEXT_BOX_HEIGHT - 2 * PADDING;

        int start = currentPageStart;
        int end = fullText.length();
        int lastSpace = -1;

        for (int i = start; i <= fullText.length(); i++) {
            if (i < fullText.length() && Character.isWhitespace(fullText.charAt(i))) {
                lastSpace = i;
            }
//
            String testText = fullText.substring(start, i);
            layout.setText(font, testText, Color.WHITE, maxWidth, Align.left, true);

            if (layout.height > maxHeight) {
                end = (lastSpace > start) ? lastSpace : i - 1;
                break;
            }
        }

        hasMoreText = end < fullText.length();
        currentPageEnd = end;
    }

    private void handleTap() {
        if (isAnimating) {
            completeCurrentAnimation();
            return;
        }

        if (hasMoreText) {
            advanceToNextPage();
        } else {
            // TODO: Добавить логику завершения диалога
        }
    }

    private void completeCurrentAnimation() {
        currentText = fullText.substring(currentPageStart, currentPageEnd);
        textProgress = currentText.length();
        isAnimating = false;
    }

    private void advanceToNextPage() {
        currentPageStart = currentPageEnd;
        findNextPageBreak();
        currentText = "";
        textProgress = 0;
        isAnimating = true;
    }

    @Override
    public void render(float delta) {
        update(delta);
        draw();
    }

    private void update(float delta) {
        updateTextAnimation(delta);
        stage.act(delta);
    }

    private void updateTextAnimation(float delta) {
        if (!isAnimating) return;

        timeSinceLastChar += delta;
        if (timeSinceLastChar >= charDisplayTime) {
            timeSinceLastChar = 0;
            textProgress++;

            if (textProgress >= currentPageEnd - currentPageStart) {
                isAnimating = false;
                textProgress = currentPageEnd - currentPageStart;
            }

            currentText = fullText.substring(currentPageStart, currentPageStart + (int)textProgress);
        }
    }

    private void draw() {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
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
        findNextPageBreak();
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {
        Gdx.input.setInputProcessor(null);
    }

    @Override
    public void dispose() {
        if (batch != null) batch.dispose();
        if (stage != null) stage.dispose();
        if (font != null) font.dispose();
        if (whitePixel != null) whitePixel.dispose();
        if (layout != null) layout.reset();
    }
}
