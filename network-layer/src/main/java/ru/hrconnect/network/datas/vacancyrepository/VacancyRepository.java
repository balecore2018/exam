package ru.hrconnect.network.datas.vacancyrepository;

import ru.hrconnect.network.caches.vacancycache.VacancyCache;
import ru.hrconnect.network.datas.vacancyapi.VacancyApi;
import ru.hrconnect.network.domains.vacancy.Vacancy;
import java.util.List;

/** Связывает Swagger API и локальный SQLite-кэш. */
public final class VacancyRepository {
    private final VacancyApi api;
    private final VacancyCache cache;
    private volatile boolean lastLoadFromCache;

    public VacancyRepository(VacancyApi api, VacancyCache cache) {
        this.api = api;
        this.cache = cache;
    }

    public List<Vacancy> loadShort(String search) throws Exception {
        lastLoadFromCache = false;
        try {
            List<Vacancy> result = api.getShort(search);
            cache.saveAll(result);
            return result;
        } catch (Exception networkError) {
            List<Vacancy> saved = cache.find(search);
            if (!saved.isEmpty()) { lastLoadFromCache = true; return saved; }
            throw networkError;
        }
    }

    public Vacancy loadDetails(int id) throws Exception {
        lastLoadFromCache = false;
        try {
            Vacancy result = api.getDetails(id);
            cache.save(result);
            return result;
        } catch (Exception networkError) {
            Vacancy saved = cache.findById(id);
            if (saved != null) { lastLoadFromCache = true; return saved; }
            throw networkError;
        }
    }

    public boolean wasLastLoadFromCache() { return lastLoadFromCache; }
}
