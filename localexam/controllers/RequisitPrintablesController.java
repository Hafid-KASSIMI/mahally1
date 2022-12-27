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
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.ToggleGroup;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.template.generators.RequisitPDFFileGenerator;
import net.mdrassty.object.Group;
import net.mdrassty.util.SpinnerInitializer;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class RequisitPrintablesController extends BasePrintableController {

    @FXML
    private RadioButton callsAllByCodeRB, callsAllNaturalRB, callsRoomRB, callsGroupRB, callsCodeRB;
    @FXML
    private ToggleGroup callsTG;
    @FXML
    private ComboBox<Room> callsRoomsCB;
    @FXML
    private ComboBox<Group> callsGroupsCB;
    @FXML
    private Spinner<Integer> callsCodeSP;

    public RequisitPrintablesController() {
        super();
        generator = new RequisitPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        callsRoomsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            callsRoomRB.setSelected(true);
            PREF_BUNDLE.update("REQUISIT_PRINTABLE_SELECTED_ROOM_INTEGER", cur + "");
        });
        callsGroupsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            callsGroupRB.setSelected(true);
            PREF_BUNDLE.update("REQUISIT_PRINTABLE_SELECTED_GROUP_INTEGER", cur + "");
        });
        callsAllByCodeRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("REQUISIT_PRINTABLE_ALL_CODES_SELECTED_BOOLEAN", cur ? "1" : "0"));
        callsAllNaturalRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("REQUISIT_PRINTABLE_ALL_NATURAL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        callsRoomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("REQUISIT_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN", cur ? "1" : "0"));
        callsGroupRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("REQUISIT_PRINTABLE_SINGLE_GROUP_SELECTED_BOOLEAN", cur ? "1" : "0"));
        callsCodeRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("REQUISIT_PRINTABLE_SINGLE_CODE_SELECTED_BOOLEAN", cur ? "1" : "0"));
        
        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (callsAllNaturalRB.isSelected()) {
                        ((RequisitPDFFileGenerator) generator).generateByGroup();
                    } else if (callsRoomRB.isSelected()) {
                        ((RequisitPDFFileGenerator) generator).generate(callsRoomsCB.getValue());
                    } else if (callsGroupRB.isSelected()) {
                        ((RequisitPDFFileGenerator) generator).generate(callsGroupsCB.getValue().getName());
                    } else if (callsCodeRB.isSelected()) {
                        ((RequisitPDFFileGenerator) generator).generate(callsCodeSP.getValue());
                    } else {
                        ((RequisitPDFFileGenerator) generator).generateByCode();
                    }
                }).start();
            }
        });
    }

    @Override
    public void refresh() {
        callsRoomsCB.getItems().clear();
        callsRoomsCB.getItems().addAll(EXAM.getRooms());
        callsGroupsCB.getItems().clear();
        callsGroupsCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getLevel().getGroups()));
        SpinnerInitializer.initialize(callsCodeSP, new SpinnerValueFactory.IntegerSpinnerValueFactory(EXAM.getFirstCode(), EXAM.getLastCode()));
        callsCodeSP.getValueFactory().valueProperty().addListener((obs, old, cur) -> {
            callsCodeRB.setSelected(true);
            PREF_BUNDLE.update("REQUISIT_PRINTABLE_CODE_INTEGER", cur + "");
        });
        try {
            callsRoomsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("REQUISIT_PRINTABLE_SELECTED_ROOM_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            callsGroupsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("REQUISIT_PRINTABLE_SELECTED_GROUP_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            callsCodeSP.getValueFactory().setValue(Integer.parseInt(PREF_BUNDLE.get("REQUISIT_PRINTABLE_CODE_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        
        callsAllByCodeRB.setSelected("1".equals(PREF_BUNDLE.get("REQUISIT_PRINTABLE_ALL_CODES_SELECTED_BOOLEAN")));
        callsAllNaturalRB.setSelected("1".equals(PREF_BUNDLE.get("REQUISIT_PRINTABLE_ALL_NATURAL_SELECTED_BOOLEAN")));
        callsRoomRB.setSelected("1".equals(PREF_BUNDLE.get("REQUISIT_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN")));
        callsGroupRB.setSelected("1".equals(PREF_BUNDLE.get("REQUISIT_PRINTABLE_SINGLE_GROUP_SELECTED_BOOLEAN")));
        callsCodeRB.setSelected("1".equals(PREF_BUNDLE.get("REQUISIT_PRINTABLE_SINGLE_CODE_SELECTED_BOOLEAN")));
        if ( callsTG.getSelectedToggle() == null )
            callsTG.selectToggle(callsTG.getToggles().get(0));
    }

}
