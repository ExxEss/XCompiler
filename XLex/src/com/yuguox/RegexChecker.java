package com.yuguox;

import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

/*
 * This class is to verify an input regular expression is well-formed or not.
 */
class RegexChecker {

    private String regex;
    private static final List<String> operatorList  = Arrays.asList("*", "+", "?", "|");
    private static final List<String> delimaterList = Arrays.asList("(", ")");

    RegexChecker(String regex) { this.regex = regex; }

    boolean isCorrectRegex() {
        return wellFormedChars()         && isFirstCharCorrect() &&
               wellCombinatedOperators() && wellFormedParentesis();
    }

    private boolean wellFormedChars() {

        for(int i = 0; i < regex.length(); i++) {
            char ch = regex.charAt(i);

            if(!operatorList.contains(Character.toString(ch)) &&
               !delimaterList.contains(Character.toString(ch)) &&
               !isLetter(ch) && !isDigit(ch))

                return false;
        }
        return true;
    }

    private boolean isFirstCharCorrect() {
        char ch = regex.charAt(0);

        return isLetter(ch) | isDigit(ch)| ch == '(';
    }

    private boolean isEmptyParentesisPair(int begin, int end) {

        for(int i = begin; i < end + 1; i++) {
            if(isLetter(regex.charAt(i)) || isDigit(regex.charAt(i)))
                return false;
        }
        return true;
    }

    private boolean wellFormedParentesis() {
        Stack<Integer> stack = new Stack<>();

        for(int i = 0; i < regex.length(); i++) {

            if(regex.charAt(i) == '(')
                stack.add(i);

            else if(regex.charAt(i) == ')') {
                if(stack.isEmpty())
                    return false;

                if(isEmptyParentesisPair(stack.pop(), i))
                    return false;
            }
        }

        if(stack.isEmpty())
            return true;

        return false;
    }

    private boolean isOperator(char ch) { return operatorList.contains(Character.toString(ch)); }

    private boolean letterAndOperators(String str) {

        return isLetter(str.charAt(0)) &&
               isOperator(str.charAt(1)) &&
               !isSeparator(str.charAt(1));
    }

    private boolean isSeparator(char ch) {return ch == '|';}

    private boolean wellCombinatedOperators() {
        List<String> illCombinatedOperators = Arrays.asList
                ("(*", "(+", "(?",
                 "(|", "**", "*+",
                 "*?", "++", "+*",
                 "+?", "?*", "?+",
                 "??", "|*", "|+",
                 "|?", "||");

        for(int i = 0; i < regex.length() - 1; i++) {
            String str = regex.substring(i, i + 2);

            if(illCombinatedOperators.contains(str) | letterAndOperators(str))
                return false;
        }
        return true;
    }
}
