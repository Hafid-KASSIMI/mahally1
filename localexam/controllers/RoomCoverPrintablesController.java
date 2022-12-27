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
import localexam.template.generators.RoomCoverPDFFileGenerator;
import localexam.template.generators.RoomCoverPDFFileGenerator.SORT;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class RoomCoverPrintablesController extends BasePrintableController {

    @FXML
    private HBox warningHB;
    @FXML
    private RadioButton marksAllMatsRB, marksOneMatRB, listsAllRB, listsByRoomRB, orderByMatterRB, orderByRoomRB, A3RB, A4RB;
    @FXML
    private ComboBox<String> marksMatterCB;
    @FXML
    private ToggleGroup listsTG, orderTG, paperTG, mattersTG;
    @FXML
    private ComboBox<Room> listsRoomsCB;
    @FXML
    private ImageView tplIV;

    public RoomCoverPrintablesController() {
        super();
        generator = new RoomCoverPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);
        
        marksMatterCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            marksOneMatRB.setSelected(true);
            PREF_BUNDLE.update("ROOM_COVER_PRINTABLE_SELECTED_MATTER_INTEGER", cur + "");
        });
        listsRoomsCB.getSelectionModel().selectedIndexProperty().addListener((obs, old, cur) -> {
            listsByRoomRB.setSelected(true);
            PREF_BUNDLE.update("ROOM_COVER_PRINTABLE_SELECTED_ROOM_INTEGER", cur + "");
        });
        marksAllMatsRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_ALL_MATTERS_SELECTED_BOOLEAN", cur ? "1" : "0"));
        marksOneMatRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN", cur ? "1" : "0"));
        listsAllRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_ALL_ROOMS_SELECTED_BOOLEAN", cur ? "1" : "0"));
        listsByRoomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN", cur ? "1" : "0"));
        orderByMatterRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_GROUP_BY_MATTER_SELECTED_BOOLEAN", cur ? "1" : "0"));
        orderByRoomRB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_GROUP_BY_ROOM_SELECTED_BOOLEAN", cur ? "1" : "0"));
        A3RB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_A3_SELECTED_BOOLEAN", cur ? "1" : "0"));
        A4RB.selectedProperty().addListener((obs, old, cur) -> PREF_BUNDLE.update("ROOM_LABEL_PRINTABLE_A4_SELECTED_BOOLEAN", cur ? "1" : "0"));
        
        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                if (destination == null) {
                    chooseDirectory();
                }
                if (destination != null) {
                    new Thread(() -> {
                        if (A3RB.isSelected()) {
                            ((RoomCoverPDFFileGenerator) generator).setA3();
                        } else {
                            ((RoomCoverPDFFileGenerator) generator).setA4();
                        }
                        if (listsByRoomRB.isSelected() && marksOneMatRB.isSelected()) {
                            ((RoomCoverPDFFileGenerator) generator).generate(listsRoomsCB.getValue(), marksMatterCB.getValue(), orderByMatterRB.isSelected() ? SORT.BY_MATTER : SORT.BY_ROOM);
                        } else {
                            if (listsByRoomRB.isSelected()) {
                                ((RoomCoverPDFFileGenerator) generator).generate(listsRoomsCB.getValue(), orderByMatterRB.isSelected() ? SORT.BY_MATTER : SORT.BY_ROOM);
                            } else if (marksOneMatRB.isSelected()) {
                                ((RoomCoverPDFFileGenerator) generator).generate(marksMatterCB.getValue(), orderByMatterRB.isSelected() ? SORT.BY_MATTER : SORT.BY_ROOM);
                            } else {
                                ((RoomCoverPDFFileGenerator) generator).generate(orderByMatterRB.isSelected() ? SORT.BY_MATTER : SORT.BY_ROOM);
                            }
                        }
                    }).start();
                }
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
        listsRoomsCB.getItems().clear();
        listsRoomsCB.setItems(EXAM.getRooms());
        marksMatterCB.getItems().clear();
        marksMatterCB.setItems(FXCollections.observableArrayList(EXAM.getPlannings().stream().map(p -> p.getLabel()).distinct().collect(Collectors.toList())));
        
        try {
            listsRoomsCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("ROOM_COVER_PRINTABLE_SELECTED_ROOM_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        
        try {
            marksMatterCB.getSelectionModel().select(Integer.parseInt(PREF_BUNDLE.get("ROOM_COVER_PRINTABLE_SELECTED_MATTER_INTEGER")));
        } catch ( NullPointerException | NumberFormatException ex ) {}
        marksAllMatsRB.setSelected("1".equals(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_ALL_MATTERS_SELECTED_BOOLEAN")));
        marksOneMatRB.setSelected("1".equals(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_SINGLE_MATTER_SELECTED_BOOLEAN")));
        listsAllRB.setSelected("1".equals(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_ALL_ROOMS_SELECTED_BOOLEAN")));
        listsByRoomRB.setSelected("1".equals(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_SINGLE_ROOM_SELECTED_BOOLEAN")));
        orderByMatterRB.setSelected("1".equals(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_GROUP_BY_MATTER_SELECTED_BOOLEAN")));
        orderByRoomRB.setSelected("1".equals(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_GROUP_BY_ROOM_SELECTED_BOOLEAN")));
        A3RB.setSelected("1".equals(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_A3_SELECTED_BOOLEAN")));
        A4RB.setSelected("1".equals(PREF_BUNDLE.get("ROOM_LABEL_PRINTABLE_A4_SELECTED_BOOLEAN")));
        if ( mattersTG.getSelectedToggle() == null )
            mattersTG.selectToggle(mattersTG.getToggles().get(0));
        if ( paperTG.getSelectedToggle() == null )
            paperTG.selectToggle(paperTG.getToggles().get(0));
        if ( orderTG.getSelectedToggle() == null )
            orderTG.selectToggle(orderTG.getToggles().get(0));
        if ( listsTG.getSelectedToggle() == null )
            listsTG.selectToggle(listsTG.getToggles().get(0));
    }

}
