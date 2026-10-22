package ru.hrconnect.network.datas.vacancyapi; // Пояснение: Размещаем интерфейс API в его собственном пакете.

import ru.hrconnect.network.domains.vacancy.Vacancy; // Пояснение: Подключаем общую модель вакансии.
import java.util.List; // Пояснение: Подключаем список кратких вакансий.

/** Контракт двух GET-запросов экзаменационного варианта 2. */ // Пояснение: Описываем методы, которые реализует сетевой клиент.
public interface VacancyApi { // Пояснение: Объявляем интерфейс сетевого слоя.
    List<Vacancy> getShort(String search) throws Exception; // Пояснение: Запрашиваем список через GET /vacancy/short.
    Vacancy getDetails(int id) throws Exception; // Пояснение: Запрашиваем карточку через GET /vacancy/{id}.
} // Пояснение: Завершаем контракт API.
