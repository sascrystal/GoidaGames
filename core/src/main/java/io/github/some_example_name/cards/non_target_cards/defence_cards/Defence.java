package io.github.some_example_name.cards.non_target_cards.defence_cards;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureAtlas;

import io.github.some_example_name.buffs.Reinforce;
import io.github.some_example_name.enemy_classes.enemies.Enemy;
import io.github.some_example_name.player.Player;


public class Defence extends DefenceCard {


    public Defence() {
        frames = new TextureAtlas(Gdx.files.internal("animationCards/electic.atlas"));
        effect = new Animation<>(1 / 15F,
            frames.findRegions("Eletric A-Sheet"),
            Animation.PlayMode.NORMAL);

        name = "Ромашковый чай";
        description = "Тип: способность. Дает 6 брони. Успокаивает...";
        shield = 6;
        cost = 1;
        texture = new Texture(Gdx.files.internal("cards/cardDefence.png"));
        soundEffect = Gdx.audio.newSound(Gdx.files.internal("sounds/shieldEffect.wav"));

    }

    @Override
    public void cardAction(Enemy x, Player y, int index) {
        totalShield = shield + y.buffStack(new Reinforce());
        super.cardAction(x, y, index);
    }
}
