package io.github.some_example_name;

import com.badlogic.gdx.Gdx;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Player {


    //penis

    protected List<Buff> buffs = new ArrayList<>();


    protected int health;
    protected int manaPool;
    protected int manaPoolMax;
    protected   int shield;// Здоровье игрока
    protected Animation animation;// Анимация для игрока

    protected List<PlayingCard> dropDeck = new ArrayList<>();
    protected List<PlayingCard> deck = new ArrayList<>();
    protected List<PlayingCard> draftDeck = new ArrayList<>();
    protected PlayingCard[] hand = new PlayingCard[6];

    public int getShield(){
        return shield;
    }

    public int getHealth() {
        return health;
    }

    public int getManaPool() {
        return manaPool;
    }

    public void beginFight(){
        draftDeck.addAll(deck);
        Collections.shuffle(draftDeck);
        ComboAttack.setCondition(false);
    }

    public void beginTurn(){
        takeCardsFromDraftDeck(4);
        manaPool = manaPoolMax;
        shield = 0;
    }

    public void endTurn(){
        int notFreeSpace = findFreeSpaceIndex();
        for (int i = 0; i<notFreeSpace;i++) {
            draftDeck.add(hand[i]);
            hand[i] = null;
        }
        decreaseBuff();
    }

    public void playCard(Enemy enemy,int index){
        hand[index].cardAction(enemy, this, index);
        addDropDeck(hand[index]);
        shiftHand(index);
    }

    public void takeCardsFromDraftDeck(int count){
        for (int i = 0; i<count; i++){
            int freeSpace = findFreeSpaceIndex();
            if(freeSpace == -1){
                break;
            }
            else {
                hand[freeSpace] =  draftDeck.get(0);
                draftDeck.remove(0);
                if(draftDeck.isEmpty()){
                    draftDeck.addAll(dropDeck);
                    Collections.shuffle(draftDeck);
                }
            }

        }
    }

    public void addDropDeck(PlayingCard x){
        dropDeck.add(x);
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


    public void giveTheCard (PlayingCard x){
        int i = findFreeSpaceIndex();
        if(i!=-1){
            hand[i] = x;
        }
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

    public float modifierBuff(modifierBuff x){
        float modifier = 1;
        if(buffs.isEmpty()){
            return  1;
        }
        for (int i = 0; i<buffs.size(); i++){
            if(x.getName().equals(buffs.get(i).getName())) {
                return x.modifier;
            }
        }
        return  modifier;
    }

    public void decreaseBuff(){
        if(buffs.isEmpty()){
            return;
        }
        for (int i = 0; i<buffs.size(); i++){
            if(buffs.get(i).decrease) {
                buffs.get(i).decreaseStack();
            }
        }
    }


    public void giveMaxMana(int count){
        manaPoolMax+=count;
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

    public void giveShield(int plusShield){
        shield+=plusShield  ;
    }

    public void useManaForCard(PlayingCard x){
            manaPool -= x.cost;
    }

}


class    CharacterKnight extends Player{
    public CharacterKnight() {
        for(int i=0 ; i<2; i++){
            deck.add(new Attack());

        }
        for(int i=2; i<4; i++){
            deck.add(new Defence());

        }
        deck.add(new CookieOfReinforce());
        deck.add(new CookieOfPower());
        deck.add(new CookieOfDobor());
        deck.add(new FeintCard());
        health = 60;// Начальное здоровье игрока
        shield = 0;
        manaPool = 2;
        manaPoolMax = 2;
        animation = new Animation(0.9f, 1.1f, 0.5f, 300f); // Инициализация анимации
    }

}
//goida
