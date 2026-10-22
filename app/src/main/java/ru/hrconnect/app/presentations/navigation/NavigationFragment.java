package ru.hrconnect.app.presentations.navigation; // Пояснение: Размещаем навигационный фрагмент в собственной папке.

import android.os.Bundle; // Пояснение: Подключаем состояние Fragment.
import android.view.LayoutInflater; // Пояснение: Подключаем загрузку XML-разметки.
import android.view.View; // Пояснение: Подключаем базовые View.
import android.view.ViewGroup; // Пояснение: Подключаем тип контейнера родителя.
import androidx.annotation.NonNull; // Пояснение: Подключаем аннотацию обязательного результата.
import androidx.annotation.Nullable; // Пояснение: Подключаем аннотацию необязательного состояния.
import androidx.fragment.app.Fragment; // Пояснение: Подключаем базовый класс AndroidX-фрагмента.
import ru.hrconnect.app.R; // Пояснение: Подключаем XML-разметку меню.

/** Нижнее меню является отдельным фрагментом, как требует задание. */ // Пояснение: Объясняем, зачем меню вынесено из Activity.
public final class NavigationFragment extends Fragment { // Пояснение: Объявляем фрагмент навигационного меню.
    @Nullable // Пояснение: Разрешаем системе передать отсутствующее сохранённое состояние.
    @Override // Пояснение: Создаём визуальное содержимое фрагмента.
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle state) { // Пояснение: Собираем нижние пункты меню.
        return inflater.inflate(R.layout.fragment_navigation, container, false); // Пояснение: Загружаем XML меню, редактируемое через Android Studio Designer.
    } // Пояснение: Завершаем создание меню.
} // Пояснение: Завершаем класс навигационного фрагмента.
