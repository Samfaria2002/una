package a3.una;

import java.util.*;

public class MiniJavaSemanticAnalyzer {
    private List<String> tokens;
    private Set<String> declaredVariables;
    public int currentTokenIndex;

    public MiniJavaSemanticAnalyzer(List<String> tokens) {
        this.currentTokenIndex = 0;
        this.tokens = tokens;
        this.declaredVariables = new HashSet<>();
    }

    public void analyze() {
        for (int i = 0; i < tokens.size(); i++) {
            if (tokens.get(i).equals("class")) {
                analyzeClass(i);
            }
        }
    }

    private void analyzeClass(int startIndex) {
        int index = startIndex;
        expect("class");
        expect("IDENTIFIER");
        expect("{");
        while (index < tokens.size() && (currentTokenIs("int") || currentTokenIs("boolean") || currentTokenIs("IDENTIFIER"))) {
            parseVarDeclaration(index);
            index += 3; // Pulando a declaração de variável
        }
        while (index < tokens.size() && currentTokenIs("public")) {
            analyzeMethodDeclaration(index);
            index++; // Pulando a declaração do método
        }
        expect("}");
    }

    private void analyzeMethodDeclaration(int index) {
        // Inicialmente, o índice aponta para a palavra-chave 'public'
        expect(index, "public");
        parseType(index + 1); // O tipo de retorno do método
        expect(index + 2, "IDENTIFIER"); // Nome do método
        expect(index + 3, "(");

        // Analisar a lista de parâmetros
        int currentIndex = index + 4; // Avançar para o primeiro parâmetro (ou fechar parênteses, se não houver parâmetros)
        if (!tokens.get(currentIndex).equals(")")) {
            while (true) {
                parseType(currentIndex); // Tipo do parâmetro
                expect(currentIndex + 1, "IDENTIFIER"); // Nome do parâmetro
                currentIndex += 2; // Avançar para a próxima possível vírgula ou parêntese de fechamento
                if (tokens.get(currentIndex).equals(")")) {
                    break; // Parêntese de fechamento, sair do loop
                } else if (!tokens.get(currentIndex).equals(",")) {
                    throw new RuntimeException("Erro de sintaxe: esperado ',' ou ')' mas encontrado " + tokens.get(currentIndex));
                }
                currentIndex++; // Avançar para o próximo parâmetro após a vírgula
            }
        }

        // Parêntese de fechamento dos parâmetros
        expect(currentIndex, ")");

        // Corpo do método (não implementado ainda)
        // Aqui você pode adicionar a lógica para analisar o corpo do método, incluindo declarações de variáveis locais e instruções

        // Por enquanto, apenas avançamos até o final do método
        while (!tokens.get(currentIndex).equals("}")) {
            currentIndex++;
        }

        // Parêntese de fechamento do método
        expect(currentIndex, "}");
    }

    private void parseVarDeclaration(int startIndex) {
        int index = startIndex;
        parseType(index);
        expect(index + 1, "IDENTIFIER");
        declaredVariables.add(tokens.get(index + 1));
        expect(index + 2, ";");
    }

    private void parseType(int index) {
        if (index < tokens.size()) {
            String token = tokens.get(index);
            if (token.equals("int") || token.equals("boolean") || token.equals("IDENTIFIER")) {
                // Token de tipo válido
            } else {
                throw new RuntimeException("Erro de sintaxe: esperado tipo mas encontrado " + token);
            }
        } else {
            throw new RuntimeException("Fim inesperado do arquivo");
        }
    }

    private boolean currentTokenIs(String expectedToken) {
        if (expectedToken.equals("IDENTIFIER")) {
            return declaredVariables.contains(tokens.get(currentTokenIndex));
        } else {
            return currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals(expectedToken);
        }
    }

    private void expect(String expectedToken) {
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals(expectedToken)) {
            currentTokenIndex++;
        } else {
            throw new RuntimeException("Erro de sintaxe: esperado " + expectedToken + " mas encontrado " + tokens.get(currentTokenIndex));
        }
    }

    private void expect(int index, String expectedToken) {
        if (index < tokens.size() && tokens.get(index).equals(expectedToken)) {
            // Token esperado encontrado, não faz nada
        } else {
            throw new RuntimeException("Erro de sintaxe: esperado " + expectedToken + " mas encontrado " + tokens.get(index));
        }
    }
}
