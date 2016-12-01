package com.android.shouldiwalk.core.database;

import android.content.Context;
import android.content.res.AssetManager;

import com.android.shouldiwalk.R;
import com.android.shouldiwalk.core.exceptions.BadXMLFormatException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

/**
 * TODO Add Documentation. Creation date: 5/7/2016.
 */
public final class SqlStatements {
    private static final String STATEMENT_TAG_NAME = "statement";
    private static final String STATEMENT_CATEGORY_ATTRIBUTE = "category";

    private static final String QUERY_ELEMENT_TAG_NAME = "query";
    private static final String QUERY_NAME_ATTRIBUTE = "name";

    private static Map<String, SqlQuery> testStatements;
    private static Map<String, SqlQuery> createStatements;
    private static Map<String, SqlQuery> displayStatements;
    private static Map<String, SqlQuery> removeStatements;
    private static Map<String, SqlQuery> updateStatements;

    private SqlStatements() {
        throw new AssertionError();
    }

    public static void updateXMLFileLocation(String fileLocation) throws FileNotFoundException {
        File file = new File(fileLocation);
        if (!file.exists()) {
            throw new FileNotFoundException("The file at location '" + fileLocation + "' was not found.");
        }
    }

    public static Map<String, SqlQuery> getAllQueriesFromCategory(
            SqlQuery.Type queryCategoryType, Context context)
            throws ParserConfigurationException, BadXMLFormatException, SAXException, IOException {

        InputStream queriesStream = DatabaseHelper.getDatabaseQueriesStream(context);
        return getAllQueriesFromCategory(queryCategoryType, queriesStream);
    }

    /**
     * If this is the first call of the function, it parses the xml file set by previously called
     * {@link SqlStatements#updateXMLFileLocation(String)} or located at the default location, at
     *
     * @param queryCategoryType The type of the query, based on the statement, for which to return the SqlQueries.
     * @return This function returns a map from the query name to the actual {@link SqlQuery} type
     * @throws ParserConfigurationException
     * @throws BadXMLFormatException
     * @throws SAXException
     * @throws IOException
     */
    public static Map<String, SqlQuery> getAllQueriesFromCategory(
            SqlQuery.Type queryCategoryType, InputStream fileInputStream)
            throws ParserConfigurationException, BadXMLFormatException, SAXException, IOException {

        initializeStatements(fileInputStream);

        switch (queryCategoryType) {
            case Test:
                return testStatements;
            case Create:
                return createStatements;
            case Display:
                return displayStatements;
            case Remove:
                return removeStatements;
            case Update:
                return updateStatements;
            default:
                throw new UnsupportedOperationException();
        }
    }

    private static void initializeStatements(InputStream fileInputStream)
            throws ParserConfigurationException, IOException, SAXException, BadXMLFormatException {

        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();

        Document sqlStatementsDocument = dBuilder.parse(fileInputStream);
        sqlStatementsDocument.getDocumentElement().normalize();

        NodeList statementNodes = sqlStatementsDocument.getElementsByTagName(STATEMENT_TAG_NAME);

        testStatements = new HashMap<>();
        createStatements = new HashMap<>();
        displayStatements = new HashMap<>();
        removeStatements = new HashMap<>();
        updateStatements = new HashMap<>();

        for (int nodeIndex = 0; nodeIndex < statementNodes.getLength(); ++nodeIndex) {
            Node statementNode = statementNodes.item(nodeIndex);

            if (statementNode.getNodeType() != Node.ELEMENT_NODE) {
                throw new BadXMLFormatException("The xml file format was unexpected.");
            }

            Element statementElement = (Element) statementNode;
            String statementCategory;

            switch (statementCategory = statementElement.getAttribute(STATEMENT_CATEGORY_ATTRIBUTE)) {
                case "test": {
                    populateWithStatements(statementElement, testStatements);
                    break;
                }

                case "create": {
                    populateWithStatements(statementElement, createStatements);
                    break;
                }
                case "display": {
                    populateWithStatements(statementElement, displayStatements);
                    break;
                }
                case "remove": {
                    populateWithStatements(statementElement, removeStatements);
                    break;
                }
                case "update": {
                    populateWithStatements(statementElement, updateStatements);
                    break;
                }
                default: {
                    System.out.println(
                            "Warning: Unrecognized category '"
                                    + statementCategory
                                    + "' while parsing statements xml.");
                }
            }
        }
    }

    private static void populateWithStatements(Element fromStatementElement, Map<String, SqlQuery> statements) {
        Node statementQueryNode = fromStatementElement.getElementsByTagName(QUERY_ELEMENT_TAG_NAME).item(0);

        statements.put(
                ((Element) statementQueryNode).getAttribute(QUERY_NAME_ATTRIBUTE),
                new SqlQuery(
                        ((Element) statementQueryNode).getAttribute(QUERY_NAME_ATTRIBUTE),
                        statementQueryNode.getTextContent()
                )
        );
    }
}