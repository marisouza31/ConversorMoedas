import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

import com.google.gson.Gson;

public class ConversorDeMoedas {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        scanner.useLocale(java.util.Locale.US);  // Garantir ponto como separador decimal

        while (true) {
            System.out.println("==== Conversor de Moedas ====");
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Dólar para Peso Argentino (USD para ARS)");
            System.out.println("2 - Peso Argentino para Dólar (ARS para USD)");
            System.out.println("3 - Dólar para Real Brasileiro (USD para BRL)");
            System.out.println("4 - Real Brasileiro para Dólar (BRL para USD)");
            System.out.println("5 - Dólar para Peso Colombiano (USD para COP)");
            System.out.println("6 - Peso Colombiano para Dólar (COP para USD)");
            System.out.println("7 - Sair");

            int opcao = scanner.nextInt();

            if (opcao == 7) {
                System.out.println("Programa encerrado.");
                break; // Sai do loop e encerra o programa
            }

            String de = "";
            String para = "";

            switch (opcao) {
                case 1 -> { de = "USD"; para = "ARS"; }
                case 2 -> { de = "ARS"; para = "USD"; }
                case 3 -> { de = "USD"; para = "BRL"; }
                case 4 -> { de = "BRL"; para = "USD"; }
                case 5 -> { de = "USD"; para = "COP"; }
                case 6 -> { de = "COP"; para = "USD"; }
                default -> {
                    System.out.println("Opção inválida.");
                    continue; // Volta para o início do loop
                }
            }

            System.out.print("Digite o valor a converter: ");
            double valor = scanner.nextDouble();

            try {
                String apiKey = "1d110ddaff27dc706404a837"; // Substitua pela sua chave da API
                String urlStr = String.format(java.util.Locale.US, "https://v6.exchangerate-api.com/v6/%s/pair/%s/%s/%.2f", apiKey, de, para, valor);

                URL url = new URL(urlStr);
                HttpURLConnection conexao = (HttpURLConnection) url.openConnection();
                conexao.setRequestMethod("GET");

                BufferedReader leitor = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                StringBuilder resposta = new StringBuilder();
                String linha;

                while ((linha = leitor.readLine()) != null) {
                    resposta.append(linha);
                }

                leitor.close();

                // Desserializando o JSON usando GSON
                Gson gson = new Gson();
                ConversaoResponse conversao = gson.fromJson(resposta.toString(), ConversaoResponse.class);

                if ("success".equals(conversao.result)) {
                    System.out.printf("Valor convertido: %.2f %s = %.2f %s\n", valor, de, conversao.conversion_result, para);
                } else {
                    System.out.println("Erro ao realizar a conversão.");
                }

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Erro ao converter moeda: " + e.getMessage());
            }
        }

        scanner.close();
    }
}
