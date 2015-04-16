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

import org.olap4j.CellSetAxisMetaData;
import org.olap4j.CellSetMetaData;
import org.olap4j.impl.ArrayNamedListImpl;
import org.olap4j.impl.Olap4jUtil;
import org.olap4j.metadata.*;

import java.sql.SQLException;
import java.util.*;

/**
 * Implementation of {@link org.olap4j.CellSetMetaData}
 * for XML/A providers.
 *
 * @author jhyde
 * @since Jun 13, 2007
 */
class XmlaOlap4jCellSetMetaData implements CellSetMetaData {
    final XmlaOlap4jCube cube;
    private final NamedList<CellSetAxisMetaData> axisMetaDataList =
        new ArrayNamedListImpl<CellSetAxisMetaData>() {
            public String getName(Object axisMetaData) {
                return ((CellSetAxisMetaData)axisMetaData)
                    .getAxisOrdinal().name();
            }
        };
    private final XmlaOlap4jCellSetAxisMetaData filterAxisMetaData;
    private final NamedList<Property> cellProperties =
        new ArrayNamedListImpl<Property>() {
            public String getName(Object property) {
                return ((Property)property).getName();
            }
        };
    final Map<String, Property> propertiesByTag;

    XmlaOlap4jCellSetMetaData(
        XmlaOlap4jStatement olap4jStatement,
        XmlaOlap4jCube cube,
        XmlaOlap4jCellSetAxisMetaData filterAxisMetaData,
        List<CellSetAxisMetaData> axisMetaDataList,
        List<XmlaOlap4jCellProperty> cellProperties)
    {
        assert olap4jStatement != null;
        assert cube != null;
        assert filterAxisMetaData != null;
        this.cube = cube;
        this.filterAxisMetaData = filterAxisMetaData;
        this.axisMetaDataList.addAll(axisMetaDataList);
        this.propertiesByTag = new HashMap<String, Property>();
        for (XmlaOlap4jCellProperty cellProperty : cellProperties) {
            Property property;
            try {
                property = Property.StandardCellProperty.valueOf(
                    cellProperty.propertyName);
                this.propertiesByTag.put(cellProperty.tag, property);
            } catch (IllegalArgumentException e) {
                property = cellProperty;
                this.propertiesByTag.put(property.getName(), property);
            }
            this.cellProperties.add(property);
        }
    }

    private XmlaOlap4jCellSetMetaData(
        XmlaOlap4jStatement olap4jStatement,
        XmlaOlap4jCube cube,
        XmlaOlap4jCellSetAxisMetaData filterAxisMetaData,
        List<CellSetAxisMetaData> axisMetaDataList,
        Map<String, Property> propertiesByTag,
        List<Property> cellProperties)
    {
        assert olap4jStatement != null;
        assert cube != null;
        assert filterAxisMetaData != null;
        this.cube = cube;
        this.filterAxisMetaData = filterAxisMetaData;
        this.axisMetaDataList.addAll(axisMetaDataList);
        this.propertiesByTag = propertiesByTag;
        this.cellProperties.addAll(cellProperties);
    }

    XmlaOlap4jCellSetMetaData cloneFor(
        XmlaOlap4jPreparedStatement preparedStatement)
    {
        return new XmlaOlap4jCellSetMetaData(
            preparedStatement,
            cube,
            filterAxisMetaData,
            axisMetaDataList,
            propertiesByTag,
            cellProperties);
    }

    // implement CellSetMetaData

    public NamedList<Property> getCellProperties() {
        return Olap4jUtil.cast(cellProperties);
    }

    public Cube getCube() {
        return cube;
    }

    public NamedList<CellSetAxisMetaData> getAxesMetaData() {
        return axisMetaDataList;
    }

    public CellSetAxisMetaData getFilterAxisMetaData() {
        return filterAxisMetaData;
    }

// implement ResultSetMetaData

    public int getColumnCount() throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isAutoIncrement(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isCaseSensitive(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isSearchable(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isCurrency(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int isNullable(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isSigned(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getColumnDisplaySize(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getColumnLabel(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getColumnName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getSchemaName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getPrecision(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getScale(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getTableName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getCatalogName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public int getColumnType(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getColumnTypeName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isReadOnly(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isWritable(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isDefinitelyWritable(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public String getColumnClassName(int column) throws SQLException {
        throw new UnsupportedOperationException();
    }

    // implement Wrapper

    public <T> T unwrap(Class<T> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }

    public boolean isWrapperFor(Class<?> iface) throws SQLException {
        throw new UnsupportedOperationException();
    }
}

// End XmlaOlap4jCellSetMetaData.java
