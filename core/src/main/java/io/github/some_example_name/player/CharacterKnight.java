package io.github.some_example_name.player;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;

import io.github.some_example_name.cards.non_target_cards.defence_cards.Defence;
import io.github.some_example_name.cards.target_cards.attack_cards.Attack;
import io.github.some_example_name.utils.Account;
import io.github.some_example_name.utils.ShopSkin;

public class CharacterKnight extends Player {
    public CharacterKnight() {
        Account account = new Account();
        ShopSkin[] shopSkins = ShopSkin.getAllSkins();
        String path = shopSkins[account.getIndexOfSelectedSkin()].getPath();

        textureRightSight = new Texture(Gdx.files.internal(path + "/right_sight.png"));
        textureLeftSight = new Texture(Gdx.files.internal(path + "/left_sight.png"));

        sprite = new Sprite(textureRightSight);

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
