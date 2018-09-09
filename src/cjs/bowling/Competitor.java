/**
 * @author Chris Shields
 * 
 * The Competitor class holds the name of the player and the associated Frames of the game 
 */
package cjs.bowling;

public class Competitor
{
	private String name;
	private Frame[] frames;
	
	public String getName()	{ return name; }
	public Frame[] getFrames()	{ return frames; }

	public Competitor(String name)
	{
		// Competitor name must be at least 2 characters
		if (name.length() < 2)
			throw new IllegalArgumentException();

		this.name = name;

		frames = new Frame[Game.FRAMES];
	}

	/**
	 * Attempt to submit the number of pins knocked down for a ball in a frame,
	 * with validation
	 * 
	 * @param frame: The number of the frame (1 - Game.FRAMES)
	 * @param ball: The ball within the frame (1, 2 or 3)
	 * @param pins: The number of pins knocked down by the ball (0 - Game.PINS)
	 */
	public void submitPinsForBall(int frame, int ball, int pins)
	{
		if (frame < 1 || frame > Game.FRAMES)
			throw new UnsupportedOperationException("Invalid Frame (" + frame + ")");

		if (pins < 0 || pins > Game.PINS)
			throw new IllegalArgumentException("Invalid number of pins (" + pins + ")");

		// Create zero offset array index
		int frameArrayIndex = frame - 1;

		if (frames[frameArrayIndex] == null)
			frames[frameArrayIndex] = new Frame(frame == Game.FRAMES); // True if final frame

		switch (ball)
		{
			case 1: frames[frameArrayIndex].setBall1(pins); break;
			case 2: frames[frameArrayIndex].setBall2(pins); break;
			case 3: frames[frameArrayIndex].setBall3(pins); break;
			default: throw new UnsupportedOperationException("Invalid ball (" + ball + ")");
		}
	}

	/**
	 * Loop over the competitor's frames, updating the score where possible 
	 */
	public void updateFrameScores()
	{
		// Loop over frames
		for (int i = 0; i < Game.FRAMES; i++)
		{
			Frame frame = null;
			Frame nextFrame = null;
			Frame frameAfterThat = null;

			frame = frames[i];
			
			if (frame == null || frame.getBall1() == null) // Check frame has been played
				break;
			
			if (frame.isScoreCalculated()) // If score has already been calculated, there is no need to do it again
				continue;
			
			if (i + 1 < Game.FRAMES) // There is at least one additional frame
				nextFrame = frames[i+1];
			
			if (i + 2 < Game.FRAMES) // There are at least two additional frames
				frameAfterThat = frames[i+2];

			frame.updateScore(nextFrame, frameAfterThat);
		}
	}
}
