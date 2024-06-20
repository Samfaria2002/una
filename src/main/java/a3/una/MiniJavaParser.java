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
        while (currentTokenIndex < tokens.size() && (currentTokenIs("int") || currentTokenIs("boolean") || currentTokenIs("IDENTIFIER"))) {
            parseVarDeclaration();
        }
        while (currentTokenIndex < tokens.size() && currentTokenIs("public")) {
            parseMethodDeclaration();
        }
        expect("}");
    }

    private void parseVarDeclaration() {
        parseType();
        expect("IDENTIFIER");
        expect(";");
    }

    private void parseMethodDeclaration() {
        expect("public");
        parseType();
        expect("IDENTIFIER");
        expect("(");
        parseParameterList();
        expect(")");
        expect("{");
        while (currentTokenIndex < tokens.size() && (currentTokenIs("int") || currentTokenIs("boolean") || currentTokenIs("IDENTIFIER"))) {
            parseVarDeclaration();
        }
        while (!currentTokenIs("}")) {
            parseStatement();
        }
        expect("}");
    }

    private void parseType() {
        if (currentTokenIs("int")) {
            expect("int");
            if (currentTokenIs("[")) {
                expect("[");
                expect("]");
            }
        } else if (currentTokenIs("boolean")) {
            expect("boolean");
        } else {
            expect("IDENTIFIER");
        }
    }

    private void parseParameterList() {
        if (currentTokenIs("int") || currentTokenIs("boolean") || currentTokenIs("IDENTIFIER")) {
            parseType();
            expect("IDENTIFIER");
            while (currentTokenIs(",")) {
                expect(",");
                parseType();
                expect("IDENTIFIER");
            }
        }
    }

    private void parseStatement() {
        if (currentTokenIs("{")) {
            expect("{");
            while (!currentTokenIs("}")) {
                parseStatement();
            }
            expect("}");
        } else if (currentTokenIs("if")) {
            expect("if");
            expect("(");
            parseExpression();
            expect(")");
            parseStatement();
            if (currentTokenIs("else")) {
                expect("else");
                parseStatement();
            }
        } else if (currentTokenIs("while")) {
            expect("while");
            expect("(");
            parseExpression();
            expect(")");
            parseStatement();
        } else if (currentTokenIs("System.out.println")) {
            expect("System.out.println");
            expect("(");
            parseExpression();
            expect(")");
            expect(";");
        } else if (currentTokenIs("IDENTIFIER")) {
            expect("IDENTIFIER");
            if (currentTokenIs("=")) {
                expect("=");
                parseExpression();
                expect(";");
            } else {
                expect("[");
                parseExpression();
                expect("]");
                expect("=");
                parseExpression();
                expect(";");
            }
        }
    }

    private void parseExpression() {
        parseOrExpression();
    }

    private void parseOrExpression() {
        parseAndExpression();
        while (currentTokenIs("||")) {
            expect("||");
            parseAndExpression();
            // Código para lidar com a operação '||' (OR) aqui
        }
    }

    private void parseAndExpression() {
        parseEqualityExpression();
        while (currentTokenIs("&&")) {
            expect("&&");
            parseEqualityExpression();
            // Código para lidar com a operação '&&' (AND) aqui
        }
    }

    private void parseEqualityExpression() {
        parseRelationalExpression();
        while (currentTokenIs("==") || currentTokenIs("!=")) {
            String op = tokens.get(currentTokenIndex);
            expect(op); // Pode ser '==' ou '!='
            parseRelationalExpression();
            // Código para lidar com as operações de igualdade aqui
        }
    }

    private void parseRelationalExpression() {
        parseAdditiveExpression();
        while (currentTokenIs("<") || currentTokenIs("<=") || currentTokenIs(">") || currentTokenIs(">=")) {
            String op = tokens.get(currentTokenIndex);
            expect(op); // Pode ser '<', '<=', '>' ou '>='
            parseAdditiveExpression();
            // Código para lidar com as operações relacionais aqui
        }
    }

    private void parseAdditiveExpression() {
        parseMultiplicativeExpression();
        while (currentTokenIs("+") || currentTokenIs("-")) {
            String op = tokens.get(currentTokenIndex);
            expect(op); // Pode ser '+' ou '-'
            parseMultiplicativeExpression();
            // Código para lidar com as operações de adição e subtração aqui
        }
    }

    private void parseMultiplicativeExpression() {
        parsePrimaryExpression();
        while (currentTokenIs("*") || currentTokenIs("/")) {
            String op = tokens.get(currentTokenIndex);
            expect(op); // Pode ser '*' ou '/'
            parsePrimaryExpression();
            // Código para lidar com as operações de multiplicação e divisão aqui
        }
    }

    private void parsePrimaryExpression() {
        if (currentTokenIs("(")) {
            expect("(");
            parseExpression();
            expect(")");
        } else if (currentTokenIs("IDENTIFIER")) {
            parseIdentifier();
        } else if (currentTokenIs("INTEGER_LITERAL") || currentTokenIs("BOOLEAN_LITERAL")) {
            expect("INTEGER_LITERAL", "BOOLEAN_LITERAL");
        } else {
            throw new RuntimeException("Erro de sintaxe: expressão inválida");
        }
    }

    private void parseIdentifier() {
        expect("IDENTIFIER");
        if (currentTokenIs("(")) {
            parseMethodCall();
        } else if (currentTokenIs("[")) {
            parseArrayAccess();
        }
    }

    private void parseMethodCall() {
        expect("(");
        if (!currentTokenIs(")")) {
            parseExpression();
            while (currentTokenIs(",")) {
                expect(",");
                parseExpression();
            }
        }
        expect(")");
    }

    private void parseArrayAccess() {
        expect("[");
        parseExpression();
        expect("]");
    }

    private boolean currentTokenIs(String expectedToken) {
        return currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals(expectedToken);
    }

    private void expect(String expectedToken) {
        if (currentTokenIndex < tokens.size() && tokens.get(currentTokenIndex).equals(expectedToken)) {
            currentTokenIndex++;
        } else {
            throw new RuntimeException("Erro de sintaxe: esperado " + expectedToken + " mas encontrado " + tokens.get(currentTokenIndex));
        }
    }

    private void expect(String... expectedTokens) {
        for (String expectedToken : expectedTokens) {
            expect(expectedToken);
        }
    }
}
