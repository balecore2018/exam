package ru.hrconnect.app.presentations.list;

import android.os.Bundle;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.FragmentActivity;
import ru.hrconnect.app.R;
import ru.hrconnect.app.BuildConfig;
import ru.hrconnect.app.presentations.navigation.NavigationFragment;
import ru.hrconnect.network.caches.vacancycache.VacancyCache;
import ru.hrconnect.network.datas.httpvacancyapi.HttpVacancyApi;
import ru.hrconnect.network.datas.vacancyrepository.VacancyRepository;
import ru.hrconnect.network.domains.vacancy.Vacancy;
import ru.hrconnect.uikit.components.primarybutton.PrimaryButton;
import ru.hrconnect.uikit.components.vacancycard.VacancyCard;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/** Единственная страница приложения: поиск и список вакансий. */
public final class MainActivity extends FragmentActivity {
    private final ExecutorService executor = Executors.newSingleThreadExecutor();
    private VacancyRepository repository;
    private EditText searchInput;
    private LinearLayout results;
    private TextView status;

    @Override
    protected void onCreate(Bundle state) {
        super.onCreate(state);
        repository = new VacancyRepository(new HttpVacancyApi(BuildConfig.API_BASE_URL), new VacancyCache(this));
        setContentView(R.layout.activity_main);
        searchInput = findViewById(R.id.searchInput);
        results = findViewById(R.id.vacancyList);
        status = findViewById(R.id.resultStatus);
        PrimaryButton searchButton = findViewById(R.id.searchButton);
        searchButton.setOnClickListener(view -> loadVacancies(searchInput.getText().toString()));
        getSupportFragmentManager().beginTransaction().replace(R.id.navigationContainer, new NavigationFragment()).commit();
        loadVacancies("");
    }

    private void loadVacancies(String query) {
        status.setText("Loading vacancies...");
        executor.execute(() -> {
            try {
                List<Vacancy> vacancies = repository.loadShort(query);
                boolean offline = repository.wasLastLoadFromCache();
                runOnUiThread(() -> showVacancies(vacancies, offline));
            } catch (Exception error) {
                runOnUiThread(() -> status.setText("Could not load vacancies: " + error.getMessage()));
            }
        });
    }

    private void showVacancies(List<Vacancy> vacancies, boolean offline) {
        results.removeAllViews();
        status.setText(vacancies.size() + " vacancies found" + (offline ? " · offline" : ""));
        for (Vacancy vacancy : vacancies) {
            VacancyCard card = new VacancyCard(this, vacancy);
            card.setOnClickListener(view -> loadVacancyDetails(vacancy.id));
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(-1, -2);
            params.bottomMargin = dp(14);
            results.addView(card, params);
        }
    }

    private void loadVacancyDetails(int vacancyId) {
        status.setText("Loading vacancy details...");
        executor.execute(() -> {
            try {
                Vacancy vacancy = repository.loadDetails(vacancyId);
                boolean offline = repository.wasLastLoadFromCache();
                runOnUiThread(() -> status.setText("Loaded: " + vacancy.title + (offline ? " · offline" : "")));
            } catch (Exception error) {
                runOnUiThread(() -> status.setText("Could not load details: " + error.getMessage()));
            }
        });
    }

    private int dp(int value) {
        return Math.round(value * getResources().getDisplayMetrics().density);
    }

    @Override
    protected void onDestroy() {
        executor.shutdownNow();
        super.onDestroy();
    }
}
