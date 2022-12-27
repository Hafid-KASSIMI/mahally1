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

import net.mdrassty.util.ALIGNMENT;
import net.mdrassty.util.pdf.PAvailableFonts;
import net.mdrassty.util.pdf.PDFRectangle;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ExecutionPage {

    private final PDFRectangle title, school, year, ministery, subtitle, date, prepStart, prepEnd, start, end, matter, room, cands, am, pm;
    private final PDFRectangle[] seance;

    public ExecutionPage() {
        title = new PDFRectangle();
        school = new PDFRectangle();
        year = new PDFRectangle();
        ministery = new PDFRectangle();
        subtitle = new PDFRectangle();
        date = new PDFRectangle();
        prepStart = new PDFRectangle();
        prepEnd = new PDFRectangle();
        start = new PDFRectangle();
        end = new PDFRectangle();
        matter = new PDFRectangle();
        room = new PDFRectangle();
        cands = new PDFRectangle();
        am = new PDFRectangle();
        pm = new PDFRectangle();
        seance = new PDFRectangle[4];
        for (int i = 0; i < 4; i++) {
            seance[i] = new PDFRectangle();
        }

        title.reset(51.291023f, 117.77238f, 492.69351f, 29.514742f);
        school.reset(47.816002f, 42.374817f, 198.42975f, 64.391235f);
        year.reset(47.816002f, 111.90299f, 103.02701f, 19.470001f);
        ministery.reset(349.02933f, 42.374817f, 198.42975f, 64.391235f);
        subtitle.reset(51.291023f, 158.29346f, 492.69351f, 29.514742f);
        date.reset(74.235596f, 233.55042f, 492.69351f, 13.604839f);
        prepStart.reset(318.03839f, 282.36148f, 248.89075f, 13.604839f);
        prepEnd.reset(28.346458f, 282.36148f, 248.89075f, 13.604842f);
        start.reset(318.03839f, 307.63321f, 248.89075f, 13.604839f);
        end.reset(28.346458f, 307.63321f, 248.89075f, 13.604842f);
        matter.reset(348.49954f, 208.40623f, 218.42957f, 13.604839f);
        room.reset(28.346458f, 208.40622f, 145.86208f, 13.604839f);
        cands.reset(176.15309f, 208.40622f, 170.40189f, 13.604839f);
        am.reset(466.53461f, 258.69458f, 12f, 12f);
        pm.reset(411.74854f, 258.69458f, 12f, 12f);
        seance[0].reset(252.54654f, 259.45297f, 12f, 12f);
        seance[1].reset(191.43231f, 259.45297f, 12f, 12f);
        seance[2].reset(129.16966f, 259.45297f, 12f, 12f);
        seance[3].reset(67.135521f, 259.45297f, 12f, 12f);

        title.setFormat(18f, PAvailableFonts.TIMES, true);
        subtitle.setFormat(16, PAvailableFonts.TIMES, true);
        ministery.setFormat(11f, PAvailableFonts.MAGHRIBI);
        ministery.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        school.setFormat(11f, PAvailableFonts.MAGHRIBI);
        school.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        year.setFormat(12f, PAvailableFonts.TIMES);
        year.getFormat().setRotation(90);
        matter.setFormat(12, PAvailableFonts.TIMES);
        matter.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        date.setFormat(matter.getFormat());
        cands.setFormat(matter.getFormat());
        room.setFormat(matter.getFormat());
        start.setFormat(matter.getFormat());
        end.setFormat(matter.getFormat());
        prepStart.setFormat(matter.getFormat());
        prepEnd.setFormat(matter.getFormat());
        am.setFormat(10, PAvailableFonts.ICOMOON);
        pm.setFormat(am.getFormat());
        for (PDFRectangle s : seance) {
            s.setFormat(am.getFormat());
        }

    }

    public PDFRectangle getTitle() {
        return title;
    }

    public PDFRectangle getSchool() {
        return school;
    }

    public PDFRectangle getYear() {
        return year;
    }

    public PDFRectangle getMinistery() {
        return ministery;
    }

    public PDFRectangle getSubtitle() {
        return subtitle;
    }

    public PDFRectangle getDate() {
        return date;
    }

    public PDFRectangle getPrepStart() {
        return prepStart;
    }

    public PDFRectangle getPrepEnd() {
        return prepEnd;
    }

    public PDFRectangle getStart() {
        return start;
    }

    public PDFRectangle getEnd() {
        return end;
    }

    public PDFRectangle getMatter() {
        return matter;
    }

    public PDFRectangle getRoom() {
        return room;
    }

    public PDFRectangle getCands() {
        return cands;
    }

    public PDFRectangle getAm() {
        return am;
    }

    public PDFRectangle getPm() {
        return pm;
    }

    public PDFRectangle getSeance(int index) {
        return seance[index];
    }

    public int getSeanceLength() {
        return seance.length;
    }

}
