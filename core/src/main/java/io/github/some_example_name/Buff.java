package io.github.some_example_name;


abstract class Buff {
    private static int index;
    int  stack;
    protected static  String name;
    protected static  String description;


    public static String getName() {
        return name;
    }

    public static String getDescription() {
        return description;
    }

    public static void setStaticBuff(Player x){
        x.endlessBuffs[0] = new Power();
        x.endlessBuffs[1] = new Reinforce();
        x.talentsBuffs[0] = new KingOfCookieBuff();
        x.talentsBuffs[1] = new CookiesOfMadnessBuff();
    }

    public static void setStaticBuff(Enemy x){
        x.endlessBuffs[0] = new Power();
        x.endlessBuffs[1] = new Reinforce();
    }


}

class EndlessBuff extends Buff{

}

class Power extends EndlessBuff{
    private static final int index;

    static{
        index = 0;
        name = "Cила";
        description = "За каждый стак дает доп 1 урон к атакам";
    }
    public Power(){
        stack = 0;

    }
    public  static int getIndex() {
        return index;
    }
}

class Reinforce extends EndlessBuff{
    private static final int  index;
    public static int getIndex() {
        return index;
    }

    static{
        index = 1;
        name = "Укрепление";
        description = "За каждый стак дает доп 1 защиту к способностям";
    }
    public Reinforce(){
        stack = 0;

    }
}
//class manaReservation extends Buff{
    //public  manaReservation(){
        //name = "Отложенная мана";
        //duration = 1;
        //description = "Дает 1 ману на следующий ход";
        //stack++;
    //}

    //@Override
    //public void buffAction(Player x){
        //x.manaPool += stack;
        //stack = 0;

    //}
//}

class TalentBuff extends Buff{

}

class KingOfCookieBuff extends TalentBuff{
    private static final int index;
    static {
        index = 0;
        name = "Король печенек";
        description = "Дает возможность защищаться от урона печенек";
    }
    public  static int getIndex() {
        return index;
    }

    public KingOfCookieBuff() {
        stack = 0;
    }

}

class CookiesOfMadnessBuff extends TalentBuff{
    private static final int index;
    static {
        index = 1;
        name ="Печеньковое безумие";
        description = "Дает за каждый стак в начале хода дает дополнительную карту печенек";
    }

    public static int getIndex(){
        return index;
    }

    public CookiesOfMadnessBuff(){
        stack = 0;
    }

}

class TurnBuff extends  Buff {
}

class ProgrammerBuff extends TurnBuff {
    private static final int index;
    static {
        index = 0;
        name = "Усиление атаки";
        description = "Дает за каждый стак в начале хода множитель урона x2";
    }

    public ProgrammerBuff() {
        stack = 0;
    }

    public static int getIndex(){
        return index;
    }

}
