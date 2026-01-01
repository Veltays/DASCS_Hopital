package



import com.sun.net.httpserver.HttpServer;
import java.net.InetSocketAddress;
import java.io.IOException;

// APIs
import api.TodoApi;
import api.DoctorsAPI;
import api.PatientAPI;
import api.SpecialtiesAPI;
import api.ConsultationsAPI;


public class RestServer {
    static void main() throws IOException {

        System.out.println("Demarrage du serveur REST...");


        ConfigLoader config = new ServeurGeneriqueTCP.utils.ConfigLoader();


        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);

        server.createContext("/api/doctors", new DoctorsAPI());
        server.createContext("/api/patients", new PatientAPI());
        server.createContext("/api/specialties", new SpecialtiesAPI());
        server.createContext("/api/consultations", new ConsultationsAPI());

        // Démarrage du serveur
        server.start();

        System.out.println("Serveur REST demarre sur http://localhost:8080");


    }
}
