package poo.calendar.mainscene.groups;

import java.util.UUID;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import poo.calendar.model.CalendarGroup;

/**
 * Class that represents a widget for displaying a calendar group in the UI.
 */
public class GroupView extends HBox {
	private UUID mID;
	
	/**
	 * Default constructor
	 * @param title
	 * @param color
	 * @param id
	 */
	public GroupView(CalendarGroup group){
		mID = group.getID();
		
		//TODO: Treat giant labels
		Label label = new Label();
		label.textProperty().bind(group.nameProperty());
		label.setFont(Font.font(14));
		
		Circle circle = new Circle(10.0, Paint.valueOf(group.getColor().toString()));
		circle.setStrokeWidth(2.0);
		group.colorProperty().addListener(color -> {
			circle.setFill(Paint.valueOf(group.getColor().toString()));
		});
		
		this.setSpacing(3);
		this.setAlignment(Pos.CENTER_LEFT);
		this.getChildren().addAll(circle, label);
	}
	
	public UUID getID(){
		return mID;
	}
}
