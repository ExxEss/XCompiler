package com.yuguox;

import java.util.LinkedList;

class State {
    LinkedList<IndexedRegex> regexes;

    State(LinkedList<IndexedRegex> regexes) {
        this.regexes = regexes;
    }

    @Override
    public boolean equals(Object unObjeto) {
        if (this == unObjeto)
            return true;

        if (unObjeto == null)
            return false;

        if (getClass() != unObjeto.getClass())
            return false;

        if (unObjeto instanceof State) {
            State state = (State) unObjeto;
            return compareStateList(this.regexes, state.regexes);
        }
        return false;
    }

    private boolean compareStateList(LinkedList<IndexedRegex> first,
                                     LinkedList<IndexedRegex> second) {
        for(IndexedRegex ir : first) {
            if(!second.contains(ir))
                return false;
        }

        for(IndexedRegex ir : second) {
            if(!first.contains(ir))
                return false;
        }
        return true;
    }

}


