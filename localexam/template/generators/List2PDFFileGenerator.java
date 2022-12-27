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
import localexam.template.metrics.List2Page;
import net.mdrassty.object.Student;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class List2PDFFileGenerator extends ListPDFFileGenerator {

    private List2Page metrics;
    private int i;

    public List2PDFFileGenerator() {
        super("/localexam/template/resources/LIST2_TEMPLATE.pdf");
        metrics = null;
    }

    @Override
    protected Boolean generate(Predicate<Room> predicate) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new List2Page();
        }
        processing.set(true);
        saved.set(false);
        Map<Room, List<Student>> map = EXAM.getStudents().stream().collect(Collectors.groupingBy(stu -> ((StudentExamParameters) stu.getUserData()).getRoom()));
        map.keySet().stream().filter(predicate).sorted((r1, r2) -> Objects.compare(r1.getNumero(), r2.getNumero(), Comparator.naturalOrder())).forEach(room -> {
            pg = baseFile.clonePage(0);
            metrics.prepareRows(room.getCapacity());
            baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
            baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
            baseFile.placeString(metrics.getLevel(), pg, "المستوى : " + EXAM.getLevel().getName());
            baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
            baseFile.placeString(metrics.getCapacity(), pg, "عدد الممتحنين : " + room.getCapacity() + " ) " + map.get(room).stream().filter(stu -> stu.isFemale()).count() + " ث (");
            baseFile.placeString(metrics.getRoom(), pg, "القاعة : " + room.getLabel());
            baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
            try {
                metrics.prepareDividers().forEach(div -> {
                    baseFile.drawNFill(div, pg);
                });
            } catch (CloneNotSupportedException ex) {
            }
            i = 0;
            map.get(room).forEach(stu -> {
                metrics.prepareRow(i++);
                baseFile.placeString(metrics.getMassar(), pg, stu.getCode());
                baseFile.resizeNPlaceString(metrics.getFullName(), pg, stu.getFullName());
                baseFile.placeString(metrics.getBirth(), pg, stu.getBirthDate());
                //placeString(metrics.getGender(), pg, stu.isFemale() ? "\ue9dd" : "\ue9dc");
                baseFile.placeString(metrics.getGender(), pg, stu.getGender());
                baseFile.placeString(metrics.getGroup(), pg, ((StudentExamParameters) stu.getUserData()).getGroup());
                baseFile.placeString(metrics.getCode(), pg, ((StudentExamParameters) stu.getUserData()).getExamCode() + "");
            });
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("LIST_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }
}
