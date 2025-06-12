package cz.muni.fi.pb162.hw03;

import com.github.stefanbirkner.systemlambda.SystemLambda;
import cz.muni.fi.pb162.hw03.impl.ApplicationRunner;
import org.assertj.core.api.SoftAssertions;
import org.assertj.core.api.junit.jupiter.InjectSoftAssertions;
import org.assertj.core.api.junit.jupiter.SoftAssertionsExtension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static cz.muni.fi.pb162.hw03.TestUtils.absolutePath;
import static cz.muni.fi.pb162.hw03.TestUtils.listDir;
import static cz.muni.fi.pb162.hw03.TestUtils.parse;
import static cz.muni.fi.pb162.hw03.TestUtils.resource;
import static java.nio.charset.StandardCharsets.UTF_8;

@ExtendWith(SoftAssertionsExtension.class)
public class ApplicationTest {

    @InjectSoftAssertions
    protected SoftAssertions softly;

    @Test
    public void sortLargeCsvFile(@TempDir Path out) throws Exception {
        var example = resource("/040");
        var data = example.resolve("data.csv");
        var filters = example.resolve("filters.csv");

        var stdAndErrOut = runApp(data, filters, out, "-c", "Labels");

        var outFiles = listDir(out);

        softly.assertThat(stdAndErrOut).isEmpty();
        softly.assertThat(outFiles).hasSize(3);
    }

    @Test
    public void sortsCsvWithDefaultLabelColumn(@TempDir Path out) throws Exception {
        verifyApplicationRun(resource("/010"), out, ",", UTF_8);
    }

    @Test
    public void sortsCsvWithCustomLabelColumnAndDelimiter(@TempDir Path out) throws Exception {
        var args = List.of("-c", "categories");
        verifyApplicationRun(resource("/020"), out, ";", UTF_8, args);
    }

    @Test
    public void sortsCsvWithISO88592(@TempDir Path out) throws Exception {
        verifyApplicationRun(resource("/030"), out, ",", Charset.forName("ISO-8859-2"));
    }

    public void verifyApplicationRun(Path example, Path output, String delimiter, Charset charset) throws Exception {
        verifyApplicationRun(example, output, delimiter, charset, List.of());
    }

    public void verifyApplicationRun(Path example, Path output, String delimiter, Charset charset,
                                     List<String> moreArgs) throws Exception {
        var data = example.resolve("data.csv");
        var filters = example.resolve("filters.csv");
        var exampleOut = example.resolve("out");

        var args = new ArrayList<>(moreArgs);

        if (!UTF_8.equals(charset)) {
            args.addAll(List.of("--charset", charset.name()));
        }
        if (!",".equals(delimiter)) {
            args.addAll(List.of("-d", delimiter));
        }

        var stdAndErrOut = runApp(data, filters, output, args.toArray(new String[0]));
        var actual = listDir(output);
        var expected = listDir(exampleOut);

        softly.assertThat(stdAndErrOut).isEmpty();
        softly.assertThat(actual.keySet()).hasSameElementsAs(expected.keySet());

        for (var file : expected.keySet()) {
            assertLines(expected.get(file), actual.get(file), charset, delimiter);
        }
    }

    protected void assertLines(Path expectedFile, Path actualFile, Charset charset, String delimiter) throws Exception {
        var expected = TestUtils.parse(expectedFile, delimiter, charset);
        var actual = TestUtils.parse(actualFile, delimiter, charset);

        softly.assertThat(actual).hasSameElementsAs(expected);
    }

    protected String runApp(Path data, Path filters, Path output, String... args) throws Exception {
        var baseArgs = List.of("-i", absolutePath(data), "-f", absolutePath(filters), "-o", absolutePath(output));
        var cmdArgs = Stream.concat(baseArgs.stream(), Stream.of(args)).toArray(String[]::new);
        return runApp(cmdArgs);
    }

    protected String runApp(String... args) throws Exception {
        return SystemLambda.tapSystemErrAndOut(() -> ApplicationRunner.main(args));
    }
}