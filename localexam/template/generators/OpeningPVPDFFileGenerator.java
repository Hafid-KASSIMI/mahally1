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

import java.time.LocalDate;
import java.util.Objects;
import java.util.function.Predicate;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Planning;
import localexam.template.metrics.OpeningPvPage;
import net.mdrassty.util.CustomDate;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class OpeningPVPDFFileGenerator extends PDFDocumentGenerator {

    private OpeningPvPage metrics;
    private int minutes = 15;
    
    public OpeningPVPDFFileGenerator() {
        super("/localexam/template/resources/OPENING_TEMPLATE.pdf");
        metrics = null;
        try {
            minutes = Integer.parseInt(PREF_BUNDLE.get("OPENING_TRIAL_MINUTES_AMOUNT_INTEGER"));
        } catch ( NullPointerException | NumberFormatException ex ) {}
    }

    public Boolean generate(LocalDate date) {
        return generate(p -> Objects.equals(p.getDate(), date));
    }

    public Boolean generate(String matter) {
        return generate(p -> Objects.equals(p.getLabel(), matter));
    }

    public Boolean generate() {
        return generate(p -> true);
    }
    
    private Boolean generate(Predicate<Planning> predicate) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new OpeningPvPage();
        }
        processing.set(true);
        saved.set(false);
        pg = baseFile.getPage(0);
        baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
        baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
        baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
        baseFile.resizeNPlaceString(metrics.getSubtitle(), pg, PREF_BUNDLE.get("OPENING_PV_TITLE_STRING"));
        baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
        for ( int i = 0; i < 4; i++ ) {
            if ( "1".equals(PREF_BUNDLE.get("OPENING_PROTAGONIST_" + i + "_INCLUDED_BOOLEAN")) ) {
                baseFile.placeString(metrics.getNum(i), pg, (i + 1) + "");
                baseFile.resizeNPlaceString(metrics.getFullName(i), pg, PREF_BUNDLE.get("OPENING_PROTAGONIST_" + i + "_FULL_NAME_STRING"));
                baseFile.resizeNPlaceString(metrics.getCode(i), pg, PREF_BUNDLE.get("OPENING_PROTAGONIST_" + i + "_CODE_STRING"));
                baseFile.resizeNPlaceString(metrics.getStatus(i), pg, PREF_BUNDLE.get("OPENING_PROTAGONIST_" + i + "_STATUS_STRING"));
            }
        }
        
        EXAM.getPlannings().filtered(predicate).forEach(p -> {
            pg = baseFile.clonePage(0);
            baseFile.resizeNPlaceString(metrics.getMatter(), pg, p.getLabel());
            baseFile.resizeNPlaceString(metrics.getDate(), pg, CustomDate.toArabicMaDateFormat(p.getDate()));
            baseFile.resizeNPlaceString(metrics.getHour(), pg, p.getStartTime().minusMinutes(minutes).toString());
        });
        
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("OPENING_TRIAL_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
