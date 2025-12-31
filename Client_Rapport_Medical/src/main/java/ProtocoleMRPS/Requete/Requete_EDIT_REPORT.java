package ProtocoleMRPS.Requete;

import ProtocoleMRPS.MyCrypto;
import protocol.RequeteMRPS;

import javax.crypto.SecretKey;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.time.LocalDate;

public class Requete_EDIT_REPORT extends RequeteMRPS
{
    private static final long serialVersionUID = 1L;
    private byte[] data;

    public byte[] getData() {
        return data;
    }

    public void setData(byte[] data) {
        this.data = data;
    }

    public Requete_EDIT_REPORT(Integer reportId, String newDescription, SecretKey sessionKey) throws Exception
    {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        dos.writeInt(reportId);         // l'id du rapport à modifier
        dos.writeUTF(newDescription);
        dos.flush();

        byte[] messageClair = baos.toByteArray();
        this.data = MyCrypto.CryptSymAES(sessionKey, messageClair);
    }
}
