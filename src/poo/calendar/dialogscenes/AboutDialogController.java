package poo.calendar.dialogscenes;

import java.io.InputStream;
import java.util.Scanner;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import poo.calendar.ControlledWidget;

public class AboutDialogController extends ControlledWidget<VBox> {
	private VBox mWidget;
	
	public AboutDialogController(){
	}
	
	/**
	 * Initializes the widget to a valid state.
	 * Automatically called upon construction.
	 */
	protected void initializeWidget(){
		InputStream is = this.getClass().getResourceAsStream("/poo/calendar/resources/about.txt");
		StringBuilder builder = new StringBuilder();
		
		try(Scanner sc = new Scanner(is)){
			while(sc.hasNextLine()){
				builder.append(sc.nextLine() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		Label lbl = new Label(builder.toString());
		lbl.setAlignment(Pos.CENTER);
		lbl.setWrapText(true);
		lbl.setTextAlignment(TextAlignment.CENTER);
		lbl.setFont(Font.font("Liberation Sans", 14));
		lbl.setPrefSize(300, 300);
		mWidget = new VBox(lbl);
		mWidget.setAlignment(Pos.CENTER);
	}
	
	/**
	 * Returns the VBox that holds the About dialog.
	 */
	public VBox getWidget(){
		return mWidget;
	}
}
