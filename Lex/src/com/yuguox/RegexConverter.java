package com.yuguox;

import java.util.Stack;
import java.util.LinkedList;
import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

/*
 * This class is to converter non-basic regular expression to basic
 * regular expression.
 */
class RegexConverter {
    private Stack<IndexedRegex> nonBasicStack  = new Stack<>();
    private LinkedList<IndexedRegex> basicList = new LinkedList<>();

    RegexConverter(IndexedRegex ir) {
        if(ir.isBasic())
            basicList.add(ir);
        else
            nonBasicStack.add(ir);
    }

    LinkedList<IndexedRegex> getBasicList() {

        while(!nonBasicStack.isEmpty()) {
            IndexedRegex ir = nonBasicStack.pop();
            char ch = ir.regex.charAt(ir.index);

            if(ch == '(')
                firstTypeConverter(ir);
            if(ch == ')')
                secondTypeConverter(ir);
            if(ch == '|')
                thirdTypeConverter(ir);
        }
        return basicList;
    }

    // Find all the first type non-basic regexes between a pair of parentetheses.
    private void firstTypeConverter(IndexedRegex ir) {

        // The next character could be '(' again.
        addIR(new IndexedRegex(ir.regex, ir.message, ir.index + 1));

        int rightParenthsisIndex = moveToRightParenthesis(ir.regex, ir.message,
                ir.index, false);

        if(rightParenthsisIndex < ir.regex.length() - 1) {
            char ch = ir.regex.charAt(rightParenthsisIndex + 1);

            // Only two cases here.
            if (ch == '*' | ch == '?') {

                // Next first type non-basic or basic index.
                int nextIndex = findFowardIndex(ir.regex, rightParenthsisIndex + 2);
                addIR(new IndexedRegex(ir.regex, ir.message, nextIndex));
            }
        }
    }

    private void secondTypeConverter(IndexedRegex ir) {
        String regex = ir.regex;
        int index = ir.index;
        String message = ir.message;
        char ch;

        if(isAccepted(ir.regex, ir.index)) {
            basicList.add(new IndexedRegex(regex, message, regex.length())); // Acepted.

            if(isFinalized(ir.regex, ir.index))
                return;
        }

        ch = regex.charAt(index + 1);

        if(ch == '(')
            addIR(new IndexedRegex(regex, message, index + 1));

        else if(ch == '|')
            addIR(new IndexedRegex(regex, message, moveToRightParenthesis(regex, message,
                    index + 1, true)));

        else if(ch == '*' | ch == '+')
            // Back to left parenthesis.
            addIR(new IndexedRegex(regex, message, moveToLeftParenthesis(regex, index)));

        else if(ch == '?')
            addIR(new IndexedRegex(regex, message, index + 2));
    }

    private void thirdTypeConverter(IndexedRegex ir) {

        int rightParentesisIndex = moveToRightParenthesis(ir.regex, ir.message,
                ir.index, true);

        if(!isFinalized(ir.regex, ir.index))
            addIR(new IndexedRegex(ir.regex, ir.message, rightParentesisIndex));
        else
            basicList.add(new IndexedRegex(ir.regex, ir.message, ir.regex.length()));
    }

    private int moveToLeftParenthesis(String regex, int index) {
        Stack<String> stack = new Stack<>();
        stack.push(")");

        while (!stack.isEmpty()) {

            index = index - 1;
            String stringAtIndex = Character.toString(regex.charAt(index));

            if (stringAtIndex.equals("("))
                stack.pop();

            else if (stringAtIndex.equals(")"))
                stack.push(stringAtIndex);
        }
        return index;
    }

    private int moveToRightParenthesis(String regex, String message, int index,
                                       boolean parenthesisSearchingOnly) {
        Stack<String> stack = new Stack<>();
        stack.push("(");

        while (!stack.isEmpty()) {
            index += 1;
            String stringAtIndex = Character.toString(regex.charAt(index));

            if (stringAtIndex.equals("("))
                stack.push(stringAtIndex);

            else if (stringAtIndex.equals(")"))
                stack.pop();

            else if (stringAtIndex.equals("|") && stack.size() == 1
                                               && !parenthesisSearchingOnly)
                addIR(new IndexedRegex(regex, message, index + 1));
        }
        return index;
    }

    private int findFowardIndex(String regex, int index) {

        if(index == regex.length())
            return index;

        char ch = regex.charAt(index);

        while(ch != '(' && !isLetter(ch) &&
                !isDigit(ch) && index < regex.length()) {
            index++;
        }
        return index;
    }

    private boolean isFinalized(String regex, int index) {
       return index == regex.length() - 1 |
              (index == regex.length() - 2 &&
              regex.charAt(index + 1) == '?');
    }

    private boolean isAccepted(String regex, int index) {

        for(int i = index + 1; i < regex.length(); i++) {
            if(isLetter(regex.charAt(i)) | isDigit(regex.charAt(i)))
                return false;
        }
        return true;
    }

    private void addIR(IndexedRegex ir) {
        IndexedRegex neoIR = new IndexedRegex(ir.regex, ir.message, ir.index);

        if(ir.index == ir.regex.length() || isLetter(ir.regex.charAt(ir.index))
                                         || isDigit(ir.regex.charAt(ir.index)))
            addBasicIR(neoIR);
        else
            nonBasicStack.push(neoIR);
    }

    private void addBasicIR(IndexedRegex ir) {
        if(!basicList.contains(ir))
            basicList.add(ir);
    }

}
