package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw02.HasLabels;
import cz.muni.fi.pb162.hw02.impl.Filter;
import net.cechacek.edu.pb162.csv.CsvToolkit;
import net.cechacek.edu.pb162.csv.DefaultToolkit;
import net.cechacek.edu.pb162.csv.ValueConvertor;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {


    public static void main(String[] args) throws IOException {
        Path inputFile = Paths.get("data.csv");
        Path outputDirectory = Paths.get("out");
        Path filterFile = Paths.get("filters.csv");
        CsvToolkit toolkit = DefaultToolkit.create(';', '"', StandardCharsets.UTF_8);
        ElementHeadedConvertor elementConvertor = new ElementHeadedConvertor();
        Set<HasLabels> labeledItems = new LinkedHashSet<>();
        Set<Map<String, String>> csvFiltersMaps = new LinkedHashSet<>();
        int csvColumns = 0;
        List<String> csvHeader;

        //reads csv file with articles/Pokemon and saves each line as a HasLabels object.
        try (var reader = toolkit.readWithHeader(inputFile)) {
            while (reader.ready()) {
                var line = reader.read(elementConvertor);
                if (csvColumns == 0) {
                    csvColumns = elementConvertor.toData(line).entrySet().size();
                }
                labeledItems.add(line);
            }
        }

        if (csvColumns == 4){
            csvHeader = List.of("name", "date_published", "hits", "labels");
        } else csvHeader = List.of("Name", "Labels", "Total", "HP", "Attack", "Defense",
                "Sp. Atk", "Sp. Def", "Speed");

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
                try (var writer = toolkit.writeWithHeader(outputFile, csvHeader)) {
                    filteredLabeledItems.forEach(filteredLabeledItem -> {
                        try {
                            writer.write(elementConvertor, filteredLabeledItem);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    });
                }
            }
        }
    }

    static class ElementHeadedConvertor implements ValueConvertor<Map<String, String>, HasLabels> {
        private String[] header;

        @Override
        public HasLabels toDomain(Map<String, String> data) {
            header = data.keySet().toArray(new String[0]);
            if (header.length == 4){
                return new Article(
                        data.get(header[0]),
                        data.get(header[1]),
                        data.get(header[2]),
                        new LinkedHashSet<>(Set.of(data.get(header[3]).split(" ")))
                );
            } if (header.length == 10) {
                return new Pokemon(
                        data.get(header[1]),
                        new LinkedHashSet<>(Set.of(data.get(header[2]).split(" "))),
                        data.get(header[3]),
                        data.get(header[4]),
                        data.get(header[5]),
                        data.get(header[6]),
                        data.get(header[7]),
                        data.get(header[8]),
                        data.get(header[9])
                );
            } else {
                System.out.println("kokoot");
                return null;
            }
        }

        @Override
        public Map<String, String> toData(HasLabels labeled) {
            LinkedHashMap<String, String> orderedMap = new LinkedHashMap<>();
            if (labeled instanceof Article) {
                orderedMap.put(header[0], ((Article) labeled).title());
                orderedMap.put(header[1], ((Article) labeled).date());
                orderedMap.put(header[2], ((Article) labeled).hits());
                orderedMap.put(header[3], ((Article) labeled).labelsToString());
            } else {
                orderedMap.put(header[1], ((Pokemon) labeled).name());
                orderedMap.put(header[2], ((Pokemon) labeled).labelsToString());
                orderedMap.put(header[3], ((Pokemon) labeled).total());
                orderedMap.put(header[4], ((Pokemon) labeled).hitPoints());
                orderedMap.put(header[5], ((Pokemon) labeled).attack());
                orderedMap.put(header[6], ((Pokemon) labeled).defense());
                orderedMap.put(header[7], ((Pokemon) labeled).specialAttack());
                orderedMap.put(header[8], ((Pokemon) labeled).specialDefense());
                orderedMap.put(header[9], ((Pokemon) labeled).speed());
            }
            return orderedMap;
        }
    }
}