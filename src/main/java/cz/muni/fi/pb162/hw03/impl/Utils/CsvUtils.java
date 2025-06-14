package cz.muni.fi.pb162.hw03.impl.Utils;

import cz.muni.fi.pb162.hw02.HasLabels;
import cz.muni.fi.pb162.hw02.LabelFilter;
import cz.muni.fi.pb162.hw02.impl.LabeledOperations;
import cz.muni.fi.pb162.hw03.impl.LabeledItemHeadedConvertor;
import net.cechacek.edu.pb162.csv.CsvToolkit;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Collection;
import java.util.Map;
import java.util.Set;

/**
 * Utility class for reading and writing CSV files.
 *
 * @author Viktor Sulla <sulla.viktor@gmail.com>
 */
public final class CsvUtils {
    private CsvUtils() {
    }

    /**
     * Method that reads csv file with a conversion of data to
     * an instance of HasLabels which is then stored in a set.
     *
     * @param toolkit - toolkit for work with CSV file.
     * @param labeledItemConvertor - convertor of labeled items.
     * @param inputFile - input file with csv data.
     * @param labeledItems - set of items with labels.
     * @throws IOException - input output exception.
     */
    public static void readCsvWithConversion(CsvToolkit toolkit, LabeledItemHeadedConvertor labeledItemConvertor,
                                             Path inputFile, Set<HasLabels> labeledItems) throws IOException {
        try (var reader = toolkit.readWithHeader(inputFile)) {
            while (reader.ready()) {
                var line = reader.read(labeledItemConvertor);
                labeledItems.add(line);
            }
        }
    }

    /**
     * Method that reads csv file without a conversion of data
     * which is then stored in a set.
     *
     * @param toolkit - toolkit for work with CSV file.
     * @param inputFile - input file with csv data.
     * @param csvData - set of data represented by a map where keys are headers and values are data.
     * @throws IOException - input output exception.
     */
    public static void readCsvWithoutConversion(CsvToolkit toolkit, Path inputFile,
                                                Set<Map<String, String>> csvData) throws IOException {
        try (var reader = toolkit.readWithHeader(inputFile)) {
            while (reader.ready()) {
                var line = reader.read();
                csvData.add(line);
            }
        }
    }

    /**
     * Method that for each filter expression writes filtered non-empty labeled items to a new csv file.
     *
     * @param toolkit - toolkit for work with CSV file.
     * @param labeledItemHeadedConvertor - convertor of labeled items.
     * @param outputDirectory - path to the output directory.
     * @param labeledItems - items with labels.
     * @param csvFilters - set of csv filter data.
     * @throws IOException - input output exception.
     */
    public static void writeFilteredItems(CsvToolkit toolkit, LabeledItemHeadedConvertor labeledItemHeadedConvertor,
                                          Path outputDirectory, Set<HasLabels> labeledItems,
                                          Set<Map<String, String>> csvFilters) throws IOException {
        for (Map<String, String> csvFilter : csvFilters) {
            Path outputPath = outputDirectory.resolve(csvFilter.get("name") + ".csv");
            LabelFilter filter = LabeledOperations.expressionFilter(csvFilter.get("expression"));
            Collection<HasLabels> filteredLabeledItems = filter.matching(labeledItems);

            if (!filteredLabeledItems.isEmpty()) {
                writeCsvWithConversion(toolkit, outputPath, labeledItemHeadedConvertor, filteredLabeledItems);
            }
        }
    }

    /**
     * Method that writes conversed data to a csv file.
     *
     * @param toolkit - toolkit for work with CSV file.
     * @param outputPath - path to the output csv file.
     * @param labeledItemHeadedConvertor - convertor of labeled items.
     * @param labeledItems - items with labels.
     * @throws IOException - input output exception.
     */
    public static void writeCsvWithConversion(CsvToolkit toolkit, Path outputPath,
                                          LabeledItemHeadedConvertor labeledItemHeadedConvertor,
                                          Collection<HasLabels> labeledItems) throws IOException {
        try (var writer = toolkit.writeWithHeader(outputPath, labeledItemHeadedConvertor.getHeader())) {
            labeledItems.forEach(labeledItem -> {
                try {
                    writer.write(labeledItemHeadedConvertor, labeledItem);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

    }
}
