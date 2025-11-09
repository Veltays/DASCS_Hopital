import controller.MainController;
import model.ConnectServer;
import view.Client;
import view.MaPage;

public class Main {
    public static void main(String[] args) {
        javax.swing.SwingUtilities.invokeLater(() -> {
            try {
                Client mainView = new Client();
                ConnectServer connectServer = new ConnectServer("127.0.0.1", 6769);
                new MainController(mainView, connectServer);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}