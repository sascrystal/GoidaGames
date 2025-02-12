package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Player {
    public PlayingCard[] deckCard; // Все карты игрока
    public PlayingCard[] hand;// Карты в руке игрока
    public EndlessBuff[] endlessBuffs = new EndlessBuff[2];
    public TalentBuff[] talentsBuffs = new TalentBuff[2];
    public int health;
    protected List<PlayingCard> cardList;
    public int manaPool;
    public int manaPoolMax;
    public  int shield;// Здоровье игрока
    public Animation animation;// Анимация для игрока
    protected static int usedCardsInDeck;
    static {
        usedCardsInDeck = 0;
    }


    public void doborCards(int count){

        for(int i = findFreeSpaceIndex(), j = 0; j<count && i<hand.length;j++,i++){
            hand[i] = cardList.get(usedCardsInDeck);
            usedCardsInDeck++;
        }
    }

    public void giveMaxMana(int count){
        manaPoolMax+=count;
    }

    public void shiftHand(int index){
        if (index ==hand.length-1){
            hand[index] = null;
        } else{
            for(int i = index;i<hand.length-1;i++){
                hand[i] = hand[i+1];
            }
            if(hand[hand.length-1] != null){
                hand[hand.length-1] = null;
            }
        }
    }

    public int findFreeSpaceIndex(){
        int index = -1;
        for (int i = 0; i<hand.length;i++){
            if(hand[i] == null){
                index=i;
                break;
            }
        }
        return index;
    }

    public void burnTheCard(Class x){
        PlayingCard[] newDeck =  new PlayingCard[deckCard.length-1];
        for (int j = 0, i = 0; i<deckCard.length; i++){
            if(deckCard[i].getClass() != x){
                newDeck[j] = deckCard[i];
                j++;
            }
        }
        deckCard = newDeck;
    }

    public void giveTheCard (PlayingCard x){
        int i = findFreeSpaceIndex();
        if(i!=-1){
            hand[i] = x;
        }
    }


    public void drawRandomCards() {
        Collections.shuffle(cardList);// Перемешиваем карты
        usedCardsInDeck=0;
        hand = new PlayingCard[6];
        int i;

        //Основной добор
        doborCards(4);
        i = 4;

        //Добор от CookieOfMadnees
        if(talentsBuffs[CookiesOfMadnessBuff.getIndex()].stack>0){
            for(int j = 0;j<talentsBuffs[CookiesOfMadnessBuff.getIndex()].stack && i<hand.length;j++,i++){
                int chance = (int)(Math.random()*5);
                switch (chance){
                    case 0:
                        hand[i] = new CookieOfPower();
                        break;
                    case 1:
                        hand[i] = new CookieOfReinforce();
                        break;
                    case 2:
                        hand[i] = new CookiesOfMadnessEthereal();
                        break;
                    case 3:
                        hand[i] = new KingOfCookieEthereal();
                        break;
                    case 4:
                        hand[i] = new CookieOfDobor();
                        break;
                }
            }
        }

    }
    public int getShield(){
        return shield;
    }
    public int getHealth() {
        return health;
    }

    public void takeDamage(int damage) {
            if (shield-damage>0){
                shield -= damage;

            } else {
                health -= (damage-shield);// Уменьшение здоровья
                if (health < 0) {
                    health = 0; // Убедитесь, что здоровье не становится отрицательным
                }
                animation.setRotation(15); // Наклон вправо при получении урона
                // Возврат в исходное положение через некоторое время
                Gdx.app.postRunnable(() -> {
                    animation.setRotation(0); // Возврат в исходное положение
                });
            }
    }
    public void useManaForCard(PlayingCard x){
            manaPool -= x.cost;
    }
    public void resetAfterTurn(){
            manaPool = manaPoolMax;
            shield = 0;
            ComboAttack.setCondition(false);
            drawRandomCards();
    }

    public int getManaPool() {
        return manaPool;
    }
}
class    CharacterKnight extends Player{
    public CharacterKnight() {
        Buff.setStaticBuff(this);
        deckCard = new PlayingCard[6];
        for(int i=0 ; i<2; i++){
            deckCard[i] = new Attack();
        }
        for(int i=2; i<4; i++){
            deckCard[i] = new Defence();
        }
        deckCard[4] = new ComboAttack();
        deckCard[5] = new ComboAttack();
        health = 60;// Начальное здоровье игрока
        shield = 0;
        manaPool = 2;
        manaPoolMax = 2;
        cardList = new ArrayList<>();
        Collections.addAll(cardList, deckCard);
        drawRandomCards();
        animation = new Animation(0.9f, 1.1f, 0.5f, 300f); // Инициализация анимации
    }

}
