package io.github.some_example_name.cards.non_target_cards.buff_cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.buffs.Power;
import io.github.some_example_name.cards.non_target_cards.NonTargetCard;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class CookieOfPower extends NonTargetCard {
    int power;
    int damageSelf;

    public CookieOfPower() {
        name = "Печенька силы";
        description = "Тип: способность. Дает 1 силу, наносит 2 урон ВАМ. Что нас не убивает, делает нас сильнее";
        power = 1;
        damageSelf = 2;
        cost = 0;
        texture = new Texture(Gdx.files.internal("cards/cardCookieOfPower.png"));

    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        y.giveBuff(new Power());
        y.takeDamage(damageSelf);

    }
}
