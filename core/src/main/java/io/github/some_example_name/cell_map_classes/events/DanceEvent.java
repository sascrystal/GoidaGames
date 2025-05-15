package io.github.some_example_name.cell_map_classes.events;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

import io.github.some_example_name.cards.PlayingCard;
import io.github.some_example_name.cards.non_target_cards.buff_cards.DanceKiller;
import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.player.Player;

public class DanceEvent extends DialogEvent {
    public DanceEvent() {
        score = 40;
        background = new Texture(Gdx.files.internal("backgrounds/dance_event_background.jpg"));
        eventDescription = "Ты заходишь в комнату, которая сделана как будто для господ 18 века. По среди этой красоты стоит девушка, возрастом 20-25 лет, одетая в красное платье. Замечая тебя, она протягивает тебе руку и приглашает на танец. Что ты будешь делать?";
        dialogOptions = new String[3];
        dialogOptions[0] = "Станцую с ней";
        dialogOptions[1] = "Убью её, вдруг монстр?";
        dialogOptions[2] = "Лучше уйти из этого места";
    }

    @Override
    public void actionThird(Player player, CellMap[][] map) {
        super.actionThird(player, map);
    }

    @Override
    public void actionSecond(Player player, CellMap[][] map) {
        PlayingCard givingCard = new DanceKiller();
        player.addCardInDeck(givingCard);
        super.actionSecond(player, map);
    }

    @Override
    public void actionFirst(Player player, CellMap[][] map) {
        player.healing(15);
        super.actionFirst(player, map);
    }
}
