package a3.una;

import java.util.List;

public class MiniJavaParser {
    private List<String> tokens;
    private int currentTokenIndex;

    public MiniJavaParser(List<String> tokens) {
        this.tokens = tokens;
        this.currentTokenIndex = 0;
    }

    public void parse() {
        while (currentTokenIndex < tokens.size()) {
            parseClass();
        }
    }

    private void parseClass() {
        expect("class");
        expect("IDENTIFIER");
        expect("{");
        // Implemente a análise dos membros da classe aqui
        expect("}");
    }

    private void expect(String expectedToken) {
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals(expectedToken)) {
            currentTokenIndex++;
        } else {
            throw new RuntimeException("Erro de sintaxe: esperado " + expectedToken + " mas encontrado " + tokens.get(currentTokenIndex));
        }
    }
}
