package io.github.some_example_name.cards.non_target_cards.buff_cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.buffs.Reinforce;
import io.github.some_example_name.cards.non_target_cards.NonTargetCard;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class CookieOfReinforce extends NonTargetCard {
    int reinforce, damageSelf;

    public CookieOfReinforce() {
        name = "Железная печенька";
        description = "Тип: способность. Дает 1 укрепление, наносит 2 урон ВАМ P.S сколько бы вы ее не макали в чай она все равно каменная";
        reinforce = 1;
        damageSelf = 2;
        cost = 0;
        texture = new Texture(Gdx.files.internal("cards/cardCookieOfReinforce.png"));

    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.giveBuff(new Reinforce());
        y.takeDamage(damageSelf);

    }
}
