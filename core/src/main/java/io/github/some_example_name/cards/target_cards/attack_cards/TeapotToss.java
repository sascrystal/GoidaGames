package io.github.some_example_name.cards.target_cards.attack_cards;

import com.badlogic.gdx.graphics.Texture;

public class TeapotToss extends CardAttack {
    public TeapotToss() {
        name = "Облить кипятком";
        texture = new Texture("cards/cardTeapotToss.png");
        description = "Тип: атака. Наносит 9 урона. Горячо!";
        damage = 9;
        cost = 2;
    }
}
