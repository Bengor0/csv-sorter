package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw02.HasLabels;
import cz.muni.fi.pb162.hw03.cmd.CommandLine;
import cz.muni.fi.pb162.hw03.impl.Utils.CsvUtils;
import net.cechacek.edu.pb162.csv.CsvToolkit;
import net.cechacek.edu.pb162.csv.DefaultToolkit;

import java.io.IOException;
import java.nio.file.Path;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

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
        LabeledItemHeadedConvertor labeledItemConvertor = new LabeledItemHeadedConvertor(options.getLabelColumn());
        Set<HasLabels> labeledItems = new LinkedHashSet<>();
        Set<Map<String, String>> csvFilters = new LinkedHashSet<>();

        CsvUtils.readCsvWithConversion(toolkit, labeledItemConvertor, inputFile, labeledItems);
        CsvUtils.readCsvWithoutConversion(toolkit, filterFile, csvFilters);
        CsvUtils.writeFilteredItems(toolkit, labeledItemConvertor, outputDirectory, labeledItems, csvFilters);
    }
}
