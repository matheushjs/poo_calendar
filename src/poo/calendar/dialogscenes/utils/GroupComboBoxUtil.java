package poo.calendar.dialogscenes.utils;

import javafx.geometry.Pos;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.layout.HBox;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Circle;
import javafx.util.Callback;
import poo.calendar.model.CalendarGroup;

/**
 * Class that holds utilities for use in the ComboBox that contains calendar groups.
 */
public class GroupComboBoxUtil {
	
	/**
	 * @return a CallBack for adding to a combo box that should display calendar groups.
	 * This Callback will display each CalendarGroups with their color followed by their name.
	 */
	public static Callback<ListView<CalendarGroup>, ListCell<CalendarGroup>> getAddCallback(){
		return new Callback<ListView<CalendarGroup>, ListCell<CalendarGroup>>(){
			@Override
			public ListCell<CalendarGroup> call(ListView<CalendarGroup> p){
				return new ListCell<CalendarGroup>() {
					private final HBox box;
					private final Circle circle;
					private final Label lbl;
					{
						setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
						circle = new Circle(5);
						lbl = new Label();
						box = new HBox(circle, lbl);
						box.setAlignment(Pos.CENTER_LEFT);
						box.setSpacing(3);
					}
					
					@Override
					protected void updateItem(CalendarGroup item, boolean empty){
						super.updateItem(item, empty);
						if(item == null || empty){
							circle.setVisible(false);
							lbl.setText("None");
							setGraphic(box);
						} else {
							circle.setFill(Paint.valueOf(item.getColor().toString()));
							circle.setVisible(true);
							lbl.setText(item.getName());
							setGraphic(box);
						}
					}
				};
			}
		};
	}
}
