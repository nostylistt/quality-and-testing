import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Main {

    private static final Path TEXT_FILE = Path.of("src", "txt", "harry.txt");
    private static final Charset TEXT_CHARSET = Charset.forName("windows-1252");
    private static final int TOP_SIZE = 30;

    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();

        String text = Files.readString(TEXT_FILE, TEXT_CHARSET);
        List<String> words = splitIntoWords(text);
        Map<String, Integer> frequencies = countFrequencies(words);
        List<Map.Entry<String, Integer>> top = findTop(frequencies, TOP_SIZE);

        top.forEach(entry -> System.out.println(entry.getKey() + " " + entry.getValue()));

        long elapsedMillis = (System.nanoTime() - start) / 1_000_000;
        System.out.println("------");
        System.out.println(elapsedMillis + " ms");
    }

    static List<String> splitIntoWords(String text) {
        String normalized = text.toLowerCase(Locale.ROOT)
                .replaceAll("[‘’]", "'")
                .replaceAll("[^a-z']+", " ")
                .replaceAll("(^|\\s)'+|'+(\\s|$)", " ");
        return Arrays.stream(normalized.split("\\s+"))
                .filter(word -> !word.isEmpty())
                .toList();
    }

    static Map<String, Integer> countFrequencies(List<String> words) {
        Map<String, Integer> frequencies = new HashMap<>();
        for (String word : words) {
            frequencies.merge(word, 1, Integer::sum);
        }
        return frequencies;
    }

    static List<Map.Entry<String, Integer>> findTop(Map<String, Integer> frequencies, int limit) {
        return frequencies.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed()
                        .thenComparing(Map.Entry.comparingByKey()))
                .limit(limit)
                .toList();
    }
}
