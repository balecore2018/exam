package ru.hrconnect.network.domains.vacancy; // Пояснение: Размещаем модель вакансии в её собственном пакете предметной области.

import org.json.JSONObject; // Пояснение: Подключаем JSON-объект ответа Swagger.

/** Модель краткой вакансии и её подробной карточки. */ // Пояснение: Объясняем назначение модели данных.
public final class Vacancy { // Пояснение: Объявляем неизменяемую модель вакансии.
    public final int id; // Пояснение: Храним идентификатор вакансии для запроса подробностей.
    public final String title; // Пояснение: Храним название вакансии.
    public final String department; // Пояснение: Храним название команды или отдела.
    public final String workFormat; // Пояснение: Храним формат занятости.
    public final int applicantsCount; // Пояснение: Храним количество откликов.
    public final String salaryRange; // Пояснение: Храним зарплатный диапазон.
    public final String experience; // Пояснение: Храним требуемый опыт.
    public final String city; // Пояснение: Храним город.
    public final String description; // Пояснение: Храним подробное описание.

    public Vacancy(int id, String title, String department, String workFormat, int applicantsCount, String salaryRange, String experience, String city, String description) { // Пояснение: Создаём объект вакансии из готовых значений.
        this.id = id; // Пояснение: Сохраняем ID.
        this.title = title == null ? "Без названия" : title; // Пояснение: Сохраняем заголовок или подставляем понятный текст.
        this.department = safe(department, "Product Team"); // Пояснение: Сохраняем отдел с резервным значением.
        this.workFormat = safe(workFormat, "Full-time"); // Пояснение: Сохраняем формат занятости с резервным значением.
        this.applicantsCount = applicantsCount; // Пояснение: Сохраняем число откликов.
        this.salaryRange = safe(salaryRange, ""); // Пояснение: Сохраняем зарплату или пустую строку.
        this.experience = safe(experience, ""); // Пояснение: Сохраняем опыт или пустую строку.
        this.city = safe(city, ""); // Пояснение: Сохраняем город или пустую строку.
        this.description = safe(description, ""); // Пояснение: Сохраняем описание или пустую строку.
    } // Пояснение: Завершаем конструктор модели.

    public static Vacancy fromJson(JSONObject root) { // Пояснение: Преобразуем короткий или подробный JSON в модель.
        JSONObject data = root.optJSONObject("data"); // Пояснение: Проверяем наличие вложенного объекта Data в подробном ответе.
        if (data == null) data = root; // Пояснение: Для краткого ответа используем корневой JSON.
        int id = root.optInt("id", data.optInt("id", 0)); // Пояснение: Читаем ID из корня либо из объекта данных.
        String title = data.optString("title", root.optString("title", "Без названия")); // Пояснение: Читаем название из вложенного или короткого ответа.
        String department = data.optString("teamOfEmployment", data.optString("department", "Product Team")); // Пояснение: Читаем команду либо поле отдела краткого ответа.
        String format = data.optString("workFormat", "Full-time"); // Пояснение: Читаем формат работы.
        int applicants = data.optInt("applicantsCount", data.optInt("requiredQuantity", 0)); // Пояснение: Читаем отклики либо количество мест старого API.
        String salary = data.optString("salaryRange", ""); // Пояснение: Читаем зарплатный диапазон.
        String experience = data.optString("experience", ""); // Пояснение: Читаем требуемый опыт.
        String city = data.optString("city", root.optString("city", data.optString("idCity", ""))); // Пояснение: Читаем название города из краткого или подробного ответа.
        String description = data.optString("description", ""); // Пояснение: Читаем подробное описание вакансии.
        return new Vacancy(id, title, department, format, applicants, salary, experience, city, description); // Пояснение: Возвращаем нормализованную модель.
    } // Пояснение: Завершаем преобразование JSON.

    private static String safe(String value, String fallback) { // Пояснение: Подменяем отсутствующие текстовые значения.
        return value == null || "null".equals(value) ? fallback : value; // Пояснение: Возвращаем значение или заданный резервный текст.
    } // Пояснение: Завершаем проверку текстового значения.
} // Пояснение: Завершаем класс модели Vacancy.
