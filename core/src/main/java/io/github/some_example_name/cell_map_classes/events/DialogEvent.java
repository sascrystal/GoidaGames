package io.github.some_example_name.cell_map_classes.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

import io.github.some_example_name.Main;
import io.github.some_example_name.screens.MapScreen;

import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.player.Player;
import io.github.some_example_name.screens.QuestionMarkScreen;

public abstract class DialogEvent {
    protected String[] dialogOptions;
    protected String eventDescription;
    protected Texture background;
    private static final DialogEvent[] EVENTS;
    static{
        EVENTS = new DialogEvent[3];
        EVENTS[0] = new GolikovEvent();
        EVENTS[1] = new ShrineEvent();
        EVENTS[2] = new DanceEvent();
    }

    public Texture getBackground() {
        return background;
    }

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
    public static DialogEvent generateEventAct1(){
        int random = MathUtils.random(EVENTS.length -1 );
        DialogEvent randomEvent =  EVENTS[random];
        returnCardFromPool(random);
        return randomEvent;
    }
    private static void returnCardFromPool(int index){
        switch (index){
            case 0:
                EVENTS[index] = new GolikovEvent();
                break;
            case 1:
                EVENTS[index] = new ShrineEvent();
                break;
            case 2:
                EVENTS[index] = new DanceEvent();
                break;
        }
    }

}



