package io.github.some_example_name.player;


import io.github.some_example_name.cards.non_target_cards.defence_cards.Defence;
import io.github.some_example_name.cards.target_cards.attack_cards.Attack;

public class CharacterKnight extends Player {
    public CharacterKnight() {

        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Attack());
        deck.add(new Defence());
        deck.add(new Defence());
        deck.add(new Defence());
        deck.add(new Defence());


        maxHealth = 60;
        health = maxHealth;// Начальное здоровье игрока
        shield = 0;
        manaPool = 3;
        manaPoolMax = 3;
        draftCount = 4;
        score = 0;
    }
}
