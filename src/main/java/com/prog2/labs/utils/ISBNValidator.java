package com.prog2.labs.utils;

/**
 * The ISBN Validator class.
 *   Validate ISBN10 & ISBN13 formats
 *
 * @author <a href="mailto:Chailikeg@yahoo.com">Olena Chailik</a>
 */
public class ISBNValidator {

    /**
     * Method check
     *  Validate ISBN10 & ISBN13 formats
     *  
     * @param isbn
     * @return boolean (valid | not valid)
     */
    public static boolean check(String isbn) {
        boolean checkResult = false;
        isbn = prepareISBN(isbn);
        if (isbn.length() == 10) {
            checkResult = checkISBN10(isbn);
        } else if (isbn.length() == 13) {
            checkResult = checkISBN13(isbn);
        }

        return checkResult;
    }

    /**
     * Method checkISBN10
     *  Validate only ISBN10 formats
     *  
     * @param isbn
     * @return boolean (valid | not valid)
     */
    public static boolean checkISBN10(String isbn) {
        isbn = prepareISBN(isbn);
        int sum = 0;

        for(int i = 0; i < 10; ++i) {
            String vStr = isbn.substring(i, i + 1);
            if (i >= 9 && vStr == "X") {
                sum += 10;
            } else {
                sum += Integer.parseInt(vStr) * (10 - i);
            }
        }

        return sum % 11 == 0;
    }

    /**
     * Method checkISBN13
     *  Validate only ISBN13 formats
     *  
     * @param isbn
     * @return boolean (valid | not valid)
     */
    public static boolean checkISBN13(String isbn) {
        isbn = prepareISBN(isbn);
        int sum = 0;

        for(int i = 0; i < 13; ++i) {
            int dVal = Integer.parseInt(isbn.substring(i, i + 1));
            if (i % 2 == 0) {
                sum += dVal * 1;
            } else {
                sum += dVal * 3;
            }
        }

        return sum % 10 == 0;
    }

    /**
     * Method prepareISBN
     *  Delete all delimiters 
     *  
     * @param isbn
     * @return isbn
     */
    private static String prepareISBN(String isbn) {
        return isbn.replaceAll("-", "");
    }
}
