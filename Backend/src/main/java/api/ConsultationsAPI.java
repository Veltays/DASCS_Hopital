package api;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.dao.ConsultationDAO;
import model.dao.DoctorDAO;
import model.dao.SpecialtyDAO;
import model.entity.Consultation;
import model.entity.Doctor;
import model.entity.Specialty;
import model.viewmodel.ConsultationSearchVM;
import model.viewmodel.DoctorSearchVM;
import model.viewmodel.SpecialtySearchVM;
import utils.ApiConstants;

import java.io.IOException;
import java.sql.Array;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;



public class ConsultationsAPI extends RestAPI implements HttpHandler {

    private final ConsultationDAO consultationDAO = new ConsultationDAO();
    private static final DoctorDAO doctorDAO = new DoctorDAO();
    private static final SpecialtyDAO specialtyDAO = new SpecialtyDAO();


    @Override
    public void handle(HttpExchange exchange) throws IOException {


        if (exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());

            ConsultationSearchVM consultationVM = new ConsultationSearchVM();
            DoctorSearchVM doctorVM = new DoctorSearchVM();
            SpecialtySearchVM specialtyVM = new SpecialtySearchVM();

            // ---- filtres consultation directs ----
            if (queryParams.containsKey("date")) {
                consultationVM.setDate(LocalDate.parse(queryParams.get("date")));
            }
            // ---- filtres patients directs ----
            if (queryParams.containsKey("patientId")) {
                consultationVM.setPatient_id(Integer.parseInt(queryParams.get("patientId")));
            }

            if (queryParams.containsKey("doctor_id")) {
                try {
                    consultationVM.setDoctor_id(Integer.parseInt(queryParams.get("doctor_id")));
                } catch (NumberFormatException e) {
                    // ignore
                }
            }
                // ---- filtre doctor ----
            if (queryParams.containsKey("doctor")) {
                doctorVM.setLastname(queryParams.get("doctor"));
            }

            // ---- filtre specialty ----
            if (queryParams.containsKey("specialty_id")) {
                try {
                    Integer specialtyId = Integer.parseInt(queryParams.get("specialty_id"));
                    doctorVM.setSpecialty_id(specialtyId);  // filtrage par spécialité
                } catch (NumberFormatException e) {
                    // ignore
                }
            }

// ---- si doctor ou specialty → récupérer consultations correspondantes ----
            if (queryParams.containsKey("doctor") || queryParams.containsKey("specialty_id")) {
                ArrayList<Doctor> doctors = null;
                try {
                    doctors = doctorDAO.load(doctorVM);
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }

                if (doctors.isEmpty()) {
                    sendResponse(exchange, ApiConstants.HTTP_OK, "[]");
                    return;
                }

                ArrayList<Consultation> allConsultations = new ArrayList<>();
                for (Doctor d : doctors) {
                    consultationVM.setDoctor_id(d.getId());
                    try {
                        allConsultations.addAll(consultationDAO.load(consultationVM));
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                }

                sendResponse(exchange, ApiConstants.HTTP_OK, convertConsultationListToJson(allConsultations));
                return;
            }



            // ---- récupération finale ----
            ArrayList<Consultation> consultations = null;
            try {
                consultations = consultationDAO.load(consultationVM);
                //System.out.println(consultations);
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }



            if (queryParams.containsKey("patientId")) {
                sendResponse(exchange, ApiConstants.HTTP_OK, convertConsultationListToJson(consultations));
            }

            else{
                ArrayList<Consultation> availableConsultations = new ArrayList<>();

                for (Consultation c : consultations) {
                    if (c.getPatient_id() == 0) {
                        availableConsultations.add(c);
                    }
                }
                sendResponse(exchange, ApiConstants.HTTP_OK, convertConsultationListToJson(availableConsultations));
            }
        }

        else if (exchange.getRequestMethod().equalsIgnoreCase("PUT")) {

            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());

            // id obligatoire
            if (!queryParams.containsKey("id")) {
                sendResponse(exchange, ApiConstants.HTTP_BAD_REQUEST, "Id was not given");
                return;
            }

            Integer consultationId = Integer.parseInt(queryParams.get("id"));

            // lecture du body
            String body = readRequestBody(exchange);

            String patientIdStr = getJsonValue(body, "patientId");
            String reason = getJsonValue(body, "reason");

            if (patientIdStr == null || reason == null) {
                sendResponse(exchange, ApiConstants.HTTP_BAD_REQUEST, "Patient Id or Reason was not given");
                return;
            }

            try {
                consultationDAO.load(new ConsultationSearchVM());
                Consultation cons = consultationDAO.getById(consultationId);

                // consultation inexistante
                if (cons == null) {
                    sendResponse(exchange, ApiConstants.HTTP_OK, "Consultation dont exist ");
                    return;
                }

                // attribution patient + raison
                cons.setPatient_id(Integer.parseInt(patientIdStr));
                cons.setReason(reason);

                consultationDAO.save(cons);

                sendResponse(exchange,
                        ApiConstants.HTTP_OK,
                        "yes");

            } catch (SQLException | NumberFormatException e) {
                sendResponse(exchange,
                        ApiConstants.HTTP_INTERNAL_ERROR,
                        "no");
            }
        }
        else if (exchange.getRequestMethod().equalsIgnoreCase("DELETE")) {

            Map<String, String> queryParams = parseQueryParams(exchange.getRequestURI().getQuery());

            // id obligatoire
            if (!queryParams.containsKey("id")) {
                sendResponse(exchange, ApiConstants.HTTP_BAD_REQUEST, "no");
                return;
            }

            int consultationId = Integer.parseInt(queryParams.get("id"));

            try {
                consultationDAO.load(new ConsultationSearchVM());
                Consultation cons = consultationDAO.getById(consultationId);

                // consultation inexistante
                if (cons == null) {
                    sendResponse(exchange, ApiConstants.HTTP_OK, "no");
                    return;
                }

                // libération de la consultation
                cons.setPatient_id(null);
                cons.setReason(null);

                consultationDAO.save(cons);

                // succès
                sendResponse(exchange,
                        ApiConstants.HTTP_OK,
                        "yes");

            } catch (SQLException e) {
                sendResponse(exchange, ApiConstants.HTTP_INTERNAL_ERROR, "no");
            }



        }
        else {
            exchange.sendResponseHeaders(ApiConstants.HTTP_METHOD_NOT_ALLOWED, -1);
            return;
        }


    }

    private static String convertConsultationListToJson(ArrayList<Consultation> consultations) {

        StringBuilder json = new StringBuilder("[");

        for (int i = 0; i < consultations.size(); i++) {
            Consultation c = consultations.get(i);

            //System.out.println(c);
            json.append("{")
                    .append("\"id\":").append(c.getId()).append(",")
                    .append("\"doctor_id\":").append(c.getDoctor_id()).append(",")
                    .append("\"patient_id\":").append(c.getPatient_id()).append(",")
                    .append("\"date\":\"").append(c.getDate()).append("\",")
                    .append("\"hour\":\"").append(c.getHour()).append("\",")
                    .append("\"duration\":\"").append(c.getDuration()).append("\",")
                    .append("\"reason\":\"").append(c.getReason()).append("\"")
                    .append("}");

            if (i < consultations.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");
        return json.toString();
    }
}

