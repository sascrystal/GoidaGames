package io.github.some_example_name.cards.target_cards.attack_cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;

public class LetsGoGambling extends CardAttack {
    public LetsGoGambling() {
        name = "LetsGoGambling";
        description = "Тип: Атака. Наносит врагу 12 урона ИЛИ враг делает действие";
        damage = 12;
        cost = 1;
        texture = new Texture(Gdx.files.internal("cards/cardLetsGoGambling.png"));
    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        int letsGoGambling = (int) (Math.random() * 2);
        switch (letsGoGambling) {
            case 0:
                totalDamage = totalDamageCalculation(y) * 2;
                super.cardAction(x, y, index);
                break;
            case 1:
                x.moveList[x.indexMoveList].enemyAction(x, y);
                soundEffect.play(0.7f);
                y.useManaForCard(this);
                break;

        }

    }
}
