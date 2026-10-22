package ru.hrconnect.app.presentations.list; // Пояснение: Размещаем экран списка в отдельном пакете приложения.

import android.os.Bundle; // Пояснение: Подключаем состояние Activity.
import android.widget.EditText; // Пояснение: Подключаем поле поиска по названию.
import android.widget.LinearLayout; // Пояснение: Подключаем вертикальную компоновку страницы.
import android.widget.TextView; // Пояснение: Подключаем подписи и заголовок.
import androidx.fragment.app.FragmentActivity; // Пояснение: Подключаем Activity, умеющую показывать AndroidX-фрагменты.
import ru.hrconnect.app.R; // Пояснение: Подключаем ID и разметку Android-приложения.
import ru.hrconnect.app.BuildConfig; // Пояснение: Подключаем адрес тестового сервера из Gradle-настроек приложения.
import ru.hrconnect.app.presentations.navigation.NavigationFragment; // Пояснение: Подключаем отдельный фрагмент навигационного меню.
import ru.hrconnect.network.caches.vacancycache.VacancyCache; // Пояснение: Подключаем локальную базу вакансий.
import ru.hrconnect.network.datas.httpvacancyapi.HttpVacancyApi; // Пояснение: Подключаем HTTP-запросы к серверу.
import ru.hrconnect.network.datas.vacancyrepository.VacancyRepository; // Пояснение: Подключаем источник данных с офлайн-кэшем.
import ru.hrconnect.network.domains.vacancy.Vacancy; // Пояснение: Подключаем модель вакансии.
import ru.hrconnect.uikit.components.primarybutton.PrimaryButton; // Пояснение: Подключаем кнопку UI-Kit.
import ru.hrconnect.uikit.components.vacancycard.VacancyCard; // Пояснение: Подключаем карточку вакансии UI-Kit.
import java.util.List; // Пояснение: Подключаем список моделей вакансии.
import java.util.concurrent.ExecutorService; // Пояснение: Подключаем фоновое выполнение запросов.
import java.util.concurrent.Executors; // Пояснение: Подключаем создание фонового потока.

/** Единственная страница приложения: поиск и список вакансий. */ // Пояснение: Фиксируем минимальный экран по заданию.
public final class MainActivity extends FragmentActivity { // Пояснение: Объявляем главную Activity со списком.
    private final ExecutorService executor = Executors.newSingleThreadExecutor(); // Пояснение: Выполняем сеть и SQLite вне UI-потока.
    private VacancyRepository repository; // Пояснение: Храним общий репозиторий API и базы.
    private EditText searchInput; // Пояснение: Храним поле ввода поискового названия.
    private LinearLayout results; // Пояснение: Храним контейнер карточек вакансий.
    private TextView status; // Пояснение: Храним количество результатов или сообщение об ошибке.

    @Override // Пояснение: Создаём интерфейс при открытии страницы.
    protected void onCreate(Bundle state) { // Пояснение: Инициализируем зависимости и элементы экрана.
        super.onCreate(state); // Пояснение: Вызываем стандартную инициализацию Activity.
        repository = new VacancyRepository(new HttpVacancyApi(BuildConfig.API_BASE_URL), new VacancyCache(this)); // Пояснение: Соединяем API-сервер со структурированной SQLite-базой.
        setContentView(R.layout.activity_main); // Пояснение: Загружаем экран из XML, редактируемого в Android Studio Designer.
        searchInput = findViewById(R.id.searchInput); // Пояснение: Находим поле поиска из XML-разметки.
        results = findViewById(R.id.vacancyList); // Пояснение: Находим контейнер карточек из XML-разметки.
        status = findViewById(R.id.resultStatus); // Пояснение: Находим подпись результата из XML-разметки.
        PrimaryButton searchButton = findViewById(R.id.searchButton); // Пояснение: Находим кнопку UI-Kit, добавленную в XML.
        searchButton.setOnClickListener(view -> loadVacancies(searchInput.getText().toString())); // Пояснение: Запускаем поиск по введённому названию.
        getSupportFragmentManager().beginTransaction().replace(R.id.navigationContainer, new NavigationFragment()).commit(); // Пояснение: Добавляем XML-фрагмент меню в нижний контейнер.
        loadVacancies(""); // Пояснение: Загружаем полный список при первом открытии.
    } // Пояснение: Завершаем начальную настройку экрана.

    private void loadVacancies(String query) { // Пояснение: Загружаем список или результат поиска.
        status.setText("Loading vacancies..."); // Пояснение: Показываем состояние загрузки.
        executor.execute(() -> { // Пояснение: Выполняем запрос и обращение к базе в фоне.
            try { // Пояснение: Обрабатываем успешный ответ и сетевую ошибку.
                List<Vacancy> vacancies = repository.loadShort(query); // Пояснение: Вызываем GET /vacancy/short и сохраняем ответ в SQLite.
                boolean offline = repository.wasLastLoadFromCache(); // Пояснение: Определяем, вернул ли репозиторий данные из базы.
                runOnUiThread(() -> showVacancies(vacancies, offline)); // Пояснение: Обновляем View только в главном потоке.
            } catch (Exception error) { // Пояснение: Обрабатываем случай, когда нет сети и локальных данных.
                runOnUiThread(() -> status.setText("Could not load vacancies: " + error.getMessage())); // Пояснение: Показываем понятное состояние ошибки.
            } // Пояснение: Завершаем обработку результата запроса.
        }); // Пояснение: Завершаем фоновую задачу.
    } // Пояснение: Завершаем загрузку списка.

    private void showVacancies(List<Vacancy> vacancies, boolean offline) { // Пояснение: Отображаем полученные данные в карточках UI-Kit.
        results.removeAllViews(); // Пояснение: Удаляем карточки предыдущего результата поиска.
        status.setText(vacancies.size() + " vacancies found" + (offline ? " · offline" : "")); // Пояснение: Показываем число результатов и источник данных.
        for (Vacancy vacancy : vacancies) { // Пояснение: Создаём карточку для каждой вакансии.
            VacancyCard card = new VacancyCard(this, vacancy); // Пояснение: Используем графический элемент карточки из UI-Kit.
            card.setOnClickListener(view -> loadVacancyDetails(vacancy.id)); // Пояснение: При нажатии запрашиваем подробности, не открывая отдельный экран.
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2); // Пояснение: Задаём карточке ширину контейнера.
            params.bottomMargin = dp(14); // Пояснение: Добавляем расстояние до следующей карточки.
            results.addView(card, params); // Пояснение: Добавляем карточку в прокручиваемый список.
        } // Пояснение: Завершаем отображение всех вакансий.
    } // Пояснение: Завершаем заполнение списка.

    private void loadVacancyDetails(int vacancyId) { // Пояснение: Выполняем запрос подробностей выбранной вакансии.
        status.setText("Loading vacancy details..."); // Пояснение: Показываем состояние загрузки без перехода с экрана списка.
        executor.execute(() -> { // Пояснение: Не выполняем сетевой запрос в главном потоке.
            try { // Пояснение: Запрашиваем подробности и учитываем локальный кэш.
                Vacancy vacancy = repository.loadDetails(vacancyId); // Пояснение: Вызываем GET /vacancy/{id}; репозиторий сохраняет результат в SQLite.
                boolean offline = repository.wasLastLoadFromCache(); // Пояснение: Проверяем, получены ли подробности из локальной базы.
                runOnUiThread(() -> status.setText("Loaded: " + vacancy.title + (offline ? " · offline" : ""))); // Пояснение: Показываем результат, не открывая новую страницу.
            } catch (Exception error) { // Пояснение: Обрабатываем сетевую ошибку и отсутствие кэшированной вакансии.
                runOnUiThread(() -> status.setText("Could not load details: " + error.getMessage())); // Пояснение: Отображаем причину ошибки на текущей странице.
            } // Пояснение: Завершаем обработку ответа.
        }); // Пояснение: Завершаем фоновую задачу запроса.
    } // Пояснение: Завершаем обработчик запроса вакансии по ID.

    private int dp(int value) { // Пояснение: Переводим dp в реальные пиксели экрана.
        return Math.round(value * getResources().getDisplayMetrics().density); // Пояснение: Учитываем плотность дисплея.
    } // Пояснение: Завершаем преобразование размера.

    @Override // Пояснение: Освобождаем фоновый поток после закрытия приложения.
    protected void onDestroy() { // Пояснение: Обрабатываем уничтожение Activity.
        executor.shutdownNow(); // Пояснение: Останавливаем оставшиеся фоновые запросы.
        super.onDestroy(); // Пояснение: Вызываем стандартное завершение Activity.
    } // Пояснение: Завершаем очистку экрана.
} // Пояснение: Завершаем класс главной страницы.
