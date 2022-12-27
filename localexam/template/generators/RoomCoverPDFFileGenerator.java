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

import java.time.Duration;
import java.util.Comparator;
import java.util.IntSummaryStatistics;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Planning;
import localexam.bones.Room;
import localexam.bones.StudentExamParameters;
import localexam.template.metrics.RoomCoverPage;
import net.mdrassty.object.Student;
import net.mdrassty.util.CustomDate;
import net.mdrassty.util.Misc;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class RoomCoverPDFFileGenerator extends PDFDocumentGenerator {

    private RoomCoverPage metrics;

    public enum SORT {
        BY_ROOM, BY_MATTER
    };

    public RoomCoverPDFFileGenerator() {
        super("/localexam/template/resources/ROOM_COVER_A4_TEMPLATE.pdf");
        metrics = null;
    }
    
    public void setA3() {
        baseFile.setTpl("/localexam/template/resources/ROOM_COVER_A3_TEMPLATE.pdf");
    }
    
    public void setA4() {
        baseFile.setTpl("/localexam/template/resources/ROOM_COVER_A4_TEMPLATE.pdf");
    }

    public Boolean generate(Room room, SORT order) {
        if (order == SORT.BY_MATTER) {
            return generateByMatter(r -> Objects.equals(room, r), m -> true);
        } else {
            return generateByRoom(r -> Objects.equals(room, r), m -> true);
        }
    }

    public Boolean generate(String matter, SORT order) {
        if (order == SORT.BY_MATTER) {
            return generateByMatter(r -> true, p -> Objects.equals(matter, p.getLabel()));
        } else {
            return generateByRoom(r -> true, p -> Objects.equals(matter, p.getLabel()));
        }
    }

    public Boolean generate(Room room, String matter, SORT order) {
        if (order == SORT.BY_MATTER) {
            return generateByMatter(r -> Objects.equals(room, r), p -> Objects.equals(matter, p.getLabel()));
        } else {
            return generateByRoom(r -> Objects.equals(room, r), p -> Objects.equals(matter, p.getLabel()));
        }
    }

    public Boolean generate(SORT order) {
        if (order == SORT.BY_MATTER) {
            return generateByMatter(r -> true, m -> true);
        } else {
            return generateByRoom(r -> true, m -> true);
        }
    }

    private Boolean generateByMatter(Predicate<Room> predicate, Predicate<Planning> matter) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new RoomCoverPage();
        }
        processing.set(true);
        saved.set(false);
        Map<Room, List<Student>> map = EXAM.getStudents().stream().collect(Collectors.groupingBy(stu -> ((StudentExamParameters) stu.getUserData()).getRoom()));
        EXAM.getPlannings().stream().filter(matter).forEach(p -> {
            map.keySet().stream().filter(predicate).sorted((r1, r2) -> Objects.compare(r1.getNumero(), r2.getNumero(), Comparator.naturalOrder())).forEach(room -> {
                IntSummaryStatistics iss = map.get(room).stream().collect(Collectors.summarizingInt(s -> ((StudentExamParameters) s.getUserData()).getExamCode()));
                pg = baseFile.clonePage(0);
                baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
                baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
                baseFile.placeMultilineString(metrics.getCodes(), pg, "من : " + iss.getMin() + "\nإلى : " + iss.getMax());
                baseFile.resizeNPlaceString(metrics.getCands(), pg, "عدد الممتحنين : " + room.getCapacity() + " ) " + 
                        Misc.ARCounter("أنثى", "أنثى", "أنثيان", "إناث", true, (int) map.get(room).stream().filter(stu -> stu.isFemale()).count()) + " (");
                baseFile.resizeNPlaceString(metrics.getRoom(), pg, room.getLabel());
                baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
                baseFile.placeMultilineString(metrics.getPlan(), pg, "المادة : " + p.getLabel()
                        + "\n تاريخ الإنجاز : " + CustomDate.toArabicMaDateFormat(p.getDate())
                        + "\n المدة : " + Misc.ARCounter("دقيقة", "دقيقة", "دقيقتان", "دقائق", true, (int) Duration.between(p.getStartTime(), p.getEndTime()).toMinutes())
                        + "\n من : " + p.getStartTime() + " إلى " + p.getEndTime()
                );
            });
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("ROOM_COVER_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

    private Boolean generateByRoom(Predicate<Room> predicate, Predicate<Planning> matter) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new RoomCoverPage();
        }
        processing.set(true);
        saved.set(false);
        Map<Room, List<Student>> map = EXAM.getStudents().stream().collect(Collectors.groupingBy(stu -> ((StudentExamParameters) stu.getUserData()).getRoom()));
        map.keySet().stream().filter(predicate).sorted((r1, r2) -> Objects.compare(r1.getNumero(), r2.getNumero(), Comparator.naturalOrder())).forEach(room -> {
            IntSummaryStatistics iss = map.get(room).stream().collect(Collectors.summarizingInt(s -> ((StudentExamParameters) s.getUserData()).getExamCode()));
            EXAM.getPlannings().stream().filter(matter).forEach(p -> {
                pg = baseFile.clonePage(0);
                baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
                baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
                baseFile.placeMultilineString(metrics.getCodes(), pg, "من : " + iss.getMin() + "\nإلى : " + iss.getMax());
                baseFile.resizeNPlaceString(metrics.getCands(), pg, "عدد الممتحنين : " + room.getCapacity() + " ) " + map.get(room).stream().filter(stu -> stu.isFemale()).count() + " ث (");
                baseFile.resizeNPlaceString(metrics.getRoom(), pg, room.getLabel());
                baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
                baseFile.placeMultilineString(metrics.getPlan(), pg, "المادة : " + p.getLabel()
                        + "\n تاريخ الإنجاز : " + CustomDate.toArabicMaDateFormat(p.getDate())
                        + "\n المدة : " + Misc.ARCounter("دقيقة", "دقيقة", "دقيقتان", "دقائق", true, (int) Duration.between(p.getStartTime(), p.getEndTime()).toMinutes())
                        + "\n من : " + p.getStartTime() + " إلى " + p.getEndTime()
                );
            });
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("ROOM_COVER_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
