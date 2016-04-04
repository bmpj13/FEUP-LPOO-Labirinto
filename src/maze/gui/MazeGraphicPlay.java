package maze.gui;


import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedList;

import javax.swing.Timer;

import maze.logic.Dragon;
import maze.logic.MovementInfo;
import maze.logic.MovementInfoDragon;
import maze.logic.MovementInfoHero;
import maze.logic.Position;
import maze.logic.Maze;
import maze.logic.Dragon.DRAGON_STATE;
import maze.logic.Hero.HERO_STATE;
import maze.logic.Maze.DIRECTION;


public class MazeGraphicPlay extends MazeGraphics {
	private static final long serialVersionUID = 1L;

	private Maze maze;
	private BufferedImage doorImg;

	private ArrayList<MovementInfo> movementsInfo;

	private Timer timer;
	private int animateIteration;
	private static final int numAnimations = 8;
	private boolean onAnimation;

	private int blockWidth;
	private int blockHeight;


	private boolean gameFinished;


	MazeGraphicPlay() {		
		super();

		gameFinished = false;

		onAnimation = false;
		animateIteration = 0;
		timer = new Timer(120, new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent ae) {

				animateIteration++;
				if (animateIteration > numAnimations && onAnimation)
					onAnimation = false;


				if (onAnimation)
					timer.setDelay(50);
				else
					timer.setDelay(120);

				repaint();
			}
		});
		timer.start();
	}


	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);


		if (maze != null) {

			paintStaticElements(g);

			if (!gameFinished) {

				if (movementsInfo != null && onAnimation)
					paintAnimatedCharacters(g);
				else
					paintStoppedCharacters(g);
			}
			else {
				heroEndAnimation(g, (MovementInfoHero) movementsInfo.get(0));
				
				if (onAnimation)
					paintDragonsAnimated(g);
				else 
					paintDragonsStatic(g);
			}
		}
	}




	private void paintDragonsAnimated(Graphics g) {
		
		for (int i = 1; i < movementsInfo.size(); i++) {
			animate(g, movementsInfo.get(i));
		}
	}


	private void heroEndAnimation(Graphics g, MovementInfoHero heroInfo) {
		// Only called when game ends

		BufferedImage[][] image = retrieveImageEnd(heroInfo);
		Position drawPosition = retrieveDrawPositionEnd(heroInfo);
		Position indexPosition = retrieveAnimationIndexEnd(image, heroInfo);

		g.drawImage(image[indexPosition.y][indexPosition.x], drawPosition.x, drawPosition.y, drawPosition.x + blockWidth,
				drawPosition.y + blockHeight, 0, 0, image[indexPosition.y][indexPosition.x].getWidth(),
				image[indexPosition.y][indexPosition.x].getHeight(), null);
	}



	// TODO amizade
	private Position retrieveAnimationIndexEnd(BufferedImage[][] image, MovementInfoHero heroInfo) {

		if (heroInfo.moveDirection == DIRECTION.STAY) {
			
			if (heroInfo.heroState == HERO_STATE.DEAD && !onAnimation)
				return new Position(DOWN, image[DOWN].length - 1);
			else
				return new Position(DOWN, animateIteration % image[DOWN].length);
		}
			

		return retrieveAnimationIndex(image, heroInfo);
	}



	private Position retrieveDrawPositionEnd(MovementInfoHero heroInfo) {

		if (!onAnimation) {
			
			Position heroPos = maze.getHero().getPosition();
			heroInfo.moveDirection = DIRECTION.STAY;
			return new Position(heroPos.y * blockHeight, heroPos.x * blockWidth);
		}

		return retrieveDrawPosition(heroInfo);
	}



	private BufferedImage[][] retrieveImageEnd(MovementInfoHero heroInfo) {

		if (heroInfo.heroState == HERO_STATE.DEAD) {

			if (!onAnimation)
				return super.heroDyingImg;
			else
				return super.heroUnarmedImg;
		}
		else {

			if (!onAnimation)
				return super.heroWinningImg;
			else
				return super.heroArmedImg;
		}
	}







	private void paintStoppedCharacters(Graphics g) {

		Position heroPosition = maze.getHero().getPosition();
		if (maze.getHero().hasSword()) {
			g.drawImage(heroArmedImg[DOWN][0], heroPosition.x * blockWidth, heroPosition.y * blockHeight, 
					(heroPosition.x + 1) * blockWidth, (heroPosition.y + 1) * blockHeight,
					0, 0, heroArmedImg[DOWN][0].getWidth(), heroArmedImg[DOWN][0].getHeight(), null);
		}
		else {
			g.drawImage(heroUnarmedImg[DOWN][0], heroPosition.x * blockWidth, heroPosition.y * blockHeight,
					(heroPosition.x + 1) * blockWidth, (heroPosition.y + 1) * blockHeight,
					0, 0, heroUnarmedImg[DOWN][0].getWidth(), heroUnarmedImg[DOWN][0].getHeight(), null);
		}

		paintDragonsStatic(g);
	}



	private void paintDragonsStatic(Graphics g) {

		LinkedList<Dragon> dragons = maze.getDragonList();
		for (Dragon dragon : dragons) {
			animate(g, new MovementInfoDragon(dragon.getPosition(), DIRECTION.STAY, dragon.getState()));
		}
	}



	private void paintAnimatedCharacters(Graphics g) {
		for (MovementInfo info : movementsInfo) {
			animate(g, info);
		}
	}



	private void paintStaticElements(Graphics g) {
		
		g.drawImage(super.backgroundImg, 0, 0, this.getWidth(), this.getHeight(), null);

		int dimension = maze.getDimension();
		blockWidth =  this.getWidth() / dimension;
		blockHeight = this.getHeight() / dimension;

		for (int i = 0; i < dimension; i++) {

			int y = i*blockHeight;
			int height = y + blockHeight;

			for (int j = 0; j < dimension; j++) {

				int x = j*blockWidth;
				int width = x + blockWidth;

				char symbol = maze.getMazeContent(i, j);

				if (symbol == Maze.Symbol_Wall) {
					g.drawImage(wallImg, x, y, width, height, 0, 0, wallImg.getWidth(), wallImg.getHeight(), null);
				}
				else if (symbol == Maze.Symbol_Path) {
					g.drawImage(pathImg, x, y, width, height, 0, 0, pathImg.getWidth(), pathImg.getHeight(), null);
				}
				else if (symbol == Maze.Symbol_Sword || symbol == Maze.Symbol_DragonOnSword) {
					g.drawImage(pathImg, x, y, width, height, 0, 0, pathImg.getWidth(), pathImg.getHeight(), null);
					g.drawImage(swordImg, x, y, width, height, 0, 0, swordImg.getWidth(), swordImg.getHeight(), null);
				}
				else if (symbol == Maze.Symbol_Exit) {
					g.drawImage(wallImg, x, y, width, height, 0, 0, wallImg.getWidth(), wallImg.getHeight(), null);
					g.drawImage(doorImg, width, y, x, height, 0, 0, doorImg.getWidth(), doorImg.getHeight(), null);
				}
				else {
					// Dragons and Hero
					g.drawImage(pathImg, x, y, width, height, 0, 0, pathImg.getWidth(), pathImg.getHeight(), null);
				}
			}
		}
	}


	public void animate(Graphics g, MovementInfo moveInfo) {

		BufferedImage[][] image = retrieveImage(moveInfo);
		Position drawPosition = retrieveDrawPosition(moveInfo);
		Position indexPosition = retrieveAnimationIndex(image, moveInfo);


		g.drawImage(image[indexPosition.y][indexPosition.x], drawPosition.x, drawPosition.y, drawPosition.x + blockWidth,
				drawPosition.y + blockHeight,0, 0, image[indexPosition.y][indexPosition.x].getWidth(),
				image[indexPosition.y][indexPosition.x].getHeight(), null);
	}



	private BufferedImage[][] retrieveImage(MovementInfo moveInfo) {

		if (moveInfo instanceof MovementInfoDragon) {
			MovementInfoDragon infoDragon = (MovementInfoDragon) moveInfo;

			if (infoDragon.dragonState == DRAGON_STATE.SLEEPING)
				return super.dragonSleepingImg;
			else
				return super.dragonActiveImg;
		}
		else {

			MovementInfoHero infoHero = (MovementInfoHero) moveInfo;

			if (infoHero.hasSword)
				return super.heroArmedImg;
			else
				return super.heroUnarmedImg;
		}
	}



	private Position retrieveAnimationIndex(BufferedImage[][] image, MovementInfo moveInfo) {

		int dirIndex;
		int animIndex;

		switch (moveInfo.moveDirection) {
		case UP:
			dirIndex = UP;
			animIndex = animateIteration % image[dirIndex].length;
			break;

		case LEFT:
			dirIndex = LEFT;
			animIndex = animateIteration % image[dirIndex].length;
			break;

		case DOWN:
			dirIndex = DOWN;
			animIndex = animateIteration % image[dirIndex].length;
			break;

		case RIGHT:
			dirIndex = RIGHT;
			animIndex = animateIteration % image[dirIndex].length;
			break;

		default:
			dirIndex = DOWN;
			if (moveInfo instanceof MovementInfoDragon)
				animIndex = animateIteration % image[dirIndex].length;
			else
				animIndex = 0;
			break;
		}

		return new Position(dirIndex, animIndex);
	}




	private Position retrieveDrawPosition(MovementInfo moveInfo) {

		int x = 0;
		int y = 0;
		int animateOffset = 0;

		switch (moveInfo.moveDirection) {
		case UP:
			if (animateIteration == numAnimations) {
				x = moveInfo.lastPosition.x * blockWidth;
				y = (moveInfo.lastPosition.y - 1) * blockHeight;
			} 
			else {
				animateOffset = -blockHeight / numAnimations;
				y += animateIteration * animateOffset;
			}
			break;

		case LEFT:
			if (animateIteration == numAnimations) {
				x = (moveInfo.lastPosition.x - 1) * blockWidth;
				y = moveInfo.lastPosition.y * blockHeight;
			} 
			else {
				animateOffset = -blockWidth / numAnimations;
				x += animateIteration * animateOffset;
			}
			break;

		case DOWN:
			if (animateIteration == numAnimations) {
				x = moveInfo.lastPosition.x * blockWidth;
				y = (moveInfo.lastPosition.y + 1) * blockHeight;
			} 
			else {
				animateOffset = blockHeight / numAnimations;
				y +=  animateIteration * animateOffset;
			}
			break;

		case RIGHT:
			if (animateIteration == numAnimations) {
				x = (moveInfo.lastPosition.x + 1) * blockWidth;
				y = moveInfo.lastPosition.y * blockHeight;
			} 
			else {
				animateOffset = blockWidth / numAnimations;
				x += animateIteration * animateOffset;
			}
			break;

		default:
			x = moveInfo.lastPosition.x * blockWidth;
			y = moveInfo.lastPosition.y * blockHeight;
			break;
		}


		if (animateOffset != 0) {

			x += moveInfo.lastPosition.x * blockWidth;
			y += moveInfo.lastPosition.y * blockHeight;
		}

		return new Position(y, x);
	}

	

	public void updateImage(ArrayList<MovementInfo> moveInfo) {

		movementsInfo = moveInfo;
		onAnimation = true;
		animateIteration = 0;
		repaint();

		if (maze.getDragonList().size() == 0 && maze.getHero().hasSword())
			this.doorImg = super.openDoorImg;
	}



	public void setMaze(Maze maze) {

		this.maze = maze;
		Position exitPos = maze.getExitPosition();


		if (exitPos.x == 0)
			this.doorImg = rotate(super.closedDoorImg, Math.PI/2);
		else if (exitPos.x == maze.getDimension() - 1)
			this.doorImg = rotate(super.closedDoorImg, -Math.PI/2);
		else if (exitPos.y == maze.getDimension() - 1)
			this.doorImg = rotate(super.closedDoorImg, Math.PI);
		else
			this.doorImg = super.closedDoorImg;
	}



	public boolean animationAllowed() {

		return !onAnimation;
	}


	public void gameFinished() {
		gameFinished = true;
	}


	public void gameStarted() {
		gameFinished = false;
	}
}
