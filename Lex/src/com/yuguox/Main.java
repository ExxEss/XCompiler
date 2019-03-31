package com.yuguox;

/*
 * @Author: EssExx
 * @Start date: 01/09/2018
 * @Version: 0.1
 */

import java.util.LinkedList;

import static java.lang.System.out;

public class Main {

    public static void main(String[] args) {
        String letters = "(a|A|b|B|c|C|d|D|e|E|f|F|g|" +
                         "G|h|H|i|I|j|J|k|K|l|L|m|M|" +
                         "n|N|o|O|p|P|q|Q|r|R|s|S|t|" +
                         "T|u|U|v|V|w|W|x|X|y|Y|z|Z)";

        String digits = "(0|1|2|3|4|5|6|7|8|9)";

        String word   = letters + "+";
        String number = digits + "+";
        String id     = letters  + "(" + "(" + letters  + "|" + digits + ")" + "*" + ")" + "?";

        String msg1 = " is a word.";
        String msg2 = " is a number.";
        String msg3 = " is an id.";

        IndexedRegex ir1 = new IndexedRegex(word, msg1, 0);
        IndexedRegex ir2 = new IndexedRegex(number, msg2, 0);
        IndexedRegex ir3 = new IndexedRegex(id, msg3, 0);

        LinkedList<IndexedRegex> regexes = new LinkedList<>();

        regexes.add(ir1);
        regexes.add(ir2);
        regexes.add(ir3);

        Automata automata = new Automata(regexes);

        String inputStr1 = "hello";
        String inputStr2 = "2018";
        String inputStr3 = "str1";

        out.println(inputStr1 + automata.matchInput("hello"));
        out.println(inputStr2 + automata.matchInput("2018"));
        out.println(inputStr3 + automata.matchInput("str1"));

    }
}
