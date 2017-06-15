package poo.calendar.dialogscenes.utils;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.layout.Border;
import javafx.scene.layout.BorderStroke;
import javafx.scene.layout.BorderStrokeStyle;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;

/**
 * Class for managing warnings.
 * Given a Pane, this class will allow the user of the class to add/remove warnings to that pane in a
 * specified index.
 * Whenever there is a warning to show, the given pane receives a red border.
 */
public class WarningHandler {
	// Index where the warning should appear on the Parent node.
	private int mIndex;
	
	// Label with the warnings
	private Label mWarningLabel;
	
	// Parent node
	private Pane mParent;
	
	private static final Border mWarningBorder = new Border(new BorderStroke(Color.RED, BorderStrokeStyle.SOLID, null, null));
	
	public WarningHandler(Pane parent){
		mIndex = -1;
		mParent = parent;
		
		mWarningLabel = new Label("");
		mWarningLabel.setFont(Font.font(10));
		mWarningLabel.setTextFill(Paint.valueOf("red"));
		mWarningLabel.setWrapText(true);
	}
	
	
	/**
	 * Adds the given string to the list of warnings that appears beside 
	 * the Name form Box, and adds a RED border to the parent node.
	 * @param warning
	 */
	public void addWarning(String warning){
		String old = mWarningLabel.getText();
		mWarningLabel.setText(old + "\n * " + warning);
		
		ObservableList<Node> list = mParent.getChildren();
		if(!list.contains(mWarningLabel)){
			if(mIndex == -1){
				list.add(mWarningLabel);
			} else {
				try {
					list.add(mIndex, mWarningLabel);
				} catch (IndexOutOfBoundsException e) {
					list.add(mWarningLabel);
				}
			}
			
			mParent.setBorder(mWarningBorder);
		}
	}
	
	/**
	 * Removes all warnings, and clears the RED border of the parent node.
	 */
	public void clear(){
		mWarningLabel.setText("");
		mParent.getChildren().remove(mWarningLabel);
		mParent.setBorder(Border.EMPTY);
	}
	
	/**
	 * Sets the index at which the warning should be added in the parent Pane.
	 * @param index
	 */
	public void setIndex(int index){
		mIndex = index;
	}
}
