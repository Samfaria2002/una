package a3.una;

import java.io.*;
import java.util.*;

public class MiniJavaCompiler {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java MiniJavaCompiler <caminho_para_o_arquivo>");
            return;
        }
        String filePath = args[0];
        try {
            // Análise léxica
            List<String> tokens = MiniJavaLexer.analyze(filePath);

            // Salvando tokens no arquivo de saída
            File outputDir = new File("output");
            outputDir.mkdir(); // Cria o diretório de saída se não existir
            BufferedWriter writer = new BufferedWriter(new FileWriter("output/output.tokens"));
            for (String token : tokens) {
                writer.write(token);
                writer.newLine();
            }
            writer.close();

            // Análise sintática
            MiniJavaParser parser = new MiniJavaParser(tokens);
            parser.parse();
            System.out.println("Análise sintática completa");

            // Análise semântica
            MiniJavaSemanticAnalyzer analyzer = new MiniJavaSemanticAnalyzer(tokens);
            analyzer.analyze();
            System.out.println("Análise semântica completa");

        } catch (IOException e) {
            System.err.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (RuntimeException e) {
            System.err.println(e.getMessage());
        }
    }
}
