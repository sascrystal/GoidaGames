package io.github.some_example_name.cards.target_cards.attack_cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.buffs.MentalDamage;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class PhantomPain extends CardAttack {
    public PhantomPain() {
        name = "Фантомная боль";
        description = "тип: атака. Наносит 8 урона. Накладывает дебафф ментальные повреждения";
        texture = new Texture(Gdx.files.internal("cards/cardPhantomPain.png"));
        damage = 8;
        cost = 1;
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        super.cardAction(x, y, index);
        x.giveBuff(new MentalDamage());
    }
}
