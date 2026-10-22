package ru.hrconnect.network.datas.httpvacancyapi; // Пояснение: Размещаем HTTP-клиент в его собственном пакете.

import org.json.JSONArray; // Пояснение: Подключаем чтение массива списка вакансий.
import org.json.JSONObject; // Пояснение: Подключаем чтение JSON-объекта вакансии.
import ru.hrconnect.network.datas.vacancyapi.VacancyApi; // Пояснение: Подключаем реализуемый интерфейс запросов.
import ru.hrconnect.network.domains.vacancy.Vacancy; // Пояснение: Подключаем модель, в которую преобразуем JSON.
import java.io.BufferedReader; // Пояснение: Подключаем чтение ответа сервера.
import java.io.InputStreamReader; // Пояснение: Подключаем преобразование потока ответа в текст.
import java.net.HttpURLConnection; // Пояснение: Подключаем HTTP-соединение.
import java.net.URLEncoder; // Пояснение: Подключаем безопасное кодирование поиска в URL.
import java.net.URL; // Пояснение: Подключаем URL запроса.
import java.nio.charset.StandardCharsets; // Пояснение: Подключаем UTF-8 для текста запроса и ответа.
import java.util.ArrayList; // Пояснение: Подключаем создание изменяемого списка результатов.
import java.util.List; // Пояснение: Подключаем контракт коллекции вакансий.

/** Реализация запросов к серверу Swagger. */ // Пояснение: Объясняем роль класса в архитектуре.
public final class HttpVacancyApi implements VacancyApi { // Пояснение: Объявляем реализацию интерфейса запросов.
    private final String baseUrl; // Пояснение: Храним базовый адрес тестового API.

    public HttpVacancyApi(String baseUrl) { // Пояснение: Принимаем адрес сервера при создании клиента.
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl; // Пояснение: Убираем завершающий слеш, чтобы корректно собрать маршрут.
    } // Пояснение: Завершаем конструктор клиента.

    @Override // Пояснение: Реализуем первый запрос из экзаменационного задания.
    public List<Vacancy> getShort(String search) throws Exception { // Пояснение: Получаем короткий список, при необходимости добавляя поиск.
        String route = "/vacancy/short"; // Пояснение: Выбираем Swagger-маршрут краткого списка.
        if (search != null && !search.trim().isEmpty()) route += "?search=" + URLEncoder.encode(search.trim(), StandardCharsets.UTF_8.name()); // Пояснение: Кодируем введённое название в параметр запроса.
        JSONArray response = new JSONArray(get(route)); // Пояснение: Разбираем тело ответа как JSON-массив.
        List<Vacancy> result = new ArrayList<>(); // Пояснение: Создаём результирующий список моделей.
        for (int index = 0; index < response.length(); index++) result.add(Vacancy.fromJson(response.getJSONObject(index))); // Пояснение: Преобразуем каждую строку массива в Vacancy.
        return result; // Пояснение: Возвращаем полученные вакансии репозиторию.
    } // Пояснение: Завершаем реализацию первого GET-запроса.

    @Override // Пояснение: Реализуем второй запрос из экзаменационного задания.
    public Vacancy getDetails(int id) throws Exception { // Пояснение: Получаем подробные данные выбранной вакансии.
        return Vacancy.fromJson(new JSONObject(get("/vacancy/" + id))); // Пояснение: Запрашиваем /vacancy/{id} и преобразуем объект в модель.
    } // Пояснение: Завершаем реализацию второго GET-запроса.

    private String get(String route) throws Exception { // Пояснение: Выполняем общий GET по переданному маршруту.
        HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl + route).openConnection(); // Пояснение: Открываем соединение с нужным URL.
        connection.setRequestMethod("GET"); // Пояснение: Указываем метод чтения GET.
        connection.setConnectTimeout(8000); // Пояснение: Ограничиваем ожидание подключения.
        connection.setReadTimeout(8000); // Пояснение: Ограничиваем ожидание ответа сервера.
        connection.setRequestProperty("Accept", "application/json"); // Пояснение: Просим вернуть JSON.
        int status = connection.getResponseCode(); // Пояснение: Читаем HTTP-код ответа.
        java.io.InputStream stream = status >= 200 && status < 300 ? connection.getInputStream() : connection.getErrorStream(); // Пояснение: Выбираем поток успешного ответа или ошибки.
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8)); // Пояснение: Декодируем тело ответа как UTF-8.
        StringBuilder body = new StringBuilder(); // Пояснение: Создаём буфер полного текста ответа.
        String line; // Пояснение: Объявляем переменную одной прочитанной строки.
        while ((line = reader.readLine()) != null) body.append(line); // Пояснение: Собираем все строки тела в одну строку.
        reader.close(); // Пояснение: Закрываем поток ответа.
        connection.disconnect(); // Пояснение: Освобождаем сетевое соединение.
        if (status < 200 || status >= 300) throw new IllegalStateException("HTTP " + status + ": " + body); // Пояснение: Передаём HTTP-ошибку вызывающему репозиторию.
        return body.toString(); // Пояснение: Возвращаем JSON успешного ответа.
    } // Пояснение: Завершаем общий HTTP-метод.
} // Пояснение: Завершаем класс сетевого клиента.
