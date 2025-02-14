package io.github.some_example_name;
import com.badlogic.gdx.Audio;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;

abstract class PlayingCard {
    protected  String name,description;
    protected int cost;
    protected  Texture texture;
    protected Sound soundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/Zaglushka.wav"));

    public  void cardAction(Enemy x, Player y,int index){
        y.useManaForCard(this);
        soundEffect.play(0.7f);

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
abstract class CardAttack extends TargetCard {
    protected int damage;
    protected int totalDamage;

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        x.takeDamage(totalDamage);
    }

}

//CardAttackList
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
        totalDamage = damage;
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
        totalDamage = damage;
        super.cardAction(x,y,index);

    }
}
class LetsGoGambling extends CardAttack {
    public LetsGoGambling() {
        name = "LetsGoGambling";
        description = "Тип: Атака. Наносит врагу 12 урона ИЛИ враг делает ДВОЙНОЕ ДЕЙСТВИЕ";
        damage = 12;
        cost = 1;
        texture = new Texture(Gdx.files.internal("cards/cardLetsGoGambling.png"));
    }

    @Override
    public void cardAction(Enemy x, Player y,int index){
        int letsGoGambling = (int) (Math.random()*2);
        switch (letsGoGambling){
            case 0:
                totalDamage = damage*2;
                super.cardAction(x,y,index);
                break;
            case 1:
                x.moveList[x.indexMoveList].enemyAction(x,y);
                x.moveList[x.indexMoveList].enemyAction(x,y);
                soundEffect.play(0.7f);
                y.useManaForCard(this);
                break;

        }

    }
}



abstract class  nonTargetCard extends PlayingCard {
}
//CardDefence
class DefenceCard extends nonTargetCard{
    protected int shield;
    protected int totalShield;
    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.giveShield(totalShield);

    }
}
class Defence extends DefenceCard {



    public Defence() {
        name = "Защита";
        description = "Тип: способность. Дает 6 брони";
        shield = 6;
        cost = 1;
        texture = new Texture(Gdx.files.internal("cards/cardDefence.png"));
        soundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/shieldEffect.wav"));

    }

    @Override
    public  void cardAction(Enemy x, Player y,int index){
        int totalShield = shield;
        super.cardAction(x,y,index);
    }
}
class CookieOfPower extends nonTargetCard{
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
        y.endlessBuffs[Power.getIndex()].stack+=power;
        if(y.talentsBuffs[KingOfCookieBuff.getIndex()].stack>0){
            y.takeDamage(damageSelf);
        } else {
            y.health -= damageSelf;
        }

    }
}

class CookieOfReinforce extends nonTargetCard{
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
        y.endlessBuffs[Reinforce.getIndex()].stack+=reinforce;
        if(y.talentsBuffs[KingOfCookieBuff.getIndex()].stack>0){
            y.takeDamage(damageSelf);
        } else {
            y.health -= damageSelf;
        }

    }
}

class CookieOfDobor extends nonTargetCard{
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

        if(y.talentsBuffs[KingOfCookieBuff.getIndex()].stack>0){
            y.takeDamage(damageSelf);
        } else {
            y.health -= damageSelf;
        }

        y.takeCardsFromDraftDeck(extraCards);
    }


}

class CookieOfMana extends nonTargetCard{
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
        if(y.talentsBuffs[KingOfCookieBuff.getIndex()].stack>0){
            y.takeDamage(damageSelf);
        } else {
            y.health -= damageSelf;
        }
        y.giveMaxMana(extraMana);

    }
}


abstract class CardTalent extends nonTargetCard{
}

class KingOfCookie extends  CardTalent {
    public KingOfCookie(){
        name = "Король Печенек";
        description = "Тип: талант. Если используется, то карты печенек сначала наносит урон щиту";
        texture = new Texture(Gdx.files.internal("cards/card6.png"));
        cost = 2;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index){
        y.talentsBuffs[KingOfCookieBuff.getIndex()].stack += 1;

    }
}
class CookiesOfMadness extends CardTalent{
    public CookiesOfMadness() {
        name = "Печеньковое безумие";
        description = "Тип: талант. За каждый стак в начале хода дает дополнительную карту печенек";
        texture = new Texture(Gdx.files.internal("cards/card6.png"));
        cost = 2;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        y.talentsBuffs[CookiesOfMadnessBuff.getIndex()].stack += 1;

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

class KingOfCookieEthereal extends KingOfCookie {
    public KingOfCookieEthereal() {
        name = "Король Печенек";
        description = "Тип: талант. Если используется, то карты печенек сначала наносит урон щиту. Эфирная. Одноразовая";
        texture = new Texture(Gdx.files.internal("cards/card6.png"));
        cost = 2;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
            y.talentsBuffs[CookiesOfMadnessBuff.getIndex()].stack += 1;

    }
}

