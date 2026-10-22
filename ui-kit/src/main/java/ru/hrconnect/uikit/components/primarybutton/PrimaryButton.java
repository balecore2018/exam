package ru.hrconnect.uikit.components.primarybutton; // Пояснение: Размещаем кнопку в её собственном пакете UI-Kit.

import android.content.Context; // Пояснение: Подключаем контекст приложения для создания View.
import android.graphics.Color; // Пояснение: Подключаем палитру кнопки.
import android.graphics.drawable.GradientDrawable; // Пояснение: Подключаем скруглённый фон кнопки.
import android.util.AttributeSet; // Пояснение: Подключаем XML-атрибуты для создания кнопки в Android Designer.
import android.widget.Button; // Пояснение: Подключаем базовый Android Button.

/** Кнопка UI-Kit с синей заливкой или контурным стилем. */ // Пояснение: Описываем компонент, используемый на странице списка.
public final class PrimaryButton extends Button { // Пояснение: Создаём переиспользуемый графический элемент №1.
    public PrimaryButton(Context context, String title, boolean primary) { // Пояснение: Принимаем подпись и стиль кнопки.
        super(context); // Пояснение: Инициализируем базовую Android-кнопку.
        configure(context, title, primary); // Пояснение: Применяем общий вид кнопки, создаваемой из Java.
    } // Пояснение: Завершаем конструктор кнопки из Java.

    public PrimaryButton(Context context, AttributeSet attributes) { // Пояснение: Позволяем добавлять кнопку UI-Kit прямо из XML-разметки.
        super(context, attributes); // Пояснение: Инициализируем кнопку атрибутами Android Designer.
        configure(context, getText() == null ? "" : getText().toString(), true); // Пояснение: Оформляем XML-кнопку как основную.
    } // Пояснение: Завершаем конструктор кнопки из XML.

    private void configure(Context context, String title, boolean primary) { // Пояснение: Настраиваем единый стиль обоих вариантов кнопки.
        setText(title); // Пояснение: Устанавливаем текст на кнопке.
        setAllCaps(false); // Пояснение: Оставляем регистр подписи без автоматической замены.
        setTextSize(12); // Пояснение: Задаём компактный размер текста.
        setPadding(12, 0, 12, 0); // Пояснение: Добавляем внутренние боковые отступы.
        setTextColor(primary ? Color.WHITE : Color.rgb(0, 76, 210)); // Пояснение: Выбираем белый или синий текст по стилю.
        GradientDrawable background = new GradientDrawable(); // Пояснение: Создаём фигуру фона кнопки.
        background.setColor(primary ? Color.rgb(0, 76, 210) : Color.WHITE); // Пояснение: Выбираем синюю или белую заливку.
        background.setCornerRadius(context.getResources().getDisplayMetrics().density * 7); // Пояснение: Скругляем углы на семь dp.
        background.setStroke(Math.round(context.getResources().getDisplayMetrics().density), Color.rgb(0, 76, 210)); // Пояснение: Рисуем тонкую синюю границу.
        setBackground(background); // Пояснение: Применяем фон к кнопке.
        if (primary) setElevation(2); // Пояснение: Добавляем тень только основной синей кнопке.
    } // Пояснение: Завершаем настройку компонента кнопки.
} // Пояснение: Завершаем класс PrimaryButton.
