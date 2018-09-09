/**
 * @author Chris Shields
 * 
 * Contains the pins knocked down for each ball in the
 * frame and the score for the frame as a whole
 */
package cjs.bowling;

public class Frame
{
	private Boolean isFinalFrame;
	private Boolean isStrike = false;
	private Boolean isSpare = false;
	private Integer ball1 = null;
	private Integer ball2 = null;
	private Integer ball3 = null;
	private Integer score = null;
	
	public Boolean isFinalFrame() { return this.isFinalFrame; }
	public Boolean isStrike() { return this.isStrike; }
	public Boolean isSpare() { return this.isSpare;	}
	public Integer getBall1() { return ball1; }
	public Integer getBall2() { return ball2; }
	public Integer getBall3() { return ball3; }
	public Boolean isScoreCalculated() { return score != null;	}
	public Integer getScore() {	return score; }
	
	public Frame(Boolean isFinalFrame)
	{
		this.isFinalFrame = isFinalFrame;
	}
	
	/**
	 * Validate ball to ensure the number of pins knocked down is between
	 * 0 and Game.PINS
	 * 
	 * @param pins
	 */
	private void ValidateBall(int pins)
	{
		if (pins < 0 || pins > Game.PINS)
			throw new IllegalArgumentException("Invalid number of pins ("+ pins + ")");
	}

	/**
	 * Attempt to set Ball #1 of the Frame
	 * 
	 * @param pins: The number of pins knocked down by Ball #1
	 */
	public void setBall1(int pins)
	{
		ValidateBall(pins);
		
		if (pins == Game.PINS)
			isStrike = true;
			
		this.ball1 = pins;
	}

	/**
	 * Attempt to set Ball #2 of the Frame
	 * 
	 *@param pins: The number of pins knocked down by Ball #2
	 */
	public void setBall2(int pins)
	{
		ValidateBall(pins);
		
		if (!isFinalFrame && isStrike)
			throw new UnsupportedOperationException("Invalid Ball (2) when shot #1 is a strike");
		
		if (!isFinalFrame || (isFinalFrame && !isStrike))
		{
			// Check there are enough pins left to knock down for the entered score
			if (pins > (Game.PINS - getBall1()))
				throw new IllegalArgumentException("Not enough pins left!");
			
			if (ball1 + pins == Game.PINS)
				isSpare = true;
		}
		
		this.ball2 = pins;
	}

	/**
	 * Attempt to set Ball #3 of the Frame
	 * 
	 * @param pins: The number of pins knocked down by Ball #3
	 */
	public void setBall3(int pins)
	{
		ValidateBall(pins);
		
		if (!isFinalFrame)
			throw new UnsupportedOperationException("Invalid shot (3) when frame is not final");
		else if (!isStrike && !isSpare) 
			throw new UnsupportedOperationException("Invalid shot (3) when not strike or spare");

		this.ball3 = pins;
	}
	
	/**
	 * Update score for this frame, if enough information is available yet
	 * 
	 * @param nextFrame: The frame that proceeds this one
	 * @param frameAfterThat: The frame that proceeds nextFrame
	 */
	public void updateScore(Frame nextFrame, Frame frameAfterThat)
	{
		Integer nextBall = null;
		Integer ballAfterThat = null;
		
		if (isStrike() || isSpare())
			nextBall = determineNextBall(nextFrame);
		if (isStrike())
			ballAfterThat = determineBallAfterNext(nextFrame, frameAfterThat);
		
		// Attempt to calculate score if enough information is known
		calculateScore(nextBall, ballAfterThat);
	}
	
	/**
	 * Determine the number of pins knocked down by the ball bowled after this one
	 * 
	 * @param nextFrame: The frame that proceeds this one
	 * @return: The number of pins knocked down by the next ball
	 */
	private Integer determineNextBall(Frame nextFrame)
	{
		if (nextFrame != null) // There is at least one additional frame, look ahead
		{
			return nextFrame.getBall1();
		}
		else if (this.isFinalFrame) // This is the last frame
		{
			if (this.isStrike())
				return this.getBall2();
			else if (this.isSpare())
				return this.getBall3();
			else
				return null;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Determine the number of pins knocked down by the ball bowled after the next ball
	 * e.g. 2 balls ahead
	 * 
	 * @param nextFrame: The frame that proceeds this one
	 * @param frameAfterThat: The frame that proceeds nextFrame
	 * @return: The number of pins knocked down by the ball after next
	 */
	private Integer determineBallAfterNext(Frame nextFrame, Frame frameAfterThat)
	{
		if (nextFrame != null) // There is at least one additional frame, look ahead
		{
			if (nextFrame.isStrike())
			{
				if (frameAfterThat != null) // There are at least two additional frames
					return frameAfterThat.getBall1();
				else // This is the last but one frame
					return nextFrame.getBall2();
			}
			else
			{
				return nextFrame.getBall2();
			}
		}
		else if (this.isFinalFrame) // This is the last frame
		{
			if (this.isStrike())
				return this.getBall3();
			else
				return null;
		}
		else
		{
			return null;
		}
	}
	
	/**
	 * Determine score, using the number of pins knocked down by the next ball
	 * and the ball after that if necessary
	 * 
	 * @param nextBall
	 * @param ballAfterThat
	 */
	private void calculateScore(Integer nextBall, Integer ballAfterThat)
	{
		if (isStrike)
		{
			if (nextBall != null && ballAfterThat != null)
				score = ball1 + nextBall + ballAfterThat;
			else
				score = null; // Unable to calculate score yet
		}
		else if (isSpare)
		{
			if (nextBall != null)
				score = ball1 + ball2 + nextBall;
			else
				score = null; // Unable to calculate score yet
		}
		else
		{
			score = ball1 + ball2;
		}
	}
}
