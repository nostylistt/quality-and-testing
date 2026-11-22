import java.io.BufferedReader;
import java.io.IOException;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryPoolMXBean;
import java.lang.management.MemoryType;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;

public class Main {

    private static final Path TEXT_FILE = Path.of("src", "txt", "harry.txt");
    private static final Charset TEXT_CHARSET = Charset.forName("windows-1252");
    private static final int TOP_SIZE = 30;
    private static final int EXPECTED_DISTINCT_WORDS = 8192;

    private static final Comparator<Map.Entry<String, int[]>> BY_FREQUENCY =
            Comparator.<Map.Entry<String, int[]>>comparingInt(entry -> entry.getValue()[0])
                    .thenComparing(Map.Entry::getKey, Comparator.reverseOrder());

    public static void main(String[] args) throws IOException {
        long start = System.nanoTime();

        Map<String, int[]> frequencies = countFrequencies(TEXT_FILE);
        List<Map.Entry<String, int[]>> top = findTop(frequencies, TOP_SIZE);

        StringBuilder output = new StringBuilder();
        for (Map.Entry<String, int[]> entry : top) {
            output.append(entry.getKey()).append(' ').append(entry.getValue()[0]).append('\n');
        }
        System.out.print(output);

        long elapsedMillis = (System.nanoTime() - start) / 1_000_000;
        System.out.println("------");
        System.out.println(elapsedMillis + " ms");
        System.out.println(peakHeapUsedKb() + " KB peak heap");
    }

    static Map<String, int[]> countFrequencies(Path file) throws IOException {
        Map<String, int[]> frequencies = new HashMap<>(EXPECTED_DISTINCT_WORDS);
        StringBuilder word = new StringBuilder(32);
        try (BufferedReader reader = Files.newBufferedReader(file, TEXT_CHARSET)) {
            int next;
            while ((next = reader.read()) != -1) {
                char c = (char) next;
                if (c >= 'a' && c <= 'z') {
                    word.append(c);
                } else if (c >= 'A' && c <= 'Z') {
                    word.append((char) (c + ('a' - 'A')));
                } else if (isApostrophe(c) && word.length() > 0) {
                    word.append('\'');
                } else {
                    addWord(word, frequencies);
                }
            }
        }
        addWord(word, frequencies);
        return frequencies;
    }

    private static boolean isApostrophe(char c) {
        return c == '\'' || c == '‘' || c == '’';
    }

    private static void addWord(StringBuilder word, Map<String, int[]> frequencies) {
        int length = word.length();
        while (length > 0 && word.charAt(length - 1) == '\'') {
            length--;
        }
        if (length > 0) {
            word.setLength(length);
            frequencies.computeIfAbsent(word.toString(), key -> new int[1])[0]++;
        }
        word.setLength(0);
    }

    static List<Map.Entry<String, int[]>> findTop(Map<String, int[]> frequencies, int limit) {
        PriorityQueue<Map.Entry<String, int[]>> heap = new PriorityQueue<>(limit + 1, BY_FREQUENCY);
        for (Map.Entry<String, int[]> entry : frequencies.entrySet()) {
            heap.offer(entry);
            if (heap.size() > limit) {
                heap.poll();
            }
        }
        List<Map.Entry<String, int[]>> top = new ArrayList<>(heap);
        top.sort(BY_FREQUENCY.reversed());
        return top;
    }

    private static long peakHeapUsedKb() {
        long peak = 0;
        for (MemoryPoolMXBean pool : ManagementFactory.getMemoryPoolMXBeans()) {
            if (pool.getType() == MemoryType.HEAP) {
                peak += pool.getPeakUsage().getUsed();
            }
        }
        return peak / 1024;
    }
}
