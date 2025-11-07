import model.ConnectServer;
import view.MaPage;
import controller.MainController;
import java.io.IOException;

public class Main {
    public static void main (String[] args)
    {
        MaPage view = new MaPage();
        ConnectServer connectServer = new ConnectServer("127.0.0.1",6768);

        try {
            new MainController(view, connectServer);
        } catch(IOException e)
        {
            throw new RuntimeException(e);
        }

        view.setVisible(true);

    }

}
