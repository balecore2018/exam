# HR Connect — вариант 2

Проект Android Studio на Java содержит только функции варианта 2.

## Что реализовано

- Страница списка вакансий с XML-разметкой `app/src/main/res/layout/activity_main.xml`.
- Поиск вакансии по названию.
- Нижнее меню в отдельном `NavigationFragment`; его XML — `app/src/main/res/layout/fragment_navigation.xml`.
- UI-Kit: кнопка поиска `PrimaryButton` и карточка `VacancyCard`.
- Макет карточки UI-Kit находится в `ui-kit/src/main/res/layout/vacancy_card.xml`; приложение создаёт его через `new VacancyCard(this, vacancy)`.
- Первый запрос: `GET /vacancy/short?search=...`.
- Второй запрос: `GET /vacancy/{id}` вызывается в фоне для загрузки подробностей в SQLite; нажатие на карточку ничего не открывает.
- Сохранение списка и подробностей в структурированную SQLite-таблицу `vacancies`.
- Офлайн-показ вакансий и подробностей из SQLite.

Пояснения на русском добавлены к строкам Java-кода и XML-разметки.

## Запуск

1. Открой `C:\Users\balecore\Desktop\v2` в Android Studio как существующий Gradle-проект.
2. Запусти сервер из `C:\Users\balecore\Desktop\01.03\экзамен\Instrumentarii.Api` командой `dotnet run --urls http://0.0.0.0:8080`.
3. Запусти конфигурацию `app` на эмуляторе. Адрес API для эмулятора — `http://10.0.2.2:8080`.
