/**
 * @author Chris Shields
 * 
 * UI interface
 */
package cjs.bowling;

import java.util.List;

public abstract interface UserInterface
{
	/**
	 * Requests and retrieves a list of competitors from the user
	 * 
	 * @return: List of competitors
	 */
	abstract List<Competitor> getCompetitors();
	
	/**
	 * Gets the number of pins knocked down by a ball
	 * 
	 * @param competitor: The competitor bowling
	 * @param frame: The frame being played (1 - Game.FRAMES) 
	 * @param ball: The ball being bowled (1 - 3)
	 * @return: The number of pins knocked down
	 */
	abstract int getPinsForBall(Competitor competitor, int frame, int ball);
	
	/**
	 * Displays the scoreboard
	 * 
	 * @param competitors: The list of competitors
	 * (containing the data for the individual frames)
	 */
	abstract void showScoreboard(List<Competitor> competitors);

	/**
	 * Display a message to the user
	 * 
	 * @param message: The message to display
	 */
	abstract void displayMessage(String message);
}
