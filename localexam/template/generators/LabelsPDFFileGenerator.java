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
import localexam.template.metrics.LabelsPage;
import net.mdrassty.object.Student;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class LabelsPDFFileGenerator extends PDFDocumentGenerator {

    private LabelsPage metrics;
    private int i;

    public LabelsPDFFileGenerator() {
        super("/localexam/template/resources/LABEL_TEMPLATE.pdf");
        metrics = null;
    }

    public Boolean generate(Room room) {
        return generate(r -> room.getNumero() == r.getNumero());
    }

    public Boolean generate() {
        return generate(r -> true);
    }

    private Boolean generate(Predicate<Room> predicate) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new LabelsPage();
        }
        processing.set(true);
        saved.set(false);
        i = 0;
        Map<Room, List<Student>> map = EXAM.getStudents().stream().collect(Collectors.groupingBy(stu -> ((StudentExamParameters) stu.getUserData()).getRoom()));
        map.keySet().stream().filter(predicate).sorted((r1, r2) -> Objects.compare(r1.getNumero(), r2.getNumero(), Comparator.naturalOrder()))
                .forEach(room -> {
                    map.get(room).forEach(stu -> {
                        if (i % metrics.getSize() == 0) {
                            pg = baseFile.clonePage(0);
                            i = 0;
                        }
                        baseFile.placeMultilineString(metrics.getMinistery(i), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
                        baseFile.placeMultilineString(metrics.getSchool(i), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
                        baseFile.resizeNPlaceString(metrics.getRoom(i), pg, room.getLabel());
                        baseFile.resizeNPlaceString(metrics.getTitle(i), pg, EXAM.getLabel());
                        baseFile.resizeNPlaceString(metrics.getFullName(i), pg, stu.getFullName());
                        //placeString(metrics.getGender(), pg, stu.isFemale() ? "\ue9dd" : "\ue9dc");
                        baseFile.placeString(metrics.getCode(i), pg, ((StudentExamParameters) stu.getUserData()).getExamCode() + "");
                        i++;
                    });
                });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("LABELS_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
