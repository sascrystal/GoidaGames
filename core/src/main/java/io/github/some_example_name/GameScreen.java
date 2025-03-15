package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Music;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

    public class GameScreen implements Screen {
    private boolean endTurnButtonPressed = false;
    private SpriteBatch batch;
    private Rectangle[] cardBounds;
    private boolean[] isCardVisible;
    private TextureRegion draggedCard;
    private float draggedCardX, draggedCardY;
    private boolean isDragging;
    private int draggedCardIndex;

    public static final int HAND_META = 10;

    private float[] initialCardPositionsX,initialCardPositionsY;

    private Music backgroundMusic;

    private Sound soundEffectCardTaking, soundEffectPlaceCard, soundEffectEndTurn,
        soundEffectNotEnoughMana;

    private Texture cardInfoTexture,attackImage, BGImage, interfaceImage;// Текстура для отображения информации о карте

    private String cardName; // Название карты
    private String cardDescription; // Описание карты
    private boolean isCardInfoVisible;

    // Добавляем противника и игрока
    private Enemy[] enemies = new Enemy[3];
    private final Player player;
    private boolean playerTurn;
    private BitmapFont font;

    // Кнопка завершения хода
    private Texture endTurnButtonTexture;
    private Rectangle endTurnButtonBounds;

    private  float elapsedTime = 0;

    private float cardAnimationTime;
    private List<PlayingCard> animationCardQueue =  new ArrayList<>();


    // Невидимое поле для карт
    private Rectangle invisibleCardArea;

    public GameScreen(Enemy[] enemies, Player player){
        this.enemies = enemies;
        this.player = player;
        player.beginFight();
        player.beginTurn();
    }



    @Override
    public void show() {
        BGImage = new Texture(Gdx.files.internal("menu/BG_2.png"));
        attackImage = new Texture(Gdx.files.internal("HUD/attak.png"));
        interfaceImage = new Texture(Gdx.files.internal("HUD/interface.png"));

        cardBounds = new Rectangle[HAND_META];
        isCardVisible = new boolean[HAND_META];
        initialCardPositionsX = new float[HAND_META];
        initialCardPositionsY = new float[HAND_META];

        preRenderCards();

        // Создаем невидимое поле для карт
        invisibleCardArea = new Rectangle(0, 0, Gdx.graphics.getWidth(), 250); // Задаем ширину и высоту


        cardInfoTexture = new Texture(Gdx.files.internal("HUD/card_info.png")); // Загружаем текстуру
        isCardInfoVisible = false; // Изначально информация о карте скрыта


        // Инициализация шрифта
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        font.getData().setScale(2.0f);

        // Инициализация кнопки завершения хода
        endTurnButtonTexture = new Texture(Gdx.files.internal("HUD/endhod_new.png"));
        endTurnButtonBounds = new Rectangle(
            (float) (Gdx.graphics.getWidth() / 1.173),
            (float) (Gdx.graphics.getHeight() / 2.8),
            endTurnButtonTexture.getWidth(),
            endTurnButtonTexture.getHeight());

        batch = new SpriteBatch();

        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/fightMusic.mp3"));
        soundEffectCardTaking = Gdx.audio.newSound(Gdx.files.internal("sounds/takeCard.wav"));
        soundEffectPlaceCard = Gdx.audio.newSound(Gdx.files.internal("sounds/placeCard.wav"));
        soundEffectEndTurn = Gdx.audio.newSound(Gdx.files.internal("sounds/endTurn.wav"));
        soundEffectNotEnoughMana = Gdx.audio.newSound(Gdx.files.internal("sounds/notEnoughMana.wav"));

        playerTurn = true; // Начинаем с хода игрока

        cardAnimationTime = 0F;

    }

    @Override
    public void render(float delta) {
        elapsedTime += delta;

        if (!animationCardQueue.isEmpty()) {
            cardAnimationTime += delta;

            // Проверка завершения анимации
            if (animationCardQueue.get(0).effect.isAnimationFinished(cardAnimationTime)) {
                animationCardQueue.remove(0);
                cardAnimationTime = 0F;
            }
        }


        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.3f);
        backgroundMusic.play();

        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(BGImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        batch.draw(attackImage, (float)(Gdx.graphics.getWidth()/1.7), (float)(Gdx.graphics.getHeight()/1.5));
        batch.draw(interfaceImage, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        // Отображение значений здоровья и щита
        font.draw(batch, String.valueOf(player.getHealth()), 20, (float)(Gdx.graphics.getHeight()/1.5));
        font.draw(batch, String.valueOf(player.getShield()), 120, (float)(Gdx.graphics.getHeight()/1.5));
        font.draw(batch, String.valueOf(player.getManaPool()), 220, (float)(Gdx.graphics.getHeight()/2.25));

        // Отрисовка противника
        enemies[0].draw(batch, elapsedTime);

        if (enemies[0].isAlive()) {
            font.draw(batch,
                "Health: " + enemies[0].getHealth(),
                (float)(Gdx.graphics.getWidth() / 2.3),
                enemies[0].getBounds().y + enemies[0].getBounds().height + 70);
            enemies[0].draw(batch,elapsedTime); // Рисуем противника

        }

        // Отображение атаки противника
        if (enemies[0].isAlive()) {
            font.draw(batch, String.valueOf(enemies[0].moveList[enemies[0].getIndexMoveList()].showNumericalValue(enemies[0], player)), (float)(Gdx.graphics.getWidth()/1.55), (float)(Gdx.graphics.getHeight()/1.38)); // Отображение Атаки
        }

        // Отображаем текстуру поля карт, масштабируя её под размеры невидимого поля
        //batch.draw(cardFieldTexture, invisibleCardArea.x, invisibleCardArea.y,invisibleCardArea.width + 100, invisibleCardArea.height + 100);

        renderingCards();

        // Если карта перетаскивается, отрисовываем её в позиции курсора
        if (isDragging && draggedCard != null) {
            batch.draw(draggedCard, draggedCardX, draggedCardY,cardBounds[0].width, cardBounds[0].height);
        }

        //информация о карте
        if (isCardInfoVisible) {
            float cardInfoX = Gdx.graphics.getWidth() - cardInfoTexture.getWidth() - 10;
            float cardInfoY = Gdx.graphics.getHeight() - cardInfoTexture.getHeight() - 10;

            // Отрисовка текстуры информации о карте
            batch.draw(cardInfoTexture, cardInfoX, cardInfoY);

            // Отрисовка названия карты
            font.draw(batch, cardName, cardInfoX + ((float) cardInfoTexture.getWidth() / 2) - 250, cardInfoY + cardInfoTexture.getHeight() - 10); // Отображаем название карты

            // Отрисовка описания карты с переносом текста
            float descriptionX = cardInfoX + 10; // Отступ для описания
            float descriptionY = cardInfoY + cardInfoTexture.getHeight() - 110; // Позиция для описания
            float maxWidth = cardInfoTexture.getWidth() - 20; // Максимальная ширина для текста (отступы)

            // Используем метод draw с учетом ширины
            font.draw(batch, cardDescription, descriptionX, descriptionY, maxWidth, 1, true); // true для переноса строк
        }

        // Отрисовка кнопки завершения хода
        batch.draw(endTurnButtonTexture, endTurnButtonBounds.x, endTurnButtonBounds.y);


        if(!animationCardQueue.isEmpty()){
            animationCardQueue.get(0).draw(cardAnimationTime, batch, enemies[0]);
            if (animationCardQueue.get(0).effect.isAnimationFinished(cardAnimationTime)) {
                animationCardQueue.remove(0);
                cardAnimationTime = 0F;
            }
        }


        batch.end();
        handleInput();

    }


    private void handleInput() {
        if (Gdx.input.isTouched()) {

            float touchX = Gdx.input.getX();
            float touchY = Gdx.graphics.getHeight() - Gdx.input.getY();

            // Проверка нажатия на кнопку завершения хода
            // Сбрасываем флаг, если не нажатие на кнопку
            endTurnButtonPressed = endTurnButtonBounds.contains(touchX, touchY) && playerTurn; // Устанавливаем флаг нажатия

            if (!isDragging) {

                for (int i = 0; i < cardBounds.length; i++) {
                    if (player.hand[i]!= null && isCardVisible[i] && cardBounds[i].contains(touchX, touchY)) {
                        isDragging = true;
                        draggedCard = new TextureRegion(player.hand[i].texture);
                        draggedCardX = touchX - cardBounds[i].width / 2;
                        draggedCardY = touchY - cardBounds[i].height / 2;

                        // Сохраняем информацию о карте
                        cardName = player.hand[i].getName();
                        cardDescription = player.hand[i].getDescription();
                        isCardInfoVisible = true; // Показываем информацию о карте

                        // Сделаем карту невидимой сразу, как только она берется
                        soundEffectCardTaking.play(0.7f);
                        isCardVisible[i] = false;
                        draggedCardIndex = i;
                        break;
                    }
                }
            } else {
                isCardInfoVisible = true; // Скрываем информацию о карте
                // Обновляем позицию перетаскиваемой карты
                draggedCardX = touchX -  (float) draggedCard.getRegionWidth() / 2;
                draggedCardY = touchY -  (float) draggedCard.getRegionHeight() / 2;
            }
        } else {
            if (isDragging) {


                // Проверяем, находится ли карта в невидимом поле
                if (invisibleCardArea.contains(draggedCardX, draggedCardY)) {
                    soundEffectPlaceCard.play(0.7f);
                    // Возвращаем карту на исходную позицию
                    returnCard();
                } else {

                    // Если карта не попадает на противника, обрабатываем ее
                    if (manaPoolCheck() && enemies[0].getBounds().overlaps(new Rectangle(draggedCardX, draggedCardY, draggedCard.getRegionWidth(), draggedCard.getRegionHeight()))) {
                        useCard();
                    } else if (player.hand[draggedCardIndex] instanceof NonTargetCard && manaPoolCheck() &&!invisibleCardArea.contains(draggedCardX, draggedCardY)){
                        useCard();
                    } else {
                        soundEffectNotEnoughMana.play(0.7f);
                        returnCard();
                    }
                }
            }
            isDragging = false; // Завершаем перетаскивание
            draggedCard = null; // Освобождаем ссылку на перетаскиваемую карту

            // Если кнопка завершения хода была нажата, выполняем завершение хода
            if (endTurnButtonPressed) {
                soundEffectEndTurn.play(0.8f);
                endTurn(); // Завершение хода
                endTurnButtonPressed = false; // Сбрасываем флаг
            }
        }
    }

    private void endTurn() {
        playerTurn = false;// Завершаем ход игрока
        enemies[0].endTurn(player);
        // Проверка здоровья игрока
        if (player.getHealth() <= 0) {
            backgroundMusic.stop();
            this.dispose();
            ((Main) Gdx.app.getApplicationListener()).setScreen(new FirstScreen());
        }

        player.endTurn();

        playerTurn = true; // Ход переходит обратно к игроку
        player.beginTurn(); // Обновляем карты в руке игрока

        // Обновляем видимость карт
        // Делаем все карты видимыми (или можете настроить по вашему усмотрению)
        Arrays.fill(isCardVisible, true);
    }


    @Override
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        cardInfoTexture.dispose();
        batch.dispose();
        BGImage.dispose();
        interfaceImage.dispose();
        attackImage.dispose();
        endTurnButtonTexture.dispose();
        enemies[0].dispose();
        font.dispose();

        for (int i = 0; i < player.hand.length-1 && player.hand[i] != null;  i++) {
            player.hand[i].texture.dispose();
        }

        if (backgroundMusic != null) {
            backgroundMusic.stop();
            backgroundMusic.dispose();
        }

        soundEffectEndTurn.dispose();
        soundEffectCardTaking.dispose();
        soundEffectNotEnoughMana.dispose();
        soundEffectPlaceCard.dispose();


    }

    private void preRenderCards() {
        float cardHeight = 300;
        float cardWidth = (float)(cardHeight*0.7); // Ширина карты
         // Высота карты
        float startX = (float) ((Gdx.graphics.getWidth() - (cardWidth * player.hand.length)) / 1.2); // Центрирование по X
        float startY = 50; // Фиксированная позиция Y

        for (int i = 0; i < cardBounds.length; i++) {
            cardBounds[i] = new Rectangle(startX + i * cardWidth, startY, cardWidth, cardHeight);

            isCardVisible[i] = true;
            initialCardPositionsX[i] = cardBounds[i].x;
            initialCardPositionsY[i] = cardBounds[i].y;
        }
    }

    private void renderingCards() {
        for (int i = 0; i < player.hand.length; i++) {
            if (isCardVisible[i] && player.hand[i] != null) {
                // Используем координаты cardBounds для отрисовки карт
                batch.draw(player.hand[i].getTexture(),
                    cardBounds[i].x, cardBounds[i].y,
                    cardBounds[i].width,cardBounds[i].height);

            }
        }
    }

    private void useCard(){
        animationCardQueue.add(player.hand[draggedCardIndex]);
        player.playCard(enemies[0], draggedCardIndex);
        enemies[0].enemyReactionOfCard(player,draggedCardIndex);
        isCardInfoVisible = false; // Показываем информацию о карте
        preRenderCards();
        if (enemies[0].getHealth() <= 0) {
            backgroundMusic.stop();
            this.dispose();
            ((Main) Gdx.app.getApplicationListener()).setScreen(new FirstScreen());
        }
    }

    private void returnCard(){
        cardBounds[draggedCardIndex].setPosition(initialCardPositionsX[draggedCardIndex],
            initialCardPositionsY[draggedCardIndex]);

        isCardVisible[draggedCardIndex] = true;
        isCardInfoVisible = false;
    }

    private boolean manaPoolCheck(){
        return player.manaPool - player.hand[draggedCardIndex].cost >= 0;

    }

    private boolean enemyExist(short index){
        return enemies[index] == null;
    }



}
//СОСАТЬ АМЕРИКА
