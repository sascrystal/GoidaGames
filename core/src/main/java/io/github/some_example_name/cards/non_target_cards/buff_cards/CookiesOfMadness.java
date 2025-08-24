package io.github.some_example_name.cards.non_target_cards.buff_cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.buffs.CookiesOfMadnessBuff;
import io.github.some_example_name.cards.non_target_cards.NonTargetCard;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class CookiesOfMadness extends NonTargetCard {
    public CookiesOfMadness() {
        name = "Печенька безумия";
        description = "Тип: талант. За каждый стак в начале хода дает дополнительную карту печенек";
        texture = new Texture(Gdx.files.internal("cards/cardCookieOfMadness.png"));
        cost = 2;
        burnable = true;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.giveBuff(new CookiesOfMadnessBuff());


    }
}
