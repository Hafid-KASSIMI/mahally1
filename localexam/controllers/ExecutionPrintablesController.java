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
import java.time.LocalDate;
import java.util.ResourceBundle;
import java.util.stream.Collectors;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import static localexam.Settings.PREF_BUNDLE;
import localexam.bones.Room;
import localexam.template.generators.ExecutionPDFFileGenerator;
import localexam.template.generators.ExecutionPDFFileGenerator.GROUPING;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class ExecutionPrintablesController extends BasePrintableController {

    @FXML
    private RadioButton allMattersRB, byMatterRB, byDayRB, byRoomRB, allRoomsRB, orderByMatterRB, orderByRoomRB;
    @FXML
    private ToggleGroup options1TG, options2TG, options3TG;
    @FXML
    private ComboBox<String> mattersCB;
    @FXML
    private ComboBox<LocalDate> daysCB;
    @FXML
    private HBox warningHB;
    @FXML
    private ComboBox<Room> roomsCB;
    @FXML
    private ImageView tplIV;

    public ExecutionPrintablesController() {
        super();
        generator = new ExecutionPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        mattersCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            byMatterRB.setSelected(true);
            PREF_BUNDLE.update("EXECUTION_PRINTABLE_SELECTED_MATTER_INTEGER", cur + "");
        });
        daysCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            byDayRB.setSelected(true);
            PREF_BUNDLE.update("EXECUTION_PRINTABLE_SELECTED_DAY_INTEGER", cur + "");
        });
        roomsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            byRoomRB.setSelected(true);
            PREF_BUNDLE.update("EXECUTION_PRINTABLE_SELECTED_ROOM_INTEGER", cur + "");
        });
        allMattersRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("EXECUTION_PRINTABLE_ALL_MATTERS_SELECTED_BOOLEAN", cur ? "1" : "0"));
        byMatterRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("EXECUTION_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN", cur ? "1" : "0"));
        byDayRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("EXECUTION_PRINTABLE_SINGLE_DAY_SELECTED_BOOLEAN", cur ? "1" : "0"));
        byRoomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("EXECUTION_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN", cur ? "1" : "0"));
        allRoomsRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("EXECUTION_PRINTABLE_ALL_ROOMS_SELECTED_BOOLEAN", cur ? "1" : "0"));
        orderByMatterRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("EXECUTION_PRINTABLE_GROUP_BY_MATTER_SELECTED_BOOLEAN", cur ? "1" : "0"));
        orderByRoomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("EXECUTION_PRINTABLE_GROUP_BY_ROOM_SELECTED_BOOLEAN", cur ? "1" : "0"));
        
        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    if (byDayRB.isSelected()) {
                        if (allRoomsRB.isSelected()) {
                            ((ExecutionPDFFileGenerator) generator).generate(daysCB.getValue(), orderByMatterRB.isSelected() ? GROUPING.BY_MATTER : GROUPING.BY_ROOM);
                        } else {
                            ((ExecutionPDFFileGenerator) generator).generate(daysCB.getValue(), roomsCB.getSelectionModel().getSelectedItem(), orderByMatterRB.isSelected() ? GROUPING.BY_MATTER : GROUPING.BY_ROOM);
                        }
                    } else if (byMatterRB.isSelected()) {
                        if (allRoomsRB.isSelected()) {
                            ((ExecutionPDFFileGenerator) generator).generate(mattersCB.getValue(), orderByMatterRB.isSelected() ? GROUPING.BY_MATTER : GROUPING.BY_ROOM);
                        } else {
                            ((ExecutionPDFFileGenerator) generator).generate(mattersCB.getValue(), roomsCB.getSelectionModel().getSelectedItem(), orderByMatterRB.isSelected() ? GROUPING.BY_MATTER : GROUPING.BY_ROOM);
                        }
                    } else {
                        if (allRoomsRB.isSelected()) {
                            ((ExecutionPDFFileGenerator) generator).generate(orderByMatterRB.isSelected() ? GROUPING.BY_MATTER : GROUPING.BY_ROOM);
                        } else {
                            ((ExecutionPDFFileGenerator) generator).generate(roomsCB.getSelectionModel().getSelectedItem(), orderByMatterRB.isSelected() ? GROUPING.BY_MATTER : GROUPING.BY_ROOM);
                        }
                    }
                }).start();
            }
        });
        warningHB.managedProperty().bind(warningHB.visibleProperty());
    }

    @Override
    public void getReady() {
        super.getReady();
        warningHB.setVisible(EXAM.getPlannings().isEmpty());
        EXAM.getPlannings().addListener((ListChangeListener) (c -> {
            warningHB.setVisible(c.getList().isEmpty());
        }));
    }

    @Override
    public void refresh() {
        mattersCB.getItems().clear();
        mattersCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(p -> p.getLabel()).distinct().collect(Collectors.toList())));
        daysCB.getItems().clear();
        daysCB.getItems().addAll(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(p -> p.getDate()).distinct().collect(Collectors.toList())));
        roomsCB.getItems().clear(); // This must be done. Don't know why ?
        roomsCB.getItems().addAll(EXAM.getRooms());
        try {
            mattersCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("EXECUTION_PRINTABLE_SELECTED_MATTER_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            daysCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("EXECUTION_PRINTABLE_SELECTED_DAY_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        try {
            roomsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("EXECUTION_PRINTABLE_SELECTED_ROOM_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        allMattersRB.setSelected("1".equals(PREF_BUNDLE.get("EXECUTION_PRINTABLE_ALL_MATTERS_SELECTED_BOOLEAN")));
        byMatterRB.setSelected("1".equals(PREF_BUNDLE.get("EXECUTION_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN")));
        byDayRB.setSelected("1".equals(PREF_BUNDLE.get("EXECUTION_PRINTABLE_SINGLE_DAY_SELECTED_BOOLEAN")));
        byRoomRB.setSelected("1".equals(PREF_BUNDLE.get("EXECUTION_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN")));
        allRoomsRB.setSelected("1".equals(PREF_BUNDLE.get("EXECUTION_PRINTABLE_ALL_ROOMS_SELECTED_BOOLEAN")));
        orderByMatterRB.setSelected("1".equals(PREF_BUNDLE.get("EXECUTION_PRINTABLE_GROUP_BY_MATTER_SELECTED_BOOLEAN")));
        orderByRoomRB.setSelected("1".equals(PREF_BUNDLE.get("EXECUTION_PRINTABLE_GROUP_BY_ROOM_SELECTED_BOOLEAN")));
        
        if ( options1TG.getSelectedToggle() == null )
            options1TG.selectToggle(options1TG.getToggles().get(0));
        if ( options2TG.getSelectedToggle() == null )
            options2TG.selectToggle(options2TG.getToggles().get(0));
        if ( options3TG.getSelectedToggle() == null )
            options3TG.selectToggle(options3TG.getToggles().get(0));
    }

}
