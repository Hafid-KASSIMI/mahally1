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

import net.mdrassty.util.A4_PAPER;
import net.mdrassty.util.ALIGNMENT;
import net.mdrassty.util.pdf.PAvailableFonts;
import net.mdrassty.util.pdf.PDFRectangle;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class CorrectionHeaderPage {

    private final PDFRectangle level, title, matter, papers, from, to, numero, school, ministery;

    public CorrectionHeaderPage() {
        level = new PDFRectangle(A4_PAPER.getLandscapeHeight());
        title = new PDFRectangle(A4_PAPER.getLandscapeHeight());
        matter = new PDFRectangle(A4_PAPER.getLandscapeHeight());
        papers = new PDFRectangle(A4_PAPER.getLandscapeHeight());
        from = new PDFRectangle(A4_PAPER.getLandscapeHeight());
        to = new PDFRectangle(A4_PAPER.getLandscapeHeight());
        numero = new PDFRectangle(A4_PAPER.getLandscapeHeight());
        school = new PDFRectangle(A4_PAPER.getLandscapeHeight());
        ministery = new PDFRectangle(A4_PAPER.getLandscapeHeight());

        level.reset(185.3310303613082f, 233.28134400550638f, 601.9073879503885f, 42.00000462135257f);
        title.reset(87.88599267016066f, 156.28757103326734f, 666.8618035151525f, 55.93635365934622f);
        matter.reset(185.3310303613082f, 296.338773939752f, 601.9073879503885f, 42.00000462135257f);
        papers.reset(185.3310303613082f, 422.45362630824286f, 601.9073879503885f, 42.00000462135257f);
        from.reset(437.831494505782f, 485.51103374248765f, 292.12470229427873f, 50.000005930181665f);
        to.reset(112.67761457529673f, 485.51107874248936f, 292.1247473340459f, 50.000005930181665f);
        numero.reset(69.84569224699213f, 317.21277474556416f, 122.77124409996392f, 41.75647136195131f);
        school.reset(63.84115787994114f, 51.76590349835171f, 293.8850740891754f, 83.46424822202283f);
        ministery.reset(484.90753962111313f, 51.76590349835171f, 293.88514915545414f, 83.46424822202283f);

        title.setFormat(30f, PAvailableFonts.TIMES, true);
        ministery.setFormat(14f, PAvailableFonts.MAGHRIBI);
        ministery.getFormat().setAlignment(ALIGNMENT.MIDDLE_LEFT);
        school.setFormat(14f, PAvailableFonts.MAGHRIBI);
        school.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        level.setFormat(28, PAvailableFonts.TIMES);
        level.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        matter.setFormat(level.getFormat());
        papers.setFormat(level.getFormat());
        from.setFormat(36, PAvailableFonts.TIMES);
        from.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        to.setFormat(36, PAvailableFonts.TIMES);
        to.getFormat().setAlignment(ALIGNMENT.MIDDLE_RIGHT);
        numero.setFormat(18, PAvailableFonts.TIMES);
        numero.getFormat().setRotation(90);

    }

    public PDFRectangle getLevel() {
        return level;
    }

    public PDFRectangle getTitle() {
        return title;
    }

    public PDFRectangle getMatter() {
        return matter;
    }

    public PDFRectangle getPapers() {
        return papers;
    }

    public PDFRectangle getFrom() {
        return from;
    }

    public PDFRectangle getTo() {
        return to;
    }

    public PDFRectangle getNumero() {
        return numero;
    }

    public PDFRectangle getSchool() {
        return school;
    }

    public PDFRectangle getMinistery() {
        return ministery;
    }

}
