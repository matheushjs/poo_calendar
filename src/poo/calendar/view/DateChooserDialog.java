package poo.calendar.view;

import javafx.scene.control.TextInputDialog;

public class DateChooserDialog extends TextInputDialog {
	public DateChooserDialog(String windowTitle){
		setTitle(windowTitle);
		setHeaderText("Teste");
		setContentText("Teste");
	}
}
