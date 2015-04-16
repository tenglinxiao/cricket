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
package org.olap4j.transform;

import org.olap4j.Axis;
import org.olap4j.CellSet;
import org.olap4j.mdx.ParseTreeNode;
import org.olap4j.metadata.Member;

/**
 * Roll-up level transformation
 *
 * <p>Description: Replaces a member at a specific position on an axis by all
 * the members of its parent's level. The member to roll-up is identified from
 * a CellSet with the axis, positionOrdinalInAxis and memberOrdinalInPosition
 * arguments.
 *
 * <p>Example of use: the user clicks on a member in a crosstab axis, in order
 * to roll up to the members of the upper level.
 *
 * <p>Applicability: this transform is applicable only to members in a query
 * that are have a parent. (Note: how would this work in parent-child
 * hierarchies?)
 *
 * @author etdub
 * @since Aug 4, 2008
 */
public class RollUpLevelTransform extends AxisTransform {

    private final Member memberToDrill;

    /**
     * Creates a RollUpLevelTransform.
     *
     * @param axis Axis
     * @param positionOrdinalInAxis Position ordinal on Axis
     * @param memberOrdinalInPosition Member ordinal in Position
     * @param cellSet Cell set
     */
    public RollUpLevelTransform(
        Axis axis,
        int positionOrdinalInAxis,
        int memberOrdinalInPosition,
        CellSet cellSet)
    {
        super(axis);

        memberToDrill = TransformUtil.getMemberFromCellSet(
            axis, positionOrdinalInAxis, memberOrdinalInPosition, cellSet);
    }

    public String getName() {
        return "Roll member up a level";
    }

    public String getDescription() {
        return "Replaces the member expression on the axis by all members "
            + "on its parent level";
    }

    @Override
    protected ParseTreeNode processAxisExp(ParseTreeNode exp) {
        // FIXME: for now only 1 dimension on an axis is supported,
        // (naive implementation only used for proof of concept)
        return MdxHelper.makeSetCallNode(
            MdxHelper.makeMembersCallNode(
                MdxHelper.makeLevelCallNode(
                    MdxHelper.makeParentCallNode(
                        MdxHelper.makeMemberNode(memberToDrill)))));
    }
}

// End RollUpLevelTransform.java
