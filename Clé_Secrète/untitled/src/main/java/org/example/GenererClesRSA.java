package org.example;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.*;
import java.security.*;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import java.io.*;
import java.security.*;

public class GenererClesRSA {
    public static void main(String[] args) throws Exception {
        Security.addProvider(new BouncyCastleProvider());

        // IMPORTANT : forcer BC pour la génération
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA", "BC");
        keyGen.initialize(2048, new SecureRandom());
        KeyPair keyPair = keyGen.generateKeyPair();

        PublicKey clePublique = keyPair.getPublic();
        PrivateKey clePrivee = keyPair.getPrivate();

        System.out.println("Clé publique : " + clePublique.getClass().getName());
        System.out.println("Clé privée : " + clePrivee.getClass().getName());

        // Sérialisation de la clé publique
        ObjectOutputStream oosPublic = new ObjectOutputStream(
                new FileOutputStream("clePubliqueServeur.ser"));
        oosPublic.writeObject(clePublique);
        oosPublic.close();
        System.out.println("Clé publique sauvegardée dans clePubliqueServeur.ser");

        // Sérialisation de la clé privée
        ObjectOutputStream oosPrivate = new ObjectOutputStream(
                new FileOutputStream("clePriveeServeur.ser"));
        oosPrivate.writeObject(clePrivee);
        oosPrivate.close();
        System.out.println("Clé privée sauvegardée dans clePriveeServeur.ser");
    }
}
