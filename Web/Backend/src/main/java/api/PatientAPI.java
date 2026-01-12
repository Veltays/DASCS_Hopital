package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.dao.ConsultationDAO;
import model.dao.PatientDAO;
import model.entity.Patient;
import utils.ApiConstants;

import java.io.IOException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.Date;

public class PatientAPI extends RestAPI implements HttpHandler {

    private static final PatientDAO patientDAO = new PatientDAO();

    @Override
    public void handle(HttpExchange exchange) throws IOException {




        if (exchange.getRequestMethod().equalsIgnoreCase("POST")) {

            String requestBody = readRequestBody(exchange);





            System.out.println(requestBody);
            String idPatient  = getJsonValue(requestBody, "patientId");
            String lastName   = getJsonValue(requestBody, "lastName");
            String firstName  = getJsonValue(requestBody, "firstName");
            String birthDate  = getJsonValue(requestBody, "birthDate");
            String newPatient = getJsonValue(requestBody, "newPatient");
            boolean isNewPatient = "true".equalsIgnoreCase(newPatient);

            Patient patient = new Patient();
            patient.setFirstname(firstName);
            patient.setLastname(lastName);

            try {
                Date birthDateObj = new SimpleDateFormat("dd/MM/yyyy").parse(birthDate);
                patient.setBirthDate(birthDateObj);
            } catch (Exception e) {
                sendResponse(exchange, ApiConstants.HTTP_BAD_REQUEST, "Invalid birthDate format");
                return;
            }

            if (!isNewPatient) {
                if (idPatient == null) {
                    sendResponse(exchange, ApiConstants.HTTP_BAD_REQUEST, "patientId required");
                    return;
                }
                patient.setId(Integer.parseInt(idPatient));
            }

            try {
                patientDAO.save(patient);
            }
            catch (SQLException e)
            {
                throw new RuntimeException(e);
            }

            // réponse attendue par l’énoncé
            sendResponse(exchange, ApiConstants.HTTP_OK, patient.getId().toString());
        }
        else
        {
            exchange.sendResponseHeaders(ApiConstants.HTTP_METHOD_NOT_ALLOWED, -1);
            return;
        }


    }



}
