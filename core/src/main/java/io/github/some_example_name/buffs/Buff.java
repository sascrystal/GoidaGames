package io.github.some_example_name.buffs;


import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;


public abstract class Buff {
    protected boolean decrease;
    //это флаги, которые показывают когда триггерится бафф
    protected boolean inEndTurn = false, inBeginTurn = false, inCardAction = false,
        inBeginFight = false, inReaction = false;
    protected int stack;
    protected String name, description;
    protected Sprite sprite;


    public String getName() {
        return name;
    }

    public int getStack() {
        return stack;
    }

    public String getDescription() {
        return description;
    }

    public boolean isDecrease() {
        return decrease;
    }

    public boolean triggerBuff(String situation) {
        switch (situation) {
            case "EndTurn":
                return inEndTurn;
            case "BeginTurn":
                return inBeginTurn;
            case "CardAction":
                return inCardAction;
            case "BeginFight":
                return inBeginFight;
            case "Reaction":
                return inReaction;
            default:
                return false;
        }
    }

    public void buffAction() {

    }

    public void buffAction(Enemy owner) {

    }

    public void buffAction(Player player) {

    }

    public void addStack() {
        stack += 1;
    }

    public void decreaseStack() {
        stack -= 1;
    }


}


