package io.github.some_example_name;


public abstract class Buff {
    protected boolean decrease;
    protected boolean endTurn = false, beginTurn = false, inActionCard = false;
    protected int  stack;
    protected   String name;
    protected   String description;


    public  String getName() {
        return name;
    }

    public int getStack() {
        return stack;
    }

    public  String getDescription() {
        return description;
    }

    public void addStack(Buff x){
        this.stack += x.stack;
    }

    public void decreaseStack(){
        stack -= 1;
    }

    public void  buffAction(Enemy enemy, Player player){
    }


}

class BonusCard extends Buff{

    public BonusCard(int stack) {
        name = "Дополнительные карты";
        description = "За каждый стак дает доп 1 карту в доборе в начале хода";
        this.stack = stack;
        decrease = true;
        beginTurn =true;
    }


    public BonusCard() {
        name = "Дополнительные карты";
        description = "За каждый стак дает доп 1 карту в доборе в начале хода ";
        stack = 1;
        decrease = true;
        beginTurn =true;
    }

    @Override
    public void decreaseStack() {
        stack = 0;
    }

    @Override
    public void buffAction(Enemy enemy, Player player) {
        player.takeCardsFromDraftDeck(stack);
    }
}

class  Barricade extends Buff {

    public Barricade() {
        name = "Баррикада";
        description = "За каждый стак дает 5 брони в начале хода ";
        stack = 5;
        decrease = false;
        beginTurn =true;
    }

    @Override
    public void buffAction(Enemy enemy, Player player) {
        player.giveShield(stack);
    }
}

class Power extends Buff{

    public Power(){
        name = "Сила";
        description = "За каждый стак дает доп 1 урон к атакам";
        decrease = true;
        stack = 1;

    }
}

class Reinforce extends Buff{


    public Reinforce(){
        name = "Укрепление";
        description = "За каждый стак дает доп 1 защиту к способностям";
        decrease = true;
        stack = 1;

    }
}

class TalentBuff extends Buff{

}



class CookiesOfMadnessBuff extends TalentBuff{




    public CookiesOfMadnessBuff(){
        name ="Печеньковое безумие";
        description = "Дает за каждый стак в начале хода дает дополнительную карту печенек";
        decrease = true;
        stack = 1;
    }

}

class TurnBuff extends  Buff {
}

class ProgrammerBuff extends TurnBuff {

    public ProgrammerBuff() {
        stack = 0;
    }



}


