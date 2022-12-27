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
public class ClosingPvPage {

    private final PDFRectangle title, ministery, school, matter, year, subtitle, date, hour, count, observerSignature, directorSignature;
    private final PDFRectangle[] fullName, num, code, status, signature;

    public ClosingPvPage() {
        title = new PDFRectangle();
        ministery = new PDFRectangle();
        school = new PDFRectangle();
        matter = new PDFRectangle();
        year = new PDFRectangle();
        subtitle = new PDFRectangle();
        date = new PDFRectangle();
        hour = new PDFRectangle();
        count = new PDFRectangle();
        observerSignature = new PDFRectangle();
        directorSignature = new PDFRectangle();
        fullName = new PDFRectangle[5];
        num = new PDFRectangle[5];
        code = new PDFRectangle[5];
        status = new PDFRectangle[5];
        signature = new PDFRectangle[5];
        for (int i = 0; i < 5; i++) {
            fullName[i] = new PDFRectangle();
            num[i] = new PDFRectangle();
            code[i] = new PDFRectangle();
            status[i] = new PDFRectangle();
            signature[i] = new PDFRectangle();
        }

        title.reset(51.33634994005459f, 117.77239204644168f, 493.12891106249833f, 29.514742889376173f);
        ministery.reset(349.33777274998687f, 42.374821635821874f, 198.60510551459015f, 64.39123748573542f);
        school.reset(47.858257508486396f, 42.374821635821874f, 198.60510551459015f, 64.39123748573542f);
        matter.reset(51.33634994005459f, 207.94485802742605f, 279.72389568086396f, 19.469918251609492f);
        year.reset(47.858257508486396f, 111.90299681986173f, 103.11805159698689f, 19.470001501612707f);
        subtitle.reset(51.33634994005459f, 158.29346861070226f, 493.12891106249833f, 29.514742889376173f);
        date.reset(241.1400236072869f, 235.24515158131646f, 261.38702040120165f, 19.469916751609436f);
        hour.reset(51.33634994005459f, 235.24515158131646f, 110.26370825150984f, 19.469916751609436f);
        count.reset(465.9079041669099f, 260.5801000593374f, 31.467158724128033f, 19.469916751609436f);
        observerSignature.reset(306.36225313452786f, 700.6197795464658f, 261.38702040120165f, 19.469916751609436f);
        directorSignature.reset(27.662194685987377f, 700.6197795464658f, 261.38702040120165f, 19.469916751609436f);
        fullName[0].reset(351.6294336389295f, 332.98236535432756f, 177.39651719730327f, 33.0480695257754f);
        fullName[1].reset(351.6294336389295f, 366.80996166019554f, 177.39651719730327f, 33.0480695257754f);
        fullName[2].reset(351.6294336389295f, 400.63752796606235f, 177.39651719730327f, 33.0480695257754f);
        fullName[3].reset(351.6294336389295f, 434.46516927193204f, 177.39651719730327f, 33.0480695257754f);
        fullName[4].reset(351.6294336389295f, 468.292713077798f, 177.39651719730327f, 33.0480695257754f);
        num[0].reset(529.8061446971541f, 332.98236535432756f, 36.752895939597046f, 33.0480695257754f);
        num[1].reset(529.8061446971541f, 366.80996166019554f, 36.752895939597046f, 33.0480695257754f);
        num[2].reset(529.8061446971541f, 400.63752796606235f, 36.752895939597046f, 33.0480695257754f);
        num[3].reset(529.8061446971541f, 434.46516927193204f, 36.752895939597046f, 33.0480695257754f);
        num[4].reset(529.8061446971541f, 468.292713077798f, 36.752895939597046f, 33.0480695257754f);
        code[0].reset(267.6878436222246f, 332.9823203543258f, 83.16135862264427f, 33.048115275777164f);
        code[1].reset(267.6878436222246f, 366.8099091601935f, 83.16135862264427f, 33.048115275777164f);
        code[2].reset(267.6878436222246f, 400.63752796606235f, 83.16135862264427f, 33.048115275777164f);
        code[3].reset(267.6878436222246f, 434.46507177192825f, 83.16135862264427f, 33.048115275777164f);
        code[4].reset(267.6878436222246f, 468.292713077798f, 83.16135862264427f, 33.048115275777164f);
        status[0].reset(123.69332073423442f, 332.9823203543258f, 143.21430650718523f, 33.048115275777164f);
        status[1].reset(123.69332073423442f, 366.8099091601935f, 143.21430650718523f, 33.048115275777164f);
        status[2].reset(123.69332073423442f, 400.63752796606235f, 143.21430650718523f, 33.048115275777164f);
        status[3].reset(123.69332073423442f, 434.46507177192825f, 143.21430650718523f, 33.048115275777164f);
        status[4].reset(123.69332073423442f, 468.292713077798f, 143.21430650718523f, 33.048115275777164f);
        signature[0].reset(29.242517008798522f, 332.9823203543258f, 93.67058509264263f, 33.048115275777164f);
        signature[1].reset(29.242517008798522f, 366.8099091601935f, 93.67058509264263f, 33.048115275777164f);
        signature[2].reset(29.242517008798522f, 400.63752796606235f, 93.67058509264263f, 33.048115275777164f);
        signature[3].reset(29.242517008798522f, 434.46507177192825f, 93.67058509264263f, 33.048115275777164f);
        signature[4].reset(29.242517008798522f, 468.292713077798f, 93.67058509264263f, 33.048115275777164f);

        title.setFormat(18f, PAvailableFonts.TIMES, true);
        subtitle.setFormat(16, PAvailableFonts.TIMES, true);
        ministery.setFormat(11f, PAvailableFonts.MAGHRIBI);
        ministery.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        school.setFormat(11f, PAvailableFonts.MAGHRIBI);
        school.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        year.setFormat(12f, PAvailableFonts.TIMES);
        year.getFormat().setRotation(90);
        matter.setFormat(16, PAvailableFonts.TIMES);
        matter.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        date.setFormat(matter.getFormat());
        hour.setFormat(matter.getFormat());
        count.setFormat(matter.getFormat());
        observerSignature.setFormat(14, PAvailableFonts.TIMES, true);
        observerSignature.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        directorSignature.setFormat(14, PAvailableFonts.TIMES, true);
        directorSignature.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        num[0].setFormat(14, PAvailableFonts.TIMES);
        fullName[0].setFormat(num[0].getFormat());
        code[0].setFormat(num[0].getFormat());
        status[0].setFormat(num[0].getFormat());
        for (int i = 1; i < 5; i++) {
            fullName[i].setFormat(fullName[0].getFormat());
            num[i].setFormat(num[0].getFormat());
            code[i].setFormat(code[0].getFormat());
            status[i].setFormat(status[0].getFormat());
        }

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

    public PDFRectangle getMatter() {
        return matter;
    }

    public PDFRectangle getYear() {
        return year;
    }

    public PDFRectangle getSubtitle() {
        return subtitle;
    }

    public PDFRectangle getDate() {
        return date;
    }

    public PDFRectangle getHour() {
        return hour;
    }

    public PDFRectangle getCount() {
        return count;
    }

    public PDFRectangle getObserverSignature() {
        return observerSignature;
    }

    public PDFRectangle getDirectorSignature() {
        return directorSignature;
    }

    public PDFRectangle getFullName(int index) {
        return fullName[index];
    }

    public int getFullNameLength() {
        return fullName.length;
    }

    public PDFRectangle getNum(int index) {
        return num[index];
    }

    public int getNumLength() {
        return num.length;
    }

    public PDFRectangle getCode(int index) {
        return code[index];
    }

    public int getCodeLength() {
        return code.length;
    }

    public PDFRectangle getStatus(int index) {
        return status[index];
    }

    public int getStatusLength() {
        return status.length;
    }

    public PDFRectangle getSignature(int index) {
        return signature[index];
    }

    public int getSignatureLength() {
        return signature.length;
    }

}
