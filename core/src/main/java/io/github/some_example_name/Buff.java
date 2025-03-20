package io.github.some_example_name;


public abstract class Buff {
    protected boolean decrease;
    //это флаги, которые показывают когда триггерится бафф
    protected boolean inEndTurn = false, inBeginTurn = false, inCardAction = false,
        inBeginFight = false, inReaction = false;
    protected int  stack;
    protected String name, description;



    public  String getName() {
        return name;
    }

    public int getStack() {
        return stack;
    }

    public  String getDescription() {
        return description;
    }

    public boolean triggerBuff(String situation){
        switch (situation){
            case "EndTurn":
                return inEndTurn;
            case "BeginTurn":
                return inBeginTurn;
            case "CardAction":
                return inCardAction;
            case "BeginFight":
                return inBeginFight;
            case "Reaction":
                return  inReaction;
            default:
                return false;
        }
    }

    public void buffAction(){

    }

    public void buffAction(Enemy owner){

    }

    public void buffAction(Player player){

    }

    public void addStack(){
        stack += 1;
    }

    public void decreaseStack(){
        stack -= 1;
    }


}
class MentalDamage extends Buff{

    public MentalDamage() {
        name = "Ментальный урон";
        description = "В начале хода наносит за каждый стак";
        stack = 4;

        decrease = false;
        inEndTurn = true;
    }

    @Override
    public void buffAction(Enemy owner) {
        owner.takeDamage(stack);
    }
}

class BonusCard extends Buff{

    public BonusCard(int stack) {
        name = "Дополнительные карты";
        description = "За каждый стак дает доп 1 карту в доборе";
        this.stack = stack;
        decrease = true;
        inBeginTurn = true;

    }

    public BonusCard() {
        name = "Дополнительные карты";
        description = "За каждый стак дает доп 1 карту в доборе";
        stack = 1;
        decrease = true;
        inBeginTurn = true;
    }

    @Override
    public void buffAction(Player player) {
        player.takeCardsFromDraftDeck(stack);
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

class CookiesOfMadnessBuff extends Buff{




    public CookiesOfMadnessBuff(){
        name ="Печеньковое безумие";
        description = "Дает за каждый стак в начале хода дает дополнительную карту печенек";
        decrease = false;
        inBeginTurn = true;
        stack = 1;
    }

    @Override
    public void buffAction(Player player) {
        int COUNT_OF_TYPE_COOKIES = 4;
        PlayingCard card = null;

        int answer = (int) (Math.random() * COUNT_OF_TYPE_COOKIES);
        switch (answer) {
            case 0:
                card = new CookieOfReinforce();
                break;
            case 1:
                card = new CookieOfMana();
                break;
            case 2:
                card = new CookieOfPower();
                break;
            case 3:
                card = new CookieOfDobor();
                break;
        }
        player.giveTheCard(card);


    }


}

class  HamsterBuff extends Buff{
    public HamsterBuff() {
        name ="Хомячье Безумие";
        description = "Дает 1 силу каждый раз, когда используется карта на него";
        decrease = false;
        inCardAction = true;
        stack = 1;
    }

    @Override
    public void buffAction(Enemy owner) {
        super.buffAction(owner);
        owner.giveBuff(new Power());
    }
}


