# scrambler

## Task 1
Complete the function (scramble str1 str2) that returns true if a portion of str1 characters can be rearranged to match str2, otherwise returns false.
### Notes:
Only lower case letters will be used (a-z). No punctuation or digits will be included.
Performance needs to be considered
### Examples:
(scramble? “rekqodlw” ”world') ==> true

(scramble? “cedewaraaossoqqyt” ”codewars”) ==> true

(scramble? “katas”  “steak”) ==> false

## Task 2
Create a web service that accepts two strings in a request and applies function scramble? from previous task to them.

## Task 3
Create a UI in ClojureScript with two inputs for strings and a scramble button. When the button is fired it should call the API from previous task and display a result.
### Notes
Please pay attention to tests, code readability and error cases.

## Prerequisites

You will need [Leiningen][1] 2.0 or above installed.

[1]: https://github.com/technomancy/leiningen

## Running in dev

To start a web server for the application, run in separate terminals:

    lein run 
    lein figwheel
    
To run tests, execute:

    lein test

## Running in prod

To compile for production:

    lein uberjar

The resulting jar can be found in the target/uberjar folder.
It can be run as follows:

    java -jar scrambler.jar

Running application will be available at http://localhost:3000/ on your machine.

Demo application is available at http://flexiana.erakhtin.ru

![scrambler](/scrambler.png)
