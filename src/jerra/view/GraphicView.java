package jerra.view;

import java.util.Map;

import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.paint.Color;
import jerra.core.Rect;
import jerra.entity.Entity;
import jerra.room.Room;
import jerra.stats.Character;
import jerra.stats.Stats.Type;

public class GraphicView extends View<Room> {

	private Map<String, Image> imageDictionary;

	private int tick = 0;
	
	private Canvas canvas;
	
	public GraphicView(Room model, Canvas canvas, Map<String, Image> imageDictionary) {
		super(model);
		
		this.canvas = canvas;
		this.imageDictionary = imageDictionary;
	}
	
	public Canvas getCanvas() {
		return this.canvas;
	}
	
	// private static Color getColor(String symbol) {
	// 	Color color = Color.RED;
		
	// 	switch(symbol) {
	// 		case "P":
	// 			color = Color.BLUE;
	// 			break;
			
	// 		case "B": 
	// 			color = Color.GREEN;
	// 			break;

	// 		case "W":
	// 			color = Color.YELLOW;
	// 	}
		
	// 	return color;
	// }

	@Override
	public void render() {
		Room model = this.getModel();
		Canvas canvas = this.getCanvas();
		GraphicsContext context = canvas.getGraphicsContext2D();

		context.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());
		
		for(Entity entity : model.getEntities()) {
			Rect pos = entity.getPosition();

			Image image = this.imageDictionary.get(entity.image());

			context.setFill(Color.RED);

			// entity.getStats().setOnChangeValue((event, type) -> context.setFill(Color.YELLOW));

			this.showHealthBar(entity, context);
			
			// Align center of image and position
			context.drawImage(image, pos.centerX() - image.getWidth()/2, pos.centerY() - image.getHeight()/2);
		
			tick++;

		}
	}

	private void showHealthBar(Entity entity, GraphicsContext context) {
		if(this.tick % 60 > 30) {
			return;
		}

		if (entity.symbol().equals("P")) {
			Character character = (Character) entity;
//				context.setFill(Color.WHITE);
//				context.fillRect(entity.getPosition().left(), entity.getPosition().centerY() - 25, entity.getPosition().width(), 4);
			context.setFill(Color.GREEN);
			context.fillRect(entity.getPosition().left(), entity.getPosition().centerY() - 25, entity.getPosition().width()*character.getStats().getValue(Type.HEALTH)/character.getStats().getValue(Type.VITALITY), 4);
		} else if (entity.symbol().equals("E")) {
			System.out.println("Entity health changed");
			Character character = (Character) entity;
//				context.setFill(Color.WHITE);
//				context.fillRect(entity.getPosition().left(), entity.getPosition().centerY() - 25, entity.getPosition().width(), 4);
			context.setFill(Color.RED);
			context.fillRect(entity.getPosition().left(), entity.getPosition().centerY() - 25, entity.getPosition().width()*character.getStats().getValue(Type.HEALTH)/character.getStats().getValue(Type.VITALITY), 4);
		}
	}

}
