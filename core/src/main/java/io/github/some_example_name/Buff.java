package io.github.some_example_name;


public abstract class Buff {
    protected boolean decrease;
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

    public void addStack(){
        stack += 1;
    }

    public void decreaseStack(){
        stack -= 1;
    }


}

class BonusCard extends Buff{

    public BonusCard(int stack) {
        name = "Дополнительные карты";
        description = "За каждый стак дает доп 1 карту в доборе";
        this.stack = stack;
        decrease = true;
    }

    public BonusCard() {
        name = "Дополнительные карты";
        description = "За каждый стак дает доп 1 карту в доборе";
        stack = 1;
        decrease = true;
    }

    @Override
    public void decreaseStack() {
        stack = 0;
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

class KingOfCookieBuff extends TalentBuff{


    public KingOfCookieBuff() {
        name = "Король печенек";
        description = "Дает возможность защищаться от урона печенек";
        decrease = false;
        stack = 1;
    }

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


