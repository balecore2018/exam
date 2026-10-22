package ru.hrconnect.uikit.components.vacancycard; // Пояснение: Размещаем карточку вакансии в собственном пакете UI-Kit.

import android.content.Context; // Пояснение: Подключаем контекст для загрузки макета.
import android.util.AttributeSet; // Пояснение: Подключаем XML-атрибуты Android View.
import android.view.LayoutInflater; // Пояснение: Подключаем инфлятор XML-разметки.
import android.widget.LinearLayout; // Пояснение: Подключаем контейнер карточки.
import android.widget.TextView; // Пояснение: Подключаем текстовые поля вакансии.
import ru.hrconnect.network.domains.vacancy.Vacancy; // Пояснение: Подключаем модель данных вакансии.
import ru.hrconnect.uikit.R; // Пояснение: Подключаем ресурсы XML-макета UI-Kit.

/** Карточка вакансии из экзаменационного макета. */ // Пояснение: Объявляем переиспользуемый UI-компонент.
public final class VacancyCard extends LinearLayout { // Пояснение: Создаём компонент-контейнер карточки.
    public VacancyCard(Context context, Vacancy vacancy) { // Пояснение: Создаём карточку и передаём ей данные вакансии.
        super(context); // Пояснение: Инициализируем базовый LinearLayout.
        initialize(context, vacancy); // Пояснение: Загружаем макет и заполняем его данными.
    } // Пояснение: Завершаем конструктор карточки для приложения.

    public VacancyCard(Context context, AttributeSet attributes) { // Пояснение: Разрешаем Android Studio отображать компонент при XML-предпросмотре.
        super(context, attributes); // Пояснение: Инициализируем View атрибутами из макета.
        initialize(context, new Vacancy(0, "Senior Product Designer", "Product Team", "Full-time", 24, "$120k - $160k", "", "", "")); // Пояснение: Показываем примерные данные в UI Designer.
    } // Пояснение: Завершаем XML-конструктор компонента.

    private void initialize(Context context, Vacancy vacancy) { // Пояснение: Настраиваем карточку и связываем её макет с моделью.
        setOrientation(VERTICAL); // Пояснение: Располагаем строку вакансии и счётчик по вертикали.
        setClickable(true); // Пояснение: Разрешаем экрану списка обработать нажатие для запроса подробностей.
        setFocusable(false); // Пояснение: Не включаем карточку в обход элементов клавиатурой.
        setBackgroundResource(R.drawable.vacancy_card_background); // Пояснение: Применяем белый фон, контур и скругление из UI-Kit.
        setElevation(context.getResources().getDisplayMetrics().density * 2); // Пояснение: Добавляем лёгкую тень карточке.
        LayoutInflater.from(context).inflate(R.layout.vacancy_card, this, true); // Пояснение: Загружаем XML-карточку, редактируемую в Android Studio Designer.
        TextView badge = findViewById(R.id.vacancyBadge); // Пояснение: Находим круглый значок вакансии из XML.
        TextView title = findViewById(R.id.vacancyTitle); // Пояснение: Находим заголовок вакансии из XML.
        TextView team = findViewById(R.id.vacancyTeam); // Пояснение: Находим название отдела из XML.
        TextView format = findViewById(R.id.vacancyFormat); // Пояснение: Находим формат работы из XML.
        TextView salary = findViewById(R.id.vacancySalary); // Пояснение: Находим зарплату из XML.
        TextView applicants = findViewById(R.id.vacancyApplicants); // Пояснение: Находим число откликов из XML.
        badge.setText(initials(vacancy.title)); // Пояснение: Записываем инициалы названия вакансии в круг.
        title.setText(vacancy.title); // Пояснение: Записываем название вакансии.
        team.setText(vacancy.department); // Пояснение: Записываем команду или отдел.
        format.setText(vacancy.workFormat); // Пояснение: Записываем формат занятости.
        salary.setText("▣ " + vacancy.salaryRange); // Пояснение: Записываем диапазон зарплаты.
        salary.setVisibility(vacancy.salaryRange.isEmpty() ? GONE : VISIBLE); // Пояснение: Скрываем зарплатную строку, если сервер её не прислал.
        applicants.setText(String.valueOf(vacancy.applicantsCount)); // Пояснение: Показываем число откликов.
    } // Пояснение: Завершаем создание и заполнение карточки.

    private String initials(String title) { // Пояснение: Получаем короткую подпись для круглого значка.
        String[] words = title.trim().split("\\s+"); // Пояснение: Разделяем название на слова.
        if (words.length > 1) return (words[0].substring(0, 1) + words[1].substring(0, 1)).toUpperCase(); // Пояснение: Берём первые буквы первых двух слов.
        return title.substring(0, Math.min(2, title.length())).toUpperCase(); // Пояснение: Для одного слова берём первые две буквы.
    } // Пояснение: Завершаем получение инициалов.
} // Пояснение: Завершаем класс VacancyCard.
