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
import java.time.LocalTime;
import java.util.Objects;
import java.util.function.Predicate;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Planning;
import localexam.bones.Room;
import localexam.template.metrics.ExecutionPage;
import net.mdrassty.util.CustomDate;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ExecutionPDFFileGenerator extends PDFDocumentGenerator {

    private ExecutionPage metrics;
    private int prepStartMinutes = 5, prepEndMinutes = 0;

    public enum GROUPING {
        BY_ROOM,
        BY_MATTER
    }

    public ExecutionPDFFileGenerator() {
        super("/localexam/template/resources/EXECUTION_TEMPLATE.pdf");
        metrics = null;
        try {
            prepStartMinutes = Integer.parseInt(PREF_BUNDLE.get("EXECUTION_PRINTABLE_PREP_START_MINUTES_AMOUNT_INTEGER"));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            prepEndMinutes = Integer.parseInt(PREF_BUNDLE.get("EXECUTION_PRINTABLE_PREP_END_MINUTES_AMOUNT_INTEGER"));
        } catch ( NullPointerException | NumberFormatException ex ) {}
    }

    public Boolean generate(LocalDate date, GROUPING g) {
        return generate(p -> Objects.equals(p.getDate(), date), r -> true, g);
    }

    public Boolean generate(LocalDate date, Room room, GROUPING g) {
        return generate(p -> Objects.equals(p.getDate(), date), r -> Objects.equals(r, room), g);
    }

    public Boolean generate(String matter, GROUPING g) {
        return generate(p -> Objects.equals(p.getLabel(), matter), r -> true, g);
    }

    public Boolean generate(String matter, Room room, GROUPING g) {
        return generate(p -> Objects.equals(p.getLabel(), matter), r -> Objects.equals(r, room), g);
    }

    public Boolean generate(GROUPING g) {
        return generate(p -> true, r -> true, g);
    }

    public Boolean generate(Room room, GROUPING g) {
        return generate(p -> true, r -> Objects.equals(r, room), g);
    }

    private Boolean generate(Predicate<Planning> planPredicate, Predicate<Room> roomPredicate, GROUPING g) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new ExecutionPage();
        }
        processing.set(true);
        saved.set(false);
        pg = baseFile.getPage(0);
        baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
        baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
        baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
        baseFile.resizeNPlaceString(metrics.getSubtitle(), pg, PREF_BUNDLE.get("EXECUTION_TITLE_STRING"));
        baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
        if (GROUPING.BY_ROOM.equals(g)) {
            placeDataGroupedByRoom(planPredicate, roomPredicate);
        } else {
            placeDataGroupedByPlan(planPredicate, roomPredicate);
        }
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("EXECUTION_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

    private void placeData(Room r, Planning p) {
        baseFile.resizeNPlaceString(metrics.getMatter(), pg, "المادة : " + p.getLabel());
        baseFile.resizeNPlaceString(metrics.getDate(), pg, "تاريخ الإجراء : " + CustomDate.toArabicMaDateFormat(p.getDate()));
        baseFile.placeString(metrics.getRoom(), pg, "القاعة : " + r.getLabel());
        baseFile.placeString(metrics.getCands(), pg, "عدد الممتحنين : " + r.getCapacity());
        baseFile.resizeNPlaceString(metrics.getPrepStart(), pg, "توقيت بداية التحضير لاجتياز الاختبار : " + p.getStartTime().minusMinutes(prepStartMinutes).toString());
        baseFile.resizeNPlaceString(metrics.getPrepEnd(), pg, "توقيت نهاية التحضير لاجتياز الاختبار : " + p.getStartTime().minusMinutes(prepEndMinutes).toString());
        baseFile.resizeNPlaceString(metrics.getStart(), pg, "توقيت بداية إجراء الاختبار : " + p.getStartTime().toString());
        baseFile.resizeNPlaceString(metrics.getEnd(), pg, "توقيت نهاية إجراء الاختبار : " + p.getEndTime().toString());
        if ( p.getStartTime().isBefore(LocalTime.NOON) ) {
            baseFile.placeString(metrics.getAm(), pg, "");
            baseFile.placeString(metrics.getSeance((int) EXAM.getPlannings().stream().filter(px -> 
                    px.getStartTime().isBefore(p.getStartTime()) && px.getDate().equals(p.getDate())).count()), pg, "");
        }
        else {
            baseFile.placeString(metrics.getPm(), pg, "");
            baseFile.placeString(metrics.getSeance((int) EXAM.getPlannings().stream().filter(px -> 
                    px.getStartTime().isBefore(p.getStartTime()) && px.getDate().equals(p.getDate()) && px.getStartTime().isAfter(LocalTime.NOON)).count()), pg, "");
        }
    }

    private void placeDataGroupedByRoom(Predicate<Planning> planPredicate, Predicate<Room> roomPredicate) {
        EXAM.getRooms().filtered(roomPredicate).forEach(room -> {
            EXAM.getPlannings().filtered(planPredicate).forEach(p -> {
                pg = baseFile.clonePage(0);
                placeData(room, p);
            });
        });
    }

    private void placeDataGroupedByPlan(Predicate<Planning> planPredicate, Predicate<Room> roomPredicate) {
        EXAM.getPlannings().filtered(planPredicate).forEach(p -> {
            EXAM.getRooms().filtered(roomPredicate).forEach(room -> {
                pg = baseFile.clonePage(0);
                placeData(room, p);
            });
        });
    }

}
