package poo.calendar.dialogscenes;

import java.io.InputStream;
import java.util.Scanner;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import poo.calendar.ControlledWidget;
import poo.calendar.widgets.ElfCalendarText;

public class AboutDialogController extends ControlledWidget<VBox> {
	private VBox mWidget;

	private static final Background mainBG =
			new Background(new BackgroundFill(Color.WHITE, null, null));

	public AboutDialogController(){
	}

	/**
	 * Initializes the widget to a valid state.
	 * Automatically called upon construction.
	 */
	protected void initializeWidget(){
		//Icon source: https://www.iconfinder.com/icons/1287530/calendar_date_event_month_schedule_time_icon#size=48
		InputStream is = this.getClass().getClassLoader().getResourceAsStream("about.txt");
		StringBuilder builder = new StringBuilder();

		try(Scanner sc = new Scanner(is)){
			while(sc.hasNextLine()){
				builder.append(sc.nextLine() + "\n");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		ElfCalendarText text = new ElfCalendarText(60);

		Label lbl = new Label(builder.toString());
		lbl.setAlignment(Pos.CENTER);
		lbl.setWrapText(true);
		lbl.setTextAlignment(TextAlignment.CENTER);
		lbl.setFont(Font.font("Liberation Sans", 14));
		lbl.setPrefSize(Double.MAX_VALUE, 300);

		mWidget = new VBox(text, lbl);
		mWidget.setAlignment(Pos.CENTER);
		mWidget.setSpacing(50);
		mWidget.setBackground(mainBG);
	}

	/**
	 * Returns the VBox that holds the About dialog.
	 */
	public VBox getWidget(){
		return mWidget;
	}
}
