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
public class RoomCoverPage {

    private final PDFRectangle ministery, school, room, cands, codes, plan, title;

    public RoomCoverPage() {
        ministery = new PDFRectangle();
        school = new PDFRectangle();
        room = new PDFRectangle();
        cands = new PDFRectangle();
        codes = new PDFRectangle();
        plan = new PDFRectangle();
        title = new PDFRectangle();

        ministery.reset(362.57938173773607f, 42.10743012549959f, 204.8507174876377f, 100.0000013603629f);
        school.reset(28.746836354711313f, 42.10743012549959f, 204.8507174876377f, 100.0000013603629f);
        room.reset(28.74683335206016f, 231.84989395024718f, 538.3079637557197f, 170.29595157404168f);
        cands.reset(28.746836354711313f, 414.56045350353776f, 538.3080087954869f, 64.91324275588671f);
        codes.reset(28.746836354711313f, 491.8882764886723f, 538.3080087954869f, 97.36179375851853f);
        plan.reset(28.746825094769505f, 601.6646782264398f, 538.3079637557197f, 211.50353316480386f);
        title.reset(28.74683335206016f, 154.52204846511177f, 538.3079637557197f, 64.91323750588651f);

        title.setFormat(18f, PAvailableFonts.TIMES, true);
        ministery.setFormat(14f, PAvailableFonts.MAGHRIBI);
        ministery.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        school.setFormat(14f, PAvailableFonts.MAGHRIBI);
        school.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);

        room.setFormat(100, PAvailableFonts.TIMES, true);
        cands.setFormat(32, PAvailableFonts.TIMES);
        codes.setFormat(cands.getFormat());
        plan.setFormat(28, PAvailableFonts.TIMES);
        title.setFormat(cands.getFormat());
    }

    public PDFRectangle getMinistery() {
        return ministery;
    }

    public PDFRectangle getSchool() {
        return school;
    }

    public PDFRectangle getRoom() {
        return room;
    }

    public PDFRectangle getCands() {
        return cands;
    }

    public PDFRectangle getCodes() {
        return codes;
    }

    public PDFRectangle getPlan() {
        return plan;
    }

    public PDFRectangle getTitle() {
        return title;
    }

}
