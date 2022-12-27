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
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Planning;
import localexam.bones.Room;
import localexam.bones.StudentExamParameters;
import localexam.template.metrics.PvPage;
import net.mdrassty.object.Student;
import net.mdrassty.util.CustomDate;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class PvPDFFileGenerator extends PDFDocumentGenerator {

    private PvPage metrics;
    private int i;

    public PvPDFFileGenerator() {
        super("/localexam/template/resources/PV_TEMPLATE.pdf");
        metrics = null;
    }

    public Boolean generateMatter(String matter) {
        return generate(p -> Objects.equals(p.getLabel(), matter));
    }

    public Boolean generateDay(LocalDate day) {
        return generate(p -> Objects.equals(p.getDate(), day));
    }

    public Boolean generate() {
        return generate(p -> true);
    }

    private Boolean generate(Predicate<Planning> predicate) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new PvPage();
        }
        processing.set(true);
        saved.set(false);
        EXAM.getPlannings().stream().filter(predicate).sorted((p1, p2) -> {
            int c = Objects.compare(p1.getDate(), p2.getDate(), Comparator.naturalOrder());
            return (c == 0) ? Objects.compare(p1.getStartTime(), p2.getStartTime(), Comparator.naturalOrder()) : c;
        }).forEach(p -> {
            Map<Room, List<Student>> map = EXAM.getStudents().stream().collect(Collectors.groupingBy(stu -> ((StudentExamParameters) stu.getUserData()).getRoom()));
            map.keySet().stream().sorted((r1, r2) -> Objects.compare(r1.getNumero(), r2.getNumero(), Comparator.naturalOrder())).forEach(room -> {
                pg = baseFile.clonePage(0);
                metrics.prepareRows(room.getCapacity());
                baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
                baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
                baseFile.placeString(metrics.getLevel(), pg, "المستوى : " + EXAM.getLevel().getName());
                baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
                baseFile.placeString(metrics.getCapacity(), pg, "عدد الممتحنين : " + room.getCapacity() + " ) " + map.get(room).stream().filter(stu -> stu.isFemale()).count() + " ث (");
                baseFile.placeString(metrics.getRoom(), pg, "القاعة : " + room.getLabel());
                baseFile.placeString(metrics.getMatter(), pg, "المادة : " + p.getLabel());
                baseFile.placeString(metrics.getDate(), pg, "التاريخ : " + CustomDate.toArabicMaDateFormat(p.getDate()));
                baseFile.placeString(metrics.getTime(), pg, "التوقيت : من " + p.getStartTime() + " إلى " + p.getEndTime());
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
                    baseFile.resizeNPlaceString(metrics.getFullName(), pg, stu.getFullName());
                    //placeString(metrics.getGender(), pg, stu.isFemale() ? "\ue9dd" : "\ue9dc");
                    baseFile.placeString(metrics.getBirth(), pg, stu.getBirthDate());
                    baseFile.placeString(metrics.getCode(), pg, ((StudentExamParameters) stu.getUserData()).getExamCode() + "");
                });
            });
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("TRIAL_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
