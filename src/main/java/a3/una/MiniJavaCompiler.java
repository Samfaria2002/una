package a3.una;

import java.io.IOException;
import java.util.List;

public class MiniJavaCompiler {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Uso: java MiniJavaCompiler <arquivo>");
            return;
        }

        String filePath = args[0];

        try {
            // Etapa 1: Análise Léxica
            List<String> tokens = MiniJavaLexer.analyze(filePath);
            
            // Etapa 2: Análise Sintática
            MiniJavaParser parser = new MiniJavaParser(tokens);
            parser.parse();

            // Etapa 3: Análise Semântica
            MiniJavaSemanticAnalyzer semanticAnalyzer = new MiniJavaSemanticAnalyzer(tokens);
            semanticAnalyzer.analyze();

            // Compilação concluída sem erros
            System.out.println("Compilação concluída com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo: " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Erro durante a compilação: " + e.getMessage());
        }
    }
}
