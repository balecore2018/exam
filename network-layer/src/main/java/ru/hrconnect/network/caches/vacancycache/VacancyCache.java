package ru.hrconnect.network.caches.vacancycache; // Пояснение: Размещаем SQLite-хранилище в его собственном пакете кэша.

import android.content.ContentValues; // Пояснение: Подключаем запись значений в SQLite.
import android.content.Context; // Пояснение: Подключаем контекст для открытия базы.
import android.database.Cursor; // Пояснение: Подключаем чтение строк из базы.
import android.database.sqlite.SQLiteDatabase; // Пояснение: Подключаем операции SQLite.
import android.database.sqlite.SQLiteOpenHelper; // Пояснение: Подключаем создание и обновление схемы базы.
import ru.hrconnect.network.domains.vacancy.Vacancy; // Пояснение: Подключаем модель, которую сохраняет кэш.
import java.util.ArrayList; // Пояснение: Подключаем список результатов локального поиска.
import java.util.List; // Пояснение: Подключаем интерфейс списка моделей.

/** Структурированная база вакансий для списка и офлайн-режима. */ // Пояснение: Объясняем назначение локальной базы.
public final class VacancyCache extends SQLiteOpenHelper { // Пояснение: Объявляем помощник базы данных вакансий.
    private static final String TABLE = "vacancies"; // Пояснение: Задаём название таблицы вакансий.

    public VacancyCache(Context context) { super(context, "hr_connect_vacancies.db", null, 1); } // Пояснение: Открываем отдельную базу с версией схемы 1.

    @Override // Пояснение: Создаём таблицу при первом запуске.
    public void onCreate(SQLiteDatabase db) { // Пояснение: Определяем структурированные поля записи.
        db.execSQL("CREATE TABLE vacancies (id INTEGER PRIMARY KEY, title TEXT NOT NULL, department TEXT, work_format TEXT, applicants_count INTEGER, salary_range TEXT, experience TEXT, city TEXT, description TEXT, updated_at INTEGER NOT NULL)"); // Пояснение: Создаём столбцы вакансии вместо хранения всего ответа одним JSON-текстом.
        db.execSQL("CREATE INDEX index_vacancies_title ON vacancies(title COLLATE NOCASE)"); // Пояснение: Индексируем название для быстрого поиска.
    } // Пояснение: Завершаем создание схемы.

    @Override // Пояснение: Обрабатываем повышение версии схемы базы.
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) { // Пояснение: Обновляем структуру таблицы при изменении версии.
        db.execSQL("DROP TABLE IF EXISTS vacancies"); // Пояснение: Удаляем несовместимую старую таблицу.
        onCreate(db); // Пояснение: Создаём актуальную структуру таблицы.
    } // Пояснение: Завершаем обновление схемы.

    public void saveAll(List<Vacancy> vacancies) { // Пояснение: Сохраняем полученный список в структурированную базу.
        SQLiteDatabase db = getWritableDatabase(); // Пояснение: Открываем базу на запись.
        db.beginTransaction(); // Пояснение: Начинаем транзакцию, чтобы сохранить список целиком.
        try { // Пояснение: Гарантируем завершение или откат транзакции.
            for (Vacancy vacancy : vacancies) save(db, vacancy); // Пояснение: Сохраняем каждую вакансию отдельной строкой.
            db.setTransactionSuccessful(); // Пояснение: Помечаем транзакцию успешной после всех записей.
        } finally { // Пояснение: Завершаем транзакцию при успехе или ошибке.
            db.endTransaction(); // Пояснение: Фиксируем успешную транзакцию или откатываем изменения.
        } // Пояснение: Завершаем безопасную запись списка.
    } // Пояснение: Завершаем сохранение списка.

    public void save(Vacancy vacancy) { save(getWritableDatabase(), vacancy); } // Пояснение: Сохраняем одну подробную карточку.

    private void save(SQLiteDatabase db, Vacancy vacancy) { // Пояснение: Преобразуем модель в значения столбцов.
        ContentValues row = new ContentValues(); // Пояснение: Создаём набор значений строки SQLite.
        row.put("id", vacancy.id); // Пояснение: Сохраняем ID вакансии.
        row.put("title", vacancy.title); // Пояснение: Сохраняем название.
        row.put("department", vacancy.department); // Пояснение: Сохраняем отдел.
        row.put("work_format", vacancy.workFormat); // Пояснение: Сохраняем формат работы.
        row.put("applicants_count", vacancy.applicantsCount); // Пояснение: Сохраняем количество откликов.
        row.put("salary_range", vacancy.salaryRange); // Пояснение: Сохраняем диапазон зарплаты.
        row.put("experience", vacancy.experience); // Пояснение: Сохраняем требуемый опыт.
        row.put("city", vacancy.city); // Пояснение: Сохраняем город.
        row.put("description", vacancy.description); // Пояснение: Сохраняем описание.
        row.put("updated_at", System.currentTimeMillis()); // Пояснение: Сохраняем время последнего обновления данных.
        db.insertWithOnConflict(TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE); // Пояснение: Добавляем вакансию или обновляем существующую по ID.
    } // Пояснение: Завершаем преобразование и запись строки.

    public List<Vacancy> find(String search) { // Пояснение: Ищем сохранённые вакансии при отсутствии интернета.
        List<Vacancy> result = new ArrayList<>(); // Пояснение: Создаём список результатов офлайн-поиска.
        String pattern = "%" + (search == null ? "" : search.trim()) + "%"; // Пояснение: Формируем шаблон поиска подстроки.
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT id, title, department, work_format, applicants_count, salary_range, experience, city, description FROM vacancies WHERE title LIKE ? COLLATE NOCASE ORDER BY title", new String[]{pattern})) { // Пояснение: Читаем совпадения из отдельных столбцов SQLite.
            while (cursor.moveToNext()) result.add(fromCursor(cursor)); // Пояснение: Преобразуем каждую строку базы в модель.
        } // Пояснение: Автоматически закрываем курсор после чтения.
        return result; // Пояснение: Возвращаем найденные локальные вакансии.
    } // Пояснение: Завершаем поиск в кэше.

    public Vacancy findById(int id) { // Пояснение: Ищем подробности вакансии по её ID в локальной базе.
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT id, title, department, work_format, applicants_count, salary_range, experience, city, description FROM vacancies WHERE id = ?", new String[]{String.valueOf(id)})) { // Пояснение: Выполняем параметризованный поиск строки.
            return cursor.moveToFirst() ? fromCursor(cursor) : null; // Пояснение: Возвращаем модель или null, если её нет в кэше.
        } // Пояснение: Закрываем курсор после чтения.
    } // Пояснение: Завершаем получение из базы.

    private Vacancy fromCursor(Cursor cursor) { // Пояснение: Восстанавливаем модель из структурированной строки.
        return new Vacancy(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8)); // Пояснение: Передаём все сохранённые поля конструктору Vacancy.
    } // Пояснение: Завершаем восстановление модели.
} // Пояснение: Завершаем класс SQLite-кэша.
