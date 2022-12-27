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

import java.text.DecimalFormat;
import java.util.IntSummaryStatistics;
import java.util.Objects;
import java.util.stream.Collectors;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.bones.StudentExamParameters;
import localexam.template.metrics.TechCardPage;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class TechCardPDFFileGenerator extends PDFDocumentGenerator {

    private TechCardPage metrics;
    private int i;
    private IntSummaryStatistics stats;
    private long size, girls;
    private final DecimalFormat decimalFormat = new DecimalFormat("#00.00");
    private final DecimalFormat intFormat = new DecimalFormat("#00");

    public TechCardPDFFileGenerator() {
        super("/localexam/template/resources/CARD_TEMPLATE.pdf");
        metrics = null;
    }

    public Boolean generate() {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new TechCardPage();
        }
        processing.set(true);
        saved.set(false);
        pg = baseFile.clonePage(0);
        baseFile.placeMultilineString(metrics.getMinistery(), pg, PREF_BUNDLE.get("KINGDOM_AR_MULTILINE").replaceAll("%new_line%", "\n"));
        baseFile.placeMultilineString(metrics.getSchool(), pg, EXAM.getInfos().getAcademy() + "\n" + EXAM.getInfos().getDirection() + "\n" + EXAM.getInfos().getSchool());
        baseFile.resizeNPlaceString(metrics.getTitle(), pg, EXAM.getLabel());
        baseFile.resizeNPlaceString(metrics.getSubtitle(), pg, PREF_BUNDLE.get("TECH_CARD_TITLE_STRING"));
        baseFile.placeNRotateString(metrics.getYear(), pg, EXAM.getInfos().getYear());
        stats = EXAM.getRooms().stream().collect(Collectors.summarizingInt(Room::getCapacity));
        baseFile.resizeNPlaceString(metrics.getRoomsCount(), pg, stats.getCount() + "");
        baseFile.resizeNPlaceString(metrics.getRoomsMaxSize(), pg, stats.getMax() + "");
        baseFile.resizeNPlaceString(metrics.getRoomsMinSize(), pg, stats.getMin() + "");
        baseFile.resizeNPlaceString(metrics.getAverageRoomsSize(), pg, decimalFormat.format(stats.getAverage()));
        size = EXAM.getStudents().size();
        girls = EXAM.getStudents().stream().filter(stu -> stu.isFemale()).count();
        baseFile.resizeNPlaceString(metrics.getStudentsCount(), pg, size + "");
        baseFile.resizeNPlaceString(metrics.getGirlsCount(), pg, girls + "");
        baseFile.resizeNPlaceString(metrics.getBoysCount(), pg, (size - girls) + "");

        i = 0;
        metrics.prepareRows(EXAM.getRooms().size());
        try {
            metrics.prepareDividers().forEach(div -> {
                baseFile.drawNFill(div, pg);
            });
        } catch (CloneNotSupportedException ex) {
        }
        EXAM.getRooms().forEach(room -> {
            metrics.prepareRow(i++);
            baseFile.placeString(metrics.getNumero(), pg, intFormat.format(room.getNumero()));
            baseFile.resizeNPlaceString(metrics.getLabel(), pg, room.getLabel() + "");
            stats = EXAM.getStudents().stream().filter(stu -> Objects.equals(room, ((StudentExamParameters) stu.getUserData()).getRoom()))
                    .collect(Collectors.summarizingInt(s -> ((StudentExamParameters) s.getUserData()).getExamCode()));
            baseFile.resizeNPlaceString(metrics.getFirstCode(), pg, stats.getMin() + "");
            baseFile.resizeNPlaceString(metrics.getLastCode(), pg, stats.getMax() + "");
            size = stats.getCount();
            girls = EXAM.getStudents().stream().filter(stu -> Objects.equals(room, ((StudentExamParameters) stu.getUserData()).getRoom())).filter(stu -> stu.isFemale()).count();
            baseFile.resizeNPlaceString(metrics.getSize(), pg, intFormat.format(size));
            baseFile.resizeNPlaceString(metrics.getGirls(), pg, intFormat.format(girls));
            baseFile.resizeNPlaceString(metrics.getBoys(), pg, intFormat.format(size - girls));
        });
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("TECHNICAL_CARD_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
