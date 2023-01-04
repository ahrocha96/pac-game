package gameobject;

public abstract class GameCharacter extends GameObject {
	
	public GameCharacter(int starting_x_position, int starting_y_position) {
		super(starting_x_position, starting_y_position);
	}
	
	public abstract void stopMoving();
	public abstract void move();
	
	public boolean moving() {
		return x_direction != 0 || y_direction != 0;
	}

	public void requestDirectionChange(String requestedDirection) {

		int requested_dx = 0;
		int requested_dy = 0;

		switch (requestedDirection) {
		case "Up":
			requested_dx = 0;
			requested_dy = -2;
			break;
		case "Down":
			requested_dx = 0;
			requested_dy = 2;
			break;

		case "Right":
			requested_dx = 2;
			requested_dy = 0;
			break;

		case "Left":
			requested_dx = -2;
			requested_dy = 0;
			break;
		}

		this.requested_dx = requested_dx;
		this.requested_dy = requested_dy;
		this.requestedDirection = requestedDirection;

		requestCounter = 30;
		changingDirection = true;
	}
}
