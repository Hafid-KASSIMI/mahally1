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
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.stream.Stream;
import javafx.application.Application;
import javafx.stage.Stage;
import static localexam.Settings.*;
import localexam.bones.Saver;
import net.mdrassty.database.Preferences;
import static net.mdrassty.util.Misc.generateLocalUId;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class Launcher extends Application {

    private final static List<File> FILES_TO_OPEN = new ArrayList();

    @Override
    public void start(Stage stage) throws Exception {
        if (FILES_TO_OPEN.isEmpty()) {
            showWindowForFile(null);
        } else {
            FILES_TO_OPEN.forEach(file -> showWindowForFile(file));
        }
        FILES_TO_OPEN.clear();
    }

    public static void main(String[] args) {
        FILES_TO_OPEN.clear();
        Stream.of(args).filter(arg -> isMahallyFile(arg)).forEach(file -> {
            if (isMahallyFile(file)) {
                FILES_TO_OPEN.add(new File(file));
            }
        });
        SAVER = new Saver(APP_VERSION, APP_DATE, generateLocalUId(APP_VERSION));
        Settings.PREF_BUNDLE = new Preferences(Settings.PREF_DB_PATH, Settings.DB_FOLDER_PATH, ResourceBundle.getBundle("localexam.preferences.default"));
        launch(args);
    }

    public static boolean isMahallyFile(String arg) {
        try {
            Path path = Paths.get(arg);
            if (Files.exists(path) && Files.isRegularFile(path)) {
                return arg.endsWith(Settings.APP_EXTENSION);
            }
        } catch (InvalidPathException | SecurityException ipe) {
        }
        return false;
    }

    public static boolean isMahallyMarksFile(String arg) {
        try {
            Path path = Paths.get(arg);
            if (Files.exists(path) && Files.isRegularFile(path)) {
                return arg.endsWith(Settings.APP_EXTENSION2);
            }
        } catch (InvalidPathException | SecurityException ipe) {
        }
        return false;
    }

    private void showWindowForFile(File file) {
        MahallyWindow window;
        if (file == null) {
            window = new LocalExam();
            window.load();
            window.show();
            return;
        }
        if (isMahallyMarksFile(file.getPath())) {
            window = new LocalExamMarking(SAVER.parseMarks(file), file);
            window.load();
            window.show();
        } else {
            window = new LocalExam();
            window.load();
            window.openFile(file);
            window.show();
        }

    }

    public static void checkNShowWindowForFile(File file) {
        if (file == null) {
            return;
        }
        MahallyWindow window;
        if (isMahallyMarksFile(file.getPath())) {
            window = new LocalExamMarking(SAVER.parseMarks(file), file);
            window.load();
            window.show();
        } else if (isMahallyFile(file.getPath())) {
            window = new LocalExam();
            window.load();
            window.openFile(file);
            window.show();
        }

    }
}
