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
package localexam.template.generators;

import java.util.ArrayList;
import static localexam.Settings.PREF_BUNDLE;
import localexam.template.metrics.CorrectionHeaderPage;
import net.mdrassty.util.Misc;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class CorrectionHeaderPDFFileGenerator extends PDFDocumentGenerator {

    private CorrectionHeaderPage metrics;
    private int i, first;
    
    public CorrectionHeaderPDFFileGenerator() {
        super("/localexam/template/resources/CORRECTION_HEADER_TEMPLATE.pdf");
        metrics = null;
    }
    
    public Boolean generate(String matter, int size) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new CorrectionHeaderPage();
        }
        processing.set(true);
        saved.set(false);
        int max = EXAM.getStudentsCount() / size + (EXAM.getStudentsCount() % size == 0 ? 0 : 1);
        first = EXAM.getFirstCode() + EXAM.getTranslationGap();
        ArrayList<Integer> divs = Misc.getSemiEvenDivArray(EXAM.getStudentsCount(), max);
        i = 1;
        pg = baseFile.getPage(0);
        baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
        baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
        baseFile.placeString(metrics.getLevel(), pg, "المستوى : " + EXAM.getLevel().getName());
        baseFile.placeString(metrics.getMatter(), pg, "المادة : " + matter);
        divs.forEach(n -> {             
            pg = baseFile.clonePage(0);
            baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
            baseFile.placeNRotateString(metrics.getPapers(), pg, "عدد الأوراق : " + n);
            baseFile.placeNRotateString(metrics.getFrom(), pg, "من : " + first);
            first = first + n;
            baseFile.placeNRotateString(metrics.getTo(), pg, "إلى : " + first);
            baseFile.placeNRotateString(metrics.getNumero(), pg, (i++) + " / " + divs.size());
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("CORRECTION_HEADER_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
