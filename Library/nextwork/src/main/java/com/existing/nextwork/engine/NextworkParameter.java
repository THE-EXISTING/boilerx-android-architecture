package com.existing.nextwork.engine;

/**
 * Created by「 The Khaeng 」on 05 Feb 2018 :)
 */

public abstract class NextworkParameter {
    private boolean isForceFetch = false;

    public NextworkParameter(boolean isForceFetch ){
        this.isForceFetch = isForceFetch;
    }

    public boolean isForceFetch(){
        return isForceFetch;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        NextworkParameter trigger = (NextworkParameter) o;

        return isForceFetch == trigger.isForceFetch;
    }

    @Override
    public int hashCode() {
        return (isForceFetch ? 1 : 0);
    }

}
