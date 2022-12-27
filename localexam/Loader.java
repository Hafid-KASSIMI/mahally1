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

import java.io.IOException;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import localexam.controllers.BaseController;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 * @param <S>
 * @param <T>
 */
public class Loader<S extends Parent, T extends BaseController> {

    private S root = null;
    private T controller = null;
    private Exam exam;
    private String view;
    private final BooleanProperty loaded = new SimpleBooleanProperty(false);

    public Loader() {

    }

    public Loader(S root, T controller) {
        this.root = root;
        this.controller = controller;
        loaded.set(true);
    }

    public Loader(String view) {
        this.view = view;
    }

    public Loader(String view, Exam exam) {
        this.view = view;
        this.exam = exam;
    }

    public void setView(String view) {
        this.view = view;
    }

    public final void load() {
        try {
            FXMLLoader FXML_LOADER = new FXMLLoader(getClass().getResource(view));
            FXML_LOADER.load();
            root = FXML_LOADER.getRoot();
            controller = FXML_LOADER.getController();
            loaded.set(true);
            controller.setExam(exam);
            controller.getReady();
        } catch (IOException ex) {
            loaded.set(false);
        }
    }

    public S getRoot() {
        return root;
    }

    public T getController() {
        return controller;
    }

    public boolean isLoaded() {
        return loaded.get();
    }

    public void setLoaded(boolean value) {
        loaded.set(value);
    }

    public BooleanProperty loadedProperty() {
        return loaded;
    }

    public void setExam(Exam exam) {
        this.exam = exam;
        if (loaded.get()) {
            controller.setExam(exam);
        }
    }

}
