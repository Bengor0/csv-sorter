package cz.muni.fi.pb162.hw03;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.IntStream;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public final class TestUtils {

    private TestUtils() {
        // intentionally made private
    }

    /**
     * Not suitable for copy-paste programming. Intentionally memory inefficient.
     * <p>
     * If the same (or very similar) code is spotted in your solution,
     * you might be asked to explain it in details by jcechace!
     *
     * @return parsed data
     */
    public static List<Map<String, String>> parse(Path file, String delimiter, Charset charset) throws IOException {
        var lines = Files.readAllLines(file, charset)
                .stream()
                .map(l -> Arrays.stream(l.split(delimiter)).map(String::strip).collect(toList()))
                .collect(toList());

        assert lines.size() > 1;

        var header = lines.get(0);
        var data = lines.subList(1, lines.size());

        return data.stream()
                .map(entry -> IntStream.range(0, header.size()).boxed().collect(toMap(header::get, entry::get)))
                .collect(toList());

    }

    /**
     * Lists content of directory
     *
     * @param path directory path
     * @throws IOException on any IO Error
     *
     * @return map of directory content in name:path format
     */
    public static  Map<String, Path> listDir(Path path) throws IOException {
        assert Files.isDirectory(path);

        try (var stream = Files.list(path)) {
            return stream.collect(toMap(f -> f.getFileName().toString(), Function.identity()));
        }
    }

    /**
     * Creates {@link Path} for classpath resource
     *
     * @param name absolute path name of classpath resource
     * @throws URISyntaxException on invalid path
     *
     * @return path object for class path resource
     */
    public static Path resource(String name) throws URISyntaxException {
        var resource = TestUtils.class.getResource(name);
        Objects.requireNonNull(resource, "Resource '" + name + "' not found");

        return Paths.get(resource.toURI());
    }

    /**
     * Converts path to absolute path
     * @param path path object
     *
     * @return absolute path as string
     */
    public static String absolutePath(Path path) {
        return path.toAbsolutePath().toString();
    }

}
