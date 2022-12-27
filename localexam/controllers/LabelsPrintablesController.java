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

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.template.generators.LabelsPDFFileGenerator;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class LabelsPrintablesController extends BasePrintableController {

    @FXML
    private RadioButton labelsAllRB, labelsByRoomRB;
    @FXML
    private ToggleGroup labelsTG;
    @FXML
    private ComboBox<Room> labelsRoomsCB;

    public LabelsPrintablesController() {
        super();
        generator = new LabelsPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        labelsRoomsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            labelsByRoomRB.setSelected(true);
            PREF_BUNDLE.update("LABELS_PRINTABLE_SELECTED_ROOM_INTEGER", cur + "");
        });
        labelsAllRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("LABELS_PRINTABLE_ALL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        labelsByRoomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("LABELS_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN", cur ? "1" : "0"));

        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (labelsByRoomRB.isSelected()) {
                        ((LabelsPDFFileGenerator) generator).generate(labelsRoomsCB.getValue());
                    } else {
                        ((LabelsPDFFileGenerator) generator).generate();
                    }
                }).start();
            }
        });
    }

    @Override
    public void refresh() {
        labelsRoomsCB.getItems().clear();
        labelsRoomsCB.getItems().addAll(EXAM.getRooms());
        try {
            labelsRoomsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("LABELS_PRINTABLE_SELECTED_ROOM_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        labelsAllRB.setSelected("1".equals(PREF_BUNDLE.get("LABELS_PRINTABLE_ALL_SELECTED_BOOLEAN")));
        labelsByRoomRB.setSelected("1".equals(PREF_BUNDLE.get("LABELS_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN")));
        if ( labelsTG.getSelectedToggle() == null )
            labelsTG.selectToggle(labelsTG.getToggles().get(0));
    }

}
