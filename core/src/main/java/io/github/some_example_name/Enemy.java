package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.utils.Array;

import java.util.ArrayList;
import java.util.List;
public abstract class Enemy {
    public Texture texture; // Текстура противника
    protected Rectangle bounds; // Границы для проверки коллизий
    protected Animation<TextureRegion> animation; // Анимация для противника
    public int health; // Здоровье противника

    protected List<Buff> buffs = new ArrayList<>();

    public MoveEnemy[] moveList;
    public float stateTime;// Время для анимации
    public int indexMoveList;








    protected Sound takingDamageSoundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/takingDamageGamblerSoundEffect.wav"));

    public void draw(SpriteBatch batch, BitmapFont font, float elapsedTime,Player player) {
        TextureRegion currentFrame = animation.getKeyFrame(elapsedTime, true);
        batch.draw(currentFrame,
            bounds.x,
            bounds.y,
            bounds.width,
            bounds.height);// Получаем текущий кадр анимации

        font.draw(batch, "Health: " + health, bounds.getX(), bounds.getY() + bounds.getHeight() + 100);

        moveList[getIndexMoveList()].draw(batch,font, elapsedTime,this,player);


    }

    public int getIndexMoveList() {
        return indexMoveList;
    }

    public boolean isAlive() {
        return health > 0; // Проверка, жив ли противник
    }

    public int getHealth() {
        return health; // Возврат здоровья
    }

    public Rectangle getBounds() {
        return bounds; // Возврат границ
    }

    public void dispose() {
        if (texture != null) {
            texture.dispose(); // Освобождение текстуры
        }
    }
    public  void enemyReactionOfCard( Player y, int index){}//реакция мобов на использование карт



    public void identifyIndexMoveList(){ // определение действия моба
        indexMoveList = (int)(Math.random()*moveList.length);
    }

    public void takeDamage(int damage) {
        health -= damage; // Уменьшение здоровья
        takingDamageSoundEffect.play(0.7f);
        buffActionTrigger("TakeDamage");
        if (health <= 0) {
            health = 0; // Убедитесь, что здоровье не становится отрицательным
        }
    }

    public void endTurn(Player y){
        identifyIndexMoveList();
        buffActionTrigger("EndTurn");
        decreaseBuff();
        moveList[getIndexMoveList()].enemyAction(this,y);// Противник наносит урон игроку
    }


    public boolean buffExist(Buff x){
        boolean check = false;
        if (buffs.isEmpty()){
            return check;
        }
        for (int i = 0;i<buffs.size(); i++){
            if(x.getName().equals(buffs.get(i).getName())){
                check = true;
                break;
            }
        }
        return  check;
    }

    public void giveBuff (Buff x){
        if(buffExist(x)){
            for (int i = 0;i<buffs.size(); i++){
                if(x.getName().equals(buffs.get(i).getName())){
                    buffs.get(i).addStack();
                }
            }
        }else {
            buffs.add(x);
        }
    }

    public int buffStack(Buff x){
        if(buffs.isEmpty()){
            return  0;
        }
        for (int i = 0; i<buffs.size(); i++){
            if(x.getName().equals(buffs.get(i).getName())) {
                return buffs.get(i).getStack();
            }
        }
        return 0;
    }

    public float modifierBuff(ModifierBuff x){
        float modifier = 1;
        if(buffs.isEmpty()){
            return  1;
        }
        for (int i = 0; i<buffs.size(); i++){
            if(x.getName().equals(buffs.get(i).getName())) {
                return x.modifier;
            }
        }
        return  1;
    }

    public void decreaseBuff(){
        if(buffs.isEmpty()){
            return;
        }
        for (int i = 0; i<buffs.size(); i++){
            if(buffs.get(i).decrease) {
                buffs.get(i).decreaseStack();
                if(buffs.get(i).stack == 0){
                    buffs.remove(i);
                }
            }
        }
    }

    public void buffActionTrigger(String situation){
        if(buffs.isEmpty()){
            return;
        }
        for (int i = 0; i<buffs.size(); i++){
            if(buffs.get(i).triggerBuff(situation)){
                buffs.get(i).buffAction(this);
            }
        }
    }
}

class   EnemyGhost extends Enemy {
    public EnemyGhost() {
        // Загрузка текстуры спрайт-листа
        Texture spriteSheet = new Texture(Gdx.files.internal("enemies/Enemy_sprite.png"));


        // Создание TextureRegion для каждого кадра анимации
        int frameCount = 3; // Количество кадров в спрайт-листе
        int frameWidth = spriteSheet.getWidth() / frameCount; // Ширина каждого кадра
        int frameHeight = spriteSheet.getHeight(); // Высота каждого кадра

        Array<TextureRegion> frames = new Array<>();

        // Извлечение кадров из спрайт-листа
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight));
        }

        // Инициализация анимации
        animation = new Animation<>(0.3f, frames); // 0.1f - время между кадрами
        stateTime = 0f; // Инициализация времени состояния

        // Установка границ
        bounds = new Rectangle((float)(Gdx.graphics.getWidth()/2.4),
            (float)(Gdx.graphics.getHeight()/2.5),
            (float)(frameWidth/291.0),
            (float)(frameHeight/291.0));
        health = 35; // Установка здоровья
        moveList = new MoveEnemy[3]; // Установка массива возможностей моба
        for (int i = 0; i <moveList.length; i++) {
            moveList[i] = new AttackEnemy(i+1);
        }
    }

    // Метод обновления состояния (вызывается в каждом кадре)

}
class EnemyHamster extends Enemy{
    public EnemyHamster() {

        // Загрузка текстуры спрайт-листа
        Texture spriteSheet = new Texture(Gdx.files.internal("enemies/hmstr_sprite.png"));

        // Создание TextureRegion для каждого кадра анимации
        int frameCount = 3; // Количество кадров в спрайт-листе
        int frameWidth = spriteSheet.getWidth() / frameCount; // Ширина каждого кадра
        int frameHeight = spriteSheet.getHeight(); // Высота каждого кадра

        Array<TextureRegion> frames = new Array<>();

        // Извлечение кадров из спрайт-листа
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight));
        }

        // Инициализация анимации
        animation = new Animation<>(0.3f, frames); // 0.1f - время между кадрами
        stateTime = 0f; // Инициализация времени состояния

        // Установка границ
        bounds = new Rectangle(
            (float)(Gdx.graphics.getWidth()/2.4),
            (float)(Gdx.graphics.getHeight()/3.2),
            (float)(frameWidth/1.5),
            (float)(frameHeight/1.5));
        health = 70; // Установка здоровья
        moveList = new MoveEnemy[1];// Установка массива возможностей моба
        moveList[0] = new AttackEnemy(4);
    }

    @Override
    public void enemyReactionOfCard( Player y, int index){
        giveBuff(new Power());
    }
}

class EnemyGambler extends Enemy{
    public EnemyGambler() {

        // Загрузка текстуры спрайт-листа
        TextureAtlas frames = new TextureAtlas(Gdx.files.internal("enemies/Gambler.atlas"));
        animation = new Animation<>(0.3f,
            frames.findRegions("Gambler"),
            Animation.PlayMode.LOOP);


        // Инициализация анимации
        stateTime = 0f; // Инициализация времени состояния
        Texture texture = new Texture(Gdx.files.internal("enemies/Gambler.png"));
        int FRAMES = 3;
        bounds = new Rectangle((float)(GameScreen.viewport.getWorldWidth()/2.4),
            (float)(GameScreen.viewport.getWorldHeight()/3),
            (float)((texture.getWidth()/FRAMES)/2.2),
            (float)(texture.getHeight()/2.2));

        health = 70; // Установка здоровья
        moveList = new MoveEnemy[2];// Установка массива возможностей моба
        moveList[0] = new AttackEnemy(6);
        moveList[1] = new SelfHarm(6);
    }

}

class EnemyProgrammer extends Enemy{
    public EnemyProgrammer() {


        Texture spriteSheet = new Texture(Gdx.files.internal("enemies/Gambler.png"));

        // Создание TextureRegion для каждого кадра анимации
        int frameCount = 3; // Количество кадров в спрайт-листе
        int frameWidth = spriteSheet.getWidth() / frameCount; // Ширина каждого кадра
        int frameHeight = spriteSheet.getHeight(); // Высота каждого кадра

        Array<TextureRegion> frames = new Array<>();

        // Извлечение кадров из спрайт-листа
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight));
        }

        // Инициализация анимации
        animation = new Animation<>(0.3f, frames); // 0.1f - время между кадрами
        stateTime = 0f; // Инициализация времени состояния


        // Установка границ
        bounds = new Rectangle((float)(Gdx.graphics.getWidth()/2.4), (float)(Gdx.graphics.getHeight()/3.2), (float)(frameWidth/1.5), (float)(frameHeight/1.5));
        health = 70; // Установка здоровья
        moveList = new MoveEnemy[4];// Установка массива возможностей моба
        moveList[0] = new AttackProgrammer(4);
        moveList[1] = new AttackProgrammer(4);
        moveList[2] = new AttackProgrammer(4);

    }

    @Override
    public void enemyReactionOfCard( Player y, int index) {
        switch (indexMoveList){
            case 0:
                if(!(y.hand[index] instanceof CardAttack)){
                    //uniqueBuff[ProgrammerBuff.getIndex()].stack = 1;
                }
                break;
            case 1:
                if(y.hand[index] instanceof CardTalent || y.hand[index] instanceof CardAttack){
                    //uniqueBuff[ProgrammerBuff.getIndex()].stack = 1;
                }
                break;
            case 2:
                if(!(y.hand[index] instanceof CardTalent)){
                    //uniqueBuff[ProgrammerBuff.getIndex()].stack = 1;
                }
                break;
        }

    }

    @Override
    public void endTurn(Player y) {
        super.endTurn(y);
        //uniqueBuff[ProgrammerBuff.getIndex()].stack = 0;
    }
}
