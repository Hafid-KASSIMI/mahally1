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
import localexam.template.metrics.GuardsListPage;
import net.mdrassty.util.CustomDate;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class GuardsPDFFileGenerator extends PDFDocumentGenerator {

    private GuardsListPage metrics;
    private int i;
    
    public GuardsPDFFileGenerator() {
        super("/localexam/template/resources/GUARDS_LIST_TEMPLATE.pdf");
        metrics = null;
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
            metrics = new GuardsListPage();
        }
        processing.set(true);
        saved.set(false);
        pg = baseFile.getPage(0);
        baseFile.resizeNPlaceString(metrics.getAcademy(), pg, EXAM.getInfos().getAcademy());
        baseFile.resizeNPlaceString(metrics.getDirection(), pg, EXAM.getInfos().getDirection());
        baseFile.resizeNPlaceString(metrics.getSchool(), pg, EXAM.getInfos().getSchool());
        baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
        baseFile.resizeNPlaceString(metrics.getSubtitle(), pg, PREF_BUNDLE.get("GUARDS_LIST_TITLE_STRING"));
        baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
        
        EXAM.getPlannings().filtered(predicate).forEach(p -> {
            pg = baseFile.clonePage(0);
            metrics.prepareRows(EXAM.getRooms().size());
            try {
                metrics.prepareDividers().forEach(div -> {
                    baseFile.drawNFill(div, pg);
                });
            } catch (CloneNotSupportedException ex) {
            }
            i = 0;
            EXAM.getRooms().forEach(room -> {
                metrics.prepareRow(i++);
                baseFile.resizeNPlaceString(metrics.getRoom(), pg, room.getLabel());
            });
            baseFile.resizeNPlaceString(metrics.getMatter(), pg, "المادة : " + p.getLabel());
            baseFile.resizeNPlaceString(metrics.getDate(), pg, CustomDate.toArabicMaDateFormat(p.getDate()));
            baseFile.resizeNPlaceString(metrics.getTime(), pg, "من : " + p.getStartTime() + " إلى : " + p.getEndTime());
        });
        
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("GUARDS_LIST_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
