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

import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.geometry.NodeOrientation;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import static localexam.Settings.APP_NAME;
import static localexam.Settings.APP_YEAR;
import static localexam.Settings.PREF_BUNDLE;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class LocalExam extends MahallyWindow {
    

    public LocalExam() {
        super();
    }

    @Override
    public void load() {
        Parent root;
        Scene scene;
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/localexam/views/MainWindow.fxml"));
        try {
            root = loader.load();
            ctrl = loader.getController();
            scene = new Scene(root);
            scene.setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            stage.setScene(scene);
            stage.setTitle("امتحان جديد [" + APP_NAME + " " + APP_YEAR + "]");
            stage.setMaximized("1".equals(PREF_BUNDLE.get("MAIN_WINDOW_MAXIMIZED_BOOLEAN")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/localexam/icons/256.png")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/localexam/icons/192.png")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/localexam/icons/128.png")));
            stage.getIcons().add(new Image(getClass().getResourceAsStream("/localexam/icons/64.png")));
        } catch (IOException ex) {
        }
    }
    
}
