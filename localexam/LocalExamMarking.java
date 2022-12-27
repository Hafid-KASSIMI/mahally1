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
package localexam;

import java.io.File;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import static localexam.Settings.APP_NAME;
import static localexam.Settings.APP_YEAR;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Marks;
import localexam.controllers.MarkingController;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class LocalExamMarking extends MahallyWindow {

    private final File outputFile;
    private final Marks marks;

    public LocalExamMarking(Marks marks, File inOutputFile) {
        super();
        this.outputFile = inOutputFile;
        this.marks = marks;
    }

    @Override
    public void load() {
        Parent root;
        Scene scene;
        FXMLLoader loader;
        try {
            loader = new FXMLLoader(getClass().getResource("/localexam/views/markingWindow.fxml"));
            root = loader.load();
            ctrl = loader.getController();
            ((MarkingController) ctrl).openFile(outputFile);
            ((MarkingController) ctrl).setMarks(marks);
            scene = new Scene(root);
            scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            stage.setScene(scene);
            stage.setTitle(marks.getExamLabel() + " - " + marks.getMatter() + " [" + APP_NAME + " " + APP_YEAR + "]");
            stage.setMaximized("1".equals(PREF_BUNDLE.get("MARKING_WINDOW_MAXIMIZED_BOOLEAN")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/localexam/icons/256.png")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/localexam/icons/192.png")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/localexam/icons/128.png")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/localexam/icons/64.png")));
        } catch (IOException ex) {
        }
    }

    @Override
    public void openFile(File file) {
    }

}
