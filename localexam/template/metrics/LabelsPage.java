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
public class LabelsPage {

    private final PDFRectangle[] title, ministery, fullName, school, code, room;

    private final int SIZE = 12;

    public LabelsPage() {
        title = new PDFRectangle[12];
        for (int i = 0; i < 12; i++) {
            title[i] = new PDFRectangle();
        }
        ministery = new PDFRectangle[12];
        for (int i = 0; i < 12; i++) {
            ministery[i] = new PDFRectangle();
        }
        fullName = new PDFRectangle[12];
        for (int i = 0; i < 12; i++) {
            fullName[i] = new PDFRectangle();
        }
        school = new PDFRectangle[12];
        for (int i = 0; i < 12; i++) {
            school[i] = new PDFRectangle();
        }
        code = new PDFRectangle[12];
        for (int i = 0; i < 12; i++) {
            code[i] = new PDFRectangle();
        }
        room = new PDFRectangle[12];
        for (int i = 0; i < 12; i++) {
            room[i] = new PDFRectangle();
        }

        title[0].reset(302.15654476495735f, 58.55987176062354f, 279.4593320882541f, 29.514742889376173f);
        title[1].reset(14.185755107954098f, 58.55987176062354f, 279.4593320882541f, 29.514742889376173f);
        title[2].reset(302.1558166220541f, 194.65828501451614f, 279.4593320882541f, 29.514742889376173f);
        title[3].reset(14.009149676734754f, 194.65828501451614f, 279.4593320882541f, 29.514742889376173f);
        title[4].reset(302.1565222450738f, 330.7567177684095f, 279.4593320882541f, 29.514742889376173f);
        title[5].reset(14.00987631831246f, 330.7567177684095f, 279.4593320882541f, 29.514742889376173f);
        title[6].reset(302.15654476495735f, 466.85515052230284f, 279.4593320882541f, 29.514742889376173f);
        title[7].reset(14.009880071626394f, 466.85515052230284f, 279.4593320882541f, 29.514742889376173f);
        title[8].reset(302.15654476495735f, 602.953605776197f, 279.4593320882541f, 29.514742889376173f);
        title[9].reset(14.009880071626394f, 602.953605776197f, 279.4593320882541f, 29.514742889376173f);
        title[10].reset(302.15510349240657f, 739.0520160300895f, 279.4593320882541f, 29.514742889376173f);
        title[11].reset(14.00843954973836f, 739.0520160300895f, 279.4593320882541f, 29.514742889376173f);
        ministery[0].reset(469.2533529827098f, 22.123949354064724f, 112.36250885724591f, 36.43592690655899f);
        ministery[1].reset(181.28256332570655f, 22.123949354064724f, 112.36250885724591f, 36.43592690655899f);
        ministery[2].reset(469.2526248398066f, 158.22238360795814f, 112.36250885724591f, 36.43592690655899f);
        ministery[3].reset(181.10596239846393f, 158.22238360795814f, 112.36250885724591f, 36.43592690655899f);
        ministery[4].reset(469.2533529827098f, 294.32080886185116f, 112.36250885724591f, 36.43592690655899f);
        ministery[5].reset(181.10668303473935f, 294.32080886185116f, 112.36250885724591f, 36.43592690655899f);
        ministery[6].reset(469.2533529827098f, 430.4192191157437f, 112.36250885724591f, 36.43592690655899f);
        ministery[7].reset(181.10669804799508f, 430.4192191157437f, 112.36250885724591f, 36.43592690655899f);
        ministery[8].reset(469.2533529827098f, 566.5176743696378f, 112.36250885724591f, 36.43592690655899f);
        ministery[9].reset(181.10668303473935f, 566.5176743696378f, 112.36250885724591f, 36.43592690655899f);
        ministery[10].reset(469.2518891902754f, 702.6160846235304f, 112.36250885724591f, 36.43592690655899f);
        ministery[11].reset(181.1052417621885f, 702.6160846235304f, 112.36250885724591f, 36.43592690655899f);
        fullName[0].reset(302.15656728484095f, 88.07461839999986f, 279.45786078919184f, 29.0158578701174f);
        fullName[1].reset(14.18577837850049f, 88.07461839999986f, 279.45786078919184f, 29.0158578701174f);
        fullName[2].reset(302.1558391419377f, 224.17303615389264f, 279.45786078919184f, 29.0158578701174f);
        fullName[3].reset(14.009172947281147f, 224.17303615389264f, 279.45786078919184f, 29.0158578701174f);
        fullName[4].reset(302.15654476495735f, 360.27146890778596f, 279.45786078919184f, 29.0158578701174f);
        fullName[5].reset(14.009898838196065f, 360.27146890778596f, 279.45786078919184f, 29.0158578701174f);
        fullName[6].reset(302.15656728484095f, 496.3698791616784f, 279.45786078919184f, 29.0158578701174f);
        fullName[7].reset(14.009903342172787f, 496.3698791616784f, 279.45786078919184f, 29.0158578701174f);
        fullName[8].reset(302.15656728484095f, 632.4683344155726f, 279.45786078919184f, 29.0158578701174f);
        fullName[9].reset(14.009903342172787f, 632.4683344155726f, 279.45786078919184f, 29.0158578701174f);
        fullName[10].reset(302.15512601229017f, 768.5667296694645f, 279.45786078919184f, 29.0158578701174f);
        fullName[11].reset(14.008462820284755f, 768.5667296694645f, 279.45786078919184f, 29.0158578701174f);
        school[0].reset(302.15654476495735f, 22.123949354064724f, 112.36250885724591f, 36.43592690655899f);
        school[1].reset(14.185755107954098f, 22.123949354064724f, 112.36250885724591f, 36.43592690655899f);
        school[2].reset(302.1558166220541f, 158.22238360795814f, 112.36250885724591f, 36.43592690655899f);
        school[3].reset(14.009149676734754f, 158.22238360795814f, 112.36250885724591f, 36.43592690655899f);
        school[4].reset(302.1565222450738f, 294.32080886185116f, 112.36250885724591f, 36.43592690655899f);
        school[5].reset(14.00987631831246f, 294.32080886185116f, 112.36250885724591f, 36.43592690655899f);
        school[6].reset(302.15654476495735f, 430.4192191157437f, 112.36250885724591f, 36.43592690655899f);
        school[7].reset(14.009880071626394f, 430.4192191157437f, 112.36250885724591f, 36.43592690655899f);
        school[8].reset(302.15654476495735f, 566.5176743696378f, 112.36250885724591f, 36.43592690655899f);
        school[9].reset(14.009880071626394f, 566.5176743696378f, 112.36250885724591f, 36.43592690655899f);
        school[10].reset(302.15510349240657f, 702.6160846235304f, 112.36250885724591f, 36.43592690655899f);
        school[11].reset(14.00843954973836f, 702.6160846235304f, 112.36250885724591f, 36.43592690655899f);
        code[0].reset(441.88696522518524f, 117.090469520117f, 139.72748536873115f, 30.311125920119448f);
        code[1].reset(153.91617556818196f, 117.090469520117f, 139.72748536873115f, 30.311125920119448f);
        code[2].reset(441.8862370822819f, 253.18888727400977f, 139.72748536873115f, 30.311125920119448f);
        code[3].reset(153.73957464093937f, 253.18888727400977f, 139.72748536873115f, 30.311125920119448f);
        code[4].reset(441.88696522518524f, 389.28732002790315f, 139.72748536873115f, 30.311125920119448f);
        code[5].reset(153.74030278384262f, 389.28732002790315f, 139.72748536873115f, 30.311125920119448f);
        code[6].reset(441.88696522518524f, 525.3857302817955f, 139.72748536873115f, 30.311125920119448f);
        code[7].reset(153.7403102904705f, 525.3857302817955f, 139.72748536873115f, 30.311125920119448f);
        code[8].reset(441.88696522518524f, 661.4841855356898f, 139.72748536873115f, 30.311125920119448f);
        code[9].reset(153.74030278384262f, 661.4841855356898f, 139.72748536873115f, 30.311125920119448f);
        code[10].reset(441.8855014327508f, 797.5826557895847f, 139.72748536873115f, 30.311125920119448f);
        code[11].reset(153.73885400466392f, 797.5826557895847f, 139.72748536873115f, 30.311125920119448f);
        room[0].reset(302.15654476495735f, 117.090469520117f, 139.72748536873115f, 30.311125920119448f);
        room[1].reset(14.185755107954098f, 117.090469520117f, 139.72748536873115f, 30.311125920119448f);
        room[2].reset(302.1558166220541f, 253.18888727400977f, 139.72748536873115f, 30.311125920119448f);
        room[3].reset(14.009149676734754f, 253.18888727400977f, 139.72748536873115f, 30.311125920119448f);
        room[4].reset(302.1565222450738f, 389.28732002790315f, 139.72748536873115f, 30.311125920119448f);
        room[5].reset(14.00987631831246f, 389.28732002790315f, 139.72748536873115f, 30.311125920119448f);
        room[6].reset(302.15654476495735f, 525.3857302817955f, 139.72748536873115f, 30.311125920119448f);
        room[7].reset(14.009880071626394f, 525.3857302817955f, 139.72748536873115f, 30.311125920119448f);
        room[8].reset(302.15654476495735f, 661.4841855356898f, 139.72748536873115f, 30.311125920119448f);
        room[9].reset(14.009880071626394f, 661.4841855356898f, 139.72748536873115f, 30.311125920119448f);
        room[10].reset(302.15510349240657f, 797.5826557895847f, 139.72748536873115f, 30.311125920119448f);
        room[11].reset(14.00843954973836f, 797.5826557895847f, 139.72748536873115f, 30.311125920119448f);

        title[0].setFormat(12, PAvailableFonts.TIMES);
        ministery[0].setFormat(9, PAvailableFonts.MAGHRIBI);
        ministery[0].getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        fullName[0].setFormat(16, PAvailableFonts.TIMES, true);
        school[0].setFormat(9, PAvailableFonts.MAGHRIBI);
        school[0].getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        code[0].setFormat(26, PAvailableFonts.ICOMOON, true);
        room[0].setFormat(16, PAvailableFonts.TIMES, true);
        for (int i = 1; i < 12; i++) {
            title[i].setFormat(title[0].getFormat());
            ministery[i].setFormat(ministery[0].getFormat());
            fullName[i].setFormat(fullName[0].getFormat());
            school[i].setFormat(school[0].getFormat());
            code[i].setFormat(code[0].getFormat());
            room[i].setFormat(room[0].getFormat());
        }
    }

    public PDFRectangle getTitle(int index) {
        return title[index];
    }

    public int getTitleLength() {
        return title.length;
    }

    public PDFRectangle getMinistery(int index) {
        return ministery[index];
    }

    public int getMinisteryLength() {
        return ministery.length;
    }

    public PDFRectangle getFullName(int index) {
        return fullName[index];
    }

    public int getFullNameLength() {
        return fullName.length;
    }

    public PDFRectangle getSchool(int index) {
        return school[index];
    }

    public int getSchoolLength() {
        return school.length;
    }

    public PDFRectangle getCode(int index) {
        return code[index];
    }

    public int getCodeLength() {
        return code.length;
    }

    public PDFRectangle getRoom(int index) {
        return room[index];
    }

    public int getRoomLength() {
        return room.length;
    }

    public int getSize() {
        return SIZE;
    }

}
