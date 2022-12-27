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
import localexam.template.generators.StatementPDFFileGenerator;
import net.mdrassty.object.Group;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class StatementPrintablesController extends BasePrintableController {

    @FXML
    private RadioButton statementAllByCodeRB, statementAllNaturalRB, statementRoomRB, statementGroupRB;
    @FXML
    private ToggleGroup statementTG;
    @FXML
    private ComboBox<Room> statementRoomsCB;
    @FXML
    private ComboBox<Group> statementGroupsCB;

    public StatementPrintablesController() {
        super();
        generator = new StatementPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        statementRoomsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            statementRoomRB.setSelected(true);
            PREF_BUNDLE.update("STATEMENT_PRINTABLE_SELECTED_ROOM_INTEGER", cur + "");
        });
        statementGroupsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            statementGroupRB.setSelected(true);
            PREF_BUNDLE.update("STATEMENT_PRINTABLE_SELECTED_GROUP_INTEGER", cur + "");
        });
        statementAllByCodeRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("STATEMENT_PRINTABLE_ALL_BY_CODE_SELECTED_BOOLEAN", cur ? "1" : "0"));
        statementAllNaturalRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("STATEMENT_PRINTABLE_ALL_NATURAL_SELECTED_BOOLEAN", cur ? "1" : "0"));
        statementRoomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("STATEMENT_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN", cur ? "1" : "0"));
        statementGroupRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("STATEMENT_PRINTABLE_SINGLE_GROUP_SELECTED_BOOLEAN", cur ? "1" : "0"));
        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (statementAllNaturalRB.isSelected()) {
                        ((StatementPDFFileGenerator) generator).generateByGroup();
                    } else if (statementRoomRB.isSelected()) {
                        ((StatementPDFFileGenerator) generator).generate(statementRoomsCB.getValue());
                    } else if (statementGroupRB.isSelected()) {
                        ((StatementPDFFileGenerator) generator).generate(statementGroupsCB.getValue().getName());
                    } else {
                        ((StatementPDFFileGenerator) generator).generateByRoom();
                    }
                }).start();
            }
        });
    }

    @Override
    public void refresh() {
        statementRoomsCB.getItems().clear();
        statementRoomsCB.getItems().addAll(EXAM.getRooms());
        statementGroupsCB.getItems().clear();
        statementGroupsCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getLevel().getGroups()));
        try {
            statementRoomsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("STATEMENT_PRINTABLE_SELECTED_ROOM_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            statementGroupsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("STATEMENT_PRINTABLE_SELECTED_GROUP_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        statementAllByCodeRB.setSelected("1".equals(PREF_BUNDLE.get("STATEMENT_PRINTABLE_ALL_BY_CODE_SELECTED_BOOLEAN")));
        statementAllNaturalRB.setSelected("1".equals(PREF_BUNDLE.get("STATEMENT_PRINTABLE_ALL_NATURAL_SELECTED_BOOLEAN")));
        statementRoomRB.setSelected("1".equals(PREF_BUNDLE.get("STATEMENT_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN")));
        statementGroupRB.setSelected("1".equals(PREF_BUNDLE.get("STATEMENT_PRINTABLE_SINGLE_GROUP_SELECTED_BOOLEAN")));
        if ( statementTG.getSelectedToggle() == null )
            statementTG.selectToggle(statementTG.getToggles().get(0));
    }

}
