package com.android.shouldiwalk.core.database;

/**
 * TODO Add Documentation. Creation date: 5/7/2016.
 */
public class SqlQuery {
    public enum Type {
        Test, Display, Remove, Update, Create
    }

    private final String name;
    private final String content;

    public SqlQuery(String name, String content) {
        this.name = name;
        this.content = content;
    }

    public String getName() {
        return name;
    }

    public String getQueryString() {
        return content;
    }
}