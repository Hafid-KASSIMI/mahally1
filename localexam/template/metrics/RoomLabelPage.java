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

import net.mdrassty.util.A4_PAPER;
import net.mdrassty.util.pdf.PAvailableFonts;
import net.mdrassty.util.pdf.PDFRectangle;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class RoomLabelPage {

    private final PDFRectangle[] title, school, label;

    public RoomLabelPage() {
        title = new PDFRectangle[2];
        school = new PDFRectangle[2];
        label = new PDFRectangle[2];
        for (int i = 0; i < 2; i++) {
            title[i] = new PDFRectangle(A4_PAPER.getLandscapeHeight());
            school[i] = new PDFRectangle(A4_PAPER.getLandscapeHeight());
            label[i] = new PDFRectangle(A4_PAPER.getLandscapeHeight());
        }
        title[0].reset(449.6884309168426f, 28.98422661889632f, 364.57387774923444f, 29.514742889376173f);
        title[1].reset(28.371507963919836f, 28.346464094276374f, 364.57387774923444f, 29.514742889376173f);
        school[0].reset(449.6884309168426f, 538.0521882707668f, 364.57387774923444f, 29.514742889376173f);
        school[1].reset(28.371507963919836f, 537.4144332461472f, 364.57387774923444f, 29.514742889376173f);
        label[0].reset(461.4271529522246f, 133.43682515114565f, 340.45810007101477f, 329.039742702128f);
        label[1].reset(40.42940355899475f, 133.11791763883465f, 340.45810007101477f, 329.039742702128f);

        school[0].setFormat(12, PAvailableFonts.TIMES, false);
        label[0].setFormat(140, PAvailableFonts.TIMES, true);
        title[0].setFormat(school[0].getFormat());
        for (int i = 1; i < 2; i++) {
            title[i].setFormat(title[0].getFormat());
            school[i].setFormat(school[0].getFormat());
            label[i].setFormat(label[0].getFormat());
        }

    }

    public PDFRectangle getTitle(int index) {
        return title[index];
    }

    public int getTitleLength() {
        return title.length;
    }

    public PDFRectangle getSchool(int index) {
        return school[index];
    }

    public int getSchoolLength() {
        return school.length;
    }

    public PDFRectangle getLabel(int index) {
        return label[index];
    }

    public int getLabelLength() {
        return label.length;
    }

}
