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

import java.time.LocalDate;
import java.time.LocalTime;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class Planning {

    private final SimpleStringProperty label = new SimpleStringProperty();
    private final SimpleObjectProperty<LocalDate> date = new SimpleObjectProperty();
    private final SimpleObjectProperty<LocalTime> startTime = new SimpleObjectProperty();
    private final SimpleObjectProperty<LocalTime> endTime = new SimpleObjectProperty();

    public Planning() {
        this("", LocalDate.now(), LocalTime.now(), LocalTime.now());
    }

    public Planning(String label, LocalDate date, LocalTime startTime, LocalTime endTime) {
        this.label.set(label);
        this.date.set(date);
        this.startTime.set(startTime);
        this.endTime.set(endTime);

        this.startTime.addListener((obs, old, cur) -> {
            if (this.startTime.get().isAfter(this.endTime.get())) {
                this.endTime.set(this.startTime.get().plusMinutes(5));
            }
        });
        this.endTime.addListener((obs, old, cur) -> {
            if (this.endTime.get().isBefore(this.startTime.get())) {
                this.startTime.setValue(this.endTime.get().minusMinutes(5));
            }
        });
    }

    public SimpleObjectProperty<LocalTime> endTimeProperty() {
        return endTime;
    }

    public LocalTime getEndTime() {
        return endTime.get();
    }

    public void setEndTime(LocalTime endTime) {
        this.endTime.set(endTime);
    }

    public SimpleObjectProperty<LocalTime> startTimeProperty() {
        return startTime;
    }

    public LocalTime getStartTime() {
        return startTime.get();
    }

    public void setStartTime(LocalTime startTime) {
        this.startTime.set(startTime);
    }

    public LocalDate getDate() {
        return date.get();
    }

    public SimpleObjectProperty<LocalDate> dateProperty() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date.set(date);
    }

    public SimpleStringProperty labelProperty() {
        return label;
    }

    public String getLabel() {
        return label.get();
    }

    public void setLabel(String label) {
        this.label.set(label);
    }

    @Override
    public String toString() {
        return label.get();
    }

}
