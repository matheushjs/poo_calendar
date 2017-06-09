package poo.calendar.mainscene.groups;

import java.util.UUID;

import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;

/**
 * Class that represents a widget for displaying a calendar group in the UI.
 */
public class GroupView extends HBox {
	private String mTitle;
	private Color mColor;
	private UUID mID;
	
	/**
	 * Default constructor
	 * @param title
	 * @param color
	 * @param id
	 */
	public GroupView(String title, Color color, UUID id){
		mTitle = title;
		mColor = color;
		mID = id;
		
		//TODO: Treat giant labels
		Label label = new Label(mTitle);
		label.setFont(Font.font(14));
		
		Circle circle = new Circle(10.0, Paint.valueOf(mColor.toString()));
		circle.setStrokeWidth(2.0);
		
		this.setSpacing(3);
		this.setAlignment(Pos.CENTER_LEFT);
		this.getChildren().addAll(circle, label);
	}
	
	public UUID getID(){
		return mID;
	}
}
