/**
 * @author Chris Shields
 * 
 * Basic implementation of the user interface
 */
package cjs.bowling;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.List;

public class ConsoleInterface implements UserInterface
{
	private InputStreamReader converter;
    private BufferedReader in;
    
	public ConsoleInterface()
	{
		converter = new InputStreamReader(System.in);
		in = new BufferedReader(converter);
	}

	/*
	 * @see cjs.bowling.UserInterface#getCompetitors()
	 */
	public List<Competitor> getCompetitors()
	{
		System.out.println("Enter competitors, one at a time. Enter a blank name if less than "
			+ Game.MAX_COMPETITORS + " competitors: ");
        
        List<Competitor> competitors = new ArrayList<Competitor>();

        do
        {
        	try
			{
				String line = in.readLine();
				line = line.trim();
				
				if (line.equals(""))
					break;
				
				competitors.add(new Competitor(line));
			} catch (IOException e)
			{
				e.printStackTrace();
				System.exit(-1);
			} catch (InvalidParameterException e)
	        {
	        	System.out.println("Name must be at least 2 characters long.");
	        }
	    } while (competitors.size() < Game.MAX_COMPETITORS); // Up to MAX_COMPETITORS competitors

        return competitors;
	}

	/*
	 * @see cjs.bowling.UserInterface#getScoreForShot(cjs.bowling.Competitor, int, int)
	 */
	public int getPinsForBall(Competitor competitor, int frame, int ball)
	{
		String line = null;
		Integer score = null;
		
		System.out.println("Enter number of pins knocked down for " + competitor.getName() +
			", frame #" + frame + " shot #" + ball + ":");
		
		while (score == null)
		{
			try
			{
				line = in.readLine();
				score = Integer.parseInt(line);
			} catch (IOException e)
			{
				e.printStackTrace();
				System.exit(-1);
			} catch (NumberFormatException ex)
			{
				score = null;
			}
		}
		
		return score;
		
	}

	/*
	 * @see cjs.bowling.UserInterface#showScoreboard()
	 */
	public void showScoreboard(List<Competitor> competitors)
	{
		// Show headings
		System.out.print("\t\t");
		for (int frame = 1; frame <= Game.FRAMES; frame++)
			System.out.print(frame+ "\t");
		System.out.println();
		System.out.println();
		
		for(Competitor competitor : competitors)
		{
			System.out.print(competitor.getName() + "\t\t");
			
			// Update score
			competitor.updateFrameScores();

			int score = 0;
			
			// Show pins knocked down by each ball
			for(Frame frame: competitor.getFrames())
			{
				if (frame == null)
					continue;
				
				System.out.print(frameToPinsString(frame) + "\t");
			}
			
			System.out.println();
			System.out.print("\t\t");
			
			// Show scores
			for(Frame frame: competitor.getFrames())
			{
				if (frame == null)
					break;
			
				// Show score if it has been calculated e.g. there are no outstanding strikes/spares
				if (frame.isScoreCalculated())
				{
					score += frame.getScore();
					System.out.print(score + "\t");
				}
			}
			
			System.out.println();
			System.out.println();			
		}
	}

	/*
	 * @see cjs.bowling.UserInterface#displayMessage()
	 */
	public void displayMessage(String message)
	{
		System.out.println(message);
	}
	
	/**
	 * Converts the number of pins stored as an integer into a String representation
	 * for the scoreboard
	 *  
	 * @param pin: The number of pins knocked down
	 * @return: X (strike) if pin is Game.PINS, otherwise the number of pins
	 */
	private String pinToString(int pin)
	{
		if (pin == Game.PINS)
			return "X";
		else
			return String.valueOf(pin);				
	}
	
	/** 
	 * Converts a Frame into a String representation of the number of pins knocked
	 * down for the scoreboard 
	 * 
	 * @param frame
	 * @return: A String in the format of ball1|ball2|ball3
	 */
	private String frameToPinsString(Frame frame)
	{
		String pinsString;
		
		if (frame.getBall1() == null)
			return "|";
			
		if (frame.isStrike())
		{
			pinsString = "X|";
			
			if (frame.isFinalFrame())
			{
				if (frame.getBall2() != null)
					pinsString += pinToString(frame.getBall2());
				
				pinsString += "|";
				
				if (frame.getBall3() != null)
					pinsString += pinToString(frame.getBall3());
			}
		}
		else if (frame.isSpare())
		{
			pinsString = frame.getBall1() + "|/";
			
			if (frame.isFinalFrame())
			{
				pinsString += "|";
				
				if (frame.getBall3() != null)
					pinsString += pinToString(frame.getBall3());
			}
		}
		else
		{
			pinsString = frame.getBall1() + "|";
			
			if (frame.getBall2() != null)
				pinsString += pinToString(frame.getBall2());
			
			if (frame.isFinalFrame())
				pinsString += "|";
		}
		
		return pinsString;
	}
}
