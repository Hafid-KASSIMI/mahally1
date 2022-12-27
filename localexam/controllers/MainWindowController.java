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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.ResourceBundle;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import localexam.Exam;
import localexam.Loader;
import localexam.Launcher;
import localexam.LocalExamMarking;
import localexam.MahallyWindow;
import localexam.Settings;
import static localexam.Settings.APP_EXTENSION;
import static localexam.Settings.APP_EXTENSION2;
import static localexam.Settings.APP_NAME;
import static localexam.Settings.APP_YEAR;
import static localexam.Settings.PREF_BUNDLE;
import static localexam.Settings.SAVER;
import localexam.bones.Saver.RESULT;
import localexam.bones.enums.EXAM_STATUS;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class MainWindowController extends BaseController {

    @FXML
    private Button newBtn, openBtn, saveBtn, saveAsBtn, exportBtn, importBtn, quitBtn,
            paramsBtn, timeTableBtn, multiExportBtn, enterMarksBtn, enterTransMarksBtn, marksBtn, listsBtn, labelsBtn, pvsBtn,
            techCardBtn, requisitBtn, statementBtn, savePendingBtn, forceCloseBtn, cancelCloseBtn, exportExcelFileBtn, aboutCloseBtn,
            aboutBtn, saveChangesCloseBtn, coversBtn, loadingDataCloseBtnVB, listsHeadersBtn, closingPvBtn, openingPvBtn, roomsLabelsBtn,
            correctionHeadersBtn, guardsBtn, fileNotRecognizedVBCloseBtn, executionBtn;
    @FXML
    private VBox leftBarVB, saveChangesVB, aboutVB, loadingDataVB, fileNotRecognizedVB;
    @FXML
    private StackPane bodySP;
    @FXML
    private NewExamController newExamVBController;
    @FXML
    private TabPane ribbonBarTP;
    @FXML
    private Tab filaeTab, opsTab, exImTab, printTab, helpTab;
    @FXML
    private Label schoolLbl, appLbl, yearLbl, levelLbl, stusCountLbl, girlsCountLbl, aboutDateLbl, aboutVerLbl;

    private final FileChooser fc = new FileChooser();
    private final HashMap<Button, Loader<Parent, BaseController>> boxesMap = new HashMap();
    private final ArrayList<Button> disabledRibbonBtns = new ArrayList(); // Disabled when exam's not ready

    public MainWindowController() {
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        newExamVBController.examCreatedProperty().addListener((obs, old, cur) -> {
            if (cur) {
                setExam(newExamVBController.getExam());
                ribbonBarTP.getSelectionModel().select(opsTab);
                paramsBtn.fire();
            }
        });
        init();
        newBtn.setDisable(true);
        saveBtn.setDisable(true);
        saveAsBtn.setDisable(true);
        paramsBtn.setDisable(true);

        disabledRibbonBtns.addAll(Arrays.asList(exportBtn, importBtn, timeTableBtn, multiExportBtn, enterMarksBtn,
                enterTransMarksBtn, marksBtn, listsBtn, labelsBtn, pvsBtn, techCardBtn, requisitBtn, statementBtn, exportExcelFileBtn, coversBtn,
                listsHeadersBtn, closingPvBtn, openingPvBtn, roomsLabelsBtn, correctionHeadersBtn, guardsBtn, executionBtn));
        disabledRibbonBtns.forEach(btn -> {
            btn.setDisable(true);
        });

        saveChangesVB.managedProperty().bind(saveChangesVB.visibleProperty());
        aboutVB.managedProperty().bind(aboutVB.visibleProperty());
        loadingDataVB.managedProperty().bind(loadingDataVB.visibleProperty());

        appLbl.setText(Settings.APP_NAME + " " + Settings.APP_YEAR);
        aboutDateLbl.setText(Settings.APP_DATE);
        aboutVerLbl.setText(Settings.APP_VERSION + " (" + Settings.APP_YEAR + ")");

        newBtn.setOnAction(evt -> {
            try {
                Launcher newApp = new Launcher();
                newApp.start(new Stage());
            } catch (Exception ex) {
            }
        });

        openBtn.setOnAction(evt -> {
            File f;
            setInitialDirectory("SAVE_DIR_STRING");
            fc.getExtensionFilters().clear();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("مستندات محلي", "*" + APP_EXTENSION));
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("مستندات التقيط", "*" + APP_EXTENSION2));
            fc.setTitle("تحديد المستند المراد فتحه");
            f = fc.showOpenDialog(saveAsBtn.getScene().getWindow());
            if (f != null) {
                openFile(f);
            }
        });
        saveBtn.setOnAction(evt -> {
            if (EXAM.getSource() != null) {
                SAVER.saveExam(EXAM, EXAM.getSource());
                EXAM.setStatus(EXAM_STATUS.SAVED);
            } else {
                File f;
                setInitialDirectory("SAVE_DIR_STRING");
                fc.getExtensionFilters().clear();
                fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("مستندات محلي", "*" + APP_EXTENSION));
                fc.setTitle("اختيار اسم ومكان الحفظ");
                f = fc.showSaveDialog(saveAsBtn.getScene().getWindow());
                saveAs(f);
            }
        });

        saveAsBtn.setOnAction(evt -> {
            File f;
            setInitialDirectory("SAVE_DIR_STRING");
            fc.getExtensionFilters().clear();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("مستندات محلي", "*" + APP_EXTENSION));
            fc.setTitle("اختيار اسم ومكان الحفظ");
            f = fc.showSaveDialog(saveAsBtn.getScene().getWindow());
            saveAs(f);
        });

        importBtn.setOnAction(evt -> {
            File f;
            setInitialDirectory("SAVE_DIR_STRING");
            fc.getExtensionFilters().clear();
            fc.getExtensionFilters().add(new FileChooser.ExtensionFilter("مستندات التقيط", "*" + APP_EXTENSION2));
            fc.setTitle("تحديد المستند المراد فتحه");
            f = fc.showOpenDialog(importBtn.getScene().getWindow());
            if (f != null) {
                SAVER.parseMarks(EXAM, f);
                if (SAVER.getParseResult() == RESULT.SUCCESS) {
                    enterMarksBtn.fire();
                    EXAM.setStatus(EXAM_STATUS.PENDING_CHANGES);
                }
            }
        });
        quitBtn.setOnAction(evt -> {
            if (EXAM != null) {
                if (EXAM.getStatus().equals(EXAM_STATUS.PENDING_CHANGES)) {
                    saveChangesVB.setVisible(true);
                } else {
                    ((Stage) forceCloseBtn.getScene().getWindow()).close();
                    PREF_BUNDLE.commit();
                }
            } else {
                ((Stage) forceCloseBtn.getScene().getWindow()).close();
                PREF_BUNDLE.commit();
            }
        });
        savePendingBtn.setOnAction(evt -> {
            saveChangesVB.setVisible(false);
            saveBtn.fire();
            ((Stage) forceCloseBtn.getScene().getWindow()).close();
        });
        forceCloseBtn.setOnAction(evt -> {
            ((Stage) forceCloseBtn.getScene().getWindow()).close();
        });
        cancelCloseBtn.setOnAction(evt -> {
            saveChangesVB.setVisible(false);
        });
        saveChangesCloseBtn.setOnAction(evt -> {
            saveChangesVB.setVisible(false);
        });
        aboutBtn.setOnAction(evt -> {
            aboutVB.setVisible(true);
        });
        aboutCloseBtn.setOnAction(evt -> {
            aboutVB.setVisible(false);
        });
        loadingDataCloseBtnVB.setOnAction(evt -> {
            loadingDataVB.setVisible(false);
        });
        fileNotRecognizedVBCloseBtn.setOnAction(evt -> {
            fileNotRecognizedVB.setVisible(false);
        });
    }

    private void saveAs(File f) {
        if (f != null) {
            String path = f.getAbsolutePath();
            if (!f.getName().endsWith(APP_EXTENSION)) {
                path += APP_EXTENSION;
            }
            EXAM.setSource(f);
            SAVER.saveExam(EXAM, path);
            EXAM.setStatus(EXAM_STATUS.SAVED);
            ((Stage) saveAsBtn.getScene().getWindow()).setTitle(EXAM.getLabel() + " - " + APP_NAME + " " + APP_YEAR);
            PREF_BUNDLE.update("SAVE_DIR_STRING", f.getParent());
        }
    }

    private void show(Loader<Parent, BaseController> loader) {
        if (loader.getRoot() != null) {
            Platform.runLater(() -> {
                bodySP.getChildren().forEach(node -> node.setVisible(false));
                loader.getRoot().setVisible(true);
                loader.getController().refresh();
            });
        }
    }

    @Override
    public void closeRequest() {
        quitBtn.fire();
    }

    @Override
    public final void setExam(Exam exam) {
        super.setExam(exam);
        Platform.runLater(() -> {
            schoolLbl.setText(exam.getInfos().getSchool());
            yearLbl.setText(exam.getInfos().getYear());
            levelLbl.setText(exam.getLevel().getName() + "");
            stusCountLbl.setText(exam.getStudentsCount() + "");
            girlsCountLbl.setText(exam.getGirlsCount() + "");
            leftBarVB.setVisible(true);
        });
        newExamVBController.setExam(exam);
        boxesMap.values().forEach((loader) -> {
            loader.setExam(exam);
        });
        disabledRibbonBtns.forEach(btn -> {
            btn.disableProperty().bind(exam.statusProperty().isNotEqualTo(EXAM_STATUS.READY)
                    .and(exam.statusProperty().isNotEqualTo(EXAM_STATUS.PENDING_CHANGES))
                    .and(exam.statusProperty().isNotEqualTo(EXAM_STATUS.SAVED))
            );
        });
        newBtn.disableProperty().bind(exam.statusProperty().isEqualTo(EXAM_STATUS.EMPTY));
        saveBtn.disableProperty().bind(exam.statusProperty().isNotEqualTo(EXAM_STATUS.PENDING_CHANGES));
        saveAsBtn.disableProperty().bind(exam.statusProperty().isEqualTo(EXAM_STATUS.EMPTY).or(exam.statusProperty().isEqualTo(EXAM_STATUS.NONE)));
        paramsBtn.disableProperty().bind(exam.statusProperty().isEqualTo(EXAM_STATUS.EMPTY));
        if (exam.isReady()) {
            ribbonBarTP.getSelectionModel().select(opsTab);
            paramsBtn.fire();
            Platform.runLater(() -> {
                ((Stage) paramsBtn.getScene().getWindow()).setTitle(exam.getLabel() + " [" + APP_NAME + " " + APP_YEAR + "]");
            });

        }
//        exam.statusProperty().addListener((obs, old, cur) -> System.out.println("Old : " + old + "; cur : " + cur + "; Status equals empty ? "
//                + exam.statusProperty().isNotEqualTo(EXAM_STATUS.EMPTY).get()));
    }

    private void init() {
        boxesMap.put(paramsBtn, new Loader("/localexam/views/examParameters.fxml"));
        boxesMap.put(timeTableBtn, new Loader("/localexam/views/examTimeTable.fxml"));
        boxesMap.put(exportBtn, new Loader("/localexam/views/exportMarksFile.fxml"));
        boxesMap.put(multiExportBtn, new Loader("/localexam/views/exportMarksFiles.fxml"));
        boxesMap.put(enterMarksBtn, new Loader("/localexam/views/inputMarks.fxml"));
        boxesMap.put(enterTransMarksBtn, new Loader("/localexam/views/inputTransMarks.fxml"));
        boxesMap.put(listsBtn, new Loader("/localexam/views/listsPrintables.fxml"));
        boxesMap.put(pvsBtn, new Loader("/localexam/views/pvsPrintables.fxml"));
        boxesMap.put(requisitBtn, new Loader("/localexam/views/requisitPrintables.fxml"));
        boxesMap.put(labelsBtn, new Loader("/localexam/views/labelsPrintables.fxml"));
        boxesMap.put(techCardBtn, new Loader("/localexam/views/techCardPrintables.fxml"));
        boxesMap.put(statementBtn, new Loader("/localexam/views/statementPrintables.fxml"));
        boxesMap.put(marksBtn, new Loader("/localexam/views/marksPrintables.fxml"));
        boxesMap.put(exportExcelFileBtn, new Loader("/localexam/views/excelStatement.fxml"));
        boxesMap.put(coversBtn, new Loader("/localexam/views/roomCoverPrintables.fxml"));
        boxesMap.put(listsHeadersBtn, new Loader("/localexam/views/listHeaderPrintables.fxml"));
        boxesMap.put(closingPvBtn, new Loader("/localexam/views/closingPrintables.fxml"));
        boxesMap.put(openingPvBtn, new Loader("/localexam/views/openingPrintables.fxml"));
        boxesMap.put(roomsLabelsBtn, new Loader("/localexam/views/roomLabelPrintables.fxml"));
        boxesMap.put(correctionHeadersBtn, new Loader("/localexam/views/correctionHeadersPrintables.fxml"));
        boxesMap.put(guardsBtn, new Loader("/localexam/views/guardsPrintables.fxml"));
        boxesMap.put(executionBtn, new Loader("/localexam/views/executionPrintables.fxml"));

        boxesMap.forEach((btn, loader) -> {
            btn.setOnAction(evt -> {
                if (!loader.isLoaded()) {
                    loader.load();
                    Platform.runLater(() -> {
                        bodySP.getChildren().add(loader.getRoot());
                    });
                }
                show(loader);
            });
        });
    }

    private void setInitialDirectory(String pref) {
        try {
            if (!"".equals(PREF_BUNDLE.get(pref)) && Files.exists(Paths.get(PREF_BUNDLE.get(pref)))
                    && Files.isDirectory(Paths.get(PREF_BUNDLE.get(pref)))) {
                fc.setInitialDirectory(new File(PREF_BUNDLE.get(pref)));
            }
        } catch (InvalidPathException | SecurityException ipe) {
        }
    }

    @Override
    public void openFile(File f) {
        new Thread(() -> {
            if (Launcher.isMahallyMarksFile(f.getPath())) {
                Platform.runLater(() -> {
                    MahallyWindow window = new LocalExamMarking(SAVER.parseMarks(f), f);
                    window.load();
                    window.show();
                });
            } else if (Launcher.isMahallyFile(f.getPath())) {
                if (EXAM == null || EXAM.getStatus().equals(EXAM_STATUS.EMPTY)) {
                    Platform.runLater(() -> {
                        loadingDataVB.setVisible(true);
                    });
                    Exam tmp = SAVER.parseExam(f);
                    Platform.runLater(() -> {
                        loadingDataVB.setVisible(false);
                    });
                    if (RESULT.SUCCESS.equals(SAVER.getParseResult())) {
                        setExam(tmp);
                    } else {
                        Platform.runLater(() -> {
                            fileNotRecognizedVB.setVisible(true);
                        });
                    }
                } else {
                    Platform.runLater(() -> {
                        Launcher.checkNShowWindowForFile(f);
                    });
                }
            } else {
                Platform.runLater(() -> {
                    fileNotRecognizedVB.setVisible(true);
                });
            }
            PREF_BUNDLE.update("SAVE_DIR_STRING", f.getParent());
        }).start();
    }

    @Override
    public void refresh() {
    }
}
