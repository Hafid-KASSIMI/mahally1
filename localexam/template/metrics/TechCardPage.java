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
public class TechCardPage {

    private final PDFRectangle title, ministery, school, label, numero, year, subtitle, firstCode, lastCode, size, girls, boys, roomsCount, roomsMaxSize, roomsMinSize, averageRoomsSize, studentsCount, girlsCount, boysCount;
    protected float MAX_HEIGHT, FIRST_Y, LAST_Y, HEIGHT;
    private final int DEFAULT_ROWS_COUNT = 20;
    protected final PDFRectangle DIVIDER;
    private float rowHeight, rowsCount;

    public TechCardPage() {
        title = new PDFRectangle();
        ministery = new PDFRectangle();
        school = new PDFRectangle();
        label = new PDFRectangle();
        numero = new PDFRectangle();
        year = new PDFRectangle();
        subtitle = new PDFRectangle();
        firstCode = new PDFRectangle();
        lastCode = new PDFRectangle();
        size = new PDFRectangle();
        girls = new PDFRectangle();
        boys = new PDFRectangle();
        roomsCount = new PDFRectangle();
        roomsMaxSize = new PDFRectangle();
        roomsMinSize = new PDFRectangle();
        averageRoomsSize = new PDFRectangle();
        studentsCount = new PDFRectangle();
        girlsCount = new PDFRectangle();
        boysCount = new PDFRectangle();

        title.reset(51.33634994005459f, 87.1472508642001f, 493.12891106249833f, 25.76474274461256f);
        ministery.reset(334.97284194143117f, 35.80074288203858f, 212.97002881651804f, 49.4149894075979f);
        school.reset(47.85825450583525f, 35.800745132038664f, 212.97002881651804f, 49.4149894075979f);
        label.reset(432.7009695827689f, 258.8146674911853f, 103.83160161568851f, 553.9491813844485f);
        numero.reset(537.3127725660066f, 258.8146674911853f, 29.246245550860984f, 553.9491363844468f);
        year.reset(47.858257508486396f, 105.93367158942411f, 103.11805159698689f, 19.470001501612707f);
        subtitle.reset(51.33634994005459f, 114.84351443337638f, 493.12891106249833f, 25.76474274461256f);
        firstCode.reset(328.8690151555705f, 258.8146674911853f, 103.05173053976557f, 553.9491813844485f);
        lastCode.reset(225.03705322174423f, 258.8146674911853f, 103.05173053976557f, 553.9491813844485f);
        size.reset(152.2353440021836f, 258.8146674911853f, 72.02151010399984f, 553.9491813844485f);
        girls.reset(90.69353154613442f, 258.8146674911853f, 60.761648621639345f, 553.9491813844485f);
        boys.reset(29.151716838096885f, 258.8146674911853f, 60.76159757656984f, 553.9491813844485f);
        roomsCount.reset(495.0767233552803f, 188.26385226766783f, 71.16312120189427f, 21.221107569211757f);
        roomsMaxSize.reset(423.1333670060115f, 188.26385226766783f, 71.16312120189427f, 21.221107569211757f);
        roomsMinSize.reset(351.1898905506967f, 188.26385226766783f, 71.16312120189427f, 21.221107569211757f);
        averageRoomsSize.reset(279.2465567213115f, 188.26385226766783f, 71.16312120189427f, 21.221107569211757f);
        studentsCount.reset(189.29164733096067f, 188.26382976766695f, 79.13013431125738f, 21.221107569211757f);
        girlsCount.reset(109.38130414552624f, 188.26382976766695f, 79.13013431125738f, 21.221107569211757f);
        boysCount.reset(29.47091667098738f, 188.26382976766695f, 79.13013431125738f, 21.22112631921248f);

        title.setFormat(18f, PAvailableFonts.TIMES, true);
        subtitle.setFormat(16, PAvailableFonts.TIMES, true);
        ministery.setFormat(9f, PAvailableFonts.MAGHRIBI);
        ministery.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        school.setFormat(9f, PAvailableFonts.MAGHRIBI);
        school.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        year.setFormat(12f, PAvailableFonts.TIMES);
        year.getFormat().setRotation(90);

        label.setFormat(12, PAvailableFonts.TIMES);
        numero.setFormat(label.getFormat());
        firstCode.setFormat(label.getFormat());
        lastCode.setFormat(label.getFormat());
        size.setFormat(label.getFormat());
        girls.setFormat(label.getFormat());
        boys.setFormat(label.getFormat());

        roomsCount.setFormat(14, PAvailableFonts.TIMES);
        roomsMaxSize.setFormat(roomsCount.getFormat());
        roomsMinSize.setFormat(roomsCount.getFormat());
        averageRoomsSize.setFormat(roomsCount.getFormat());
        studentsCount.setFormat(roomsCount.getFormat());
        girlsCount.setFormat(roomsCount.getFormat());
        boysCount.setFormat(roomsCount.getFormat());

        DIVIDER = new PDFRectangle();
        DIVIDER.getFormat().setBackColor(new Color(0xFF, 0x99, 0x0));
        DIVIDER.reset(28.346f, 0f, 538.736f, 0.780f);
        FIRST_Y = numero.getInkscapeY();
        HEIGHT = numero.getHeight();
        MAX_HEIGHT = (HEIGHT - DIVIDER.getHeight() * (DEFAULT_ROWS_COUNT - 1)) / DEFAULT_ROWS_COUNT;
        LAST_Y = HEIGHT + FIRST_Y;
    }

    public void prepareRows(long rowsCount) {
        float h = (HEIGHT - DIVIDER.getHeight() * (rowsCount - 1)) / rowsCount;
        if (h > MAX_HEIGHT) {
            h = MAX_HEIGHT;
        }
        numero.setHeight(h);
        label.setHeight(h);
        lastCode.setHeight(h);
        firstCode.setHeight(h);
        size.setHeight(h);
        girls.setHeight(h);
        boys.setHeight(h);
        rowHeight = h + DIVIDER.getHeight();
        this.rowsCount = rowsCount;
    }

    public void prepareRow(int index) {
        float y = FIRST_Y + rowHeight * index;
        numero.setY(y);
        label.setY(y);
        firstCode.setY(y);
        lastCode.setY(y);
        size.setY(y);
        girls.setY(y);
        boys.setY(y);
        DIVIDER.setY(y - DIVIDER.getHeight());
    }

    public ArrayList<PDFRectangle> prepareDividers() throws CloneNotSupportedException {
        ArrayList<PDFRectangle> arr = new ArrayList();
        PDFRectangle tmp;
        if (rowsCount < DEFAULT_ROWS_COUNT) {
            rowsCount = DEFAULT_ROWS_COUNT;
        }
        for (int i = 1; i < rowsCount; i++) {
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

    public PDFRectangle getLabel() {
        return label;
    }

    public PDFRectangle getNumero() {
        return numero;
    }

    public PDFRectangle getYear() {
        return year;
    }

    public PDFRectangle getSubtitle() {
        return subtitle;
    }

    public PDFRectangle getFirstCode() {
        return firstCode;
    }

    public PDFRectangle getLastCode() {
        return lastCode;
    }

    public PDFRectangle getSize() {
        return size;
    }

    public PDFRectangle getGirls() {
        return girls;
    }

    public PDFRectangle getBoys() {
        return boys;
    }

    public PDFRectangle getRoomsCount() {
        return roomsCount;
    }

    public PDFRectangle getRoomsMaxSize() {
        return roomsMaxSize;
    }

    public PDFRectangle getRoomsMinSize() {
        return roomsMinSize;
    }

    public PDFRectangle getAverageRoomsSize() {
        return averageRoomsSize;
    }

    public PDFRectangle getStudentsCount() {
        return studentsCount;
    }

    public PDFRectangle getGirlsCount() {
        return girlsCount;
    }

    public PDFRectangle getBoysCount() {
        return boysCount;
    }

    public float getRowHeight() {
        return rowHeight;
    }
}
