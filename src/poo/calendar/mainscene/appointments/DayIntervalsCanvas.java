package poo.calendar.mainscene.appointments;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.layout.Region;
import javafx.scene.paint.Color;

/**
 * Class that draws all 15-minute intervals of a day along all the space allocated for this class.
 * There should be 24 * 4 horizontal lines, where for every 4 lines, 3 are thinner than the last one.
 * The thinner lines represent XX:15, XX:30 and XX:45 time.
 */
public class DayIntervalsCanvas extends Canvas {
	/**
	 * Defaults constructor.
	 * The Region parameter is required, due to the necessity of this class to bind to its
	 * parents height and width properties.
	 * The Cnavas is not automatically added to the Region. The caller must add and then
	 * set the canvas to the back of the children list (by a call to canvas.toBack().
	 * @param reg Region that will receive this canvas.
	 */
	public DayIntervalsCanvas(Region reg){
		this.widthProperty().addListener(action -> draw());
		this.heightProperty().addListener(action -> draw());
		
		this.widthProperty().bind(reg.widthProperty());
		this.heightProperty().bind(reg.heightProperty());
		
		this.setOpacity(0.5);
	}
	
	private void draw(){
		double height = this.getHeight();
		double width = this.getWidth();
		GraphicsContext gc = this.getGraphicsContext2D();
		gc.setStroke(Color.DARKGRAY);
		gc.clearRect(0, 0, width, height);
		
		double step = height / 24.0 / 4.0;
		int counter = 0;
		for(double i = 0; i < height; i += step, counter++){
			if(counter % 4 == 0) gc.setLineWidth(2);
			else gc.setLineWidth(1);
			gc.strokeLine(0, i, width, i);
		}
	}
	
	@Override
	public boolean isResizable(){
		return true;
	}
	
	@Override
	public double prefHeight(double width){
		return -1; //USE_COMPUTED_SIZE
	}
	
	@Override
	public double prefWidth(double height){
		return -1; //USE_COMPUTED_SIZE
	}
}
