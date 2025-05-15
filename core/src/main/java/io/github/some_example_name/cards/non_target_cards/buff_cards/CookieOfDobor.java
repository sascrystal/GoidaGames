package io.github.some_example_name.cards.non_target_cards.buff_cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.cards.non_target_cards.NonTargetCard;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class CookieOfDobor extends NonTargetCard {
    int extraCards, damageSelf;

    public CookieOfDobor() {
        extraCards = 2;
        damageSelf = 2;
        name = "Печенька добора";
        description = "Тип: способность. дает 2 карты, наносит 2 урон ВАМ";
        cost = 0;
        texture = new Texture(Gdx.files.internal("cards/cardCookieOfReinforce.png"));
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.takeCardsFromDraftDeck(extraCards);
        y.takeDamage(damageSelf);
    }


}
