package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw02.HasLabels;
import cz.muni.fi.pb162.hw02.impl.Filter;
import cz.muni.fi.pb162.hw03.cmd.CommandLine;
import net.cechacek.edu.pb162.csv.CsvToolkit;
import net.cechacek.edu.pb162.csv.DefaultToolkit;
import net.cechacek.edu.pb162.csv.ValueConvertor;

import java.io.IOException;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Application Runtime
 */
final class Application {

    private final ApplicationOptions options;

    Application(ApplicationOptions options, CommandLine cli) {
        Objects.requireNonNull(options);
        Objects.requireNonNull(cli);

        this.options = options;
    }

    /**
     * Note:    This method represents the runtime logic.
     * However, you should still use proper decomposition.
     * <p>
     * Application runtime logic
     */
    void run() throws IOException {
        Path inputFile = options.getInput();
        Path outputDirectory = options.getOutput();
        Path filterFile = options.getFilters();
        CsvToolkit toolkit = DefaultToolkit.create(options.getDelimiter().charAt(0), '"', options.getCharset());
        LabeledItemHeadedConvertor labeledItemConvertor = new LabeledItemHeadedConvertor();
        Set<HasLabels> labeledItems = new LinkedHashSet<>();
        Set<Map<String, String>> csvFiltersMaps = new LinkedHashSet<>();

        //reads csv file with articles/Pokemon and saves each line as a HasLabels object.
        try (var reader = toolkit.readWithHeader(inputFile)) {
            while (reader.ready()) {
                var line = reader.read(labeledItemConvertor);
                labeledItems.add(line);
            }
        }

        //reads csv file with csvFiltersMaps and saves each line as a Map where key is name and value is filter expression.
        try (var reader = toolkit.readWithHeader(filterFile)) {
            while (reader.ready()) {
                var line = reader.read();
                csvFiltersMaps.add(line);
            }
        }

        // For each filter name writes filtered non-empty labeled items to a new csv file.
        for (Map<String, String> csvFilterMap : csvFiltersMaps) {
            Path outputFile = outputDirectory.resolve(csvFilterMap.get("name") + ".csv");
            Filter filter = new Filter(csvFilterMap.get("expression"));
            Collection<HasLabels> filteredLabeledItems = filter.matching(labeledItems);

            if (filteredLabeledItems.size() > 0) {
                try (var writer = toolkit.writeWithHeader(outputFile, labeledItemConvertor.getHeader())) {
                    filteredLabeledItems.forEach(filteredLabeledItem -> {
                        try {
                            writer.write(labeledItemConvertor, filteredLabeledItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }

    /**
     * Static class representing labeled item convertor. Provides methods for converting
     * csv data from Map to an instance of HasLabels and vice versa. Create new instance
     * of LabeledItemHeadedConvertor for each csv file if header structure or naming differs.
     */
    static class LabeledItemHeadedConvertor implements ValueConvertor<Map<String, String>, HasLabels> {
        private List<String> header = null;

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
                orderedMap.put(header.get(3), ((Article) labeled).labelsToString());
            } else if (labeled instanceof Pokemon) {
                orderedMap.put(header.get(1), ((Pokemon) labeled).name());
                orderedMap.put(header.get(2), ((Pokemon) labeled).labelsToString());
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
}
