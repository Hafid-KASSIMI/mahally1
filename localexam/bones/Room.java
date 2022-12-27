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
package localexam.bones;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class Room {

    private final SimpleStringProperty label = new SimpleStringProperty();
    private final SimpleIntegerProperty numero = new SimpleIntegerProperty();
    private final SimpleIntegerProperty capacity = new SimpleIntegerProperty();

    public Room() {
    }

    public Room(String label, int numero, int capacity) {
        this.label.set(label);
        this.numero.set(numero);
        this.capacity.set(capacity);
    }

    public int getCapacity() {
        return capacity.get();
    }

    public void setCapacity(int value) {
        capacity.set(value);
    }

    public SimpleIntegerProperty capacityProperty() {
        return capacity;
    }

    public int getNumero() {
        return numero.get();
    }

    public void setNumero(int value) {
        numero.set(value);
    }

    public SimpleIntegerProperty numeroProperty() {
        return numero;
    }

    public String getLabel() {
        return label.get();
    }

    public void setLabel(String value) {
        label.set(value);
    }

    public SimpleStringProperty labelProperty() {
        return label;
    }

    @Override
    public String toString() {
        return numero.get() + " - " + label.get();
    }

}
