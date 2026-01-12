package api;

import com.sun.net.httpserver.HttpExchange;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.HashMap;
import java.util.Map;

public class RestAPI {


    protected static Map<String, String> parseQueryParams(String query) {
        Map<String, String> queryParams = new HashMap<>();
        if (query != null)
        {
            String[] params = query.split("&");
            for (String param : params)
            {
                String[] keyValue = param.split("=");
                if (keyValue.length == 2)
                {
                    queryParams.put(keyValue[0], keyValue[1]);
                }
            }
        }

        return queryParams;
    }


    protected static void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
        System.out.println("Envoi de la réponse (" + statusCode + ") : --" + response + "--");
        exchange.sendResponseHeaders(statusCode, response.length());
        OutputStream os = exchange.getResponseBody();
        os.write(response.getBytes());
        os.close();
    }


    protected static String readRequestBody(HttpExchange exchange) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(exchange.getRequestBody()));
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null)
        {
            requestBody.append(line);
        }
        reader.close();
        return requestBody.toString();
    }


    protected static String getJsonValue(String json, String key) {

        int start = json.indexOf("\"" + key + "\"");
        if (start == -1) return null;

        start = json.indexOf(":", start) + 1;

        // ⬇️ FIX IMPORTANT
        while (json.charAt(start) == ' ') start++;

        // string
        if (json.charAt(start) == '"') {
            start++;
            int end = json.indexOf("\"", start);
            return json.substring(start, end);
        }

        // bool / number / null
        int end = json.indexOf(",", start);
        if (end == -1) end = json.indexOf("}", start);

        return json.substring(start, end).trim();
    }
}
