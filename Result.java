package engine;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Result implements Comparable<Result> {
    private Doc doc;
    private List<Match> matches;

    public Result(Doc d, List<Match> matches) {
        this.doc = d;
        this.matches = matches != null ? matches : new ArrayList<>();
    }

    public List<Match> getMatches() {
        return matches;
    }

    public Doc getDoc() {
        return doc;
    }

    public int getTotalFrequency() {
        return matches.stream().mapToInt(Match::getFreq).sum();
    }

    public double getAverageFirstIndex() {
        if (matches.isEmpty()) return 0;
        return matches.stream().mapToInt(Match::getFirstIndex).average().getAsDouble();
    }

    public String htmlHighlight() {
        if (matches.isEmpty()) return "";

        Set<Word> matchedKeywords = new HashSet<>();
        for (Match m : matches) {
            matchedKeywords.add(m.getWord());
        }

        StringBuilder sb = new StringBuilder();

        // Process title - underline matches
        for (Word w : doc.getTitle()) {
            sb.append(w.getPrefix());
            if (matchedKeywords.contains(w) && w.isKeyword()) {
                sb.append("<u>");
            }
            sb.append(w.getText());
            if (matchedKeywords.contains(w) && w.isKeyword()) {
                sb.append("</u>");
            }
            sb.append(w.getSuffix());
        }

        sb.append("\n");

        // Process body - bold matches
        for (Word w : doc.getBody()) {
            sb.append(w.getPrefix());
            if (matchedKeywords.contains(w) && w.isKeyword()) {
                sb.append("<b>");
            }
            sb.append(w.getText());
            if (matchedKeywords.contains(w) && w.isKeyword()) {
                sb.append("</b>");
            }
            sb.append(w.getSuffix());
        }

        return sb.toString().trim();
    }

    @Override
    public int compareTo(Result o) {
        int cmp = Integer.compare(o.getMatches().size(), this.getMatches().size());
        if (cmp != 0) return cmp;

        cmp = Integer.compare(o.getTotalFrequency(), this.getTotalFrequency());
        if (cmp != 0) return cmp;

        return Double.compare(this.getAverageFirstIndex(), o.getAverageFirstIndex());
    }
}