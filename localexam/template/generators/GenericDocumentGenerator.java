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

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import static localexam.Settings.PREF_BUNDLE;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class GenericDocumentGenerator {

    protected final BooleanProperty processing = new SimpleBooleanProperty(false);
    protected final BooleanProperty saved = new SimpleBooleanProperty(false);

    public GenericDocumentGenerator(String prefDirectory) {
        saved.addListener((obs, old, cur) -> {
            if (cur && "1".equals(PREF_BUNDLE.get("OPEN_OUTPUT_FOLDER_AFTER_PRODUCTION_BOOLEAN"))) {
                try {
                    Desktop.getDesktop().open(new File(PREF_BUNDLE.get(prefDirectory)));
                } catch (IOException ex) {
                }
            }
        });
    }

    public boolean isProcessing() {
        return processing.get();
    }

    public void setProcessing(boolean value) {
        processing.set(value);
    }

    public BooleanProperty processingProperty() {
        return processing;
    }

    public boolean isSaved() {
        return saved.get();
    }

    public void setSaved(boolean value) {
        saved.set(value);
    }

    public BooleanProperty savedProperty() {
        return saved;
    }

}
