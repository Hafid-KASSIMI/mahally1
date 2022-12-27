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
public class RequisitPage {

    private final PDFRectangle to, code, room, massar, group, ministery, school, year, subject, schoolName, date, morning, afternoon, instructions;
    private final PDFRectangle[] message;
    protected float MAX_HEIGHT, FIRST_Y, LAST_Y, HEIGHT, FIRST_AM_X;
    private final int DEFAULT_ROWS_COUNT = 4;
    protected final PDFRectangle HOR_DIVIDER, VER_DIVIDER;
    private float rowHeight, rowsCount;

    public RequisitPage() {
        to = new PDFRectangle();
        code = new PDFRectangle();
        room = new PDFRectangle();
        massar = new PDFRectangle();
        group = new PDFRectangle();
        ministery = new PDFRectangle();
        school = new PDFRectangle();
        year = new PDFRectangle();
        subject = new PDFRectangle();
        schoolName = new PDFRectangle();
        date = new PDFRectangle();
        morning = new PDFRectangle();
        afternoon = new PDFRectangle();
        instructions = new PDFRectangle();
        message = new PDFRectangle[2];
        for (int i = 0; i < 2; i++) {
            message[i] = new PDFRectangle();
        }

        to.reset(121.93258862120001f, 193.63346997495455f, 351.93645471876556f, 16.76474239717989f);
        code.reset(69.26072025440492f, 210.39821312213445f, 194.29726947286395f, 16.76474239717989f);
        room.reset(69.26072025440492f, 227.16296376931467f, 194.29726947286395f, 16.76474239717989f);
        massar.reset(332.2436497405246f, 210.39821312213445f, 194.29726947286395f, 16.76474239717989f);
        group.reset(332.2436497405246f, 227.16296376931467f, 194.29726947286395f, 16.76474239717989f);
        ministery.reset(334.97284194143117f, 35.80074288203858f, 212.97002881651804f, 49.4149894075979f);
        school.reset(47.85825450583525f, 35.800745132038664f, 212.97002881651804f, 49.4149894075979f);
        year.reset(47.858257508486396f, 105.93367158942411f, 103.11805159698689f, 19.470001501612707f);
        subject.reset(71.32502640375803f, 265.67073275585426f, 453.1515836576262f, 18.639742469561696f);
        schoolName.reset(121.93258862120001f, 155.12570098841493f, 351.93645471876556f, 16.76474239717989f);
        date.reset(423.13317934031477f, 393.378720185846f, 143.10669899668522f, 201.22122026786923f);
        morning.reset(226.14247924171144f, 393.3787651858477f, 196.21049122442622f, 201.22124276787014f);
        afternoon.reset(29.151759625875737f, 393.3787651858477f, 196.21049122442622f, 201.22124276787014f);
        instructions.reset(28.37150195861754f, 740.3045910784435f, 539.0586265426049f, 73.23872307728047f);
        message[0].reset(28.761609398357216f, 287.9255136149696f, 539.0586265426049f, 73.43493208485485f);
        message[1].reset(28.37150195861754f, 597.7399505749311f, 539.0586265426049f, 73.43493208485485f);

        ministery.setFormat(9f, PAvailableFonts.MAGHRIBI);
        ministery.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        school.setFormat(9f, PAvailableFonts.MAGHRIBI);
        school.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        to.setFormat(12, PAvailableFonts.TIMES);
        code.setFormat(12, PAvailableFonts.TIMES, true);
        code.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        room.setFormat(code.getFormat());
        massar.setFormat(12, PAvailableFonts.TIMES);
        massar.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        group.setFormat(massar.getFormat());
        year.setFormat(12, PAvailableFonts.TIMES);
        year.getFormat().setRotation(90);
        subject.setFormat(12, PAvailableFonts.TIMES, true);
        subject.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        schoolName.setFormat(to.getFormat());
        date.setFormat(to.getFormat());
        morning.setFormat(to.getFormat());
        afternoon.setFormat(to.getFormat());
        message[0].setFormat(to.getFormat());
        message[1].setFormat(to.getFormat());
        instructions.setFormat(12, PAvailableFonts.TIMES);
        instructions.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);

        HOR_DIVIDER = new PDFRectangle();
        HOR_DIVIDER.getFormat().setBackColor(new Color(0xFF, 0x99, 0x0));
        HOR_DIVIDER.reset(28.346f, 0f, 538.172f, 0.78f);
        FIRST_Y = date.getInkscapeY();
        HEIGHT = date.getHeight();
        MAX_HEIGHT = (HEIGHT - HOR_DIVIDER.getHeight() * (DEFAULT_ROWS_COUNT - 1)) / DEFAULT_ROWS_COUNT;
        LAST_Y = HEIGHT + FIRST_Y;
        VER_DIVIDER = new PDFRectangle();
        VER_DIVIDER.setFormat(HOR_DIVIDER.getFormat());
        VER_DIVIDER.reset(0f, 0f, 0.78f, 0f);
        FIRST_AM_X = morning.getX() - morning.getWidth();
    }

    public void prepareRows(long size) {
        float h = (HEIGHT - HOR_DIVIDER.getHeight() * (size - 1)) / size;
        if (h > MAX_HEIGHT) {
            h = MAX_HEIGHT;
        }
        date.setHeight(h);
        morning.setHeight(h);
        afternoon.setHeight(h);
        VER_DIVIDER.setHeight(h);
        rowHeight = h + HOR_DIVIDER.getHeight();
        rowsCount = size;
    }

    public ArrayList<PDFRectangle> prepareVDividers(String halfDay, int size) throws CloneNotSupportedException {
        ArrayList<PDFRectangle> arr = new ArrayList();
        PDFRectangle tmp;
        for (int i = 1; i < size; i++) {
            tmp = (PDFRectangle) VER_DIVIDER.clone();
            if ("AM".equals(halfDay)) {
                tmp.setX(morning.getX() + ((morning.getWidth() - VER_DIVIDER.getWidth() + 1) / size) * i);
            } else {
                tmp.setX(afternoon.getX() + ((afternoon.getWidth() - VER_DIVIDER.getWidth() + 1) / size) * i);
            }
            arr.add(tmp);
        }
        return arr;
    }

    public PDFRectangle getHalfDayCol(String halfDay, int index, int size) throws CloneNotSupportedException {
        PDFRectangle tmp;
        if ("AM".equals(halfDay)) {
            tmp = (PDFRectangle) morning.clone();
            tmp.setWidth((morning.getWidth() + VER_DIVIDER.getWidth()) / size - VER_DIVIDER.getWidth());
            tmp.setX(morning.getX() + ((morning.getWidth() - VER_DIVIDER.getWidth() + 1) / size) * index);
        } else {
            tmp = (PDFRectangle) afternoon.clone();
            tmp.setWidth((afternoon.getWidth() + VER_DIVIDER.getWidth()) / size - VER_DIVIDER.getWidth());
            tmp.setX(afternoon.getX() + ((afternoon.getWidth() - VER_DIVIDER.getWidth() + 1) / size) * index);
        }
        return tmp;
    }

    public void prepareRow(int index) {
        float y = FIRST_Y + rowHeight * index;
        date.setY(y);
        morning.setY(y);
        afternoon.setY(y);
        VER_DIVIDER.setY(y);
        HOR_DIVIDER.setY(y - HOR_DIVIDER.getHeight());
    }

    public ArrayList<PDFRectangle> prepareDividers() throws CloneNotSupportedException {
        ArrayList<PDFRectangle> arr = new ArrayList();
        PDFRectangle tmp;
        if (rowsCount < DEFAULT_ROWS_COUNT) {
            rowsCount = DEFAULT_ROWS_COUNT;
        }
        for (int i = 1; i < rowsCount; i++) {
            tmp = (PDFRectangle) HOR_DIVIDER.clone();
            tmp.setY(FIRST_Y + rowHeight * i - HOR_DIVIDER.getHeight());
            arr.add(tmp);
        }
        return arr;
    }

    public PDFRectangle getTo() {
        return to;
    }

    public PDFRectangle getCode() {
        return code;
    }

    public PDFRectangle getRoom() {
        return room;
    }

    public PDFRectangle getMassar() {
        return massar;
    }

    public PDFRectangle getGroup() {
        return group;
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

    public PDFRectangle getSubject() {
        return subject;
    }

    public PDFRectangle getSchoolName() {
        return schoolName;
    }

    public PDFRectangle getDate() {
        return date;
    }

    public PDFRectangle getMorning() {
        return morning;
    }

    public PDFRectangle getAfternoon() {
        return afternoon;
    }

    public PDFRectangle getInstructions() {
        return instructions;
    }

    public PDFRectangle getMessage(int index) {
        return message[index];
    }

    public int getMessageLength() {
        return message.length;
    }

    public float getRowHeight() {
        return rowHeight;
    }
}
