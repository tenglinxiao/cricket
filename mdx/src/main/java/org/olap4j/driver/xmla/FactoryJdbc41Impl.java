/*
// Licensed to Julian Hyde under one or more contributor license
// agreements. See the NOTICE file distributed with this work for
// additional information regarding copyright ownership.
//
// Julian Hyde licenses this file to you under the Apache License,
// Version 2.0 (the "License"); you may not use this file except in
// compliance with the License. You may obtain a copy of the License at:
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
*/
package org.olap4j.driver.xmla;

import org.olap4j.OlapException;
import org.olap4j.driver.xmla.proxy.XmlaOlap4jProxy;

import java.sql.*;
import java.util.*;
import java.util.concurrent.Executor;

/**
 * Implementation of {@link Factory} for JDBC 4.0.
 *
 * @author jhyde
 * @since Jun 14, 2007
 */
class FactoryJdbc41Impl implements Factory {
    /**
     * Creates a FactoryJdbc41Impl.
     */
    public FactoryJdbc41Impl() {
    }

    public Connection newConnection(
        XmlaOlap4jDriver driver,
        XmlaOlap4jProxy proxy,
        String url,
        Properties info)
        throws SQLException
    {
        return new XmlaOlap4jConnectionJdbc41(
            this, driver, proxy, url, info);
    }

    public EmptyResultSet newEmptyResultSet(
        XmlaOlap4jConnection olap4jConnection)
    {
        List<String> headerList = Collections.emptyList();
        List<List<Object>> rowList = Collections.emptyList();
        return new EmptyResultSetJdbc41(olap4jConnection, headerList, rowList);
    }

    public ResultSet newFixedResultSet(
        XmlaOlap4jConnection olap4jConnection,
        List<String> headerList,
        List<List<Object>> rowList)
    {
        return new EmptyResultSetJdbc41(
            olap4jConnection, headerList, rowList);
    }

    public XmlaOlap4jCellSet newCellSet(
        XmlaOlap4jStatement olap4jStatement) throws OlapException
    {
        return new XmlaOlap4jCellSetJdbc41(olap4jStatement);
    }

    public XmlaOlap4jStatement newStatement(
        XmlaOlap4jConnection olap4jConnection)
    {
        return new XmlaOlap4jStatementJdbc41(olap4jConnection);
    }

    public XmlaOlap4jPreparedStatement newPreparedStatement(
        String mdx,
        XmlaOlap4jConnection olap4jConnection) throws OlapException
    {
        return new XmlaOlap4jPreparedStatementJdbc41(olap4jConnection, mdx);
    }

    public XmlaOlap4jDatabaseMetaData newDatabaseMetaData(
        XmlaOlap4jConnection olap4jConnection)
    {
        return new XmlaOlap4jDatabaseMetaDataJdbc41(olap4jConnection);
    }

    // Inner classes

    private static class EmptyResultSetJdbc41
        extends FactoryJdbc4Plus.AbstractEmptyResultSet
    {
        /**
         * Creates a EmptyResultSetJdbc41.
         *
         * @param olap4jConnection Connection
         * @param headerList Column names
         * @param rowList List of row values
         */
        EmptyResultSetJdbc41(
            XmlaOlap4jConnection olap4jConnection,
            List<String> headerList,
            List<List<Object>> rowList)
        {
            super(olap4jConnection, headerList, rowList);
        }

        public <T> T getObject(
            int columnIndex,
            Class<T> type) throws SQLException
        {
            throw new UnsupportedOperationException();
        }

        public <T> T getObject(
            String columnLabel,
            Class<T> type) throws SQLException
        {
            throw new UnsupportedOperationException();
        }
    }

    private static class XmlaOlap4jConnectionJdbc41
        extends FactoryJdbc4Plus.AbstractConnection
    {
        /**
         * Creates a XmlaOlap4jConnectionJdbc41.
         *
         * @param factory Factory
         * @param driver Driver
         * @param proxy Proxy
         * @param url URL
         * @param info Extra properties
         * @throws SQLException on error
         */
        public XmlaOlap4jConnectionJdbc41(
            Factory factory,
            XmlaOlap4jDriver driver,
            XmlaOlap4jProxy proxy,
            String url,
            Properties info) throws SQLException
        {
            super(factory, driver, proxy, url, info);
        }

        public void abort(Executor executor) throws SQLException {
            throw new UnsupportedOperationException();
        }

        public void setNetworkTimeout(
            Executor executor,
            int milliseconds) throws SQLException
        {
            throw new UnsupportedOperationException();
        }

        public int getNetworkTimeout() throws SQLException {
            throw new UnsupportedOperationException();
        }
    }

    private static class XmlaOlap4jCellSetJdbc41
        extends FactoryJdbc4Plus.AbstractCellSet
    {
        /**
         * Creates an XmlaOlap4jCellSetJdbc41.
         *
         * @param olap4jStatement Statement
         * @throws OlapException on error
         */
        XmlaOlap4jCellSetJdbc41(
            XmlaOlap4jStatement olap4jStatement)
            throws OlapException
        {
            super(olap4jStatement);
        }

        public <T> T getObject(
            int columnIndex,
            Class<T> type) throws SQLException
        {
            throw new UnsupportedOperationException();
        }

        public <T> T getObject(
            String columnLabel,
            Class<T> type) throws SQLException
        {
            throw new UnsupportedOperationException();
        }
    }

    private static class XmlaOlap4jStatementJdbc41
        extends XmlaOlap4jStatement
    {
        /**
         * Creates a XmlaOlap4jStatementJdbc41.
         *
         * @param olap4jConnection Connection
         */
        XmlaOlap4jStatementJdbc41(
            XmlaOlap4jConnection olap4jConnection)
        {
            super(olap4jConnection);
        }

        public void closeOnCompletion() throws SQLException {
            throw new UnsupportedOperationException();
        }

        public boolean isCloseOnCompletion() throws SQLException {
            throw new UnsupportedOperationException();
        }
    }

    private static class XmlaOlap4jPreparedStatementJdbc41
        extends FactoryJdbc4Plus.AbstractPreparedStatement
    {
        /**
         * Creates a XmlaOlap4jPreparedStatementJdbc41.
         *
         * @param olap4jConnection Connection
         * @param mdx MDX query text
         * @throws OlapException on error
         */
        XmlaOlap4jPreparedStatementJdbc41(
            XmlaOlap4jConnection olap4jConnection,
            String mdx) throws OlapException
        {
            super(olap4jConnection, mdx);
        }

        public void closeOnCompletion() throws SQLException {
            throw new UnsupportedOperationException();
        }

        public boolean isCloseOnCompletion() throws SQLException {
            throw new UnsupportedOperationException();
        }
    }

    private static class XmlaOlap4jDatabaseMetaDataJdbc41
        extends FactoryJdbc4Plus.AbstractDatabaseMetaData
    {
        /**
         * Creates an XmlaOlap4jDatabaseMetaDataJdbc41.
         *
         * @param olap4jConnection Connection
         */
        XmlaOlap4jDatabaseMetaDataJdbc41(
            XmlaOlap4jConnection olap4jConnection)
        {
            super(olap4jConnection);
        }

        public ResultSet getPseudoColumns(
            String catalog,
            String schemaPattern,
            String tableNamePattern,
            String columnNamePattern) throws SQLException
        {
            throw new UnsupportedOperationException();
        }

        public boolean generatedKeyAlwaysReturned() throws SQLException {
            throw new UnsupportedOperationException();
        }
    }
}

// End FactoryJdbc41Impl.java
