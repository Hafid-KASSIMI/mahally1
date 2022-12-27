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
import localexam.template.generators.TechCardPDFFileGenerator;

/**
 *
 * @author H. KASSIMI (@mdrassty)
 */
public class TechCardPrintablesController extends BasePrintableController {

    public TechCardPrintablesController() {
        super();
        generator = new TechCardPDFFileGenerator();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        super.initialize(url, rb);

        generateBtn.setOnAction(evt -> {
            if (destination == null) {
                chooseDirectory();
            }
            if (destination != null) {
                new Thread(() -> {
                    ((TechCardPDFFileGenerator) generator).generate();
                }).start();
            }
        });
    }

    @Override
    public void refresh() {
    }

}
