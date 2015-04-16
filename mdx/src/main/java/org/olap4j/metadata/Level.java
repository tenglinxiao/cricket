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
package org.olap4j.metadata;

import org.olap4j.CellSet;
import org.olap4j.OlapException;

import java.util.List;

/**
 * Group of {@link Member} objects in a {@link Hierarchy},
 * all with the same attributes and at the same depth in the hierarchy.
 *
 * @author jhyde
 * @since Aug 23, 2006
 */
public interface Level extends MetadataElement {
    /**
     * Returns the depth of this <code>Level</code>.
     *
     * <p>Note #1: In an access-controlled context, the first visible level of
     * a hierarchy may not have a depth of 0.</p>
     *
     * <p>Note #2: In a parent-child hierarchy, the depth of a member (as
     * returned by may not be the same as the depth of its level.
     *
     * @return depth of this level
     */
    int getDepth();

    /**
     * Returns the {@link Hierarchy} this <code>Level</code> belongs to.
     *
     * @return hierarchy this level belongs to
     */
    Hierarchy getHierarchy();

    /**
     * Returns the Dimension this <code>Level</code> belongs to.
     * (Always equivalent to <code>getHierarchy().getDimension()</code>.)
     *
     * @return dimension this level belongs to
     */
    Dimension getDimension();

    /**
     * Returns the type of this <code>Level</code>.
     *
     * @return level type
     */
    Level.Type getLevelType();

    /**
     * Returns whether the level is calculated.
     *
     * @return Whether this level is calculated
     */
    boolean isCalculated();

    /**
     * Returns a list of definitions for the properties available to members
     * of this <code>Level</code>.
     *
     * <p>The caller should assume that the list is immutable;
     * if the caller modifies the list, behavior is undefined.</p>
     *
     * @see org.olap4j.OlapDatabaseMetaData#getProperties
     *
     * @return properties of this Level
     */
    NamedList<Property> getProperties();

    /**
     * Returns a list of {@link Member} objects that belong to this Level.
     *
     * <p>The list includes calculated members which are defined in the cube,
     * if any. The list does not include any calculated members defined in a
     * query, if accessed through a {@link CellSet}.</p>
     *
     * <p>Some levels have a very many members. In this case, calling this
     * method may be expensive in space and/or time and is not recommended.</p>
     *
     * <p>If you need to include calculated members, or if you need to query
     * specific members or subsets of members in a level, consider instead
     * generating and executing an MDX query with a single axis. MDX functions
     * {@code AddCalculatedMembers}, {@code Filter} and {@code Order} are
     * especially useful. For example,
     *
     * <pre>with member [Measures].[Zero] as 0
     * select AddCalculatedMembers([Time].[Month].Members) on 0
     * from [Sales]
     * where [Measures].[Zero]</pre>
     *
     * returns the {@code [Month]} level including calculated members. The
     * {@code [Measures].[Zero]} calculated member saves the OLAP server the
     * effort of retrieving cell values.</p>
     *
     * <p>The members of a level do not have unique names, so unlike
     * {@link Hierarchy#getRootMembers()} and
     * {@link Member#getChildMembers()} the result type
     * is a {@link List} not a {@link NamedList}.
     *
     * @return List of members in this Level
     *
     * @throws OlapException if database error occurs
     */
    List<Member> getMembers() throws OlapException;

    /**
     * Returns the number of members in this Level.
     *
     * @return number of members
     */
    int getCardinality();

    /**
     * Enumeration of the types of a {@link Level}.
     *
     * <p>Several of the values are defined by OLE DB for OLAP and/or XML/A,
     * sans the "MDLEVEL_TYPE_" prefix to their name. For example,
     * {@link #GEO_CONTINENT} corresponds to
     * the value <code>MDLEVEL_TYPE_GEO_CONTINENT</code> for the
     * <code>LEVEL_TYPE</code> property in the <code>MDSCHEMA_LEVELS</code>
     * schema rowset.
     *
     * <p>Some of the values are specified by OLE DB for OLAP:
     * <ul>
     * <li>MDLEVEL_TYPE_REGULAR         (0x0000)
     * <li>MDLEVEL_TYPE_ALL             (0x0001)
     * <li>MDLEVEL_TYPE_TIME_YEARS      (0x0014)
     * <li>MDLEVEL_TYPE_TIME_HALF_YEAR  (0x0024)
     * <li>MDLEVEL_TYPE_TIME_QUARTERS   (0x0044)
     * <li>MDLEVEL_TYPE_TIME_MONTHS     (0x0084)
     * <li>MDLEVEL_TYPE_TIME_WEEKS      (0x0104)
     * <li>MDLEVEL_TYPE_TIME_DAYS       (0x0204)
     * <li>MDLEVEL_TYPE_TIME_HOURS      (0x0304)
     * <li>MDLEVEL_TYPE_TIME_MINUTES    (0x0404)
     * <li>MDLEVEL_TYPE_TIME_SECONDS    (0x0804)
     * <li>MDLEVEL_TYPE_TIME_UNDEFINED  (0x1004)
     * </ul>
     *
     * Some of the OLE DB for OLAP values are as flags, and do not become
     * values of the enumeration:
     * <ul>
     * <li>MDLEVEL_TYPE_UNKNOWN (0x0000) signals that no other flags are set.
     *     Use {@link #REGULAR}
     * <li>MDLEVEL_TYPE_CALCULATED (0x0002) indicates that the level is
     *     calculated. Use {@link Level#isCalculated}.
     * <li>MDLEVEL_TYPE_TIME (0x0004) indicates that the level is time-related.
     *     Use {@link #isTime}.
     * <li>MDLEVEL_TYPE_RESERVED1 (0x0008) is reserved for future use.
     * </ul>
     *
     * <p>Some of the values are specified by XMLA:
     * <ul>
     * <li>MDLEVEL_TYPE_GEO_CONTINENT (0x2001)
     * <li>MDLEVEL_TYPE_GEO_REGION (0x2002)
     * <li>MDLEVEL_TYPE_GEO_COUNTRY (0x2003)
     * <li>MDLEVEL_TYPE_GEO_STATE_OR_PROVINCE (0x2004)
     * <li>MDLEVEL_TYPE_GEO_COUNTY (0x2005)
     * <li>MDLEVEL_TYPE_GEO_CITY (0x2006)
     * <li>MDLEVEL_TYPE_GEO_POSTALCODE (0x2007)
     * <li>MDLEVEL_TYPE_GEO_POINT (0x2008)
     * <li>MDLEVEL_TYPE_ORG_UNIT (0x1011)
     * <li>MDLEVEL_TYPE_BOM_RESOURCE (0x1012)
     * <li>MDLEVEL_TYPE_QUANTITATIVE (0x1013)
     * <li>MDLEVEL_TYPE_ACCOUNT (0x1014)
     * <li>MDLEVEL_TYPE_CUSTOMER (0x1021)
     * <li>MDLEVEL_TYPE_CUSTOMER_GROUP (0x1022)
     * <li>MDLEVEL_TYPE_CUSTOMER_HOUSEHOLD (0x1023)
     * <li>MDLEVEL_TYPE_PRODUCT (0x1031)
     * <li>MDLEVEL_TYPE_PRODUCT_GROUP (0x1032)
     * <li>MDLEVEL_TYPE_SCENARIO (0x1015)
     * <li>MDLEVEL_TYPE_UTILITY (0x1016)
     * <li>MDLEVEL_TYPE_PERSON (0x1041)
     * <li>MDLEVEL_TYPE_COMPANY (0x1042)
     * <li>MDLEVEL_TYPE_CURRENCY_SOURCE (0x1051)
     * <li>MDLEVEL_TYPE_CURRENCY_DESTINATION (0x1052)
     * <li>MDLEVEL_TYPE_CHANNEL (0x1061)
     * <li>MDLEVEL_TYPE_REPRESENTATIVE (0x1062)
     * <li>MDLEVEL_TYPE_PROMOTION (0x1071)
     * </ul>
     *
     * @see Level#getLevelType
     * @see org.olap4j.OlapDatabaseMetaData#getLevels
     */
    public enum Type implements XmlaConstant {

        /**
         * Indicates that the level is not related to time.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_REGULAR(0x0000)</code>.</p>
         */
        REGULAR(0x0000),

        /**
         * Indicates that the level contains the 'all' member of its hierarchy.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_ALL(0x0001)</code>.</p>
         */
        ALL(0x0001),

        /**
         * Indicates that a level holds the null member. Does not correspond to
         * an XMLA or OLE DB value.
         */
        NULL(-1),

        /**
         * Indicates that a level refers to years.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_TIME_YEARS(0x0014)</code>.</p>
         *
         * <p>It must be used in a dimension whose type is
         * {@link org.olap4j.metadata.Dimension.Type#TIME}.</p>
         */
        TIME_YEARS(0x0014),

        /**
         * Indicates that a level refers to half years.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_TIME_HALF_YEAR(0x0304)</code>.</p>
         *
         * <p>It must be used in a dimension whose type is
         * {@link org.olap4j.metadata.Dimension.Type#TIME}.</p>
         */
        TIME_HALF_YEAR(0x0024),

        /**
         * Indicates that a level refers to quarters.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_TIME_QUARTERS(0x0044)</code>.</p>
         *
         * <p>It must be used in a dimension whose type is
         * {@link org.olap4j.metadata.Dimension.Type#TIME}.</p>
         */
        TIME_QUARTERS(0x0044),

        /**
         * Indicates that a level refers to months.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_TIME_MONTHS(0x0084)</code>.</p>
         *
         * <p>It must be used in a dimension whose type is
         * {@link org.olap4j.metadata.Dimension.Type#TIME}.</p>
         */
        TIME_MONTHS(0x0084),

        /**
         * Indicates that a level refers to weeks.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_TIME_WEEKS(0x0104)</code>.</p>
         *
         * <p>It must be used in a dimension whose type is
         * {@link org.olap4j.metadata.Dimension.Type#TIME}.</p>
         */
        TIME_WEEKS(0x0104),

        /**
         * Indicates that a level refers to days.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_TIME_DAYS(0x0204)</code>.</p>
         *
         * <p>It must be used in a dimension whose type is
         * {@link org.olap4j.metadata.Dimension.Type#TIME}.</p>
         */
        TIME_DAYS(0x0204),

        /**
         * Indicates that a level refers to hours.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_TIME_HOURS(0x0304)</code>.</p>
         *
         * <p>It must be used in a dimension whose type is
         * {@link org.olap4j.metadata.Dimension.Type#TIME}.</p>
         */
        TIME_HOURS(0x0304),

        /**
         * Indicates that a level refers to minutes.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_TIME_MINUTES(0x0404)</code>.</p>
         *
         * <p>It must be used in a dimension whose type is
         * {@link org.olap4j.metadata.Dimension.Type#TIME}.</p>
         */
        TIME_MINUTES(0x0404),

        /**
         * Indicates that a level refers to seconds.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_TIME_SECONDS(0x0804)</code>.</p>
         *
         * <p>It must be used in a dimension whose type is
         * {@link org.olap4j.metadata.Dimension.Type#TIME}.</p>
         */
        TIME_SECONDS(0x0804),

        /**
         * Indicates that a level refers to an unspecified time unit.
         *
         * <p>Corresponds to the OLE DB for OLAP constant
         * <code>MDLEVEL_TYPE_TIME_UNDEFINED(0x1004)</code>.</p>
         *
         * <p>It must be used in a dimension whose type is
         * {@link org.olap4j.metadata.Dimension.Type#TIME}.</p>
         */
        TIME_UNDEFINED(0x1004),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_GEO_CONTINENT(0x2001)</code>. */
        GEO_CONTINENT(0x2001),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_GEO_REGION(0x2002)</code>. */
        GEO_REGION(0x2002),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_GEO_COUNTRY(0x2003)</code>. */
        GEO_COUNTRY(0x2003),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_GEO_STATE_OR_PROVINCE(0x2004)</code>. */
        GEO_STATE_OR_PROVINCE(0x2004),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_GEO_COUNTY(0x2005)</code>. */
        GEO_COUNTY(0x2005),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_GEO_CITY(0x2006)</code>. */
        GEO_CITY(0x2006),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_GEO_POSTALCODE(0x2007)</code>. */
        GEO_POSTALCODE(0x2007),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_GEO_POINT(0x2008)</code>. */
        GEO_POINT(0x2008),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_ORG_UNIT(0x1011)</code>. */
        ORG_UNIT(0x1011),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_BOM_RESOURCE(0x1012)</code>. */
        BOM_RESOURCE(0x1012),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_QUANTITATIVE(0x1013)</code>. */
        QUANTITATIVE(0x1013),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_ACCOUNT(0x1014)</code>. */
        ACCOUNT(0x1014),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_CUSTOMER(0x1021)</code>. */
        CUSTOMER(0x1021),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_CUSTOMER_GROUP(0x1022)</code>. */
        CUSTOMER_GROUP(0x1022),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_CUSTOMER_HOUSEHOLD(0x1023)</code>. */
        CUSTOMER_HOUSEHOLD(0x1023),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_PRODUCT(0x1031)</code>. */
        PRODUCT(0x1031),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_PRODUCT_GROUP(0x1032)</code>. */
        PRODUCT_GROUP(0x1032),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_SCENARIO(0x1015)</code>. */
        SCENARIO(0x1015),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_UTILITY(0x1016)</code>. */
        UTILITY(0x1016),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_PERSON(0x1041)</code>. */
        PERSON(0x1041),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_COMPANY(0x1042)</code>. */
        COMPANY(0x1042),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_CURRENCY_SOURCE(0x1051)</code>. */
        CURRENCY_SOURCE(0x1051),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_CURRENCY_DESTINATION(0x1052)</code>. */
        CURRENCY_DESTINATION(0x1052),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_CHANNEL(0x1061)</code>. */
        CHANNEL(0x1061),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_REPRESENTATIVE(0x1062)</code>. */
        REPRESENTATIVE(0x1062),

        /** Corresponds to XMLA constant
         * <code>MDLEVEL_TYPE_PROMOTION(0x1071)</code>. */
        PROMOTION(0x1071);

        private final int xmlaOrdinal;

        private static final Dictionary<Type> DICTIONARY =
            DictionaryImpl.forClass(Type.class);

        /**
         * Per {@link org.olap4j.metadata.XmlaConstant}, returns a dictionary
         * of all values of this enumeration.
         *
         * @return Dictionary of all values
         */
        public static Dictionary<Type> getDictionary() {
            return DICTIONARY;
        }

        /**
         * Creates a level type.
         *
         * @param xmlaOrdinal Ordinal code in XMLA or OLE DB for OLAP
         * specification
         */
        private Type(int xmlaOrdinal) {
            this.xmlaOrdinal = xmlaOrdinal;
        }

        public String xmlaName() {
            return "MDLEVEL_TYPE_" + name();
        }

        public String getDescription() {
            return "";
        }

        public int xmlaOrdinal() {
            return xmlaOrdinal;
        }

        /**
         * Returns whether this is a time-related level
         * ({@link #TIME_YEARS},
         * {@link #TIME_HALF_YEAR},
         * {@link #TIME_QUARTERS},
         * {@link #TIME_MONTHS},
         * {@link #TIME_WEEKS},
         * {@link #TIME_DAYS},
         * {@link #TIME_HOURS},
         * {@link #TIME_MINUTES},
         * {@link #TIME_SECONDS},
         * {@link #TIME_UNDEFINED}).
         *
         * @return whether this is a time-related level
         */
        public boolean isTime() {
            switch (this) {
            case TIME_YEARS:
            case TIME_HALF_YEAR:
            case TIME_QUARTERS:
            case TIME_MONTHS:
            case TIME_WEEKS:
            case TIME_DAYS:
            case TIME_HOURS:
            case TIME_MINUTES:
            case TIME_SECONDS:
            case TIME_UNDEFINED:
                return true;
            default:
                return false;
            }
        }
    }
}

// End Level.java
