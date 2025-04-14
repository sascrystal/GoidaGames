package io.github.some_example_name.cell_map_classes.events;

import com.badlogic.gdx.Gdx;

import io.github.some_example_name.Main;
import io.github.some_example_name.screens.MapScreen;

import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.player.Player;
import io.github.some_example_name.screens.QuestionMarkScreen;

public abstract class DialogEvent {
    protected String[] dialogOptions;
    protected String eventDescription;


    public  void action(Player player, CellMap[][] map, int index){
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



