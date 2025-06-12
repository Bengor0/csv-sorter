Homework Assignment no. 3, Labeled CSV Data Sorter 
====================================

**Publication date:**  May 4th, 2022  
**Submission deadline:** May 20th, 2022  
**Points:** 15 points in total (7 points for passing tests, 3 points for passing tests with the ``pedantic`` profile, 5 points for a clean implementation)


Change Log
-----------

General Information
-------------------
**Note:** Unlike previous assignments, this time you may modify ``pom.xml`` by adding dependencies.
**Note:** Read the Compiling section of this README carefully as there is an additional test profile.

The goal of this homework is to implement a simple application capable of sorting labeled data from a CSV file into a set of new CSV files in the output directory.

The application should support the following command line options.

| CLI option (long) | CLI option (short)   | Arguments | Default   | Required  | Description                              |
| ------            | ------                | ------    | ------    | ------    |------------------------------------------|
| --help            |                      |           |           | false     | Print application usage                  |
| --input           | -i                   | String    |           | true      | Path of the data CSV file                |   
| --output          | -o                   | String    |           | true      | Path of the output directory             |
| --filters         | -f                   | String    |           | true      | Path of the filter CSV file              |
| --column          | -c                   | String    | labels    | false     | Name of the column with labels           |
| --delimiter       | -d                   | String    | ,         | false     | Delimiter used by CSV files              |
| --charset         |                      | String    | UTF-8     | false     | Charset used by input (and output) files |

### Input Format
The application will be able to work with any ``input`` CSV file compliant with the following conditions

- First line in the file is a header (columns are named)  
- File has a column with space-separated labels.

The following is an example of an ``input`` CSV file

```csv
name,date_published,hits,labels
Australian Fauna,11.8.2021,123,snakes spiders animals australia danger
Danger Noodles,9.1.2022,42,snakes anaconda viper
In the Skies,8.4.1994,1432,ravens birds
Too Lazy, 9.11.2019, 5322,pandas bamboo endangered fluffy
Man's Best Friend, 13.4.2014,943,animals dogs people history
```

Beside the ``input`` CSV file, the application also requires a CSV file with ``filters``.  
The schema for the ``filters`` CSV file can be seen in the following example

```csv
name,expression
birds,birds | parrots | ravens
eggs,turtles | snakes | birds
social,society | people & animals
```
The first column (``name``) is the name of the filter. This value is also used as the name of the output CSV file (e.g.,``eggs.csv``).    
The second column (``expression``) is a logical expression as defined in [previous homework](https://gitlab.fi.muni.cz/pb162/2022-hw02-labels)

### Project Structure

The structure of the project provided as a base for your implementation should meet the following criteria.

1. Package ```cz.muni.fi.pb162.hw03``` contains classes and interfaces provided as a part of the assignment.

- **Do not modify or add any classes or subpackages into this package.**

2. Package  ```cz.muni.fi.pb162.hw03.impl``` should contain your implementation.

- **Anything outside this package will be ignored during the evaluation (unless stated otherwise).**

### Names in this Document

Unless fully classified name is provided, all class names are relative to the package ```cz.muni.fi.pb162.hw03``` or ```cz.muni.fi.pb162.hw03.impl``` for classes
implemented as a part of your solution.

### Compiling the Project

The project can be compiled and packaged in the same way you already know.

```bash

$ mvn clean install
```

The only difference is that unlike the seminar project, this time the checks for missing documentation and style violation will produce an error. You can temporarily
disable this behavior when running this command.

```bash
$ mvn clean install -Dcheckstyle.skip=true
```

You can ask your seminar teacher to help you set the ``checkstyle.skip`` property in your IDE (or just google it).

#### Testing for Efficient Implementation
This time there is an additional profile defined in the build descriptor called ``pedantic``.  
This profile severely limits the maximum available memory for JVM. However, with efficient implementation your tests should pass even when running this profile. 

To compile the project using the ``pedantic`` profile run the following command.

```bash
$ mvn clean install -Ppedantic
```

### Gitlab CI Pipeline
In order for the CI pipeline to function correctly, you need to have a fork of ``"2022-hw02-labels`` in your Gitlab namespace.  
However, such fork should already be in place, provided you have followed the standard process when working on your previous assignment.

### Submitting the Assignment
The procedure to submit your solution may differ based on your seminar group. However, it should be generally OK to
submit ```target/homework03-2022-1.0-SNAPSHOT-sources.jar``` to the homework vault.

### Running the Application
The build descriptor is configured to produce a single runnable jar file located at ``target/application.jar``. The application can be run using the following command

```bash
# Sort data.csv based on filters.csv into /tmp/output directory
$ java -jar application.jar -i data.csv -c labels -f filters.csv -o /tmp/output 

# Listing contents of /tmp/output (assuming data.csv and filters.csv as shown above)
$ ls /tmp/output
birds.csv eggs.csv social.csv

# Show contents of eggs.csv
$ cat /tmp/output/eggs.csv
name,date_published,hits,labels
Australian Fauna,11.8.2021,123,snakes spiders animals australia danger
Danger Noodles,9.1.2022,42,snakes anaconda viper
In the Skies,8.4.1994,1432,ravens birds
```

Implementation Notes
----
Generally speaking, there are no mandatory requirements for the structure of your code as long as the command line interface works correctly.
The use of classes, enums, and interfaces provided as a part of the project skeleton is up to your decision.

### External Dependencies
There are two areas where you may want to utilise external dependencies to save yourself some work. 

- Evaluation of logical label expressions
- Reading of CSV files

The first point could be covered quite nicely by using the library from [previous homework](https://gitlab.fi.muni.cz/pb162/2022-hw02-labels).  
If you inspect the ``<dependencies>`` element in ``pom.xml``, you will see there is a marked section where you can add the dependency on ``homework02-2022``.  
To add the dependency, put the following inside the ``<dependencies>`` element (in the marked section).

```xml
<dependency>
    <groupId>cz.muni.fi.pb162</groupId>
    <artifactId>homework02-2022</artifactId>
    <version>1.0-SNAPSHOT</version>
</dependency>
```

**Note:** A side effect of using ``homework02-2022`` as a dependency is that you need to build HW02 prior to building this assignment (since your implementation of ``homework02-2022`` is not publicly available to maven).   
**Note:** If you have troubles with declaring the dependency, you can always copy the desired classes over to this project. However, such approach is not usable in real-world projects. 

For the second point (Reading of CSV Files) you can use [Simple CSV Reader](https://github.com/jcechace/pb162-csv-parser) or write the code by yourself. 
If you decide to use this dependency, all information is available via the aforementioned link (including dependency information and javadocs).

### Efficient Implementation
When working with CSV files, keep in mind that the data files can be quite large. On the other hand, you can assume that the file with filters will have a reasonable number of entries.
You can check that your implementation is efficient by running the tests with ``pedantic`` maven profile (see the Compilation section of this README).






