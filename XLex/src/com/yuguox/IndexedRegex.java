package com.yuguox;

import static java.lang.Character.isDigit;
import static java.lang.Character.isLetter;

class IndexedRegex {
    String regex;
    String message;
    int index;

    IndexedRegex(String regex, String message, int index) {
        RegexChecker rc = new RegexChecker(regex);

        if(rc.isCorrectRegex()) {
            this.regex = regex;
            this.message = message;
            this.index = index;
        } else {
            System.err.println(regex + " is a ill-formed regular expresion.");
        }
    }

    boolean isBasic() { return regex.length() == index || isLetter(regex.charAt(index)) || isDigit(regex.charAt(index)); }

    @Override
    public boolean equals(Object unObjeto) {
        if (this == unObjeto)
            return true;

        if (unObjeto == null)
            return false;

        if (getClass() != unObjeto.getClass())
            return false;

        if (unObjeto instanceof IndexedRegex) {
            IndexedRegex newIR = (IndexedRegex) unObjeto;

            return this.regex.equals(newIR.regex)     &&
                   this.message.equals(newIR.message) &&
                   this.index == newIR.index;
        }
        return false;
    }

}
