package io.github.some_example_name;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.utils.viewport.StretchViewport;


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
    public static StretchViewport viewport = new StretchViewport(2400, 1080);;



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
    private final List<PlayingCard> animationCardQueue =  new ArrayList<>();
    private final List<Integer> animationCardQueueIndex = new ArrayList<>();

    private Vector2 touchPos;

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
        // Инициализация StretchViewport
        batch = new SpriteBatch();

        BGImage = new Texture(Gdx.files.internal("menu/BG_2.png"));
        attackImage = new Texture(Gdx.files.internal("HUD/attak.png"));
        interfaceImage = new Texture(Gdx.files.internal("HUD/interface.png"));

        cardBounds = new Rectangle[HAND_META];
        isCardVisible = new boolean[HAND_META];
        initialCardPositionsX = new float[HAND_META];
        initialCardPositionsY = new float[HAND_META];

        if(enemies[1]!= null){
            enemies[1].getBounds().setX(enemies[0].getBounds().getX()+ enemies[0].getBounds().getWidth() + 200);
        }
        if(enemies[2] != null){
            enemies[2].getBounds().setX(enemies[0].getBounds().getX()- enemies[1].getBounds().getWidth() - 200);
        }

        preRenderCards();

        // Создаем невидимое поле для карт
        invisibleCardArea = new Rectangle(0, 0, viewport.getWorldWidth(), 250); // Задаем ширину и высоту

        cardInfoTexture = new Texture(Gdx.files.internal("HUD/card_info.png")); // Загружаем текстуру
        isCardInfoVisible = false; // Изначально информация о карте скрыта

        // Инициализация шрифта
        font = new BitmapFont(Gdx.files.internal("fonts/font.fnt"), Gdx.files.internal("fonts/font.png"), false);
        font.getData().setScale(2.0f);

        // Инициализация кнопки завершения хода
        endTurnButtonTexture = new Texture(Gdx.files.internal("HUD/endhod_new.png"));
        // Пример обновления кнопки завершения хода
        endTurnButtonBounds = new Rectangle(
            (float) (viewport.getWorldWidth() / 1.173),
            (float) (viewport.getWorldHeight() / 2.8),
            (float) (endTurnButtonTexture.getWidth()/0.7),
            (float)(endTurnButtonTexture.getHeight()/0.7));

        batch = new SpriteBatch();

        musicShow();

        playerTurn = true; // Начинаем с хода игрока

        cardAnimationTime = 0F;
    }


    @Override
    public void render(float delta) {
        viewport.apply();
        batch.setProjectionMatrix(viewport.getCamera().combined);

        elapsedTime += delta;

        if (!animationCardQueue.isEmpty()) {
            cardAnimationTime += delta;

            // Проверка завершения анимации
            if (animationCardQueue.get(0).effect.isAnimationFinished(cardAnimationTime)) {
                animationCardQueue.remove(0);
                animationCardQueueIndex.remove(0);
                cardAnimationTime = 0F;
            }
        }

        backgroundMusic.setLooping(true);
        backgroundMusic.setVolume(0.3f);
        backgroundMusic.play();

        ScreenUtils.clear(0, 0, 0, 1);
        batch.begin();
        batch.draw(BGImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());
        batch.draw(interfaceImage, 0, 0, viewport.getWorldWidth(), viewport.getWorldHeight());

        // Отображение значений здоровья и щита
        font.draw(batch, String.valueOf(player.getHealth()), 20, (float)(viewport.getWorldHeight() / 1.5));
        font.draw(batch, String.valueOf(player.getShield()), 120, (float)(viewport.getWorldHeight() / 1.5));
        font.draw(batch, String.valueOf(player.getManaPool()), 220, (float)(viewport.getWorldHeight() / 2.25));

        // Отрисовка противника
        for (int i = 0; i<3; i++){
            if(enemies[i] != null && enemies[i].isAlive()){
                enemies[i].draw(batch, font, elapsedTime,player);
            }
        }

        renderingCards();

        if (isDragging && draggedCard != null) {
            batch.draw(draggedCard, draggedCardX, draggedCardY, cardBounds[0].width, cardBounds[0].height);
        }

        //информация о карте
        if (isCardInfoVisible) {
            float cardInfoX = viewport.getWorldWidth() - cardInfoTexture.getWidth() - 10;
            float cardInfoY = viewport.getWorldHeight() - cardInfoTexture.getHeight() - 10;


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
            animationCardQueue.get(0).draw(cardAnimationTime, batch, enemies[animationCardQueueIndex.get(0)]);
        }

        handleInput();
        batch.end();
    }




    private void handleInput() {
        if (Gdx.input.isTouched()) {
            touchPos = new Vector2(Gdx.input.getX(), Gdx.input.getY());
            viewport.unproject(touchPos);


            float touchX = touchPos.x;
            float touchY = touchPos.y;


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
                    boolean returnCard = true;
                    if (manaPoolCheck()){
                        if(player.hand[draggedCardIndex] instanceof NonTargetCard &&
                            !invisibleCardArea.contains(cardBounds[draggedCardIndex])){
                            useCard();
                            returnCard = false;
                        }else {
                            for(int i = 0; i<3; i++) {
                                if (manaPoolCheck() && enemies[i] != null &&
                                    enemies[i].getBounds().contains(touchPos.x, Gdx.graphics.getHeight() - touchPos.y)) {
                                    useCard(i);
                                    returnCard = false;
                                    break;
                                }
                            }
                        }

                    }
                    if (returnCard){
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
        for (int i = 0; i<3; i++){
            if(enemies[i] != null && enemies[i].isAlive()){
                enemies[i].endTurn(player);
            }
        }

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
    public void resize(int width, int height) {
        viewport.update(width, height, true);
    }

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
        for(int i = 0; i<3 ; i++){
            if(enemies[i] != null){
                enemies[i].dispose();
            }
        }
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
        animationCardQueue.clear();
    }

    private void preRenderCards() {
        float cardHeight = 300;
        float cardWidth = (float)(cardHeight * 0.6); // Ширина карты
        float worldWidth = viewport.getWorldWidth();
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
        for (int i = 0; i < player.hand.length; i++) {
            if (isCardVisible[i] && player.hand[i] != null) {
                // Используем координаты cardBounds для отрисовки карт
                batch.draw(player.hand[i].getTexture(), cardBounds[i].x, cardBounds[i].y, cardBounds[i].width, cardBounds[i].height);


            }
        }
    }

    private void useCard(int i){
        animationCardQueue.add(player.hand[draggedCardIndex]);
        animationCardQueueIndex.add(i);
        player.playCard(enemies[i], draggedCardIndex);
        isCardInfoVisible = false; // Показываем информацию о карте
        preRenderCards();
        if (enemies[0].getHealth() <= 0) {
            backgroundMusic.stop();
            this.dispose();
            ((Main) Gdx.app.getApplicationListener()).setScreen(new FirstScreen());
        }


    }

    private void useCard(){
        animationCardQueue.add(player.hand[draggedCardIndex]);
        animationCardQueueIndex.add(0);
        player.playCard(enemies[0], draggedCardIndex);
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

    private void musicShow(){
        backgroundMusic = Gdx.audio.newMusic(Gdx.files.internal("music/fightMusic.mp3"));
        soundEffectCardTaking = Gdx.audio.newSound(Gdx.files.internal("sounds/takeCard.wav"));
        soundEffectPlaceCard = Gdx.audio.newSound(Gdx.files.internal("sounds/placeCard.wav"));
        soundEffectEndTurn = Gdx.audio.newSound(Gdx.files.internal("sounds/endTurn.wav"));
        soundEffectNotEnoughMana = Gdx.audio.newSound(Gdx.files.internal("sounds/notEnoughMana.wav"));
    }
}
//СОСАТЬ АМЕРИКА

