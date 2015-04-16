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
package org.olap4j.layout;

import org.olap4j.*;
import org.olap4j.metadata.Member;

import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

/**
 * Formatter that can convert a {@link CellSet} into Mondrian's traditional
 * layout.
 *
 * <p><b>This class is experimental. It is not part of the olap4j
 * specification and is subject to change without notice.</b></p>
 *
 * @author jhyde
 * @since Apr 15, 2009
 */
public class TraditionalCellSetFormatter implements CellSetFormatter {
    public void format(
        CellSet cellSet,
        PrintWriter pw)
    {
        print(cellSet, pw);
    }

    /**
     * Prints a cell set.
     *
     * @param cellSet Cell set
     * @param pw Writer
     */
    private static void print(CellSet cellSet, PrintWriter pw) {
        pw.println("Axis #0:");
        printAxis(pw, cellSet.getFilterAxis());
        final List<CellSetAxis> axes = cellSet.getAxes();
        final int axisCount = axes.size();
        for (int i = 0; i < axisCount; i++) {
            CellSetAxis axis = axes.get(i);
            pw.println("Axis #" + (i + 1) + ":");
            printAxis(pw, axis);
        }
        // Usually there are 3 axes: {filter, columns, rows}. Position is a
        // {column, row} pair. We call printRows with axis=2. When it
        // recurses to axis=-1, it prints.
        List<Integer> pos = new ArrayList<Integer>(axisCount);
        for (int i = 0; i < axisCount; i++) {
            pos.add(-1);
        }
        if (axisCount == 0) {
            printCell(cellSet, pw, pos);
        } else {
            printRows(cellSet, pw, axisCount - 1, pos);
        }
    }

    /**
     * Prints the rows of cell set.
     *
     * @param cellSet Cell set
     * @param pw Writer
     * @param axis Axis ordinal
     * @param pos Partial coordinate
     */
    private static void printRows(
        CellSet cellSet, PrintWriter pw, int axis, List<Integer> pos)
    {
        final CellSetAxis _axis = cellSet.getAxes().get(axis);
        final List<Position> positions = _axis.getPositions();
        final int positionCount = positions.size();
        for (int i = 0; i < positionCount; i++) {
            pos.set(axis, i);
            if (axis == 0) {
                int row =
                    axis + 1 < pos.size()
                        ? pos.get(axis + 1)
                        : 0;
                pw.print("Row #" + row + ": ");
                printCell(cellSet, pw, pos);
                pw.println();
            } else {
                printRows(cellSet, pw, axis - 1, pos);
            }
        }
    }

    /**
     * Prints an axis and its members.
     *
     * @param pw Print writer
     * @param axis Axis
     */
    private static void printAxis(PrintWriter pw, CellSetAxis axis) {
        List<Position> positions = axis.getPositions();
        for (Position position : positions) {
            boolean firstTime = true;
            pw.print("{");
            for (Member member : position.getMembers()) {
                if (! firstTime) {
                    pw.print(", ");
                }
                pw.print(member.getUniqueName());
                firstTime = false;
            }
            pw.println("}");
        }
    }

    /**
     * Prints the formatted value of a Cell at a given position.
     *
     * @param cellSet Cell set
     * @param pw Print writer
     * @param pos Cell coordinates
     */
    private static void printCell(
        CellSet cellSet, PrintWriter pw, List<Integer> pos)
    {
        Cell cell = cellSet.getCell(pos);
        pw.print(cell.getFormattedValue());
    }
}

// End TraditionalCellSetFormatter.java
