import java.io.IOException;
import java.util.Scanner;

import org.json.JSONObject;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
/*
This project is designed for OpenAI GPT-4 keys, so keys/endpoints for Azure/google api's may not work

400	BadRequestException
401	AuthenticationException
403	PermissionDeniedException
404	NotFoundException
422	UnprocessableEntityException
429	RateLimitException
5xx	InternalServerException
others	UnexpectedStatusCodeException

        https://processing.org/reference/JSONObject_getJSONArray_.html
        https://github.com/square/okhttp
        
*/

public class Chatbot {

    private static final String API_KEY = ""; 
    OkHttpClient client = new OkHttpClient();
    public static final MediaType JSON = MediaType.get("application/json");

    public static void main(String[] args) throws IOException {
        Chatbot chatbot = new Chatbot();
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type X to exit\n");
        while (true) {
            System.out.print("Enter: ");
            String input = scanner.nextLine();
            String Json = "{"+ "\"model\": \"gpt-4\","+ "\"messages\": [{\"role\": \"user\", \"content\": \"" + input + "\"}]}";
            String AI = chatbot.run(Json);
            System.out.println("\nAI chatbot: " + AI + "\n");
            if (input.equalsIgnoreCase("X")) {
            scanner.close();
            }
        }
    }

    public String run(String Json) throws IOException {
    RequestBody body = RequestBody.create(Json, JSON);
    Request request = new Request.Builder()
            .url("https://api.openai.com/v1/chat/completions")
            .post(body)
            .addHeader("Authorization", "Bearer " + API_KEY)
            .addHeader("Content-Type", "application/json")
            .build();

    try (Response response = client.newCall(request).execute()) {
            String jsonResponse = response.body().string();
            JSONObject json = new JSONObject(jsonResponse);
            JSONObject choices = json.getJSONArray("choices").getJSONObject(0).getJSONObject("message");
            String message = choices.getString("content");
        return message;
    }
}
}