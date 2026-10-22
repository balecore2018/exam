package ru.hrconnect.network.caches.vacancycache;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import ru.hrconnect.network.domains.vacancy.Vacancy;
import java.util.ArrayList;
import java.util.List;

/** Структурированная база вакансий для списка и офлайн-режима. */
public final class VacancyCache extends SQLiteOpenHelper {
    private static final String TABLE = "vacancies";

    public VacancyCache(Context context) { super(context, "hr_connect_vacancies.db", null, 1); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE vacancies (id INTEGER PRIMARY KEY, title TEXT NOT NULL, department TEXT, work_format TEXT, applicants_count INTEGER, salary_range TEXT, experience TEXT, city TEXT, description TEXT, updated_at INTEGER NOT NULL)");
        db.execSQL("CREATE INDEX index_vacancies_title ON vacancies(title COLLATE NOCASE)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS vacancies");
        onCreate(db);
    }

    public void saveAll(List<Vacancy> vacancies) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            for (Vacancy vacancy : vacancies) save(db, vacancy);
            db.setTransactionSuccessful();
        } finally {
            db.endTransaction();
        }
    }

    public void save(Vacancy vacancy) { save(getWritableDatabase(), vacancy); }

    private void save(SQLiteDatabase db, Vacancy vacancy) {
        ContentValues row = new ContentValues();
        row.put("id", vacancy.id);
        row.put("title", vacancy.title);
        row.put("department", vacancy.department);
        row.put("work_format", vacancy.workFormat);
        row.put("applicants_count", vacancy.applicantsCount);
        row.put("salary_range", vacancy.salaryRange);
        row.put("experience", vacancy.experience);
        row.put("city", vacancy.city);
        row.put("description", vacancy.description);
        row.put("updated_at", System.currentTimeMillis());
        db.insertWithOnConflict(TABLE, null, row, SQLiteDatabase.CONFLICT_REPLACE);
    }

    public List<Vacancy> find(String search) {
        List<Vacancy> result = new ArrayList<>();
        String pattern = "%" + (search == null ? "" : search.trim()) + "%";
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT id, title, department, work_format, applicants_count, salary_range, experience, city, description FROM vacancies WHERE title LIKE ? COLLATE NOCASE ORDER BY title", new String[]{pattern})) {
            while (cursor.moveToNext()) result.add(fromCursor(cursor));
        }
        return result;
    }

    public Vacancy findById(int id) {
        try (Cursor cursor = getReadableDatabase().rawQuery("SELECT id, title, department, work_format, applicants_count, salary_range, experience, city, description FROM vacancies WHERE id = ?", new String[]{String.valueOf(id)})) {
            return cursor.moveToFirst() ? fromCursor(cursor) : null;
        }
    }

    private Vacancy fromCursor(Cursor cursor) {
        return new Vacancy(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getInt(4), cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
    }
}
