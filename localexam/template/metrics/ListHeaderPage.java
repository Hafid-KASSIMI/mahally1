/*
 * Copyright (C) 2022 H. KASSIMI
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package localexam.template.metrics;

import net.mdrassty.util.pdf.PAvailableFonts;
import net.mdrassty.util.pdf.PDFRectangle;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ListHeaderPage {

    private final PDFRectangle[] room, stats;
    private final int SIZE = 4;

    public ListHeaderPage() {
        room = new PDFRectangle[4];
        stats = new PDFRectangle[4];
        for (int i = 0; i < 4; i++) {
            room[i] = new PDFRectangle();
            stats[i] = new PDFRectangle();
        }

        room[0].reset(366.5231763543344f, 46.302694037452f, 181.41994962896229f, 148.96135325044875f);
        room[1].reset(366.5231763543344f, 246.3884645114888f, 181.41994962896229f, 148.96135325044875f);
        room[2].reset(366.5231763543344f, 446.4964297363823f, 181.41994962896229f, 148.96135325044875f);
        room[3].reset(366.5231763543344f, 646.6043949612758f, 181.41994962896229f, 148.96135325044875f);
        stats[0].reset(47.858503725880496f, 46.302694037452f, 298.83757933227866f, 148.96135325044875f);
        stats[1].reset(47.858498471240985f, 246.3884645114888f, 298.83757933227866f, 148.96135325044875f);
        stats[2].reset(47.858498471240985f, 446.4964297363823f, 298.83757933227866f, 148.96135325044875f);
        stats[3].reset(47.85849546858984f, 646.6043949612758f, 298.83757933227866f, 148.96135325044875f);

        room[0].setFormat(48, PAvailableFonts.TIMES, true);
        stats[0].setFormat(32, PAvailableFonts.TIMES);
        for (int i = 1; i < 4; i++) {
            room[i].setFormat(room[0].getFormat());
            stats[i].setFormat(stats[0].getFormat());
        }

    }

    public PDFRectangle getRoom(int index) {
        return room[index];
    }

    public int getRoomLength() {
        return room.length;
    }

    public PDFRectangle getStats(int index) {
        return stats[index];
    }

    public int getStatsLength() {
        return stats.length;
    }

    public int getSize() {
        return SIZE;
    }

}
