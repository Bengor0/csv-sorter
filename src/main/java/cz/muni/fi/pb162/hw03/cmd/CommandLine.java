package cz.muni.fi.pb162.hw03.cmd;

import com.beust.jcommander.JCommander;
import com.beust.jcommander.ParameterException;

/**
 * Command Line Interface
 */
public final class CommandLine {

    private final JCommander commander;

    /**
     * Constructs new CLI instance
     *
     * @param programName name of the program
     * @param application application object to be populated with arguments
     */
    public CommandLine(String programName, Object application) {
        commander = JCommander.newBuilder()
                .addObject(application)
                .build();

        commander.setProgramName(programName);
    }

    /**
     * Parses command line arguments (terminates on error)
     *
     * @param args command line arguments of the application
     */
    public void parseArguments(String[] args) {
        try {
            commander.parse(args);
        } catch (ParameterException e) {
            throw new CommandLineException(e.getMessage(), e);
        }
    }

    /**
     * Shows usage of the application
     */
    public void printUsage() {
        commander.usage();
    }
}
