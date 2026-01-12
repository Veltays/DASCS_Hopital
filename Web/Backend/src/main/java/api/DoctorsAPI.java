package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.dao.DoctorDAO;
import model.entity.Doctor;
import model.viewmodel.DoctorSearchVM;
import utils.ApiConstants;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;

public class DoctorsAPI extends RestAPI implements HttpHandler {

    private static final DoctorDAO doctorDAO = new DoctorDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {

        if (exchange.getRequestMethod().equalsIgnoreCase("GET"))
        {


            // 1 - Récupération des paramètres
            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());


            // 2 - Initialisation des critéres de recherches
            DoctorSearchVM doctorSearchVM = new DoctorSearchVM();

            if(queryParams.containsKey("name"))
            {
                doctorSearchVM.setLastname(queryParams.get("name"));
            }

            if (queryParams.containsKey("specialty"))
            {
                doctorSearchVM.setSpecialty_id(Integer.parseInt(queryParams.get("specialty")));
            }



            // 3 - Récupération des données
            try {
                ArrayList<Doctor> doctors = doctorDAO.load(doctorSearchVM);
                String json = convertDoctorListToJson(doctors);
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

    private static String convertDoctorListToJson(ArrayList<Doctor> doctors) {

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < doctors.size(); i++) {
            Doctor d = doctors.get(i);

            json.append("{")
                    .append("\"id\":").append(d.getId()).append(",")
                    .append("\"lastname\":\"").append(d.getLastname()).append("\",")
                    .append("\"firstname\":\"").append(d.getFirstname()).append("\",")
                    .append("\"specialty_id\":").append(d.getSpecialty_id())
                    .append("}");

            if (i < doctors.size() - 1) json.append(",");
        }

        json.append("]");
        return json.toString();
    }
}