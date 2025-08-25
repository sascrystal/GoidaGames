package io.github.some_example_name.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.cell_map_classes.events.DialogEvent;
import io.github.some_example_name.player.Player;

public class QuestionMarkScreen implements Screen {
    private final DialogEvent dialogEvent;
    private final CellMap[][] map;
    private final Player player;
    private Texture textField;
    private Texture dialogBox;
    private Rectangle[] dialogBoxBounds;
    private StretchViewport viewport;
    private SpriteBatch batch;
    private BitmapFont font;


    public QuestionMarkScreen(MapScreen map, DialogEvent dialogEvent) {
        this.player = map.getPlayer();
        this.map = map.getMap();
        this.dialogEvent = dialogEvent;
    }

    @Override
    public void show() {
        batch = new SpriteBatch();
        viewportConfiguration();
        showDialogBoxBounds();
        showFont();
        dialogBox = new Texture("cell/emptyCell.png");
        textField = new Texture("cell/emptyCell.png");
    }

    private void viewportConfiguration() {
        viewport = new StretchViewport(2400, 1080);
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.getCamera().update();
        viewport.apply();
    }

    private void showDialogBoxBounds() {
        dialogBoxBounds = new Rectangle[3];
        float initialX = 0;
        float initialY = 50;
        float height = (float) (viewport.getWorldHeight() / 4.5);
        float wight = viewport.getWorldWidth() / 2 - 15;
        for (int i = 0; i < 3; i++) {
            float indexY = initialY + height * i;
            dialogBoxBounds[i] = new Rectangle(initialX, indexY, wight, height);
        }
    }

    private void showFont() {
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        font.getData().setScale(2.0f);
    }

    @Override
    public void render(float delta) {
        batch.setProjectionMatrix(viewport.getCamera().combined);
        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        if (dialogEvent.getBackground() != null) {
            drawBackground();
        }
        drawTextField();
        drawDialogBox();
        batch.end();
        input();
    }

    private void drawBackground() {
        batch.draw(dialogEvent.getBackground(), 0, 0, viewport.getWorldWidth(), viewport.getScreenHeight());
    }

    private void drawTextField() {
        float x = viewport.getWorldWidth() / 2;
        float y = 0;
        float width = viewport.getWorldWidth() / 2;
        float height = viewport.getWorldHeight();

        // Отрисовка фона текстового поля
        batch.draw(textField, x, y, width, height);

        // Получение текста для отображения
        String text = dialogEvent.getEventDescription();
        if (text == null || text.isEmpty()) {
            return; // Если текст пустой, ничего не рисуем
        }

        // Настройки шрифта
        font.getData().setScale(1.82f); // Убедитесь, что масштаб шрифта установлен правильно

        // Максимальная ширина текста (с учетом отступов)
        float maxWidth = width - 30; // Отступы слева и справа
        float startY = y + height - font.getLineHeight() - 60; // Начинаем рисовку снизу вверх

        // Используем метод draw с автоматическим переносом строк
        font.draw(batch, text, x + 15, startY, maxWidth, 1, true);
    }

    private void drawDialogBox() {
        for (int i = 0; i < 3; i++) {
            if (dialogEvent.getDialogOptions()[i] != null) {
                batch.draw(dialogBox, dialogBoxBounds[i].getX(), dialogBoxBounds[i].getY(),
                    dialogBoxBounds[i].getWidth(), dialogBoxBounds[i].getHeight());
                font.draw(batch, dialogEvent.getDialogOptions()[i],
                    dialogBoxBounds[i].getX() + 110, dialogBoxBounds[i].getY() + 140);
            }
        }
    }

    private void input() {
        if (Gdx.input.isTouched()) {
            Vector2 touchPosition = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPosition);
            for (int i = 0; i < 3; i++) {
                if (dialogBoxBounds[i].contains(touchPosition) && dialogEvent.getDialogOptions()[i] != null) {
                    dialogEvent.action(player, map, i);
                    dispose();
                }
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
        if (textField != null) {
            textField.dispose();
            textField = null;
        }

        if (dialogBox != null) {
            dialogBox.dispose();
            dialogBox = null;
        }

        if (font != null) {
            font.dispose();
            font = null;
        }

        if (batch != null) {
            batch.dispose();
            batch = null;
        }

        if (dialogBoxBounds != null) {
            dialogBoxBounds = null;
        }


    }
}

