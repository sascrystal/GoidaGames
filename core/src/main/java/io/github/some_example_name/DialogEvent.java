package io.github.some_example_name;

import com.badlogic.gdx.Gdx;

public abstract class DialogEvent {
    protected String[] dialogOptions;
    protected String eventDescription;


    public  void action(Player player,CellMap[][] map, int index){
        switch (index){
            case 0:
                actionFirst(player,map);
                break;
            case 1:
                actionSecond(player,map);
                break;
            case 2:
                actionThird(player,map);
                break;
            default:
                Gdx.app.log("ошибка в неправильном индексе","индекс не может быть больше 2-ух");
                break;
        }
    }

    public void begin( MapScreen map){
        ((Main) Gdx.app.getApplicationListener()).setScreen(new QuestionMarkScreen(map,this));
    }

    public  void actionFirst(Player player,CellMap[][] map){
        returnToMap(player, map);
    }
    public  void actionSecond(Player player,CellMap[][] map){
        returnToMap(player, map);
    }
    public  void actionThird(Player player,CellMap[][] map){
        returnToMap(player, map);
    }

    protected void returnToMap(Player player,CellMap[][] map){
        ((Main) Gdx.app.getApplicationListener()).setScreen(new MapScreen(player, map));
    }

    public String getEventDescription() {
        return eventDescription;
    }

    public String[] getDialogOptions() {
        return dialogOptions;
    }
}

class ShrineEvent extends DialogEvent{
    public ShrineEvent() {
        eventDescription = "Вы встречаете святилище, в центре которого находится вода";
        dialogOptions = new String[3];
        dialogOptions[0] = "Выпить воду";
        dialogOptions[1] = "Уйти из этого места";
        dialogOptions[2] = "Поискать запасы";
    }

    @Override
    public void actionFirst(Player player,CellMap[][] map) {
        int heal = 15;
        player.healing(heal);
        returnToMap(player,map);
    }

    @Override
    public void actionThird(Player player, CellMap[][] map) {
        PlayingCard givingCard = new PhantomPain();
        player.giveTheCard(givingCard);
        returnToMap(player,map);
    }
}



