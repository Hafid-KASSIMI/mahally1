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
public class PvPage {

    private final PDFRectangle title, ministery, school, level, capacity, room, fullName, code, birth, year, matter, time, date;
    protected float MAX_HEIGHT, FIRST_Y, LAST_Y, HEIGHT;
    private final int DEFAULT_ROWS_COUNT = 15;
    protected final PDFRectangle DIVIDER;
    private float rowHeight, size;

    public PvPage() {
        title = new PDFRectangle();
        ministery = new PDFRectangle();
        school = new PDFRectangle();
        level = new PDFRectangle();
        capacity = new PDFRectangle();
        room = new PDFRectangle();
        fullName = new PDFRectangle();
        code = new PDFRectangle();
        birth = new PDFRectangle();
        year = new PDFRectangle();
        matter = new PDFRectangle();
        time = new PDFRectangle();
        date = new PDFRectangle();

        title.reset(51.33634994005459f, 88.32876340981078f, 493.12891106249833f, 29.514742889376173f);
        ministery.reset(334.97284194143117f, 35.80074288203858f, 212.97002881651804f, 49.4149894075979f);
        school.reset(47.85825450583525f, 35.800745132038664f, 212.97002881651804f, 49.4149894075979f);
        level.reset(308.37359401896396f, 125.40367984103709f, 259.05648268652624f, 19.469916751609436f);
        capacity.reset(28.37147043078049f, 125.40367984103709f, 121.27616404058034f, 19.469916751609436f);
        room.reset(154.61014079477377f, 125.40367984103709f, 148.80092663288195f, 19.469916751609436f);
        fullName.reset(357.6346908942443f, 195.81471005915836f, 133.85806805133114f, 466.8583530224265f);
        code.reset(492.2730053528918f, 195.81471005915836f, 74.28600075337116f, 466.8583980224282f);
        birth.reset(294.578994276f, 195.81471005915836f, 62.27547122948591f, 466.8583980224282f);
        year.reset(47.858257508486396f, 105.93367158942411f, 103.11805159698689f, 19.470001501612707f);
        matter.reset(350.1280630253918f, 144.87359059264628f, 217.3020061734705f, 19.469916751609436f);
        time.reset(28.37147043078049f, 144.87359059264628f, 130.6594488766459f, 19.469916751609436f);
        date.reset(164.5089357931049f, 144.87359059264628f, 180.14110549196886f, 19.469916751609436f);

        title.setFormat(18f, PAvailableFonts.TIMES, true);
        ministery.setFormat(9f, PAvailableFonts.MAGHRIBI);
        ministery.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        school.setFormat(9f, PAvailableFonts.MAGHRIBI);
        school.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        year.setFormat(12f, PAvailableFonts.TIMES);
        year.getFormat().setRotation(90);
        level.setFormat(12f, PAvailableFonts.TIMES);
        level.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        room.setFormat(12f, PAvailableFonts.TIMES, true);
        capacity.setFormat(12f, PAvailableFonts.TIMES);
        capacity.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        code.setFormat(12f, PAvailableFonts.TIMES);
        fullName.setFormat(14f, PAvailableFonts.TIMES);
        date.setFormat(code.getFormat());
        time.setFormat(code.getFormat());
        matter.setFormat(level.getFormat());
        birth.setFormat(code.getFormat());

        DIVIDER = new PDFRectangle();
        DIVIDER.getFormat().setBackColor(new Color(0xFF, 0x99, 0x0));
        DIVIDER.reset(219.230f, 0f, 347.837f, 0.78f);
        FIRST_Y = code.getInkscapeY();
        HEIGHT = code.getHeight();
        MAX_HEIGHT = (HEIGHT - DIVIDER.getHeight() * (DEFAULT_ROWS_COUNT - 1)) / DEFAULT_ROWS_COUNT;
        LAST_Y = HEIGHT + FIRST_Y;
    }

    public void prepareRows(long size) {
        float h = (HEIGHT - DIVIDER.getHeight() * (size - 1)) / size;
        if (h > MAX_HEIGHT) {
            h = MAX_HEIGHT;
        }
        fullName.setHeight(h);
        code.setHeight(h);
        birth.setHeight(h);
        rowHeight = h + DIVIDER.getHeight();
        this.size = size;
    }

    public void prepareRow(int index) {
        float y = FIRST_Y + rowHeight * index;
        fullName.setY(y);
        code.setY(y);
        birth.setY(y);
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

    public PDFRectangle getRoom() {
        return room;
    }

    public PDFRectangle getFullName() {
        return fullName;
    }

    public PDFRectangle getCode() {
        return code;
    }

    public PDFRectangle getBirth() {
        return birth;
    }

    public PDFRectangle getYear() {
        return year;
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

}
