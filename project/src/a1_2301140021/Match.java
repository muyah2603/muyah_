package engine;

public class Match {
    private Doc doc;
    private Word word;
    private int freq;
    private int firstIndex;

    public Match(Doc doc, Word word, int freq, int firstIndex) {
        this.doc = doc;
        this.word = word;
        this.freq = freq;
        this.firstIndex = firstIndex;
    }

    public int getFreq() {
        return freq;
    }

    public int getFirstIndex() {
        return firstIndex;
    }

    public Word getWord() {
        return word;
    }

    public Doc getDoc() {
        return doc;
    }

    public int compareTo(Match o) {
        return Integer.compare(this.firstIndex, o.firstIndex);
    }
}