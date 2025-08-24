package io.github.some_example_name.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import io.github.some_example_name.Main;
import io.github.some_example_name.cards.PlayingCard;
import io.github.some_example_name.cards.non_target_cards.NonTargetCard;
import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;
import io.github.some_example_name.utils.CustomColors;


public class GameScreen implements Screen, DrawableScreen {
    public static final int HAND_META = 10;
    private static final float MODIFICATOR_FOR_CARD_NAME = 0.61f, MODIFIER_FOR_CARD_DESCRIPTION = 0.75F;
    private static final float MAX_SCALE_FOR_DRAGGED_CARD = 2f;
    private static final float LAYOUT_HEIGHT_FOR_NAME_CARDS = (float) 10 / 225;
    private static final float SPEED_SCALING_FOR_DRAGGED_CARD = 1.5f;
    private static final float LAYOUT_HEIGHT_FOR_DESCRIPTION_CARDS = 0.28f;
    public static StretchViewport viewport = new StretchViewport(2400, 1080);
    // Добавляем противника и игрока
    private final Enemy[] enemies;
    private final Player player;
    private final List<PlayingCard> animationCardQueue = new ArrayList<>();
    private final List<Integer> animationCardQueueIndex = new ArrayList<>();
    private final CellMap[][] map;
    private BitmapFont fontForDraggedCard, fontForCardInHand;
    private int attackingEnemyIndex;
    private Texture background;
    private boolean endTurnButtonPressed = false;
    private SpriteBatch batch;
    private Rectangle[] cardBounds;
    private boolean[] isCardVisible;
    private TextureRegion draggedCard;
    private float draggedCardX, draggedCardY;
    private boolean isDragging;
    private Integer draggedCardIndex;
    private PlayingCard[] choosingCards;
    private PlayingCard chooseCard;
    private boolean needChooseCard;
    private Rectangle[] choosingCardsRectangle;
    private float[] initialCardPositionsX, initialCardPositionsY;
    private Music backgroundMusic;
    private Sound soundEffectCardTaking, soundEffectPlaceCard, soundEffectEndTurn,
        soundEffectNotEnoughMana;
    private Texture interfaceImage;// Текстура для отображения информации о карте
    private String cardName; // Название карты
    private String cardDescription; // Описание карты

    private boolean playerTurn;
    private BitmapFont font;
    // Кнопка завершения хода
    private Texture endTurnButtonTexture;
    private Rectangle endTurnButtonBounds;
    private float elapsedTime = 0;
    private float cardAnimationTime;
    private Vector2 touchPos;
    // Невидимое поле для карт
    private Rectangle invisibleCardArea;
    private float scaleForDraggedCard;

    public GameScreen(Enemy[] enemies, MapScreen map) {
        this.enemies = enemies;
        this.map = map.getMap();
        this.player = map.getPlayer();
        player.beginFight();
        player.beginTurn();
    }

    @Override
    public StretchViewport getViewport() {
        return viewport;
    }

    @Override
    public void show() {
        // Инициализация StretchViewport
        batch = new SpriteBatch();
        viewportConfiguration();
        showBackGround();
        showChooseCard();
        showInterface();
        showCards();
        showEnemies();
        showFont();
        showEndButton();
        musicShow();
        scaleForDraggedCard = 0;
        playerTurn = true; // Начинаем с хода игрока
        cardAnimationTime = 0F;
    }

    private void viewportConfiguration() {
        viewport = new StretchViewport(2400, 1080);
        viewport.getCamera().position.set(viewport.getWorldWidth() / 2, viewport.getWorldHeight() / 2, 0);
        viewport.getCamera().update();
        viewport.apply();
    }


    private void showBackGround() {
        FileHandle dirHandle = Gdx.files.internal("background_act1");
        if (!dirHandle.exists()) {
            Gdx.app.error("Background", "Directory not found: " + dirHandle.path());
            return;
        }

        FileHandle[] files = dirHandle.list();
        if (files == null || files.length == 0) {
            Gdx.app.error("Background", "No files in directory: " + dirHandle.path());
            return;
        }

        Random random = new Random();
        int typeOfBackground = random.nextInt(files.length);
        background = new Texture(files[typeOfBackground]);

    }

    private void showChooseCard() {
        choosingCards = new PlayingCard[3];
        choosingCardsRectangle = new Rectangle[3];
        needChooseCard = false;
        float initialPositionX = 500;
        float modifier = 2;
        for (int i = 0; i < choosingCardsRectangle.length; i++) {
            choosingCardsRectangle[i] = new Rectangle(initialPositionX + (i + 1) * PlayingCard.WIDTH * modifier, 400,
                PlayingCard.WIDTH * modifier, PlayingCard.HEIGHT * modifier);
        }
    }

    private void showEndButton() {
        endTurnButtonTexture = new Texture(Gdx.files.internal("HUD/endhod_new.png"));
        endTurnButtonBounds = new Rectangle(
            (float) (viewport.getWorldWidth() / 1.173),
            (float) (viewport.getWorldHeight() / 2.8),
            (float) (endTurnButtonTexture.getWidth() / 0.7),
            (float) (endTurnButtonTexture.getHeight() / 0.7));
    }

    private void showInterface() {
        interfaceImage = new Texture(Gdx.files.internal("HUD/interface.png"));
    }

    private void showCards() {
        cardBounds = new Rectangle[HAND_META];
        isCardVisible = new boolean[HAND_META];
        initialCardPositionsX = new float[HAND_META];
        initialCardPositionsY = new float[HAND_META];
        invisibleCardArea = new Rectangle(0, 0, viewport.getWorldWidth(), 250); // Задаем ширину и высоту

        cardAnimationTime = 0F;
        preRenderCards();
    }

    private void showFont() {
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        font.getData().setScale(2.0f);
        fontForCardInHand = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        fontForCardInHand.getData().setScale(1.0f);
    }

    private void showEnemies() {
        if (enemies[1] != null) {
            enemies[1].getBounds().setX(viewport.getWorldWidth() / 4 - enemies[1].getBounds().width / 2);
        }
        if (enemies[2] != null) {
            enemies[2].getBounds().setX((viewport.getWorldWidth() / 4) * 3 - enemies[1].getBounds().width / 2);
        }
    }


    @Override
    public void render(float delta) {
        viewport.apply();
        elapsedTime += delta;
        cardAnimationUpdate(delta);
        music();
        draw(delta);
        handleInput();
        logic();

    }

    private void cardAnimationUpdate(float delta) {
        if (!animationCardQueue.isEmpty()) {
            cardAnimationTime += delta;
            // Проверка завершения анимации
            if (animationCardQueue.get(0).getEffect().isAnimationFinished(cardAnimationTime)) {
                animationCardQueue.remove(0);
                animationCardQueueIndex.remove(0);
                cardAnimationTime = 0F;
            }
        }
    }

    private void music() {
        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.3f);
        backgroundMusic.play();
    }

    @Override
    public void draw(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        batch.setProjectionMatrix(viewport.getCamera().combined);
        batch.begin();
        backgroundDraw();
        interfaceDraw();
        playerStatsDraw();
        enemiesDraw(delta);
        renderingCards();
        draggedCardDraw();


        if (!animationCardQueue.isEmpty()) {
            cardAnimationDraw();
        }
        if (needChooseCard) {
            choosingCardDraw();
        }
        batch.end();
        scalingDraggedCard(delta);

    }

    private void backgroundDraw() {
        batch.draw(background, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
    }

    private void logic() {
        if (isPlayerWin()) {
            choosingCardLogic();
        }
        attackingLogic();
    }

    private void attackingLogic() {
        if (!playerTurn) {
            if (!enemies[attackingEnemyIndex].isAttacking()) {
                for (int i = attackingEnemyIndex + 1; i < enemies.length; i++) {
                    if (enemies[i] != null && enemies[i].isAlive()) {
                        attackingEnemyIndex = i;
                        enemies[attackingEnemyIndex].setAttacking(true);
                        return;
                    }
                }
                playerTurn = true;
                player.beginTurn();
            }
        }
    }

    private void choosingCardLogic() {
        if (choosingCards[0] == null) {
            generateCardsForWin();
        }
        if (chooseCard == null) {
            needChooseCard = true;
            player.dropDeckClear();
            player.draftDeckClear();
            player.handClear();
        }
        if (chooseCard != null) {
            endFight();
        }

    }

    private void generateCardsForWin() {
        choosingCards = PlayingCard.generateCard(3);
    }

    private void choosingCardDraw() {
        font.draw(batch, "Выберите карты", 300, 300);

        for (int i = 0; i < choosingCards.length; i++) {
            if (choosingCards[i] != null) {
                // Отрисовка текстуры карты
                batch.draw(choosingCards[i].getTexture(),
                    choosingCardsRectangle[i].x,
                    choosingCardsRectangle[i].y,
                    choosingCardsRectangle[i].width,
                    choosingCardsRectangle[i].height);

                // --- Отрисовка стоимости карты ---
                if (choosingCards[i].getCost() <= player.getManaPool()) {
                    fontForCardInHand.setColor(Color.GREEN);
                } else {
                    fontForCardInHand.setColor(Color.RED);
                }

                fontForCardInHand.getData().setScale(2.0f);
                fontForCardInHand.draw(
                    batch,
                    String.valueOf(choosingCards[i].getCost()),
                    choosingCardsRectangle[i].x + 20,
                    choosingCardsRectangle[i].y + choosingCardsRectangle[i].getHeight() - 20);

                fontForCardInHand.getData().setScale(2.0f);
                fontForCardInHand.setColor(Color.WHITE);

                float width = choosingCardsRectangle[i].width * MODIFICATOR_FOR_CARD_NAME;
                GlyphLayout layout = new GlyphLayout(fontForCardInHand, choosingCards[i].getName(), Color.WHITE, width, Align.center, true);
                float x = choosingCardsRectangle[i].getX() + choosingCardsRectangle[i].getWidth() / 2 - width / 2;
                float y = choosingCardsRectangle[i].getY() + choosingCardsRectangle[i].height * 0.52f;
                while (layout.height > choosingCardsRectangle[i].getHeight() * LAYOUT_HEIGHT_FOR_NAME_CARDS) {
                    fontForCardInHand.getData().setScale(fontForCardInHand.getScaleX() * 0.99f);
                    layout.setText(fontForCardInHand, choosingCards[i].getName(), Color.WHITE, width, Align.center, true);
                }
                fontForCardInHand.draw(batch, layout, x, y);
                fontForCardInHand.setColor(Color.WHITE);
                fontForCardInHand.getData().setScale(2.0f);

                width = choosingCardsRectangle[i].width * MODIFIER_FOR_CARD_DESCRIPTION;
                x = choosingCardsRectangle[i].getX() + choosingCardsRectangle[i].getWidth() / 2 - width / 2;
                y = choosingCardsRectangle[i].getY() + choosingCardsRectangle[i].height * 0.40f;
                layout.setText(fontForCardInHand, choosingCards[i].getDescription(), Color.WHITE, width, Align.center, true);
                while (layout.height > choosingCardsRectangle[i].height * 0.32f) {
                    fontForCardInHand.getData().setScale(fontForCardInHand.getScaleX() * 0.99f);
                    layout.setText(fontForCardInHand, choosingCards[i].getDescription(), Color.WHITE, width, Align.center, true);
                }
                fontForCardInHand.draw(batch, layout, x, y);
                fontForCardInHand.setColor(Color.WHITE);
                fontForCardInHand.getData().setScale(1.0f);
            }
        }

    }

    private void interfaceDraw() {
        batch.draw(interfaceImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.draw(endTurnButtonTexture, endTurnButtonBounds.x, endTurnButtonBounds.y);
    }

    private void playerStatsDraw() {
        font.draw(batch, String.valueOf(player.getHealth()), 20, (float) (viewport.getWorldHeight() / 1.5));
        font.draw(batch, String.valueOf(player.getShield()), 120, (float) (viewport.getWorldHeight() / 1.5));
        font.draw(batch, String.valueOf(player.getManaPool()), 220, (float) (viewport.getWorldHeight() / 2.25));
    }

    private void enemiesDraw(float delta) {
        for (int i = 0; i < 3; i++) {
            if (enemies[i] != null && enemies[i].isAlive()) {
                enemies[i].draw(batch, font, delta, player);
            }
        }
    }

    private void draggedCardDraw() {
        if (isDragging && draggedCard != null) {
            PlayingCard card = player.getHand()[draggedCardIndex];

            // Рассчитываем размер перетаскиваемой карты с учетом масштаба
            float scaledWidth = cardBounds[0].width * scaleForDraggedCard;
            float scaledHeight = cardBounds[0].height * scaleForDraggedCard;

            batch.draw(draggedCard, draggedCardX, draggedCardY, scaledWidth, scaledHeight);

            // --- Отрисовка стоимости карты ---
            if (card.getCost() <= player.getManaPool()) {
                fontForCardInHand.setColor(Color.GREEN);
            } else {
                fontForCardInHand.setColor(Color.WHITE);
            }

            // Масштабируем шрифт стоимости пропорционально карте
            fontForCardInHand.getData().setScale(scaleForDraggedCard);
            fontForCardInHand.draw(
                batch,
                String.valueOf(card.getCost()),
                draggedCardX + 20 * scaleForDraggedCard,
                draggedCardY + scaledHeight - 20 * scaleForDraggedCard
            );


            fontForCardInHand.setColor(Color.WHITE);
            fontForCardInHand.getData().setScale(2.0f);


            float width = scaledWidth * MODIFICATOR_FOR_CARD_NAME;

            GlyphLayout layout = new GlyphLayout(fontForCardInHand, player.getHand()[draggedCardIndex].getName(), CustomColors.DARK_BROWN, width, Align.center, true);
            float x = draggedCardX + scaledWidth / 2 - width / 2;
            float y = draggedCardY + scaledHeight * 0.52f;
            while (layout.height > scaledHeight * LAYOUT_HEIGHT_FOR_NAME_CARDS) {
                fontForCardInHand.getData().setScale(fontForCardInHand.getScaleX() * 0.99f);
                layout.setText(fontForCardInHand, player.getHand()[draggedCardIndex].getName(), CustomColors.DARK_BROWN, width, Align.center, true);
            }
            fontForCardInHand.draw(batch, layout, x, y);
            fontForCardInHand.setColor(Color.WHITE);
            fontForCardInHand.getData().setScale(2.0f);

            width = scaledWidth * MODIFIER_FOR_CARD_DESCRIPTION;
            x = draggedCardX + scaledWidth / 2 - width / 2;
            y = draggedCardY + scaledHeight * 0.40f;
            layout.setText(fontForCardInHand, player.getHand()[draggedCardIndex].getDescription(), Color.BROWN, width, Align.center, true);
            while (layout.height > scaledHeight * LAYOUT_HEIGHT_FOR_DESCRIPTION_CARDS) {
                fontForCardInHand.getData().setScale(fontForCardInHand.getScaleX() * 0.99f);
                layout.setText(fontForCardInHand, player.getHand()[draggedCardIndex].getDescription(),  Color.BROWN, width, Align.center, true);
            }
            fontForCardInHand.draw(batch, layout, x, y);

            fontForCardInHand.setColor(Color.WHITE);
            fontForCardInHand.getData().setScale(1.0f);


        }
    }

    private void scalingDraggedCard(float delta) {
        if (draggedCard == null) {
            scaleForDraggedCard = 1f;
        } else {
            if (scaleForDraggedCard <= MAX_SCALE_FOR_DRAGGED_CARD) {
                scaleForDraggedCard += delta * SPEED_SCALING_FOR_DRAGGED_CARD;
            }
        }
    }


    private void cardAnimationDraw() {
        animationCardQueue.get(0).drawAnimation(cardAnimationTime, batch, enemies[animationCardQueueIndex.get(0)]);
    }


    private void handleInput() {
        if (Gdx.input.isTouched()) {
            touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);
            if (!isPlayerWin()) {
                endTurnButtonPressed = endTurnButtonBounds.contains(touchPos) && playerTurn;
            }
            if (!isDragging) {
                if (needChooseCard) {
                    chooseCardsTouching();
                }
                touchCards();

            } else {
                moveCard();
            }
        } else {
            if (isDragging) {
                placeCard();
            }
            isDragging = false; // Завершаем перетаскивание
            draggedCard = null; // Освобождаем ссылку на перетаскиваемую карту
            if (endTurnButtonPressed) {
                soundEffectEndTurn.play(0.8f);
                endTurn(); // Завершение хода
                endTurnButtonPressed = false; // Сбрасываем флаг
            }
        }
    }

    private void chooseCardsTouching() {
        for (int i = 0;choosingCards != null && i < choosingCards.length; i++) {
            if (choosingCards[i] != null && choosingCardsRectangle[i].contains(touchPos)) {
                chooseCard = choosingCards[i];
                needChooseCard = false;
                choosingCardsDispose();
                break;
            }
        }
    }

    private void placeCard() {
        if (invisibleCardArea.contains(touchPos)) {
            soundEffectPlaceCard.play(0.7f);
            returnCard();
        } else {
            tryUseCard();
        }
    }

    private void tryUseCard() {
        boolean returnCard = true;
        if (manaPoolCheck()) {
            if (player.getHand()[draggedCardIndex] instanceof NonTargetCard &&
                !invisibleCardArea.contains(cardBounds[draggedCardIndex])) {
                useCard();
                returnCard = false;
            } else {
                for (int i = 0; i < 3; i++) {
                    if (enemies[i] != null && enemies[i].getBounds().contains(touchPos)) {
                        useCard(i);
                        returnCard = false;
                        break;
                    }
                }
            }

        }
        if (returnCard) {
            soundEffectNotEnoughMana.play(0.7f);
            returnCard();
        }
    }

    private void touchCards() {
        for (int i = 0; i < cardBounds.length; i++) {
            if (player.getHand()[i] != null && isCardVisible[i] && cardBounds[i].contains(touchPos)) {
                takeCard(i);
                break;
            }
        }
    }

    private void takeCard(int index) {
        isDragging = true;
        draggedCard = new TextureRegion(player.getHand()[index].getTexture());
        draggedCardX = touchPos.x - cardBounds[index].width * scaleForDraggedCard / 2;
        draggedCardY = touchPos.y - cardBounds[index].height * scaleForDraggedCard / 2;


        // Сохраняем информацию о карте
        cardName = player.getHand()[index].getName();
        cardDescription = player.getHand()[index].getDescription();


        // Сделаем карту невидимой сразу, как только она берется
        soundEffectCardTaking.play(0.7f);
        isCardVisible[index] = false;
        draggedCardIndex = index;
    }

    private void moveCard() {
        // Обновляем позицию перетаскиваемой карты
        draggedCardX = touchPos.x - (float) draggedCard.getRegionWidth() * scaleForDraggedCard / 2;
        draggedCardY = touchPos.y - (float) draggedCard.getRegionHeight() * scaleForDraggedCard / 2;
    }

    private void endTurn() {
        playerTurn = false;// Завершаем ход игрока
        if (player.getHealth() <= 0) {
            backgroundMusic.stop();
            this.dispose();
            ((Main) Gdx.app.getApplicationListener()).setScreen(new FirstScreen());
        }
        player.endTurn();
        attackingEnemyIndex = findFirstAliveEnemy();
        enemies[attackingEnemyIndex].setAttacking(true);
        Arrays.fill(isCardVisible, true);
    }

    private int findFirstAliveEnemy() {
        for (int i = 0; i < enemies.length; i++) {
            if (enemies[i] != null && enemies[i].isAlive()) {
                return i;
            }
        }
        return 0;
    }

    @Override
    public void resize(int width, int height) {
        viewport.update(width, height, true);
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
        try {
            // Освобождаем текстуры
            if (background != null) {
                background.dispose();
                background = null;
            }


            if (interfaceImage != null) {
                interfaceImage.dispose();
                interfaceImage = null;
            }

            if (endTurnButtonTexture != null) {
                endTurnButtonTexture.dispose();
                endTurnButtonTexture = null;
            }

            // Освобождаем шрифты
            if (font != null) {
                font.dispose();
                font = null;
            }

            if (fontForDraggedCard != null) {
                fontForDraggedCard.dispose();
                fontForDraggedCard = null;
            }

            if (fontForCardInHand != null) {
                fontForCardInHand.dispose();
                fontForCardInHand = null;
            }

            // Освобождаем SpriteBatch
            if (batch != null) {
                batch.dispose();
                batch = null;
            }

            // Освобождаем аудио ресурсы
            if (backgroundMusic != null) {
                backgroundMusic.stop();
                backgroundMusic.dispose();
                backgroundMusic = null;
            }

            if (soundEffectCardTaking != null) {
                soundEffectCardTaking.dispose();
                soundEffectCardTaking = null;
            }

            if (soundEffectPlaceCard != null) {
                soundEffectPlaceCard.dispose();
                soundEffectPlaceCard = null;
            }

            if (soundEffectEndTurn != null) {
                soundEffectEndTurn.dispose();
                soundEffectEndTurn = null;
            }

            if (soundEffectNotEnoughMana != null) {
                soundEffectNotEnoughMana.dispose();
                soundEffectNotEnoughMana = null;
            }

            // Освобождаем врагов
            if (enemies != null) {
                for (int i = 0; i < enemies.length; i++) {
                    if (enemies[i] != null) {
                        enemies[i].dispose();
                        enemies[i] = null;
                    }
                }
            }

            // Освобождаем карты выбора
            choosingCardsDispose();

            // Очищаем очереди анимаций
            animationCardQueue.clear();
            animationCardQueueIndex.clear();

            // Очищаем массивы и коллекции
            if (cardBounds != null) {
                cardBounds = null;
            }

            if (isCardVisible != null) {
                isCardVisible = null;
            }

            if (initialCardPositionsX != null) {
                initialCardPositionsX = null;
            }

            if (initialCardPositionsY != null) {
                initialCardPositionsY = null;
            }

            if (choosingCardsRectangle != null) {
                choosingCardsRectangle = null;
            }

            // Освобождаем draggedCard
            if (draggedCard != null) {
                draggedCard.getTexture().dispose();
                draggedCard = null;
            }

        } catch (Exception e) {
            Gdx.app.error("GameScreen", "Error during dispose", e);
        }
    }

    private void choosingCardsDispose() {
        Arrays.fill(choosingCards, null);
    }

    private void preRenderCards() {
        float cardHeight = 300;
        float cardWidth = (float) (cardHeight * 0.6); // Ширина карты
        float startX = 500; // Центрирование по X
        float startY = 20; // Фиксированная позиция Y


        for (int i = 0; i < cardBounds.length; i++) {
            cardBounds[i] = new Rectangle(startX + i * cardWidth, startY, cardWidth, cardHeight);


            isCardVisible[i] = true;
            initialCardPositionsX[i] = cardBounds[i].x;
            initialCardPositionsY[i] = cardBounds[i].y;
        }
    }

    private void renderingCards() {
        for (int i = 0; i < player.getHand().length; i++) {
            if (isCardVisible[i] && player.getHand()[i] != null) {
                batch.draw(player.getHand()[i].getTexture(), cardBounds[i].x, cardBounds[i].y, cardBounds[i].width, cardBounds[i].height);
                if (player.getHand()[i].getCost() <= player.getManaPool()) {
                    fontForCardInHand.setColor(Color.GREEN);
                } else {
                    fontForCardInHand.setColor(Color.RED);
                }

                fontForCardInHand.getData().setScale(1.0f);
                fontForCardInHand.draw(
                    batch,
                    String.valueOf(player.getHand()[i].getCost()),
                    cardBounds[i].x + 20,
                    cardBounds[i].y + cardBounds[i].getHeight() - 20);
                fontForCardInHand.setColor(Color.WHITE);
                fontForCardInHand.getData().setScale(2.0f);

                float width = cardBounds[i].width * MODIFICATOR_FOR_CARD_NAME;
                GlyphLayout layout = new GlyphLayout(fontForCardInHand, player.getHand()[i].getName(),  CustomColors.DARK_BROWN, width, Align.center, true);
                float x = cardBounds[i].getX() + cardBounds[i].getWidth() / 2 - width / 2;
                float y = cardBounds[i].getY() + cardBounds[i].height * 0.52f;
                while (layout.height > cardBounds[i].height * LAYOUT_HEIGHT_FOR_NAME_CARDS) {
                    fontForCardInHand.getData().setScale(fontForCardInHand.getScaleX() * 0.99f);
                    layout.setText(fontForCardInHand, player.getHand()[i].getName(),  CustomColors.DARK_BROWN, width, Align.center, true);
                }
                fontForCardInHand.draw(batch, layout, x, y);
                fontForCardInHand.setColor(Color.WHITE);
                fontForCardInHand.getData().setScale(2.0f);

                width = cardBounds[i].width * MODIFIER_FOR_CARD_DESCRIPTION;
                x = cardBounds[i].getX() + cardBounds[i].getWidth() / 2 - width / 2;
                y = cardBounds[i].getY() + cardBounds[i].height * 0.40f;
                layout.setText(fontForCardInHand, player.getHand()[i].getDescription(),  Color.BROWN, width, Align.center, true);
                while (layout.height > cardBounds[i].height * LAYOUT_HEIGHT_FOR_DESCRIPTION_CARDS) {
                    fontForCardInHand.getData().setScale(fontForCardInHand.getScaleX() * 0.99f);
                    layout.setText(fontForCardInHand, player.getHand()[i].getDescription(),  Color.BROWN, width, Align.center, true);
                }
                fontForCardInHand.draw(batch, layout, x, y);
                fontForCardInHand.setColor(Color.WHITE);
                fontForCardInHand.getData().setScale(1.0f);

            }
        }
    }

    private void useCard(int i) {
        animationCardQueue.add(player.getHand()[draggedCardIndex]);
        animationCardQueueIndex.add(i);

        enemies[i].buffActionTrigger("CardAction");

        player.playCard(enemies[i], draggedCardIndex);
        preRenderCards();
    }

    private void useCard() {
        animationCardQueue.add(player.getHand()[draggedCardIndex]);
        animationCardQueueIndex.add(0);
        player.playCard(enemies[0], draggedCardIndex);
        preRenderCards();
    }

    private void returnCard() {
        cardBounds[draggedCardIndex].setPosition(initialCardPositionsX[draggedCardIndex],
            initialCardPositionsY[draggedCardIndex]);


        isCardVisible[draggedCardIndex] = true;
    }

    private boolean manaPoolCheck() {
        return player.getManaPool() - player.getHand()[draggedCardIndex].getCost() >= 0;


    }

    private void musicShow() {
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/fightMusic.mp3"));
        soundEffectCardTaking = Gdx.audio.newSound(Gdx.files.internal("sounds/takeCard.wav"));
        soundEffectPlaceCard = Gdx.audio.newSound(Gdx.files.internal("sounds/placeCard.wav"));
        soundEffectEndTurn = Gdx.audio.newSound(Gdx.files.internal("sounds/endTurn.wav"));
        soundEffectNotEnoughMana = Gdx.audio.newSound(Gdx.files.internal("sounds/notEnoughMana.wav"));
    }

    private boolean isPlayerWin() {
        boolean isPlayerWin = true;
        for (int i = 0; i < 3; i++) {
            if (enemies[i] != null && enemies[i].isAlive()) {
                isPlayerWin = false;
                break;
            }
        }
        return isPlayerWin;

    }

    private void endFight() {

        backgroundMusic.stop();
        player.addCardInDeck(chooseCard);
        dispose();
        ((Main) Gdx.app.getApplicationListener()).setScreen(new MapScreen(player, map));


    }

}

