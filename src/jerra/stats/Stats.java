package jerra.stats;

import java.util.Map;
import java.util.HashMap;

import jerra.api.Copyable;

/**
 * Stats class that contains various types of stats
 */
public class Stats implements Copyable<Stats> {

    private Map<Type, Integer> values;

    public enum Type {
        HEALTH,
        VITALITY,
    }

    public Stats() {
        this.values = new HashMap<Type, Integer>(Type.values().length);
    }

    public Stats(Map<Type, Integer> values) {
        this.values = new HashMap<Type, Integer>(values);
    }

    public Stats(int health, int vitality) {
        this();
        this.setValue(Type.HEALTH, health);
        this.setValue(Type.VITALITY, vitality);
    }

    public Stats setValue(Type stat, int value) {
        this.values.put(stat, value);
        return this;
    }

    public int getValue(Type stat) {
        if (this.values.containsKey(stat)) {
            return this.values.get(stat);
        } else {
            return 0;
        }
    }

    @Override
    public Stats copy() {
        return new Stats(this.values);
    }
    
}