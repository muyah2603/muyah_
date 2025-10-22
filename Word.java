package engine;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Scanner;
import java.util.Set;

public class Word {
    public static Set<String> stopWords = new HashSet<>();

    private String prefix;
    private String text;
    private String suffix;
    private boolean valid;

    private Word(String prefix, String text, String suffix, boolean valid) {
        this.prefix = prefix;
        this.text = text;
        this.suffix = suffix;
        this.valid = valid;
    }

    public boolean isKeyword() {
        return valid && !stopWords.contains(text.toLowerCase());
    }

    public String getPrefix() {
        return prefix;
    }

    public String getSuffix() {
        return suffix;
    }

    public String getText() {
        return text;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Word)) return false;
        Word other = (Word) o;
        return this.text.equalsIgnoreCase(other.text);
    }

    @Override
    public int hashCode() {
        return text.toLowerCase().hashCode();
    }

    @Override
    public String toString() {
        return prefix + text + suffix;
    }

    public static Word createWord(String rawText) {
        if (rawText == null || rawText.isEmpty()) {
            return new Word("", rawText == null ? "" : rawText, "", false);
        }

        // Check for whitespace - invalid word
        if (rawText.contains(" ") || rawText.trim().isEmpty()) {
            return new Word("", rawText, "", false);
        }

        // Find start of actual text (skip non-letter prefix)
        int start = 0;
        while (start < rawText.length() && !Character.isLetter(rawText.charAt(start))) {
            start++;
        }

        // No letters found - invalid word, return as-is
        if (start == rawText.length()) {
            return new Word("", rawText, "", false);
        }

        // Find end of actual text (only letters)
        int end = start;
        while (end < rawText.length() && Character.isLetter(rawText.charAt(end))) {
            end++;
        }

        String prefix = rawText.substring(0, start);
        String text = rawText.substring(start, end);
        String suffix = rawText.substring(end);

        // Check if prefix or suffix contains alphanumeric
        boolean hasPrefixAlphaNum = prefix.matches(".*[a-zA-Z0-9].*");
        boolean hasSuffixAlphaNum = suffix.matches(".*[a-zA-Z0-9].*");

        // If invalid: return entire rawText as text with empty prefix/suffix
        if (hasPrefixAlphaNum || hasSuffixAlphaNum) {
            return new Word("", rawText, "", false);
        }

        // Valid word
        return new Word(prefix, text, suffix, true);
    }

    public static boolean loadStopWords(String fileName) {
        stopWords.clear();

        // Try multiple locations based on your project structure
        String[] possiblePaths = {
                fileName,                                    // Current directory
                "src/a1_2301140021/" + fileName,            // In your source package
                "out/production/project/" + fileName,        // In output directory
                "src/" + fileName,                           // In src folder
                "../" + fileName,                            // Parent directory
                "../../" + fileName,                         // Two levels up
                "../../../" + fileName                       // Three levels up
        };

        File file = null;
        for (String path : possiblePaths) {
            File tryFile = new File(path);
            if (tryFile.exists() && tryFile.canRead()) {
                file = tryFile;
                break;
            }
        }

        if (file == null) {
            return false;
        }

        try (Scanner scanner = new Scanner(file)) {
            while (scanner.hasNextLine()) {
                String word = scanner.nextLine().trim().toLowerCase();
                if (!word.isEmpty()) {
                    stopWords.add(word);
                }
            }
            scanner.close();
            return true;
        } catch (FileNotFoundException e) {
            return false;
        }
    }
}