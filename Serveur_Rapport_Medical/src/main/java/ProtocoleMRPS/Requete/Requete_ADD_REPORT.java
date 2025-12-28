package ProtocoleMRPS.Requete;

import ProtocoleMRPS.MyCrypto;
import protocol.*;
import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;
public class Requete_ADD_REPORT implements Requete
{
    private static final long serialVersionUID = 1L;
    private byte[] data;
    // data cryptés symétriquement + signature du médecin

    public Requete_ADD_REPORT(Integer id, Integer idPatient, LocalDate date, String description, SecretKey sessionKey) throws Exception {

        // on reçoit les données en clair puis on les crypte

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(id);
        dos.writeInt(idPatient);
        dos.writeUTF(date.toString());      // ISO 8601
        dos.writeUTF(description);
        dos.flush();

        byte[] messageClair = baos.toByteArray();

        this.data = MyCrypto.CryptSymAES(sessionKey, messageClair);
    }

    public byte[] getData() {
        return data;
    }
}
