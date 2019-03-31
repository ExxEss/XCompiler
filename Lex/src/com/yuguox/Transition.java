package com.yuguox;

class Transition {
    State state;
    State[] mapping;

    Transition(State state, int size) {
        this.state = state;
        this.mapping = new State[size];
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
            Transition neoTransition = (Transition) unObjeto;
            return this.state == neoTransition.state && compareArray(this.mapping, neoTransition.mapping);
        }
        return false;
    }

    private boolean compareArray(State[] firstArray, State[] secondArray) {

        for(int i = 0; i < firstArray.length; i++) {

            if(firstArray[i] != secondArray[i])
                return false;
        }
        return true;
    }
}
