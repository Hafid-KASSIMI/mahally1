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
package localexam.controllers;

import java.io.File;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Paths;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import static localexam.Settings.PREF_BUNDLE;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public abstract class BaseGeneratorController extends BaseController {

    @FXML
    private TextField destPathTF;
    @FXML
    private Button browseBtn;
    @FXML
    protected Button generateBtn;
    @FXML
    protected ProgressIndicator processingPI;
    protected String prefDirectory;
    
    protected File destination;
    private final DirectoryChooser dc = new DirectoryChooser();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dc.setTitle("اختيار مكان الحفظ");
        try {
            if (!"".equals(PREF_BUNDLE.get(prefDirectory)) && Files.exists(Paths.get(PREF_BUNDLE.get(prefDirectory))) && Files.isDirectory(Paths.get(PREF_BUNDLE.get(prefDirectory)))) {
                destination = new File(PREF_BUNDLE.get(prefDirectory));
                destPathTF.setText(destination.getAbsolutePath());
                dc.setInitialDirectory(destination);
            }
        } catch (InvalidPathException ipe) {
        }
        browseBtn.setOnAction(evt -> {
            chooseDirectory();
        });
        
        refreshProcessingEars();
    }

    protected void chooseDirectory() {
        destination = dc.showDialog(browseBtn.getScene().getWindow());
        if (destination != null) {
            destPathTF.setText(destination.getAbsolutePath());
            PREF_BUNDLE.update(prefDirectory, destination.getAbsolutePath());
        }
    }
    
    protected void refreshProcessingEars() {
        generateBtn.disableProperty().bind(processingPI.visibleProperty());
    }

    @Override
    public void getReady() {
        if (EXAM.getSource() != null) {
            dc.setInitialDirectory(EXAM.getSource().getParentFile());
        }
    }

}
