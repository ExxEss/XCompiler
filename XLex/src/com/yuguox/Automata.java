package com.yuguox;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.Queue;
/*
 *  @author ExxEss
 */
class Automata {
    private static final int MAXCHAR = 62; // Just digits and letters.
    private ArrayList<State> states;       // Close list for all states.
    private Queue<State> stateQueue;       // Queue for new generated states.
    private State inicialState;
    private ArrayList<Transition> transitionTable;

    Automata(LinkedList<IndexedRegex> regexes) {
        this.inicialState = iniciateAutomata(regexes);

        if(this.inicialState == null)
            return;

        states = new ArrayList<>();
        states.add(this.inicialState);

        stateQueue = new LinkedList<>();
        stateQueue.offer(this.inicialState);

        transitionTable  = new ArrayList<>();

        generateTransitionTable();
    }

    String matchInput(String str) {
        State state = this.inicialState;
        String output = "Input string can't be matched. :(";

        for(int i = 0; i < str.length(); i++) {
            if(state != null)
                state = moveToNextState(state, str.charAt(i));
            else
                return output;
        }

        if(state == null)
            return output;

        for(IndexedRegex ir : state.regexes) {

            if(ir.regex.length() == ir.index)
                return ir.message;
        }
        return output;
    }

    private State moveToNextState(State state, char ch) {

        // Return the next state from a transition.
        return getTransition(state).mapping[charMapToInt(ch)];
    }

    private Transition getTransition(State state) {

        for(Transition t : transitionTable) {
            if(t.state.equals(state))
                return t;
        }
        return null;
    }

    // Obtain the first state with all basic regular expressions.
    private State iniciateAutomata(LinkedList<IndexedRegex> regexes) {
        LinkedList<IndexedRegex> newRegexes = new LinkedList<>();

        for(IndexedRegex ir : regexes) {
            if(ir.regex == null)
                return null;

            RegexChecker rc = new RegexChecker(ir.regex);

            if(!rc.isCorrectRegex())
                return null;

            RegexConverter rconv = new RegexConverter(ir);
            mergeList(newRegexes, rconv.getBasicList());
        }
        return new State(newRegexes);
    }

    private void generateTransitionTable() {

        while(!stateQueue.isEmpty()) {
            State state = stateQueue.poll();
            transitionTable.add(transit(state));
        }
    }

    private Transition transit(State state) {
        Transition transition = new Transition(state, MAXCHAR);

        for(int i = 0; i < MAXCHAR; i++) {
            State neoState = goTo(state, intMapToChar(i));

            if(neoState != null) {

                if(!states.contains(neoState)) {
                    states.add(neoState);
                    stateQueue.offer(neoState);
                }
                transition.mapping[i] = neoState;
            }
        }
        return transition;
    }

    private State goTo(State state, char ch) {
        LinkedList<IndexedRegex> newRegexes = new LinkedList<>();

        for(IndexedRegex ir : state.regexes) {

            if(ir.index < ir.regex.length() && ir.regex.charAt(ir.index) == ch){

                IndexedRegex newIR = new IndexedRegex(ir.regex, ir.message, ir.index + 1);
                RegexConverter rconv = new RegexConverter(newIR);

                mergeList(newRegexes, rconv.getBasicList());
            }
        }
        if(newRegexes.size() == 0)
            return null;
        return new State(newRegexes);
    }

    private void mergeList(LinkedList<IndexedRegex> firstList,
                           LinkedList<IndexedRegex> secondList) {
        for(IndexedRegex ir : secondList) {

            if(!firstList.contains(ir))
                firstList.add(ir);
        }
    }

    private char intMapToChar(int i) {
        if(-1 < i && i < 10)
            return (char)(48 + i);

        if(9 < i && i < 36)
            return (char)(55 + i);

        if(35 < i && i < 62)
            return (char)(61 + i);

        return '\0';
    }

    private int charMapToInt(char ch) {
        if(47 < ch && ch < 58)
            return ch - 48;

        if(64 < ch && ch < 91)
            return ch - 55;

        if(96 < ch && ch < 123)
            return ch - 61;

        return -1;
    }
}
