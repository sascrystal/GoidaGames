package io.github.some_example_name.player;


import io.github.some_example_name.cards.target_cards.attack_cards.Attack;

public class CharacterKnight extends Player {
    public CharacterKnight() {

        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());


        maxHealth = 60;
        health = maxHealth;// Начальное здоровье игрока
        shield = 0;
        manaPool = 3;
        manaPoolMax = 3;
        draftCount = 4;
    }


}
