import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.*;

public class PageEntry implements Comparable<PageEntry> {

    @JsonProperty("pdf") private final String pdfName;
    @JsonProperty("page") private final int page;
    @JsonProperty("count") private int count;
    @JsonIgnore private final Map<String, Integer> countMap;

    public PageEntry(String pdfName, int page, Map<String, Integer> countMap) {
        this.pdfName = pdfName;
        this.page = page;
        this.countMap = countMap;
        countMap.keySet().forEach(key -> count = countMap.get(key));
    }

    protected String getPdfName() {
    return pdfName;
}
    protected int getPage() {
        return page;
    }

    protected Map<String, Integer> getCountMap() {
        return countMap;
    }
    protected int getCount() {
        return count;
    }

    @Override
    public int compareTo(PageEntry o) {
        return o.getCount() - this.getCount();
    }

    @Override
    public String toString() {
        return String.format("PageEntry{pdfName=%s, page=%d, count=%d}", pdfName, page, count);
    }
}
