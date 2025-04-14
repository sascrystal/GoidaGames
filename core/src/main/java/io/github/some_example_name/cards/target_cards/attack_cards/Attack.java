package io.github.some_example_name.cards.target_cards.attack_cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class Attack extends CardAttack {
    public Attack() {

        name = "Удар чайником";
        description = "Тип: атака. Наносит 6 урона. Крепкий чай бодрит не только вас!";

        damage = 6;
        cost = 1;

        texture = new Texture(Gdx.files.internal("cards/cardAttack.png"));
        soundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/attackEffect.wav"));
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        totalDamage = totalDamageCalculation(y);
        super.cardAction(x, y, index);
    }
}
