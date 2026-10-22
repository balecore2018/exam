package ru.hrconnect.uikit.components.vacancycard;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import ru.hrconnect.network.domains.vacancy.Vacancy;
import ru.hrconnect.uikit.R;

/** Карточка вакансии из экзаменационного макета. */
public final class VacancyCard extends LinearLayout {
    public VacancyCard(Context context, Vacancy vacancy) {
        super(context);
        initialize(context, vacancy);
    }

    public VacancyCard(Context context, AttributeSet attributes) {
        super(context, attributes);
        initialize(context, new Vacancy(0, "Senior Product Designer", "Product Team", "Full-time", 24, "$120k - $160k", "", "", ""));
    }

    private void initialize(Context context, Vacancy vacancy) {
        setOrientation(VERTICAL);
        setClickable(true);
        setFocusable(false);
        setBackgroundResource(R.drawable.vacancy_card_background);
        setElevation(context.getResources().getDisplayMetrics().density * 2);
        LayoutInflater.from(context).inflate(R.layout.vacancy_card, this, true);
        TextView badge = findViewById(R.id.vacancyBadge);
        TextView title = findViewById(R.id.vacancyTitle);
        TextView team = findViewById(R.id.vacancyTeam);
        TextView format = findViewById(R.id.vacancyFormat);
        TextView salary = findViewById(R.id.vacancySalary);
        TextView applicants = findViewById(R.id.vacancyApplicants);
        badge.setText(initials(vacancy.title));
        title.setText(vacancy.title);
        team.setText(vacancy.department);
        format.setText(vacancy.workFormat);
        salary.setText("▣ " + vacancy.salaryRange);
        salary.setVisibility(vacancy.salaryRange.isEmpty() ? GONE : VISIBLE);
        applicants.setText(String.valueOf(vacancy.applicantsCount));
    }

    private String initials(String title) {
        String[] words = title.trim().split("\\s+");
        if (words.length > 1) return (words[0].substring(0, 1) + words[1].substring(0, 1)).toUpperCase();
        return title.substring(0, Math.min(2, title.length())).toUpperCase();
    }
}
