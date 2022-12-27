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

import java.awt.Color;
import java.util.ArrayList;
import net.mdrassty.util.ALIGNMENT;
import net.mdrassty.util.pdf.PAvailableFonts;
import net.mdrassty.util.pdf.PDFRectangle;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class GuardsListPage {

    private final PDFRectangle title, matter, time, date, year, room, subtitle, direction, academy, school;
    protected float MAX_HEIGHT, FIRST_Y, LAST_Y, HEIGHT;
    private final int DEFAULT_ROWS_COUNT = 20;
    protected final PDFRectangle DIVIDER;
    private float rowHeight, size;

    public GuardsListPage() {
        title = new PDFRectangle();
        matter = new PDFRectangle();
        time = new PDFRectangle();
        date = new PDFRectangle();
        year = new PDFRectangle();
        room = new PDFRectangle();
        subtitle = new PDFRectangle();
        direction = new PDFRectangle();
        academy = new PDFRectangle();
        school = new PDFRectangle();

        title.reset(212.97366953103443f, 28.345997594258364f, 354.45647473410656f, 29.514742889376173f);
        matter.reset(381.60671664877543f, 92.65907607697656f, 185.82348016276066f, 19.469916751609436f);
        time.reset(47.85825450583525f, 92.65907607697656f, 159.35747488496557f, 19.469916751609436f);
        date.reset(212.51602796300983f, 92.65907607697656f, 163.79040137349836f, 19.469916751609436f);
        year.reset(47.858257508486396f, 92.65907607697656f, 83.85703536039017f, 19.470001501612707f);
        room.reset(483.2650519102689f, 158.31465611152018f, 83.29396320394754f, 654.3584052606088f);
        subtitle.reset(212.97371457080163f, 62.2675119037519f, 354.45647473410656f, 25.98480700310784f);
        direction.reset(47.858257508486396f, 48.54915787417366f, 153.10907795297868f, 19.500000752770788f);
        academy.reset(47.858257508486396f, 28.345997594258364f, 153.10907795297868f, 19.500000752770788f);
        school.reset(47.858257508486396f, 68.75231815408895f, 153.10907795297868f, 19.500000752770788f);

        title.setFormat(18f, PAvailableFonts.TIMES, true);
        subtitle.setFormat(16f, PAvailableFonts.TIMES, true);
        academy.setFormat(12f, PAvailableFonts.TIMES);
        academy.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        direction.setFormat(academy.getFormat());
        school.setFormat(academy.getFormat());
        year.setFormat(12f, PAvailableFonts.TIMES);
        year.getFormat().setRotation(90);
        matter.setFormat(14f, PAvailableFonts.TIMES);
        matter.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        date.setFormat(matter.getFormat());
        time.setFormat(matter.getFormat());
        room.setFormat(12f, PAvailableFonts.TIMES);

        DIVIDER = new PDFRectangle();
        DIVIDER.getFormat().setBackColor(new Color(0xFF, 0x99, 0x0));
        DIVIDER.reset(27.638f, 0f, 539.610f, 0.78f);
        FIRST_Y = room.getInkscapeY();
        HEIGHT = room.getHeight();
        MAX_HEIGHT = (HEIGHT - DIVIDER.getHeight() * (DEFAULT_ROWS_COUNT - 1)) / DEFAULT_ROWS_COUNT;
        LAST_Y = HEIGHT + FIRST_Y;
    }

    public void prepareRows(long size) {
        float h = (HEIGHT - DIVIDER.getHeight() * (size - 1)) / size;
        if (h > MAX_HEIGHT) {
            h = MAX_HEIGHT;
        }
        room.setHeight(h);
        rowHeight = h + DIVIDER.getHeight();
        this.size = size;
    }

    public void prepareRow(int index) {
        float y = FIRST_Y + rowHeight * index;
        room.setY(y);
        DIVIDER.setY(y - DIVIDER.getHeight());
    }

    public ArrayList<PDFRectangle> prepareDividers() throws CloneNotSupportedException {
        ArrayList<PDFRectangle> arr = new ArrayList();
        PDFRectangle tmp;
        if (size < DEFAULT_ROWS_COUNT) {
            size = DEFAULT_ROWS_COUNT;
        }
        for (int i = 1; i < size; i++) {
            tmp = (PDFRectangle) DIVIDER.clone();
            tmp.setY(FIRST_Y + rowHeight * i - DIVIDER.getHeight());
            arr.add(tmp);
        }
        return arr;
    }

    public PDFRectangle getTitle() {
        return title;
    }

    public PDFRectangle getMatter() {
        return matter;
    }

    public PDFRectangle getTime() {
        return time;
    }

    public PDFRectangle getDate() {
        return date;
    }

    public PDFRectangle getYear() {
        return year;
    }

    public PDFRectangle getRoom() {
        return room;
    }

    public PDFRectangle getSubtitle() {
        return subtitle;
    }

    public PDFRectangle getDirection() {
        return direction;
    }

    public PDFRectangle getAcademy() {
        return academy;
    }

    public PDFRectangle getSchool() {
        return school;
    }

    public float getRowHeight() {
        return rowHeight;
    }
}
