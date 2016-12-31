package core.database;

import android.content.res.AssetManager;

import com.android.shouldiwalk.BuildConfig;
import com.android.shouldiwalk.activities.MainActivity;
import com.android.shouldiwalk.core.database.SqlQuery;
import com.android.shouldiwalk.core.database.SqlStatements;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.annotation.Config;

import java.io.InputStream;
import java.util.Map;

import static com.android.shouldiwalk.R.string.dbQueriesResource;
import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@RunWith(RobolectricTestRunner.class)
@Config(constants = BuildConfig.class)
public class SqlStatementsTest {

    private MainActivity mainActivity;

    @Before
    public void setUp() throws Exception {
        mainActivity = Robolectric.setupActivity(MainActivity.class);
    }

    @Test
    public void when_whenRequestingTestQueries_then_parsesXMLCorrectly() throws Exception {
        AssetManager mainActivityAssets = mainActivity.getResources().getAssets();
        InputStream queriesInput = mainActivityAssets.open(mainActivity.getString(dbQueriesResource));

        Map<String, SqlQuery> testQueries
                = SqlStatements.getAllQueriesFromCategory(SqlQuery.Type.Test, queriesInput);

        assertThat(
                testQueries.get("dummy_select").getName(),
                is(equalTo("dummy_select")));
        assertThat(
                testQueries.get("dummy_select").getQueryString(),
                containsString("[dummy] select 1 from dual"));

        assertThat(
                testQueries.get("dummy_create_account").getName(),
                is(equalTo("dummy_create_account")));
        assertThat(
                testQueries.get("dummy_create_account").getQueryString(),
                containsString("[dummy] {? = call pl_records_insert.create_account(?, ?, ?)}"));
    }
}