# Logging

Logging tool provides you a transparent testing environment. It records your testing code, its actual modified error message and its original error message.

## Usage

1. Copy the loggings folder to your src directory with babel (logging tool needs to work with babel!).
2. Add ``[hiccup "1.0.5"]`` into your ``project.clj`` dependencies. Hiccup is required and used for auto-generating html file.
3. Include ``(:use [loggings.loggingtool :only [get-error start-log add-log]])`` in each of your testing file's namespace.
4. Call ``(start-log)`` in the first testing file as the very first function.
5. Call ``(expect nil (add-log
              (do
                (def file-name "this file")
                (:file (meta #'file-name)))))``
   at the beginning of each testing file to get the tested file names.
6. After running the test, a log folder will be generated in you project. Open the log_category.html to check the logs.
7. There is also a txt version log which records the latest test result.

## Logging contents

![This is the logging screen shot](/doc/logging.png)

## Trouble shooting

1. If you delete a log in the /log/history folder, it's name will still remain in the log_category until next time you run the test.
2. If the log file shows "Error loading test data!!!", it is likely that your test is broken.