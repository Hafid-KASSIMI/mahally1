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
public class List1Page {

    private final PDFRectangle title, ministery, school, level, capacity, room, fullName, code, gender, massar, group, year;
    protected float MAX_HEIGHT, FIRST_Y, LAST_Y, HEIGHT;
    private final int DEFAULT_ROWS_COUNT = 20;
    protected final PDFRectangle DIVIDER;
    private float rowHeight, size;

    public List1Page() {
        title = new PDFRectangle();
        ministery = new PDFRectangle();
        school = new PDFRectangle();
        level = new PDFRectangle();
        capacity = new PDFRectangle();
        room = new PDFRectangle();
        fullName = new PDFRectangle();
        code = new PDFRectangle();
        gender = new PDFRectangle();
        massar = new PDFRectangle();
        group = new PDFRectangle();
        year = new PDFRectangle();

        title.reset(51.33634994005459f, 88.32876340981078f, 493.12891106249833f, 29.514742889376173f);
        ministery.reset(334.677f, 35.80074288203858f, 212.782f, 49.4149894075979f);
        school.reset(47.816f, 35.800748132038784f, 212.782f, 49.41498340759766f);
        level.reset(308.37359401896396f, 131.3735900714973f, 259.05648268652624f, 19.469916751609436f);
        capacity.reset(28.37147043078049f, 131.3735900714973f, 121.27616404058034f, 19.469916751609436f);
        room.reset(154.61014079477377f, 131.3735900714973f, 148.80092663288195f, 19.469916751609436f);
        fullName.reset(297.5816679434246f, 195.81468755915745f, 171.3912449287328f, 616.8584563129747f);
        code.reset(469.75312174633444f, 195.8146650591566f, 96.80589336788196f, 616.8584038129727f);
        gender.reset(222.03443210071475f, 195.81468755915745f, 74.7669916873818f, 616.8584563129747f);
        massar.reset(110.93633964169835f, 195.8146650591566f, 110.31787607821148f, 616.8584563129747f);
        group.reset(29.242517008798522f, 195.8146650591566f, 80.91360400010656f, 616.8584563129747f);
        year.reset(47.816f, 111.903f, 103.027f, 19.470f);

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
        gender.setFormat(code.getFormat());
        massar.setFormat(code.getFormat());
        group.setFormat(code.getFormat());

        DIVIDER = new PDFRectangle();
        DIVIDER.getFormat().setBackColor(new Color(0xFF, 0x99, 0x0));
        DIVIDER.reset(27.638f, 0f, 539.610f, 0.961f);
        FIRST_Y = 195.815f;
        HEIGHT = 616.858f;
        MAX_HEIGHT = (HEIGHT - DIVIDER.getHeight() * (DEFAULT_ROWS_COUNT - 1)) / DEFAULT_ROWS_COUNT;
        LAST_Y = HEIGHT + FIRST_Y;
    }

    public void prepareRows(long size) {
        float h = (HEIGHT - DIVIDER.getHeight() * (size - 1)) / size;
        if (h > MAX_HEIGHT) {
            h = MAX_HEIGHT;
        }
        massar.setHeight(h);
        fullName.setHeight(h);
        gender.setHeight(h);
        code.setHeight(h);
        group.setHeight(h);
        rowHeight = h + DIVIDER.getHeight();
        this.size = size;
    }

    public void prepareRow(int index) {
        float y = FIRST_Y + rowHeight * index;
        massar.setY(y);
        fullName.setY(y);
        gender.setY(y);
        code.setY(y);
        group.setY(y);
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

    public PDFRectangle getGender() {
        return gender;
    }

    public PDFRectangle getMassar() {
        return massar;
    }

    public PDFRectangle getGroup() {
        return group;
    }

    public PDFRectangle getYear() {
        return year;
    }

    public float getRowHeight() {
        return rowHeight;
    }
}
