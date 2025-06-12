package cz.muni.fi.pb162.hw03.impl;

import cz.muni.fi.pb162.hw03.cmd.CommandLine;
import cz.muni.fi.pb162.hw03.cmd.CommandLineException;

/**
 * Note:    While it is possible to modify this class,
 *          it is unlikely that one would need to do so.
 *
 *
 * CLI bootstrapping class for Application
 */
public class ApplicationRunner {
    /**
     * Application entry point
     *
     * @param args command line arguments of the application
     */
    public static void main(String[] args) {
        var options = new ApplicationOptions();
        var cli = new CommandLine(Application.class.getSimpleName(), options);

        try {
            cli.parseArguments(args);

            if (options.isShowUsage()) {
                cli.printUsage();
                return;
            }

            var application = new Application(options, cli);

            application.run();

        } catch (CommandLineException ex) {
            System.err.println("Error: " + ex.getMessage());
            cli.printUsage();
        } catch (Exception ex) {
            System.err.println("Error: " + ex.getMessage());
        }
    }
}
