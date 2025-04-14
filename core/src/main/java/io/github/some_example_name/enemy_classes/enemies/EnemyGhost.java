package io.github.some_example_name.enemy_classes.enemies;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.Array;

import io.github.some_example_name.enemy_classes.enemy_moves.AttackEnemy;
import io.github.some_example_name.enemy_classes.enemy_moves.MoveEnemy;

public class EnemyGhost extends Enemy {
    public EnemyGhost() {
        // Загрузка текстуры спрайт-листа
        Texture spriteSheet = new Texture(Gdx.files.internal("enemies/Enemy_sprite.png"));


        // Создание TextureRegion для каждого кадра анимации
        int frameCount = 3; // Количество кадров в спрайт-листе
        int frameWidth = spriteSheet.getWidth() / frameCount; // Ширина каждого кадра
        int frameHeight = spriteSheet.getHeight(); // Высота каждого кадра

        Array<TextureRegion> frames = new Array<>();

        // Извлечение кадров из спрайт-листа
        for (int i = 0; i < frameCount; i++) {
            frames.add(new TextureRegion(spriteSheet, i * frameWidth, 0, frameWidth, frameHeight));
        }

        // Инициализация анимации
        animation = new Animation<>(0.3f, frames); // 0.1f - время между кадрами
        stateTime = 0f; // Инициализация времени состояния

        // Установка границ
        bounds = new Rectangle((float) (Gdx.graphics.getWidth() / 2.4),
            (float) (Gdx.graphics.getHeight() / 2.5),
            (float) (frameWidth / 291.0),
            (float) (frameHeight / 291.0));
        health = 35; // Установка здоровья
        moveList = new MoveEnemy[3]; // Установка массива возможностей моба
        for (int i = 0; i < moveList.length; i++) {
            moveList[i] = new AttackEnemy(i + 1);
        }
    }

    // Метод обновления состояния (вызывается в каждом кадре)

}
