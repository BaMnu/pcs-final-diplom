
import com.itextpdf.kernel.pdf.*;
import com.itextpdf.kernel.pdf.canvas.parser.PdfTextExtractor;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.*;

public class BooleanSearchEngine implements SearchEngine {
    private final List<PageEntry> listPageEntry = new ArrayList<>();

    public BooleanSearchEngine(File pdfsDir) throws IOException {

        try (Stream<Path> paths = Files.walk(Path.of(pdfsDir.toString()))) {
            List<Path> fileNameList = paths.filter(Files::isRegularFile).collect(Collectors.toList());

            for (Path path : fileNameList) {
                String name = path.getFileName().toString();

                try (PdfDocument doc = new PdfDocument(new PdfReader(path.toString()))) {

                    for (int i = 1; i <= doc.getNumberOfPages(); i++) {
                        int pageNum = doc.getPageNumber(doc.getPage(i));

                        String text = PdfTextExtractor.getTextFromPage(doc.getPage(i));
                        String[] words = text.split("\\P{IsAlphabetic}+");

                        Map<String, Integer> wordCount = new HashMap<>();
                        for (String word : words) {
                            if (word.isEmpty()) {
                                continue;
                            }
                            word = word.toLowerCase();
                            wordCount.put(word, wordCount.getOrDefault(word, 0) + 1);
                        }

                        for (Map.Entry<String, Integer> entry : wordCount.entrySet()) {
                            String key = entry.getKey();
                            Integer value = entry.getValue();
                            Map<String, Integer> eachWord = new HashMap<>();
                            eachWord.put(key, value);
                            PageEntry pageEntry = new PageEntry(name, pageNum, eachWord);
                            listPageEntry.add(pageEntry);

                        }
                    }

                }
            }

        }
    }

    @Override
    public List<PageEntry> search(String word) {
        return listPageEntry.stream()
                .filter(page -> page.getCountMap().containsKey(word.toLowerCase()))
                .sorted().collect(Collectors.toList());
    }
}