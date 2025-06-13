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

        for (Map<String, String> csvFilterMap : csvFiltersMaps) {
            Path outputFile = outputDirectory.resolve(csvFilterMap.get("name") + ".csv");
            Filter filter = new Filter(csvFilterMap.get("expression"));
            Set<HasLabels> filteredLabeledItems = new LinkedHashSet<>(filter.matching(labeledItems));

            if (filteredLabeledItems.size() > 0) {
                try (var writer = toolkit.writeWithHeader(outputFile, labeledItemConvertor.header)) {
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

    static class LabeledItemHeadedConvertor implements ValueConvertor<Map<String, String>, HasLabels> {
        private List<String> header;

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
                throw new RuntimeException(String.format("Unexpected number of headers (expecting 4 or 10) and got %s",
                        header.size()));
            }
        }

        @Override
        public Map<String, String> toData(HasLabels labeled) {
            LinkedHashMap<String, String> orderedMap = new LinkedHashMap<>();
            if (labeled instanceof Article) {
                orderedMap.put(header.get(0), ((Article) labeled).title());
                orderedMap.put(header.get(1), ((Article) labeled).date());
                orderedMap.put(header.get(2), ((Article) labeled).hits());
                orderedMap.put(header.get(3), ((Article) labeled).labelsToString());
            } else {
                orderedMap.put(header.get(1), ((Pokemon) labeled).name());
                orderedMap.put(header.get(2), ((Pokemon) labeled).labelsToString());
                orderedMap.put(header.get(3), ((Pokemon) labeled).total());
                orderedMap.put(header.get(4), ((Pokemon) labeled).hitPoints());
                orderedMap.put(header.get(5), ((Pokemon) labeled).attack());
                orderedMap.put(header.get(6), ((Pokemon) labeled).defense());
                orderedMap.put(header.get(7), ((Pokemon) labeled).specialAttack());
                orderedMap.put(header.get(8), ((Pokemon) labeled).specialDefense());
                orderedMap.put(header.get(9), ((Pokemon) labeled).speed());
            }
            return orderedMap;
        }
    }
}
