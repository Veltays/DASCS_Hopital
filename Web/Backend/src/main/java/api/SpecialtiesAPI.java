package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.dao.SpecialtyDAO;
import model.entity.Specialty;
import model.viewmodel.SpecialtySearchVM;
import utils.ApiConstants;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;

public class SpecialtiesAPI extends RestAPI implements HttpHandler {

    private static final SpecialtyDAO specialtyDAO = new SpecialtyDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod().equalsIgnoreCase("GET"))
        {
            try {
                SpecialtySearchVM specialtySearchVM = new SpecialtySearchVM();

                // 1 - Récupération des données
                ArrayList<Specialty> specialties = specialtyDAO.load(specialtySearchVM);

                // 2 - Conversion en JSON
                String json = convertSpecialtyListToJson(specialties);

                // 3 - Réponse
                sendResponse(exchange, ApiConstants.HTTP_OK, json);

            } catch (SQLException e) {
                sendResponse(exchange, ApiConstants.HTTP_INTERNAL_ERROR, ApiConstants.MSG_SERVER_ERROR);
            }
        }
        else
        {
            exchange.sendResponseHeaders(ApiConstants.HTTP_METHOD_NOT_ALLOWED, -1);
            return;
        }
    }

    private static String convertSpecialtyListToJson(ArrayList<Specialty> specialties) {

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < specialties.size(); i++) {
            Specialty s = specialties.get(i);

            json.append("{")
                    .append("\"id\":").append(s.getId()).append(",")
                    .append("\"name\":\"").append(s.getName()).append("\"")
                    .append("}");

            if (i < specialties.size() - 1) json.append(",");
        }

        json.append("]");
        return json.toString();
    }
}
