package poo.calendar.view;

import javafx.scene.shape.Rectangle;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.geometry.Pos;

public class CalendarWindow extends VBox {
	public CalendarWindow(){
		this.setAlignment(Pos.BASELINE_CENTER);
		this.setSpacing(20);
		
		Rectangle r1 = new Rectangle(0, 0, 200, 600);
		Rectangle r2 = new Rectangle(0, 0, 200, 100);
		r1.setFill(Color.ALICEBLUE);
		r2.setFill(Color.BEIGE);
		
		this.getChildren().addAll(r1, r2);
	}
}