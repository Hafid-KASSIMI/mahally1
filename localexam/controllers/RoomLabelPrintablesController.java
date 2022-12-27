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
import javafx.scene.image.ImageView;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.template.generators.RoomLabelPDFFileGenerator;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class RoomLabelPrintablesController extends BasePrintableController {

    @FXML
    private RadioButton pvsAllRB, pvsByRoomRB;
    @FXML
    private ToggleGroup pvTG;
    @FXML
    private ComboBox<Room> pvsRoomsCB;
    @FXML
    private ImageView tplIV;

    public RoomLabelPrintablesController() {
        super();
        generator = new RoomLabelPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
                
        pvsRoomsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            pvsByRoomRB.setSelected(true);
            PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_SELECTED_ROOM_INTEGER", cur + "");
        });
        pvsAllRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_ALL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        pvsByRoomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN", cur ? "1" : "0"));
        
        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (pvsByRoomRB.isSelected()) {
                        ((RoomLabelPDFFileGenerator) generator).generate(pvsRoomsCB.getValue());
                    } else {
                        ((RoomLabelPDFFileGenerator) generator).generate();
                    }
                }).start();
            }
        });
    }

    @Override
    public void refresh() {
        pvsRoomsCB.getItems().clear();
        pvsRoomsCB.getItems().addAll(EXAM.getRooms());
        try {
            pvsRoomsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_SELECTED_ROOM_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        pvsAllRB.setSelected("1".equals(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_ALL_SELECTED_BOOLEAN")));
        pvsByRoomRB.setSelected("1".equals(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN")));
        if ( pvTG.getSelectedToggle() == null )
            pvTG.selectToggle(pvTG.getToggles().get(0));
    }

}
