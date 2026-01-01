package utils;

public final class ApiConstants {


    // HTTP Status
    public static final int HTTP_OK = 200;
    public static final int HTTP_CREATED = 201;
    public static final int HTTP_BAD_REQUEST = 400;
    public static final int HTTP_NOT_FOUND = 404;
    public static final int HTTP_METHOD_NOT_ALLOWED = 405;
    public static final int HTTP_INTERNAL_ERROR = 500;

    // Messages
    public static final String MSG_SERVER_ERROR = "Erreur serveur";
    public static final String MSG_METHOD_NOT_ALLOWED = "Methode non autorisee";

    // Empêche l'instanciation
    private ApiConstants() {}
}