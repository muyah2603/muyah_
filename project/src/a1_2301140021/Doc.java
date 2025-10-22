package engine;

import java.util.ArrayList;
import java.util.List;

public class Doc {
    private List<Word> title;
    private List<Word> body;

    public Doc(String content) {
        String[] parts = content.split("\n", 2);
        title = tokenize(parts[0].trim());
        body = parts.length > 1 ? tokenize(parts[1].trim()) : new ArrayList<>();
    }

    private List<Word> tokenize(String text) {
        List<Word> words = new ArrayList<>();
        if (text.isEmpty()) return words;

        // Split on whitespace or punctuation boundaries
        String[] tokens = text.split("(?<=\\s)|(?=\\s)|(?<=[\\p{Punct}])|(?=[\\p{Punct}])");

        for (String token : tokens) {
            // Skip pure whitespace tokens
            if (!token.isEmpty() && !token.matches("\\s+")) {
                words.add(Word.createWord(token));
            }
        }
        return words;
    }

    public List<Word> getTitle() {
        return title;
    }

    public List<Word> getBody() {
        return body;
    }

    public List<Word> getAllWords() {
        List<Word> all = new ArrayList<>();
        if (title != null) all.addAll(title);
        if (body != null) all.addAll(body);
        return all;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Doc)) return false;
        Doc other = (Doc) o;
        return this.title.equals(other.title) && this.body.equals(other.body);
    }
}