import com.google.gson.Gson;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Scanner;

public class Main {
    public static void main() throws IOException, InterruptedException {
        Gson gson = new Gson();
        Scanner scanner = new Scanner(System.in);


        System.out.println("Digite o usuario do github: ");
        String username = scanner.nextLine();
        String endereco = "https://api.github.com/users/" + username.replace(" ", "+");

        try {
            HttpClient client = HttpClient.newHttpClient();
            HttpRequest request = HttpRequest.newBuilder().uri(URI.create(endereco)).header("Accept", "application/vnd.github.v3+json").build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            if (response.statusCode() == 404) {
                throw new ErroConsultaGitHubException("Usuário não encontrado no GitHub.");
            }

            String json = response.body();

            ConsultaJson usuario =
                    gson.fromJson(json, ConsultaJson.class);

            System.out.println("\nUsuário encontrado:");
            System.out.println("Login: "
                    + usuario.login());

            System.out.println("Nome: "
                    + usuario.name());

            System.out.println("Seguidores: "
                    + usuario.followers());

            System.out.println("Repositórios: "
                    + usuario.public_repos());

        } catch (ErroConsultaGitHubException e) {
            System.out.println("Opss… Houve um erro durante a consulta à API do GitHub.");
            e.printStackTrace();

        } catch (IOException | InterruptedException e) {
            System.out.println(e.getMessage());
        }

    }

}
