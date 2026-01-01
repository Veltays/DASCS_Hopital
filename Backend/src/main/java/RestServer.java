import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

// APIs
import api.TodoApi;
import api.DoctorsAPI;
import api.PatientAPI;
import api.SpecialtiesAPI;
import api.ConsultationsAPI;
import utils.ConfigLoader;


public class RestServer {
    static void main() throws IOException {

        System.out.println("Demarrage du serveur REST...");


        ConfigLoader config = new ConfigLoader();


        int port = Integer.parseInt(config.get("PORT_API_REST"));


        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        server.createContext("/api/specialties", new SpecialtiesAPI());
        server.createContext("/api/doctors", new DoctorsAPI());
        server.createContext("/api/patients", new PatientAPI());
        server.createContext("/api/consultations", new ConsultationsAPI());

        // Démarrage du serveur
        server.start();

        System.out.println("Serveur REST demarre sur http://localhost:8080");


    }
}
