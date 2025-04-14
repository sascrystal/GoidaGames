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
    private final SpriteBatch batch;
    private final Stage stage;
    private final BitmapFont font;
    private final Texture whitePixel;

    private String fullText;
    private String currentText;
    private float textProgress;
    private float charDisplayTime = 0.01f;
    private float timeSinceLastChar;

    private boolean isAnimating;
    private boolean hasMoreText;
    private int currentPageStart;
    private int currentPageEnd;

    // Настройки диалогового окна
    private final Color backgroundColor = new Color(0f, 0f, 0f, 0.7f);
    private float padding = 20f;
    private float textBoxHeight = 200f; // Высота окна
    private float textBoxY = 50f; // Позиция по Y снизу экрана



    // Тестовый длинный текст
    private static final String TEST_TEXT =
        "DDDD EGGS LLSLLSLSLLSLSL Это тестовый диалог для демонстрации работы системы. " +
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
        batch = new SpriteBatch();
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"),
            Gdx.files.internal("fonts/font.png"), false);
        font.getData().setScale(1.6f); // Увеличим шрифт для лучшей читаемости на мобильных

        // Создаём белую текстуру один раз при создании экрана
        Pixmap pixmap = new Pixmap(1, 1, Pixmap.Format.RGBA8888);
        pixmap.setColor(Color.WHITE);
        pixmap.fill();
        whitePixel = new Texture(pixmap);
        pixmap.dispose();

        setupDialogue(TEST_TEXT);

        // Обработка нажатий
        stage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                handleTap();
            }
        });
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

        // Находим, где закончится текущая страница текста
        findNextPageBreak();
    }

    private void findNextPageBreak() {
        GlyphLayout layout = new GlyphLayout();
        float maxWidth = Gdx.graphics.getWidth() - 2 * padding;
        float maxHeight = textBoxHeight - 2 * padding;

        int start = currentPageStart;
        int end = fullText.length();
        int lastSpace = -1; // Позиция последнего пробела

        // Сначала пробуем найти разрыв по словам
        for (int i = start; i <= fullText.length(); i++) {
            if (i < fullText.length() && Character.isWhitespace(fullText.charAt(i))) {
                lastSpace = i;
            }

            String testText = fullText.substring(start, i);
            layout.setText(font, testText, Color.WHITE, maxWidth, Align.left, true);

            // Если текст превышает допустимую высоту
            if (layout.height > maxHeight) {
                if (lastSpace > start) {
                    // Если нашли пробел - разрываем по нему
                    end = lastSpace;
                } else {
                    // Если пробелов нет - разрываем посимвольно
                    end = i - 1;
                }
                break;
            }
        }

        hasMoreText = end < fullText.length();
        currentPageEnd = end;
    }



    private void handleTap() {
        // Если текст еще анимируется, показать его весь
        if (isAnimating) {
            currentText = fullText.substring(currentPageStart, currentPageEnd);
            textProgress = currentText.length();
            isAnimating = false;
            return;
        }

        // Если есть еще текст, перейти к следующей части
        if (hasMoreText) {
            currentPageStart = currentPageEnd;
            findNextPageBreak();
            currentText = "";
            textProgress = 0;
            isAnimating = true;
        } else {
            // Диалог закончен, можно закрыть экран
            // Здесь можно добавить закрытие экрана или переход к следующему действию
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        // Обновление анимации текста
        updateTextAnimation(delta);

        // Очистка экрана
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();

        // Рисуем фон диалога
        drawDialogueBackground();

        // Рисуем текущий текст
        drawDialogueText();

        batch.end();

        stage.act(delta);
        stage.draw();
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

    private void drawDialogueBackground() {
        float x = 0;
        float y = textBoxY; // Используем настроенную позицию
        float width = Gdx.graphics.getWidth();
        float height = textBoxHeight; // Используем настроенную высоту

        batch.setColor(backgroundColor);
        batch.draw(whitePixel, x, y, width, height);
        batch.setColor(Color.WHITE);
    }


    private void drawDialogueText() {
        float x = padding;
        float y = textBoxY + textBoxHeight - padding;
        float width = Gdx.graphics.getWidth() - 2 * padding;

        // Временно уменьшаем размер шрифта для проверки
        float originalScale = font.getData().scaleX;
        font.getData().setScale(1.6f);

        GlyphLayout layout = new GlyphLayout(font, currentText, Color.WHITE, width, Align.left, true);

        // Если текст не помещается даже с уменьшенным шрифтом
        while (layout.height > textBoxHeight - 2*padding && font.getData().scaleX > 0.5f) {
            font.getData().setScale(font.getData().scaleX * 0.9f);
            layout.setText(font, currentText, Color.WHITE, width, Align.left, true);
        }

        // Восстанавливаем оригинальный размер шрифта
        font.getData().setScale(originalScale);

        // Рисуем текст
        font.draw(batch, layout, x, y + layout.height); // Корректируем позицию по Y
    }


    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
        // При изменении размера экрана пересчитываем разбиение текста
        if (fullText != null) {
            findNextPageBreak();
        }
    }

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        batch.dispose();
        stage.dispose();
        font.dispose();
        whitePixel.dispose(); // Освобождаем текстуру
    }
}
