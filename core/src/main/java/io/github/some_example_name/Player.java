package io.github.some_example_name;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public abstract class Player {


    //penis

    protected List<Buff> buffs = new ArrayList<>();


    protected int health, maxHealth;
    protected int draftCount;
    protected int manaPool;
    protected int manaPoolMax;
    protected   int shield;// Здоровье игрока

    protected List<PlayingCard> dropDeck = new ArrayList<>();
    protected List<PlayingCard> deck = new ArrayList<>();
    protected List<PlayingCard> draftDeck = new ArrayList<>();

    protected PlayingCard[] hand = new PlayingCard[GameScreen.HAND_META];

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
        buffActionTrigger("BeginFight");
        draftDeck.addAll(deck);
        Collections.shuffle(draftDeck);
        ComboAttack.setCondition(false);
    }

    public void beginTurn(){
        buffActionTrigger("BeginTurn");
        takeCardsFromDraftDeck(draftCount);
        manaPool = manaPoolMax;
        shield = 0;
        decreaseBuff();
    }

    public void endTurn(){
        buffActionTrigger("EndTurn");
        int notFreeSpace = findFreeSpaceIndex();
        for (int i = 0; i<notFreeSpace;i++) {
            if(!hand[i].ethereal){
                dropDeck.add(hand[i]);
            }
            hand[i] = null;
            if(draftDeck.isEmpty()){
                draftDeck.addAll(dropDeck);
                Collections.shuffle(draftDeck);
                dropDeck.clear();
            }
        }

    }

    public void playCard(Enemy enemy,int index){
        buffActionTrigger("cardAction");
        hand[index].cardAction(enemy, this, index);
        if(!hand[index].burnable){
            addDropDeck(hand[index]);
        }
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
                    dropDeck.clear();
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
            return false;
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
        return  modifier;
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

    public void takeDamage(int damage) {
            if (shield-damage>0){
                shield -= damage;

            } else {
                health -= (damage-shield);// Уменьшение здоровья
                if (health < 0) {
                    health = 0; // Убедитесь, что здоровье не становится отрицательным
                }
            }
    }

    public void giveShield(int plusShield){
        shield+=plusShield  ;
    }

    public void useManaForCard(PlayingCard x){
            manaPool -= x.getCost();
    }

    public void healing(int heal){
        if(maxHealth==heal+health){
            return;
        }
        health += heal;
    }

}


class CharacterKnight extends Player {
    public CharacterKnight() {
        for(int i=0 ; i<2; i++){
            deck.add(new Attack());

        }
        for(int i=2; i<4; i++){
            deck.add(new Defence());

        }
        deck.add(new Overload());


        maxHealth = 60;
        health = maxHealth;// Начальное здоровье игрока
        shield = 0;
        manaPool = 3;
        manaPoolMax = 3;
        draftCount = 4;
    }


}
//goida
