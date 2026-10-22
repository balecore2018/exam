package ru.hrconnect.network.datas.vacancyrepository; // Пояснение: Размещаем репозиторий в его собственном пакете.

import ru.hrconnect.network.caches.vacancycache.VacancyCache; // Пояснение: Подключаем локальную структурированную базу.
import ru.hrconnect.network.datas.vacancyapi.VacancyApi; // Пояснение: Подключаем интерфейс сетевого API.
import ru.hrconnect.network.domains.vacancy.Vacancy; // Пояснение: Подключаем модель вакансии.
import java.util.List; // Пояснение: Подключаем тип списка результата.

/** Связывает Swagger API и локальный SQLite-кэш. */ // Пояснение: Описываем ответственность репозитория.
public final class VacancyRepository { // Пояснение: Объявляем единый источник вакансий для экранов.
    private final VacancyApi api; // Пояснение: Храним интерфейс сетевых запросов.
    private final VacancyCache cache; // Пояснение: Храним помощник SQLite-базы.
    private volatile boolean lastLoadFromCache; // Пояснение: Сообщаем экрану, использовалась сеть или локальная база.

    public VacancyRepository(VacancyApi api, VacancyCache cache) { // Пояснение: Получаем API и кэш через конструктор.
        this.api = api; // Пояснение: Сохраняем сетевой источник.
        this.cache = cache; // Пояснение: Сохраняем локальный источник.
    } // Пояснение: Завершаем создание репозитория.

    public List<Vacancy> loadShort(String search) throws Exception { // Пояснение: Загружаем список из API с офлайн-резервом.
        lastLoadFromCache = false; // Пояснение: Сбрасываем признак перед новым запросом.
        try { // Пояснение: Сначала пытаемся получить актуальный список с сервера.
            List<Vacancy> result = api.getShort(search); // Пояснение: Вызываем первый GET-запрос сетевого слоя.
            cache.saveAll(result); // Пояснение: Сохраняем структурированные поля в SQLite.
            return result; // Пояснение: Возвращаем актуальный ответ API.
        } catch (Exception networkError) { // Пояснение: Переключаемся на локальные данные при сетевой ошибке.
            List<Vacancy> saved = cache.find(search); // Пояснение: Выполняем поиск в сохранённых строках SQLite.
            if (!saved.isEmpty()) { lastLoadFromCache = true; return saved; } // Пояснение: Помечаем и возвращаем кэшированный список.
            throw networkError; // Пояснение: Передаём ошибку экрану, когда кэша нет.
        } // Пояснение: Завершаем загрузку списка.
    } // Пояснение: Завершаем метод первого запроса.

    public Vacancy loadDetails(int id) throws Exception { // Пояснение: Загружаем подробности с офлайн-резервом.
        lastLoadFromCache = false; // Пояснение: Сбрасываем признак перед запросом карточки.
        try { // Пояснение: Сначала запрашиваем карточку с сервера.
            Vacancy result = api.getDetails(id); // Пояснение: Вызываем второй GET-запрос сетевого слоя.
            cache.save(result); // Пояснение: Обновляем подробную структурированную строку в SQLite.
            return result; // Пояснение: Возвращаем актуальную карточку.
        } catch (Exception networkError) { // Пояснение: Обрабатываем недоступность API.
            Vacancy saved = cache.findById(id); // Пояснение: Ищем карточку в локальной базе по ID.
            if (saved != null) { lastLoadFromCache = true; return saved; } // Пояснение: Помечаем и возвращаем локальную карточку.
            throw networkError; // Пояснение: Передаём ошибку экрану, если локальной записи нет.
        } // Пояснение: Завершаем загрузку подробностей.
    } // Пояснение: Завершаем метод второго запроса.

    public boolean wasLastLoadFromCache() { return lastLoadFromCache; } // Пояснение: Возвращаем экрану источник последнего результата.
} // Пояснение: Завершаем класс репозитория.
