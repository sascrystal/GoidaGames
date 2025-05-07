package io.github.some_example_name.cell_map_classes.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.MathUtils;

import java.util.ArrayList;

import io.github.some_example_name.Main;
import io.github.some_example_name.cards.PlayingCard;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfDobor;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfMana;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfPower;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookieOfReinforce;
import io.github.some_example_name.cards.non_target_cards.buff_cards.CookiesOfMadness;
import io.github.some_example_name.cards.non_target_cards.defence_cards.CupDefense;
import io.github.some_example_name.cards.non_target_cards.defence_cards.Evade;
import io.github.some_example_name.cards.non_target_cards.defence_cards.GlassShield;
import io.github.some_example_name.cards.non_target_cards.defence_cards.Inheritance;
import io.github.some_example_name.cards.target_cards.Overload;
import io.github.some_example_name.cards.target_cards.attack_cards.AttackVoid;
import io.github.some_example_name.cards.target_cards.attack_cards.ComboAttack;
import io.github.some_example_name.cards.target_cards.attack_cards.FeintCard;
import io.github.some_example_name.cards.target_cards.attack_cards.LetsGoGambling;
import io.github.some_example_name.cards.target_cards.attack_cards.PhantomPain;
import io.github.some_example_name.cards.target_cards.attack_cards.SugarSplash;
import io.github.some_example_name.cards.target_cards.attack_cards.TeapotToss;
import io.github.some_example_name.screens.MapScreen;

import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.player.Player;
import io.github.some_example_name.screens.QuestionMarkScreen;

public abstract class DialogEvent {
    protected String[] dialogOptions;
    protected String eventDescription;
    protected Texture background;

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
        ArrayList<DialogEvent> EVENTS = new ArrayList<>();
        EVENTS.add(new GolikovEvent());
        EVENTS.add(new ShrineEvent());
        EVENTS.add(new DanceEvent());
        return EVENTS.get(MathUtils.random(EVENTS.size()-1));
    }

}



