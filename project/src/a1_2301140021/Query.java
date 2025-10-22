package engine;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Query {
    private List<Word> keywords;

    public Query(String searchTerms) {
        keywords = new ArrayList<>();
        if (searchTerms == null || searchTerms.isEmpty()) return;

        // Tokenize the same way as Doc
        String[] tokens = searchTerms.split("(?<=\\s)|(?=\\s)|(?<=[\\p{Punct}])|(?=[\\p{Punct}])");

        for (String token : tokens) {
            if (!token.isEmpty() && !token.matches("\\s+")) {
                Word w = Word.createWord(token);
                if (w.isKeyword()) {
                    keywords.add(w);
                }
            }
        }
    }

    public List<Word> getKeywords() {
        return keywords;
    }

    public List<Match> matchAgainst(Doc doc) {
        List<Match> matches = new ArrayList<>();
        Map<Word, Integer> freqMap = new HashMap<>();
        Map<Word, Integer> firstIndexMap = new HashMap<>();

        List<Word> allWords = doc.getAllWords();
        if (allWords == null || allWords.isEmpty()) {
            // Trả về Match mặc định cho các keyword nếu không có từ
            for (Word keyword : keywords) {
                matches.add(new Match(doc, keyword, 0, -1));
            }
            return matches;
        }

        for (int i = 0; i < allWords.size(); i++) {
            Word w = allWords.get(i);
            if (!w.isKeyword()) continue;

            for (Word keyword : keywords) {
                if (w.equals(keyword)) {
                    freqMap.put(keyword, freqMap.getOrDefault(keyword, 0) + 1);
                    if (!firstIndexMap.containsKey(keyword)) {
                        firstIndexMap.put(keyword, i);
                    }
                }
            }
        }

        for (Word keyword : keywords) {
            int freq = freqMap.getOrDefault(keyword, 0);
            int index = firstIndexMap.getOrDefault(keyword, -1);
            matches.add(new Match(doc, keyword, freq, index)); // Luôn thêm, ngay cả freq = 0
        }
        return matches;
    }
}