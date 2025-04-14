package io.github.some_example_name.cell_map_classes.events;

import io.github.some_example_name.cards.PlayingCard;
import io.github.some_example_name.cards.target_cards.attack_cards.PhantomPain;
import io.github.some_example_name.cell_map_classes.cell_maps.CellMap;
import io.github.some_example_name.player.Player;

public class ShrineEvent extends DialogEvent {
    public ShrineEvent() {
        eventDescription = "Вы встречаете святилище, в центре которого находится вода";
        dialogOptions = new String[3];
        dialogOptions[0] = "Выпить воду";
        dialogOptions[1] = "Уйти из этого места";
        dialogOptions[2] = "Поискать запасы";
    }

    @Override
    public void actionFirst(Player player, CellMap[][] map) {
        int heal = 15;
        player.healing(heal);
        returnToMap(player, map);
    }

    @Override
    public void actionThird(Player player, CellMap[][] map) {
        PlayingCard givingCard = new PhantomPain();
        player.giveTheCard(givingCard);
        returnToMap(player, map);
    }
}
