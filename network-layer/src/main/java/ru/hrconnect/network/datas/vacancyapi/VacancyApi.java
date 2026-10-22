package ru.hrconnect.network.datas.vacancyapi;

import ru.hrconnect.network.domains.vacancy.Vacancy;
import java.util.List;

/** Контракт двух GET-запросов экзаменационного варианта 2. */
public interface VacancyApi {
    List<Vacancy> getShort(String search) throws Exception;
    Vacancy getDetails(int id) throws Exception;
}
