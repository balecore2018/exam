package ru.hrconnect.network.datas.httpvacancyapi;

import org.json.JSONArray;
import org.json.JSONObject;
import ru.hrconnect.network.datas.vacancyapi.VacancyApi;
import ru.hrconnect.network.domains.vacancy.Vacancy;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URLEncoder;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/** Реализация запросов к серверу Swagger. */
public final class HttpVacancyApi implements VacancyApi {
    private final String baseUrl;

    public HttpVacancyApi(String baseUrl) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    @Override
    public List<Vacancy> getShort(String search) throws Exception {
        String route = "/vacancy/short";
        if (search != null && !search.trim().isEmpty()) route += "?search=" + URLEncoder.encode(search.trim(), StandardCharsets.UTF_8.name());
        JSONArray response = new JSONArray(get(route));
        List<Vacancy> result = new ArrayList<>();
        for (int index = 0; index < response.length(); index++) result.add(Vacancy.fromJson(response.getJSONObject(index)));
        return result;
    }

    @Override
    public Vacancy getDetails(int id) throws Exception {
        return Vacancy.fromJson(new JSONObject(get("/vacancy/" + id)));
    }

    private String get(String route) throws Exception {
        HttpURLConnection connection = (HttpURLConnection) new URL(baseUrl + route).openConnection();
        connection.setRequestMethod("GET");
        connection.setConnectTimeout(8000);
        connection.setReadTimeout(8000);
        connection.setRequestProperty("Accept", "application/json");
        int status = connection.getResponseCode();
        java.io.InputStream stream = status >= 200 && status < 300 ? connection.getInputStream() : connection.getErrorStream();
        BufferedReader reader = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8));
        StringBuilder body = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) body.append(line);
        reader.close();
        connection.disconnect();
        if (status < 200 || status >= 300) throw new IllegalStateException("HTTP " + status + ": " + body);
        return body.toString();
    }
}
