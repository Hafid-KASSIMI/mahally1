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

import localexam.Exam;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public abstract class DocumentGenerator<T, S> extends GenericDocumentGenerator {

    protected Exam EXAM;
    protected T baseFile;
    protected S pg;

    public DocumentGenerator() {
        super("OUTPUT_DIR_STRING");
    }

    public Exam getEXAM() {
        return EXAM;
    }

    public void setEXAM(Exam EXAM) {
        this.EXAM = EXAM;
    }

}
