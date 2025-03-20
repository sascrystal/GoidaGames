package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;
import com.badlogic.gdx.graphics.g2d.TextureRegion;


public abstract class PlayingCard {
    protected  String name,description;
    protected int cost;
    protected boolean burnable = false, ethereal = false;
    protected  Texture texture = new Texture(Gdx.files.internal("cards/noDataCard.png"));
    protected Sound soundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/Zaglushka.wav"));
    protected TextureAtlas frames = new TextureAtlas(Gdx.files.internal("animationCards/gravity.atlas"));
    protected Animation<TextureRegion> effect = new Animation<>(1/15F,
        frames.findRegions("Gravity-Sheet"),
        Animation.PlayMode.NORMAL);


    public  void cardAction(Enemy x, Player y,int index){
        y.useManaForCard(this);
        soundEffect.play(0.7f);

    }

    public void draw(float time, Batch batch,Enemy enemy){
        TextureRegion currentFrame = effect.getKeyFrame(time, false);
        batch.draw(currentFrame, enemy.bounds.x,enemy.bounds.y,
            enemy.bounds.width, enemy.bounds.height);
    }


    //cardAnimation - метод, который делает анимацию для карт. Он должен быть абстрактны.
    //Но пока он для всех одинаков.


    public String getName() {
        return name;
    }

    public int getCost() {
        return cost;
    }

    public  Texture getTexture() {
        return texture;
    }

    public  String getDescription() {
        return description;
    }

}

abstract class TargetCard extends PlayingCard{

}

class Overload extends TargetCard{

    public Overload() {
        name = "Перегрузка";
        description = "Накладывает дебафф <<Перегрузка>>";
        cost = 3;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        x.giveBuff(new OverloadBuff());
    }
}
abstract class CardAttack extends TargetCard {
    protected int damage;
    protected int totalDamage;

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        totalDamage = totalDamageCalculation(y);
        x.takeDamage(totalDamage);
    }

    public int totalDamageCalculation(Player y){
        return (int)((damage+y.buffStack(new Power()))*y.modifierBuff(new Weakness()));

    }

}

//CardAttackList

class PhantomPain extends CardAttack{
    public PhantomPain() {
        name = "Фантомная боль";
        description = "тип: атака. Наносит 8 урона. Накладывает дебафф ментальные повреждения";
        damage = 8;
        cost  = 1;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        x.giveBuff(new MentalDamage());
    }
}
class Attack extends CardAttack {
    public Attack() {

        name = "Атака";
        description = "Тип: атака. Наносит 6 урона";

        damage = 6;
        cost = 1;

        texture = new Texture(Gdx.files.internal("cards/cardAttack.png"));
        soundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/attackEffect.wav"));
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        totalDamage = totalDamageCalculation(y);
        super.cardAction(x, y, index);
    }
}

class ComboAttack extends CardAttack{
    private static boolean condition;
    static {
        condition =false;
    }

    public static void setCondition(boolean condition) {
        ComboAttack.condition = condition;
    }

    public ComboAttack() {
        name ="Комбо атака";
        description = "Тип: атака. Наносит 7 урона, если использовано в одному ходу 2 карты этого типа, то получаешь в руку бесплатную эфирную карту комбо атаки";
        texture = new Texture(Gdx.files.internal("cards/cardLetsGoGambling.png"));
        damage = 7;
        cost = 1;
    }
    public ComboAttack(int cost) {
        name ="Комбо атака";
        description = "Тип: атака. Наносит 7 урона, если использовано в одному ходу 2 карты этого типа, то получаешь в руку бесплатную эфирную карту комбо атаки эфирная";
        texture = new Texture(Gdx.files.internal("cards/cardLetsGoGambling.png"));
        damage = 7;
        this.cost =cost;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        if(condition){
            y.giveTheCard(new ComboAttack(0));
            condition = false;
        } else {
            condition = true;
        }
        totalDamage = totalDamageCalculation(y);
        super.cardAction(x,y,index);

    }
}
class LetsGoGambling extends CardAttack {
    public LetsGoGambling() {
        name = "LetsGoGambling";
        description = "Тип: Атака. Наносит врагу 12 урона ИЛИ враг делает действие";
        damage = 12;
        cost = 1;
        texture = new Texture(Gdx.files.internal("cards/cardLetsGoGambling.png"));
    }

    @Override
    public void cardAction(Enemy x, Player y,int index){
        int letsGoGambling = (int) (Math.random()*2);
        switch (letsGoGambling){
            case 0:
                totalDamage = totalDamageCalculation(y)*2;
                super.cardAction(x,y,index);
                break;
            case 1:
                x.moveList[x.indexMoveList].enemyAction(x,y);
                soundEffect.play(0.7f);
                y.useManaForCard(this);
                break;

        }

    }
}

class  FeintCard extends CardAttack{
    protected int weaknessStack;
    public FeintCard() {
        name = "Финт";
        description = "Тип: атака. Наносит 8 урона и накладывает 2 слабости";
        cost = 1;
        damage = 8;
        weaknessStack = 2;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        totalDamage = totalDamageCalculation(y);
        super.cardAction(x, y, index);
        x.giveBuff(new Weakness(2));
    }
}

class attackVoid extends  CardAttack{

    public attackVoid() {
        name = "Войд";
        description = "Тип: атака. Наносит 8x2 урона и восстанавливает нанесенный урон";
        cost = 2;
        damage = 8;
    }

    @Override
    public int totalDamageCalculation(Player y) {
        return super.totalDamageCalculation(y)*2;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.healing(totalDamage);

    }
}



abstract class  NonTargetCard extends PlayingCard {
}

class DefenceCard extends NonTargetCard{
    protected int shield;
    protected int totalShield;
    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.giveShield(totalShield);

    }
}

class Evade extends DefenceCard{
    public Evade() {
        name = "Уворот";
        description = "Тип: способность. Дает 8 защиты, 1 карту из колоды и 1 карту в следующем ходу";
        shield = 8;
        cost = 1;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.takeCardsFromDraftDeck(1);
        y.giveBuff(new BonusCard(1));
    }
}
class Defence extends DefenceCard {



    public Defence() {
        frames = new TextureAtlas(Gdx.files.internal("animationCards/electic.atlas"));
        effect = new Animation<>(1/15F,
            frames.findRegions("Eletric A-Sheet"),
            Animation.PlayMode.NORMAL);

        name = "Защита";
        description = "Тип: способность. Дает 6 брони";
        shield = 6;
        cost = 1;
        texture = new Texture(Gdx.files.internal("cards/cardDefence.png"));
        soundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/shieldEffect.wav"));

    }

    @Override
    public  void cardAction(Enemy x, Player y,int index){
        totalShield = shield+y.buffStack(new Reinforce());
        super.cardAction(x,y,index);
    }
}
class CookieOfPower extends NonTargetCard{
    int power;
    int damageSelf;
    public CookieOfPower() {
        name = "Печенька силы";
        description = "Тип: способность. Дает 1 силу, наносит 2 урон ВАМ P.S что нас не убивает, делает нас сильнее";
        power = 1;
        damageSelf = 2;
        cost = 0;
        texture = new Texture(Gdx.files.internal("cards/cardCookieOfPower.png"));

    }

    @Override
    public  void cardAction(Enemy x, Player y,int index){
        super.cardAction(x,y,index);
        y.giveBuff(new Power());
        y.takeDamage(damageSelf);

    }
}

class CookieOfReinforce extends NonTargetCard{
    int reinforce,damageSelf;

    public CookieOfReinforce() {
        name = "Железная печенька";
        description = "Тип: способность. Дает 1 укрепление, наносит 2 урон ВАМ P.S сколько бы вы ее не макали в чай она все равно каменная";
        reinforce = 1;
        damageSelf = 2;
        cost = 0;
        texture = new Texture(Gdx.files.internal("cards/cardCookieOfReinforce.png"));

    }

    @Override
    public  void cardAction(Enemy x, Player y,int index){
        super.cardAction(x,y,index);
        y.giveBuff(new Reinforce());
        y.takeDamage(damageSelf);

    }
}

class CookieOfDobor extends NonTargetCard{
    int extraCards, damageSelf;
    public CookieOfDobor() {
        extraCards = 2;
        damageSelf = 2;
        name = "Печенька добора";
        description = "Тип: способность. дает 2 карты, наносит 2 урон ВАМ";
        cost = 0;
        texture = new Texture(Gdx.files.internal("cards/cardCookieOfReinforce.png"));
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x,y,index);
        y.takeCardsFromDraftDeck(extraCards);
        y.takeDamage(damageSelf);
    }


}

class CookieOfMana extends NonTargetCard{
    int extraMana,damageSelf;

    public CookieOfMana() {
        extraMana = 1;
        damageSelf = 2;
        name = "Печенька маны";
        description = "Тип: способность. дает 1 максимальную ману, наносит 2 урона ВАМ";
        cost = 0;
        texture = new Texture(Gdx.files.internal("cards/cardCookieOfReinforce.png"));
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x,y,index);

    }
}


abstract class CardTalent extends NonTargetCard{
}


class CookiesOfMadness extends CardTalent{
    public CookiesOfMadness() {
            name = "Печеньковое безумие";
            description = "Тип: талант. За каждый стак в начале хода дает дополнительную карту печенек";
        texture = new Texture(Gdx.files.internal("cards/card6.png"));
        cost = 2;
        burnable = true;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x,y,index);
        y.giveBuff(new CookiesOfMadnessBuff());


    }
}

class  CookiesOfMadnessEthereal extends CookiesOfMadness{
    public CookiesOfMadnessEthereal() {
        name = "Печеньковое безумие";
        description = "Тип: талант. За каждый стак в начале хода дает дополнительную карту печенек. Эфирная. Одноразовая";
        texture = new Texture(Gdx.files.internal("cards/card6.png"));
        cost = 2;
    }

}





