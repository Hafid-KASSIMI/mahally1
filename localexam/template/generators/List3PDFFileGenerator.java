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

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.bones.StudentExamParameters;
import localexam.template.metrics.List3Page;
import net.mdrassty.object.Student;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class List3PDFFileGenerator extends ListPDFFileGenerator {

    private List3Page metrics;
    private int i, col;

    public List3PDFFileGenerator() {
        super("/localexam/template/resources/LIST3_TEMPLATE.pdf");
        metrics = null;
    }

    @Override
    protected Boolean generate(Predicate<Room> predicate) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new List3Page();
        }
        processing.set(true);
        saved.set(false);
        col = 0;
        Map<Room, List<Student>> map = EXAM.getStudents().stream().collect(Collectors.groupingBy(stu -> ((StudentExamParameters) stu.getUserData()).getRoom()));
        map.keySet().stream().filter(predicate).sorted((r1, r2) -> Objects.compare(r1.getNumero(), r2.getNumero(), Comparator.naturalOrder())).forEach(room -> {
            if ( col == 0 ) {
                pg = baseFile.clonePage(0);
                baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
                baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
                baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
                baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
            }
            baseFile.placeString(metrics.getCapacity(col), pg, " " + room.getCapacity());
            baseFile.placeString(metrics.getFemale(col), pg, " " + map.get(room).stream().filter(stu -> stu.isFemale()).count());
            baseFile.placeString(metrics.getMale(col), pg, " " + map.get(room).stream().filter(stu -> !stu.isFemale()).count());
            baseFile.placeString(metrics.getRoom(col), pg, room.getLabel());
            metrics.prepareRows(col, room.getCapacity());
            try {
                metrics.prepareDividers(col).forEach(div -> {
                    baseFile.drawNFill(div, pg);
                });
            } catch (CloneNotSupportedException ex) {
            }
            i = 0;
            map.get(room).forEach(stu -> {
                metrics.prepareRow(col, i++);
                baseFile.resizeNPlaceString(metrics.getFullName(col), pg, stu.getFullName());
                baseFile.placeString(metrics.getGender(col), pg, stu.getGender());
                baseFile.placeString(metrics.getCode(col), pg, ((StudentExamParameters) stu.getUserData()).getExamCode() + "");
            });
            col = ++col % 2;
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("LIST_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }
}
