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
public class List3Page {

    private final PDFRectangle title, ministery, school, year;
    private final PDFRectangle[] fullName, code, gender, capacity, room, male, female;
    protected float MAX_HEIGHT, FIRST_Y, LAST_Y, HEIGHT;
    private final int DEFAULT_ROWS_COUNT = 20;
    protected final PDFRectangle DIVIDER;
    private float rowHeight, size;

    public List3Page() {
        title = new PDFRectangle();
        ministery = new PDFRectangle();
        school = new PDFRectangle();
        year = new PDFRectangle();
        fullName = new PDFRectangle[2];
        code = new PDFRectangle[2];
        gender = new PDFRectangle[2];
        capacity = new PDFRectangle[2];
        room = new PDFRectangle[2];
        male = new PDFRectangle[2];
        female = new PDFRectangle[2];
        for (int i = 0; i < 2; i++) {
            fullName[i] = new PDFRectangle();
            code[i] = new PDFRectangle();
            gender[i] = new PDFRectangle();
            capacity[i] = new PDFRectangle();
            room[i] = new PDFRectangle();
            male[i] = new PDFRectangle();
            female[i] = new PDFRectangle();
        }

        title.reset(51.33634994005459f, 90.45894349204346f, 493.12891106249833f, 29.514742889376173f);
        ministery.reset(334.97284194143117f, 35.80074288203858f, 212.97002881651804f, 49.4149894075979f);
        school.reset(47.85825450583525f, 35.800745132038664f, 212.97002881651804f, 49.4149894075979f);
        year.reset(47.858257508486396f, 111.90299681986173f, 103.11805159698689f, 19.470001501612707f);
        fullName[0].reset(349.9685096500393f, 192.19103241927124f, 136.8607267055f, 595.8584555022985f);
        fullName[1].reset(74.83206289106296f, 192.19103241927124f, 136.8607267055f, 595.8584555022985f);
        code[0].reset(487.6094527363443f, 192.19100991927036f, 78.78999398926393f, 595.8584555022985f);
        code[1].reset(212.47302999857703f, 192.19100991927036f, 78.78999398926393f, 595.8584555022985f);
        gender[0].reset(304.4477777761115f, 192.19100991927036f, 44.74050348251836f, 595.8584555022985f);
        gender[1].reset(29.311337022437375f, 192.19100991927036f, 44.74050348251836f, 595.8584555022985f);
        capacity[0].reset(487.609407696577f, 794.0722056540684f, 79.57021787669672f, 19.471182751658308f);
        capacity[1].reset(212.47296243892623f, 794.0722056540684f, 79.57021787669672f, 19.471182751658308f);
        room[0].reset(361.0685677863393f, 125.21689983382669f, 148.80092663288195f, 19.469916751609436f);
        room[1].reset(85.93213003531639f, 125.21689983382669f, 148.80092663288195f, 19.469916751609436f);
        male[0].reset(395.63846577937215f, 794.0722056540684f, 79.57021787669672f, 19.471182751658308f);
        male[1].reset(120.50202052172132f, 794.0722056540684f, 79.57021787669672f, 19.471182751658308f);
        female[0].reset(303.66753887542296f, 794.0721306540656f, 79.57021787669672f, 19.471182751658308f);
        female[1].reset(28.531101875062785f, 794.0721306540656f, 79.57021787669672f, 19.471182751658308f);

        title.setFormat(18f, PAvailableFonts.TIMES, true);
        ministery.setFormat(9f, PAvailableFonts.MAGHRIBI);
        ministery.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        school.setFormat(9f, PAvailableFonts.MAGHRIBI);
        school.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        year.setFormat(12f, PAvailableFonts.TIMES);
        year.getFormat().setRotation(90);
        fullName[0].setFormat(12, PAvailableFonts.TIMES);
        code[0].setFormat(fullName[0].getFormat());
        gender[0].setFormat(fullName[0].getFormat());
        room[0].setFormat(fullName[0].getFormat());
        capacity[0].setFormat(12f, PAvailableFonts.ICOMOON);
        male[0].setFormat(capacity[0].getFormat());
        female[0].setFormat(capacity[0].getFormat());
        for (int i = 1; i < 2; i++) {
            fullName[i].setFormat(fullName[0].getFormat());
            code[i].setFormat(code[0].getFormat());
            gender[i].setFormat(gender[0].getFormat());
            capacity[i].setFormat(capacity[0].getFormat());
            room[i].setFormat(room[0].getFormat());
            male[i].setFormat(male[0].getFormat());
            female[i].setFormat(female[0].getFormat());
        }

        DIVIDER = new PDFRectangle();
        DIVIDER.getFormat().setBackColor(new Color(0xFF, 0x99, 0x0));
        DIVIDER.reset(0f, 0f, 261.720f, 0.78f);
        FIRST_Y = code[0].getInkscapeY();
        HEIGHT = code[0].getHeight();
        MAX_HEIGHT = (HEIGHT - DIVIDER.getHeight() * (DEFAULT_ROWS_COUNT - 1)) / DEFAULT_ROWS_COUNT;
        LAST_Y = HEIGHT + FIRST_Y;
    }

    public void prepareRows(int col, long size) {
        float h = (HEIGHT - DIVIDER.getHeight() * (size - 1)) / size;
        if (h > MAX_HEIGHT) {
            h = MAX_HEIGHT;
        }
        fullName[col].setHeight(h);
        gender[col].setHeight(h);
        code[col].setHeight(h);
        rowHeight = h + DIVIDER.getHeight();
        this.size = size;
    }

    public void prepareRow(int col, int index) {
        float y = FIRST_Y + rowHeight * index;
        fullName[col].setY(y);
        gender[col].setY(y);
        code[col].setY(y);
        DIVIDER.setY(y - DIVIDER.getHeight());
    }

    public ArrayList<PDFRectangle> prepareDividers(int col) throws CloneNotSupportedException {
        ArrayList<PDFRectangle> arr = new ArrayList();
        PDFRectangle tmp;
        if (size < DEFAULT_ROWS_COUNT) {
            size = DEFAULT_ROWS_COUNT;
        }
        for (int i = 1; i < size; i++) {
            tmp = (PDFRectangle) DIVIDER.clone();
            tmp.setY(FIRST_Y + rowHeight * i - DIVIDER.getHeight());
            tmp.setX(gender[col].getX());
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

    public PDFRectangle getYear() {
        return year;
    }

    public PDFRectangle getFullName(int index) {
        return fullName[index];
    }

    public int getFullNameLength() {
        return fullName.length;
    }

    public PDFRectangle getCode(int index) {
        return code[index];
    }

    public int getCodeLength() {
        return code.length;
    }

    public PDFRectangle getGender(int index) {
        return gender[index];
    }

    public int getGenderLength() {
        return gender.length;
    }

    public PDFRectangle getCapacity(int index) {
        return capacity[index];
    }

    public int getCapacityLength() {
        return capacity.length;
    }

    public PDFRectangle getRoom(int index) {
        return room[index];
    }

    public int getRoomLength() {
        return room.length;
    }

    public PDFRectangle getMale(int index) {
        return male[index];
    }

    public int getMaleLength() {
        return male.length;
    }

    public PDFRectangle getFemale(int index) {
        return female[index];
    }

    public int getFemaleLength() {
        return female.length;
    }

    public float getRowHeight() {
        return rowHeight;
    }
}
