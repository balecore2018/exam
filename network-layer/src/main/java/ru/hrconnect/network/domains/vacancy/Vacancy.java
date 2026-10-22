package ru.hrconnect.network.domains.vacancy;

import org.json.JSONObject;

/** Модель краткой вакансии и её подробной карточки. */
public final class Vacancy {
    public final int id;
    public final String title;
    public final String department;
    public final String workFormat;
    public final int applicantsCount;
    public final String salaryRange;
    public final String experience;
    public final String city;
    public final String description;

    public Vacancy(int id, String title, String department, String workFormat, int applicantsCount, String salaryRange, String experience, String city, String description) {
        this.id = id;
        this.title = title == null ? "Без названия" : title;
        this.department = safe(department, "Product Team");
        this.workFormat = safe(workFormat, "Full-time");
        this.applicantsCount = applicantsCount;
        this.salaryRange = safe(salaryRange, "");
        this.experience = safe(experience, "");
        this.city = safe(city, "");
        this.description = safe(description, "");
    }

    public static Vacancy fromJson(JSONObject root) {
        JSONObject data = root.optJSONObject("data");
        if (data == null) data = root;
        int id = root.optInt("id", data.optInt("id", 0));
        String title = data.optString("title", root.optString("title", "Без названия"));
        String department = data.optString("teamOfEmployment", data.optString("department", "Product Team"));
        String format = data.optString("workFormat", "Full-time");
        int applicants = data.optInt("applicantsCount", data.optInt("requiredQuantity", 0));
        String salary = data.optString("salaryRange", "");
        String experience = data.optString("experience", "");
        String city = data.optString("city", root.optString("city", data.optString("idCity", "")));
        String description = data.optString("description", "");
        return new Vacancy(id, title, department, format, applicants, salary, experience, city, description);
    }

    private static String safe(String value, String fallback) {
        return value == null || "null".equals(value) ? fallback : value;
    }
}
