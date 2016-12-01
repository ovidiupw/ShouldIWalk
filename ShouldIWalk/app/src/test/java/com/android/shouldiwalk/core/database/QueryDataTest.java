package com.android.shouldiwalk.core.database;

import org.junit.Before;
import org.junit.Test;

import static com.android.shouldiwalk.core.database.QueryArithmeticOperator.Equals;
import static com.android.shouldiwalk.core.database.QueryArithmeticOperator.Greater;
import static com.android.shouldiwalk.core.database.QueryArithmeticOperator.GreaterOrEqual;
import static com.android.shouldiwalk.core.database.QueryArithmeticOperator.Lower;
import static com.android.shouldiwalk.core.database.QueryArithmeticOperator.LowerOrEqual;
import static com.android.shouldiwalk.core.database.QueryArithmeticOperator.NotEquals;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class QueryDataTest {

    private QueryData queryData;

    @Before
    public void setUp() throws Exception {
        queryData = new QueryData()
                .addBinaryCondition("A", Equals, "B")
                .addBinaryCondition("A", NotEquals, "B")
                .addBinaryCondition("A", Greater, "B")
                .addBinaryCondition("A", GreaterOrEqual, "B")
                .addBinaryCondition("A", Lower, "B")
                .addBinaryCondition("A", LowerOrEqual, "B");
    }

    @Test
    public void when_noQueryConditionsAdded_then_buildQueryReturnsDefaultTrue() throws Exception {
        queryData = new QueryData();
        assertThat(queryData.buildQueryString(), is("1 = 1"));
    }

    @Test
    public void when_addingQueryConditions_then_gettingExpectedStringsBack() {
        assertThat(
                queryData.getQueryConditions(),
                contains(
                        "A = B",
                        "A != B",
                        "A > B",
                        "A >= B",
                        "A < B",
                        "A <= B"
                ));

    }

    @Test
    public void when_buildingQueryStringWithOr_then_gettingCorrectQueryString() {
        queryData.withLogicalOperationBetweenConditions(QueryLogicOperator.OR);
        assertThat(
                queryData.buildQueryString(),
                is("A = B" +
                        " OR A != B" +
                        " OR A > B" +
                        " OR A >= B" +
                        " OR A < B" +
                        " OR A <= B")
        );
    }

    @Test
    public void when_buildingQueryStringWithAnd_then_gettingCorrectQueryString() {
        queryData.withLogicalOperationBetweenConditions(QueryLogicOperator.AND);
        assertThat(
                queryData.buildQueryString(),
                is("A = B" +
                        " AND A != B" +
                        " AND A > B" +
                        " AND A >= B" +
                        " AND A < B" +
                        " AND A <= B")
        );
    }

    @Test
    public void when_getLogicalOperator_then_returnsOrByDefault() throws Exception {
        assertThat(queryData.getQueryLogicOperator(), is(QueryLogicOperator.OR));
    }

    @Test
    public void when_settingLogicalOperator_then_returnsSetLogicalOperator() throws Exception {
        queryData.withLogicalOperationBetweenConditions(QueryLogicOperator.AND);
        assertThat(queryData.getQueryLogicOperator(), is(QueryLogicOperator.AND));
    }
}