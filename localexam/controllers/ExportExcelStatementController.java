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
import javafx.scene.control.ToggleGroup;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.template.generators.StatementXLSXGenerator;
import net.mdrassty.object.Group;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ExportExcelStatementController extends BasePrintableController {

    @FXML
    private RadioButton allByCodeRB, everythingRB, singleRoomRB, singleGroupRB;
    @FXML
    private ToggleGroup optionsTG;
    @FXML
    private ComboBox<Room> roomsCB;
    @FXML
    private ComboBox<Group> groupsCB;

    public ExportExcelStatementController() {
        super();
        generator = new StatementXLSXGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        roomsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            singleRoomRB.setSelected(true);
            PREF_BUNDLE.update("WORKBOOK_STATEMENT_SELECTED_ROOM_INTEGER", cur + "");
        });
        groupsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            singleGroupRB.setSelected(true);
            PREF_BUNDLE.update("WORKBOOK_STATEMENT_SELECTED_GROUP_INTEGER", cur + "");
        });
        allByCodeRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("WORKBOOK_STATEMENT_ALL_BY_CODE_SELECTED_BOOLEAN", cur ? "1" : "0"));
        everythingRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("WORKBOOK_STATEMENT_ALL_NATURAL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        singleRoomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("WORKBOOK_STATEMENT_SINGLE_ROOM_SELECTED_BOOLEAN", cur ? "1" : "0"));
        singleGroupRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("WORKBOOK_STATEMENT_SINGLE_GROUP_SELECTED_BOOLEAN", cur ? "1" : "0"));
        
        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (everythingRB.isSelected()) {
                        ((StatementXLSXGenerator) generator).generateByGroup();
                    } else if (singleRoomRB.isSelected()) {
                        ((StatementXLSXGenerator) generator).generate(roomsCB.getValue());
                    } else if (singleGroupRB.isSelected()) {
                        ((StatementXLSXGenerator) generator).generate(groupsCB.getValue().getName());
                    } else {
                        ((StatementXLSXGenerator) generator).generateByRoom();
                    }
                }).start();
            }
        });
    }

    @Override
    public void refresh() {
        roomsCB.getItems().clear();
        roomsCB.getItems().addAll(EXAM.getRooms());
        groupsCB.getItems().clear();
        groupsCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getLevel().getGroups()));
        try {
            roomsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("STATEMENT_PRINTABLE_SELECTED_ROOM_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            groupsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("STATEMENT_PRINTABLE_SELECTED_GROUP_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        allByCodeRB.setSelected("1".equals(PREF_BUNDLE.get("WORKBOOK_STATEMENT_ALL_BY_CODE_SELECTED_BOOLEAN")));
        everythingRB.setSelected("1".equals(PREF_BUNDLE.get("WORKBOOK_STATEMENT_ALL_NATURAL_SELECTED_BOOLEAN")));
        singleRoomRB.setSelected("1".equals(PREF_BUNDLE.get("WORKBOOK_STATEMENT_SINGLE_ROOM_SELECTED_BOOLEAN")));
        singleGroupRB.setSelected("1".equals(PREF_BUNDLE.get("WORKBOOK_STATEMENT_SINGLE_GROUP_SELECTED_BOOLEAN")));
        
        if ( optionsTG.getSelectedToggle() == null )
            optionsTG.selectToggle(optionsTG.getToggles().get(0));
    }

}
