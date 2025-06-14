package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw02.HasLabels;
import net.cechacek.edu.pb162.csv.ValueConvertor;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Static class representing labeled item convertor. Provides methods for converting
 * csv data from Map to an instance of HasLabels and vice versa. Create new instance
 * of LabeledItemHeadedConvertor for each csv file if header structure or naming differs.
 */
public class LabeledItemHeadedConvertor implements ValueConvertor<Map<String, String>, HasLabels> {
    private List<String> header;
    private final String labelsColumnName;

    /**
     * Constructor of LabeledItemHeadedConvertor.
     * @param labelsColumnName - custom csv column name for labels.
     */
    public LabeledItemHeadedConvertor(String labelsColumnName) {
        this.header = null;
        this.labelsColumnName = labelsColumnName;
    }

    public List<String> getHeader() {
        return header;
    }

    @Override
    public HasLabels toDomain(Map<String, String> data) {
        header = new ArrayList<>(data.keySet());

        if (header.size() == 4){
            return new Article(
                    data.get(header.get(0)),
                    data.get(header.get(1)),
                    data.get(header.get(2)),
                    Arrays.stream(data.get(header.get(3)).split(" "))
                            .collect(Collectors.toCollection(LinkedHashSet::new))
            );
        } if (header.size() == 10) {
            return new Pokemon(
                    data.get(header.get(1)),
                    Arrays.stream(data.get(header.get(2)).split(" "))
                            .collect(Collectors.toCollection(LinkedHashSet::new)),
                    data.get(header.get(3)),
                    data.get(header.get(4)),
                    data.get(header.get(5)),
                    data.get(header.get(6)),
                    data.get(header.get(7)),
                    data.get(header.get(8)),
                    data.get(header.get(9))
            );
        } else {
            throw new IllegalArgumentException(String.format("Unexpected number of csv file columns." +
                    " Expected number of 4 or 10 columns, instead received %s columns", header.size()));
        }
    }

    @Override
    public Map<String, String> toData(HasLabels labeled) {
        if (header == null) {
            throw new RuntimeException("The header for this instance hasn't been initialized. " +
                    "Data must be read from csv file and converted to Domain first.");
        }
        LinkedHashMap<String, String> orderedMap = new LinkedHashMap<>();
        if (labeled instanceof Article) {
            orderedMap.put(header.get(0), ((Article) labeled).title());
            orderedMap.put(header.get(1), ((Article) labeled).date());
            orderedMap.put(header.get(2), ((Article) labeled).hits());
            orderedMap.put(labelsColumnName, ((Article) labeled).labelsToString());
        } else if (labeled instanceof Pokemon) {
            orderedMap.put(header.get(1), ((Pokemon) labeled).name());
            orderedMap.put(labelsColumnName, ((Pokemon) labeled).labelsToString());
            orderedMap.put(header.get(3), ((Pokemon) labeled).total());
            orderedMap.put(header.get(4), ((Pokemon) labeled).hitPoints());
            orderedMap.put(header.get(5), ((Pokemon) labeled).attack());
            orderedMap.put(header.get(6), ((Pokemon) labeled).defense());
            orderedMap.put(header.get(7), ((Pokemon) labeled).specialAttack());
            orderedMap.put(header.get(8), ((Pokemon) labeled).specialDefense());
            orderedMap.put(header.get(9), ((Pokemon) labeled).speed());
        } else {
            throw new IllegalArgumentException(String.format("Unexpected argument instance. " +
                    "Expected instance of Article OR Pokemon. Instead received instance of %s", labeled.getClass()));
        }
        return orderedMap;
    }
}
