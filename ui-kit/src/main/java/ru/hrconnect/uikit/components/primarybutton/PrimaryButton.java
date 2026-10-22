package ru.hrconnect.uikit.components.primarybutton;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.AttributeSet;
import android.widget.Button;

/** Кнопка UI-Kit с синей заливкой или контурным стилем. */
public final class PrimaryButton extends Button {
    public PrimaryButton(Context context, String title, boolean primary) {
        super(context);
        configure(context, title, primary);
    }

    public PrimaryButton(Context context, AttributeSet attributes) {
        super(context, attributes);
        configure(context, getText() == null ? "" : getText().toString(), true);
    }

    private void configure(Context context, String title, boolean primary) {
        setText(title);
        setAllCaps(false);
        setTextSize(12);
        setPadding(12, 0, 12, 0);
        setTextColor(primary ? Color.WHITE : Color.rgb(0, 76, 210));
        GradientDrawable background = new GradientDrawable();
        background.setColor(primary ? Color.rgb(0, 76, 210) : Color.WHITE);
        background.setCornerRadius(context.getResources().getDisplayMetrics().density * 7);
        background.setStroke(Math.round(context.getResources().getDisplayMetrics().density), Color.rgb(0, 76, 210));
        setBackground(background);
        if (primary) setElevation(2);
    }
}
