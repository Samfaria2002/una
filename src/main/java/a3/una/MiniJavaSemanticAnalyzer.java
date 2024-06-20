package a3.una;

import java.util.*;

public class MiniJavaSemanticAnalyzer {
    private List<String> tokens;
    private Set<String> declaredVariables;

    public MiniJavaSemanticAnalyzer(List<String> tokens) {
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
        expect(index, "public");
        parseType(index + 1);
        expect(index + 2, "IDENTIFIER");
        expect(index + 3, "(");

        int currentIndex = index + 4;
        if (!tokens.get(currentIndex).equals(")")) {
            while (true) {
                parseType(currentIndex);
                expect(currentIndex + 1, "IDENTIFIER");
                currentIndex += 2;
                if (tokens.get(currentIndex).equals(")")) {
                    break;
                } else if (!tokens.get(currentIndex).equals(",")) {
                    throw new RuntimeException("Erro de sintaxe: esperado ',' ou ')' mas encontrado " + tokens.get(currentIndex));
                }
                currentIndex++;
            }
        }

        expect(currentIndex, ")");
        while (!tokens.get(currentIndex).equals("}")) {
            currentIndex++;
        }
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
