package edu.northeastern.team31project;

import android.provider.BaseColumns;

public final class ExerciseContract {

    // To prevent someone from accidentally instantiating the contract class,
    // make the constructor private.
    private ExerciseContract() {}

    /* Inner class that defines the table contents */
    public static class ExerciseEntry implements BaseColumns {
        public static final String TABLE_NAME = "entry";
        public static final String COLUMN_NAME_TITLE = "title";
        public static final String COLUMN_NAME_SUBTITLE = "subtitle";
    }
}
