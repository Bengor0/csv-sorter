package cz.muni.fi.pb162.hw03.impl;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.converters.PathConverter;
import cz.muni.fi.pb162.hw03.cmd.CharsetConverter;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * Command line options (flags) for Library application
 */
@SuppressWarnings({ "FieldMayBeFinal", "FieldCanBeLocal" })
final class ApplicationOptions {
    @Parameter(names = "--help", help = true)
    private Boolean showUsage = false;

    @Parameter(
            names = { "-i", "--input" }, description = "Path of input file",
            required = true, converter = PathConverter.class
    )
    private Path input = Paths.get("data.csv");

    @Parameter(
            names = { "-f", "--filters" }, description = "Path of filter input file",
            required = true, converter = PathConverter.class
    )
    private Path filters;

    @Parameter(
            names = { "-o", "--output" }, description = "Path of output directory",
            converter = PathConverter.class
    )
    private Path output = Paths.get("");

    @Parameter(names = { "-c", "--column" }, description = "Name of the column with labels")
    private String labelColumn = "labels";

    @Parameter(names = { "-d", "--delimiter" }, description = "Delimiter for CSV input")
    private String delimiter = ",";

    @Parameter(names = { "--charset" }, description = "Input encoding", converter = CharsetConverter.class)
    private Charset charset = StandardCharsets.UTF_8;

    public boolean isShowUsage() {
        return showUsage;
    }

    public Path getInput() {
        return input;
    }

    public String getDelimiter() {
        return delimiter;
    }

    public Charset getCharset() {
        return charset;
    }

    public String getLabelColumn() {
        return labelColumn;
    }

    public Path getFilters() {
        return filters;
    }

    public Path getOutput() {
        return output;
    }
}
