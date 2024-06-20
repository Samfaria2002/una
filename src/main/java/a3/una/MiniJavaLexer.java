package a3.una;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.io.*;
import java.util.*;

public class MiniJavaLexer {
    private static final Set<String> keywords = new HashSet<>(Arrays.asList(
        "class", "public", "static", "void", "main", "String", "int", "boolean", "if", "else", "while", "true", "false", "return", "System.out.println"
    ));

    public static List<String> analyze(String filePath) throws IOException {
        List<String> tokens = new ArrayList<>();
        BufferedReader reader = new BufferedReader(new FileReader(filePath));
        String line;
        while ((line = reader.readLine()) != null) {
            StringTokenizer tokenizer = new StringTokenizer(line, " {}();[]=.", true);
            while (tokenizer.hasMoreTokens()) {
                String token = tokenizer.nextToken().trim();
                if (!token.isEmpty()) {
                    if (keywords.contains(token)) {
                        tokens.add(token);
                    } else if (token.matches("[a-zA-Z][a-zA-Z0-9]*")) {
                        tokens.add("IDENTIFIER");
                    } else if (token.matches("\\d+")) {
                        tokens.add("INTEGER_LITERAL");
                    } else {
                        tokens.add(token);
                    }
                }
            }
        }
        reader.close();
        return tokens;
    }
}
