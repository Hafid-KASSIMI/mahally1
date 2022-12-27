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
public class StatementPage {

    private final PDFRectangle title, ministery, school, level, capacity, group, fullName, code, marks, year, pager, mats, subtitle;
    protected float MAX_HEIGHT, FIRST_Y, LAST_Y, HEIGHT;
    private final int DEFAULT_ROWS_COUNT = 20, PADDING = 2;
    protected final PDFRectangle DIVIDER, VER_DIVIDER;
    private float rowHeight, size;

    public StatementPage() {
        title = new PDFRectangle();
        ministery = new PDFRectangle();
        school = new PDFRectangle();
        level = new PDFRectangle();
        capacity = new PDFRectangle();
        group = new PDFRectangle();
        fullName = new PDFRectangle();
        code = new PDFRectangle();
        marks = new PDFRectangle();
        year = new PDFRectangle();
        pager = new PDFRectangle();
        mats = new PDFRectangle();
        subtitle = new PDFRectangle();

        title.reset(51.33634994005459f, 87.62174588251733f, 493.12891106249833f, 19.469911501609232f);
        ministery.reset(334.97284194143117f, 35.80074288203858f, 212.97002881651804f, 49.4149894075979f);
        school.reset(47.85825450583525f, 35.800745132038664f, 212.97002881651804f, 49.4149894075979f);
        level.reset(308.37359401896396f, 131.37359757149758f, 259.05648268652624f, 19.469916751609436f);
        capacity.reset(28.37147043078049f, 131.3735900714973f, 121.27616404058034f, 19.469916751609436f);
        group.reset(154.61014079477377f, 131.3735900714973f, 148.80092663288195f, 19.469916751609436f);
        fullName.reset(384.65855122211315f, 195.81468755915745f, 127.85285583578361f, 616.8584038129727f);
        code.reset(513.2916159320738f, 195.81468755915745f, 53.267408940758855f, 616.858358812971f);
        marks.reset(29.242517008798522f, 195.81471005915836f, 354.6358005672656f, 616.858358812971f);
        year.reset(47.858257508486396f, 111.90299681986173f, 103.11805159698689f, 19.470001501612707f);
        pager.reset(529.2060423454344f, 35.80074288203858f, 38.22404561999754f, 19.470001501612707f);
        mats.reset(29.242517008798522f, 154.81526847643113f, 354.63582308714916f, 39.58207352801167f);
        subtitle.reset(51.33634994005459f, 109.49767172700746f, 493.12891106249833f, 19.469911501609232f);

        title.setFormat(16f, PAvailableFonts.TIMES, true);
        subtitle.setFormat(title.getFormat());
        ministery.setFormat(9f, PAvailableFonts.MAGHRIBI);
        ministery.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        school.setFormat(9f, PAvailableFonts.MAGHRIBI);
        school.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        year.setFormat(12f, PAvailableFonts.TIMES);
        year.getFormat().setRotation(90);
        level.setFormat(12f, PAvailableFonts.TIMES);
        level.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        group.setFormat(12f, PAvailableFonts.TIMES, true);
        capacity.setFormat(12f, PAvailableFonts.TIMES);
        capacity.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        code.setFormat(12f, PAvailableFonts.TIMES);
        fullName.setFormat(14f, PAvailableFonts.TIMES);
        mats.setFormat(group.getFormat());
        pager.setFormat(code.getFormat());
        marks.setFormat(code.getFormat());

        DIVIDER = new PDFRectangle();
        DIVIDER.getFormat().setBackColor(new Color(0xFF, 0x99, 0x0));
        DIVIDER.reset(27.957f, 0f, 538.972f, 0.78f);
        FIRST_Y = code.getInkscapeY();
        HEIGHT = code.getHeight();
        MAX_HEIGHT = (HEIGHT - DIVIDER.getHeight() * (DEFAULT_ROWS_COUNT - 1)) / DEFAULT_ROWS_COUNT;
        LAST_Y = HEIGHT + FIRST_Y;
        VER_DIVIDER = new PDFRectangle();
        VER_DIVIDER.setFormat(DIVIDER.getFormat());
        VER_DIVIDER.reset(0f, mats.getInkscapeY(), 0.78f, 657.858f);
    }

    public void prepareRows(long size) {
        float h = (HEIGHT - DIVIDER.getHeight() * (size - 1)) / size;
        if (h > MAX_HEIGHT) {
            h = MAX_HEIGHT;
        }
        fullName.setHeight(h);
        code.setHeight(h);
        marks.setHeight(h);
        rowHeight = h + DIVIDER.getHeight();
        this.size = size;
    }

    public void prepareRow(int index) {
        float y = FIRST_Y + rowHeight * index;
        fullName.setY(y);
        code.setY(y);
        marks.setY(y);
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

    public ArrayList<PDFRectangle> prepareVDividers(int size) throws CloneNotSupportedException {
        ArrayList<PDFRectangle> arr = new ArrayList();
        PDFRectangle tmp;
        for (int i = 1; i < size; i++) {
            tmp = (PDFRectangle) VER_DIVIDER.clone();
            tmp.setX(marks.getX() + ((marks.getWidth() - VER_DIVIDER.getWidth() + 1) / size) * i);
            arr.add(tmp);
        }
        return arr;
    }

    public PDFRectangle getMatterCol(int index, int size) throws CloneNotSupportedException {
        PDFRectangle tmp;
        tmp = (PDFRectangle) marks.clone();
        tmp.setWidth((marks.getWidth() + VER_DIVIDER.getWidth()) / size - VER_DIVIDER.getWidth());
        tmp.setX(marks.getX() + ((marks.getWidth() - VER_DIVIDER.getWidth() + 1) / size) * index);
        return tmp;
    }

    public PDFRectangle getMatterHeaderCol(int index, int size) throws CloneNotSupportedException {
        PDFRectangle tmp;
        tmp = (PDFRectangle) mats.clone();
        tmp.setWidth((mats.getWidth() + VER_DIVIDER.getWidth()) / size - VER_DIVIDER.getWidth() - PADDING * 2);
        tmp.setX(mats.getX() + ((mats.getWidth() - VER_DIVIDER.getWidth() + 1) / size) * index + PADDING);
        return tmp;
    }

    public PDFRectangle getTitle() {
        return title;
    }

    public PDFRectangle getMinistery() {
        return ministery;
    }

    public PDFRectangle getSchool() {
        return school;
    }

    public PDFRectangle getLevel() {
        return level;
    }

    public PDFRectangle getCapacity() {
        return capacity;
    }

    public PDFRectangle getGroup() {
        return group;
    }

    public PDFRectangle getFullName() {
        return fullName;
    }

    public PDFRectangle getCode() {
        return code;
    }

    public PDFRectangle getMarks() {
        return marks;
    }

    public PDFRectangle getYear() {
        return year;
    }

    public PDFRectangle getPager() {
        return pager;
    }

    public PDFRectangle getMats() {
        return mats;
    }

    public PDFRectangle getSubtitle() {
        return subtitle;
    }

}
