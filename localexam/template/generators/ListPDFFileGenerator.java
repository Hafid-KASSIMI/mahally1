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
import localexam.bones.Room;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public abstract class ListPDFFileGenerator extends PDFDocumentGenerator {

    public ListPDFFileGenerator(String template) {
        super(template);
    }

    public Boolean generate(Room room) {
        return generate(r -> Objects.equals(room, r));
    }

    public Boolean generate() {
        return generate(r -> true);
    }

    protected abstract Boolean generate(Predicate<Room> predicate);

}
