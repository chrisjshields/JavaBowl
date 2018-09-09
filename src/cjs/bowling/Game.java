/**
 * @author Chris Shields
 * 
 * Using ANSI/Allman style indent style convention (braces line up!)
 * Classes start with an uppercase letter
 * instances start with a lowercase letter
 * 
 * Contains main entry point. Runs the game, a frame at a time
 * 
 */
package cjs.bowling;

import java.util.List;

public class Game
{
	public static final int FRAMES = 10;
	public static final int PINS = 10;
	public static final int MAX_COMPETITORS = 6;
	
	private static UserInterface ui;
	
	private static List<Competitor> competitors;
	
	/**
	 * Programme entry point
	 * 
	 * @param args: Unused
	 */
	public static void main(String[] args)
	{
		ui = new ConsoleInterface();
		
		competitors = ui.getCompetitors();
		
		for(int frame = 1; frame <= Game.FRAMES; frame++)
		{
			// Get pins knocked down for each competitor in the frame
			run(frame);
			
			ui.showScoreboard(competitors);
		}
	}

	/**
	 * Run a frame of the game
	 * 
	 * @param frame The number of the frame to run (1 - Game.Frames)
	 */
	public static void run(int frame)
	{
		for(Competitor competitor : competitors)
		{
			// Get number of pins knocked down for ball 1 
			int pins1 = bowl(competitor, frame, 1);
			
			// If there are still pins standing, bowl ball 2
			if (pins1 != Game.PINS)
			{
				int pins2 = bowl(competitor, frame, 2);
				
				// If final frame, spare, bowl ball 3
				if (frame == Game.FRAMES && (pins1 + pins2 == Game.PINS))
					bowl(competitor, Game.FRAMES, 3);
			}
			// If this is the final frame and ball 1 was a strike, bowl 2 extra balls
			else if (frame == Game.FRAMES) 
			{
				bowl(competitor, Game.FRAMES, 2);
				bowl(competitor, Game.FRAMES, 3);
			}
		}
	}
	
	/**
	 * Request number of pins knocked down for a ball from the UI,
	 * send it to Competitor
	 * 
	 * @param competitor: The competitor who's bowling
	 * @param frame: The number of the frame (1 - Game.FRAMES)
	 * @param ball: The ball within the frame (1, 2 or 3)
	 */
	private static int bowl(Competitor competitor, int frame, int ball)
	{
		Integer pins = null;
		
		do
		{
			try
			{
				// Get the number of pins knocked down from the ui
				pins = ui.getPinsForBall(competitor, frame, ball);
				
				// Send it to the Competitor (which keeps track of the score)
				competitor.submitPinsForBall(frame, ball, pins);
			} catch (IllegalArgumentException e)
			{
				ui.displayMessage(e.getMessage());
				pins = null;
			} catch (UnsupportedOperationException e)
			{
				ui.displayMessage(e.getMessage());
				System.exit(-1);
			}
		} while (pins == null);
		
		return pins;
	}
}
