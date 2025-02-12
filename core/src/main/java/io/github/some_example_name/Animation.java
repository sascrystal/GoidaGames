package io.github.some_example_name;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class Animation {
    private float scale; // Текущий масштаб
    private float scaleSpeed; // Скорость изменения масштаба
    private float maxScale; // Максимальный масштаб
    private float minScale; // Минимальный масштаб
    private boolean increasing; // Увеличивается ли масштаб в данный момент
    private float rotation; // Угол наклона
    private float targetRotation; // Целевой угол наклона
    private float rotationSpeed; // Скорость изменения угла наклона

    public Animation(float minScale, float maxScale, float scaleSpeed, float rotationSpeed) {
        this.minScale = minScale;
        this.maxScale = maxScale;
        this.scaleSpeed = scaleSpeed;
        this.scale = minScale; // Начальный масштаб
        this.increasing = true; // Начинаем с увеличения
        this.rotation = 0; // Начальный угол наклона
        this.targetRotation = 0; // Начальный целевой угол наклона
        this.rotationSpeed = 1f; // Установка скорости вращения
    }

    public void setRotation(float rotation) {
        this.targetRotation = rotation; // Устанавливаем целевой угол наклона
    }


    public void update(float delta) {
        // Обновление масштаба
        if (increasing) {
            scale += scaleSpeed * delta; // Увеличиваем масштаб
            if (scale >= maxScale) {
                scale = maxScale; // Ограничиваем максимальный масштаб
                increasing = false; // Меняем направление
            }
        } else {
            scale -= scaleSpeed * delta; // Уменьшаем масштаб
            if (scale <= minScale) {
                scale = minScale; // Ограничиваем минимальный масштаб
                increasing = true; // Меняем направление
            }
        }

        // Обновление угла наклона
        if (rotation != targetRotation) {
            if (Math.abs(rotation - targetRotation) < rotationSpeed * delta) {
                rotation = targetRotation; // Устанавливаем целевой угол
            } else {
                rotation += (rotation < targetRotation ? rotationSpeed : -rotationSpeed) * delta; // Плавное изменение угла
            }
        }
    }


    public void draw(SpriteBatch batch, TextureRegion textureRegion, float x, float y) {
        float width = textureRegion.getRegionWidth();
        float height = textureRegion.getRegionHeight();
        float originX = width / 2; // Центр по X
        float originY = height / 2; // Центр по Y

        // Рисуем с учетом масштаба и наклона
        batch.draw(textureRegion, x, y, originX, originY, width, height, scale, scale, rotation);
    }

    public float getScale() {
        return scale;
    }
}

