package com.android.shouldiwalk.core.database;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class QueryData {
    private List<String> queryConditions = new ArrayList<>();
    private QueryLogicOperator queryLogicOperator = QueryLogicOperator.OR;

    public static QueryData ALL_ITEMS() {
        return new QueryData();
    }

    public List<String> getQueryConditions() {
        return queryConditions;
    }

    public QueryLogicOperator getQueryLogicOperator() {
        return queryLogicOperator;
    }

    public String buildQueryString() {
        StringBuilder queryStringBuilder = new StringBuilder();
        for (int i = 0; i < queryConditions.size() - 1; i++) {
            queryStringBuilder.append(queryConditions.get(i));
            queryStringBuilder.append(" ");
            queryStringBuilder.append(queryLogicOperator.name());
            queryStringBuilder.append(" ");
        }
        if (queryConditions.size() > 0) {
            queryStringBuilder.append(queryConditions.get(queryConditions.size() - 1));
        } else {
            queryStringBuilder.append("1 = 1");
        }
        return queryStringBuilder.toString();
    }

    public QueryData withLogicalOperationBetweenConditions(QueryLogicOperator queryLogicOperator) {
        this.queryLogicOperator = queryLogicOperator;
        return this;
    }

    public QueryData addBinaryCondition(String o1, QueryArithmeticOperator operator, String o2) {
        switch (operator) {
            case Equals: {
                queryConditions.add(o1 + " = " + o2);
                break;
            }
            case NotEquals: {
                queryConditions.add(o1 + " != " + o2);
                break;
            }
            case LowerOrEqual: {
                queryConditions.add(o1 + " <= " + o2);
                break;
            }
            case Lower: {
                queryConditions.add(o1 + " < " + o2);
                break;
            }
            case GreaterOrEqual: {
                queryConditions.add(o1 + " >= " + o2);
                break;
            }
            case Greater: {
                queryConditions.add(o1 + " > " + o2);
                break;
            }
        }

        return this;
    }

    @Override
    public String toString() {
        return "QueryData{" +
                "queryConditions=" + queryConditions +
                ", queryLogicOperator=" + queryLogicOperator +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        QueryData queryData = (QueryData) o;
        return Objects.equals(queryConditions, queryData.queryConditions) &&
                queryLogicOperator == queryData.queryLogicOperator;
    }

    @Override
    public int hashCode() {
        return Objects.hash(queryConditions, queryLogicOperator);
    }


}
