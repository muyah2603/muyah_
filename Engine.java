package engine;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class Engine {
    private List<Doc> docs = new ArrayList<>();

    public int loadDocs(String dirname) {
        docs.clear(); // tránh trùng dữ liệu

        File dir = new File(dirname);
        if (!dir.exists() || !dir.isDirectory()) return 0;

        File[] files = dir.listFiles((d, name) -> name.endsWith(".txt"));
        if (files == null) return 0;

        Arrays.sort(files, Comparator.comparing(File::getName));

        for (File file : files) {
            try (Scanner scanner = new Scanner(file)) {
                scanner.useDelimiter("\\A");
                String content = scanner.hasNext() ? scanner.next() : "";
                if (!content.trim().isEmpty()) {
                    docs.add(new Doc(content));
                }
            } catch (Exception e) {
                System.err.println("Skipped invalid file: " + file.getName());
            }
        }

        return docs.size();
    }

    public Doc[] getDocs() {
        return docs.toArray(new Doc[0]);
    }

    public List<Result> search(Query q) {
        List<Result> results = new ArrayList<>();
        for (Doc d : docs) {
            List<Match> matches = q.matchAgainst(d);
            if (!matches.isEmpty()) {
                results.add(new Result(d, matches));
            }
        }
        results.sort(Comparator.reverseOrder());
        return results;
    }

    public String htmlResult(List<Result> results) {
        if (results == null || results.isEmpty()) return "";
        StringBuilder sb = new StringBuilder();
        for (Result r : results) {
            sb.append(r.htmlHighlight()).append("\n");
        }
        return sb.toString().trim();
    }
}
