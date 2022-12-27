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

import java.util.IntSummaryStatistics;
import java.util.Objects;
import java.util.stream.Collectors;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.StudentExamParameters;
import localexam.template.metrics.ListHeaderPage;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ListHeaderPDFFileGenerator extends PDFDocumentGenerator {

    private ListHeaderPage metrics;
    private int i;
    private IntSummaryStatistics stats;

    public ListHeaderPDFFileGenerator() {
        super("/localexam/template/resources/LIST_HEADER_TEMPLATE.pdf");
        metrics = null;
    }

    public Boolean generate() {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new ListHeaderPage();
        }
        processing.set(true);
        saved.set(false);
        i = 0;
        EXAM.getRooms().forEach(room -> {
            if ( i % metrics.getSize() == 0 ) {
                pg = baseFile.clonePage(0);
                i = 0;
            }
            stats = EXAM.getStudents().stream().filter(stu -> Objects.equals(room, ((StudentExamParameters) stu.getUserData()).getRoom()))
                    .collect(Collectors.summarizingInt(s -> ((StudentExamParameters) s.getUserData()).getExamCode()));
            baseFile.placeMultilineString(metrics.getStats(i), pg, "عدد الممتحنين : " + stats.getCount() + "\nمن : " + stats.getMin() + "\nإلى : " + stats.getMax());
            baseFile.resizeNWrapString(metrics.getRoom(i), pg, room.getLabel());
            i++;
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("LIST_HEADER_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
