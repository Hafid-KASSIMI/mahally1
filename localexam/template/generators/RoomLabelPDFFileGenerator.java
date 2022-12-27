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

import java.util.Objects;
import java.util.function.Predicate;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.template.metrics.RoomLabelPage;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class RoomLabelPDFFileGenerator extends PDFDocumentGenerator {

    private RoomLabelPage metrics;
    private int i;
    
    public RoomLabelPDFFileGenerator() {
        super("/localexam/template/resources/ROOM_LABEL_TEMPLATE.pdf");
        metrics = null;
    }

    public Boolean generate(Room room) {
        return generate(p -> Objects.equals(p, room));
    }

    public Boolean generate() {
        return generate(p -> true);
    }
    
    private Boolean generate(Predicate<Room> predicate) {
        boolean result;
        baseFile.reset();
        if (metrics == null) {
            metrics = new RoomLabelPage();
        }
        processing.set(true);
        saved.set(false);
        i= 0;
        EXAM.getRooms().filtered(predicate).forEach(p -> {
            if ( i % 2 == 0 ) {
                pg = baseFile.clonePage(0);
                i = 0;
            }
            baseFile.resizeNPlaceString(metrics.getSchool(i), pg, EXAM.getInfos().getSchool());
            baseFile.resizeNPlaceString(metrics.getTitle(i), pg, EXAM.getLabel());
            baseFile.wrapNResizeText(metrics.getLabel(i), pg, p.getLabel());
            i++;
        });
        
        baseFile.removePage(0);
        result = (baseFile.getDoc().getPages().getCount() > 0) ? baseFile.save(PREF_BUNDLE.get("OUTPUT_DIR_STRING") + baseFile.generateFileName(PREF_BUNDLE.get("ROOM_LABEL_FILENAME_PREFIX_STRING"), "pdf")) : false;
        processing.set(false);
        saved.set(true);
        return result;
    }

}
