package poo.calendar;

import javafx.scene.Node;

/**
 * Class that every widget controller should extend.
 */
public abstract class ControlledWidget<T extends Node> {
	public ControlledWidget(){
		initializeWidget();
	}
	
	/**
	 * @return the widget controller by this class
	 */
	public abstract T getWidget();
	
	/**
	 * This method should set up the widget to a valid state that
	 * is independent of any model data. The widget should be subject
	 * to usage even if no model has been bound to it.
	 * If the widget is loaded by FXMLLoader, then this method should
	 * load the widget, then the controller, then copy all content of the
	 * returned controller to this controller.
	 */
	protected abstract void initializeWidget();
}
