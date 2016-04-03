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
				if (animateIteration > numAnimations && onAnimation) {
					onAnimation = false;
				}

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

				if (animateIteration > numAnimations)
					onAnimation = false;

				if (movementsInfo != null && onAnimation)
					paintAnimatedCharacters(g);
				else
					paintStoppedCharacters(g);
			}
			else
				animate(g, movementsInfo.get(0));
		}
	}



	private void paintStoppedCharacters(Graphics g) {

		Position heroPosition = maze.getHeroPosition();
		if (maze.heroHasSword()) {
			g.drawImage(heroArmedImg[DOWN][0], heroPosition.x * blockWidth, heroPosition.y * blockHeight, (heroPosition.x + 1) * blockWidth,
					(heroPosition.y + 1) * blockHeight, 0, 0, heroArmedImg[DOWN][0].getWidth(), heroArmedImg[DOWN][0].getHeight(), null);
		}
		else {
			g.drawImage(heroUnarmedImg[DOWN][0], heroPosition.x * blockWidth, heroPosition.y * blockHeight, (heroPosition.x + 1) * blockWidth,
					(heroPosition.y + 1) * blockHeight, 0, 0, heroUnarmedImg[DOWN][0].getWidth(), heroUnarmedImg[DOWN][0].getHeight(), null);
		}


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

		BufferedImage[][] image;
		ArrayList<Integer> activeDirIndex;

		if (moveInfo instanceof MovementInfoDragon) {
			MovementInfoDragon infoDragon = (MovementInfoDragon) moveInfo;

			if (infoDragon.dragonState == DRAGON_STATE.SLEEPING) {
				image = super.dragonSleepingImg;
				activeDirIndex = super.dragonSleepingDirIndex;
			}
			else {
				image = super.dragonActiveImg;
				activeDirIndex = super.dragonActiveDirIndex;
			}
		}
		else {

			MovementInfoHero infoHero = (MovementInfoHero) moveInfo;

			if (infoHero.heroState == HERO_STATE.ALIVE) {

				activeDirIndex = super.heroDirIndex;

				if (infoHero.hasSword)
					image = super.heroArmedImg;
				else
					image = super.heroUnarmedImg;
			}
			else if (infoHero.heroState == HERO_STATE.DEAD) {

				if (animateIteration >= numAnimations) {
					image = super.heroDyingImg;
					activeDirIndex = super.heroDyingDirIndex;
				}
				else {
					image = super.heroUnarmedImg;
					activeDirIndex = super.heroDirIndex;
				}
			}
			else {
				image = super.heroArmedImg;
				activeDirIndex = super.heroDirIndex;
			}
		}


		Position drawPosition = getDrawPosition(moveInfo);
		Position indexPosition = getAnimationIndex(image, moveInfo, activeDirIndex, moveInfo.moveDirection);


		g.drawImage(image[indexPosition.y][indexPosition.x], drawPosition.x, drawPosition.y, drawPosition.x + blockWidth,
				drawPosition.y + blockHeight,0, 0, image[indexPosition.y][indexPosition.x].getWidth(),
				image[indexPosition.y][indexPosition.x].getHeight(), null);
	}



	private Position getAnimationIndex(BufferedImage[][] image, MovementInfo moveInfo, 
			ArrayList<Integer> imageDirIndex, DIRECTION moveDirection) {

		int dirIndex;
		int animIndex;

		switch (moveDirection) {
		case UP:
			dirIndex = imageDirIndex.get(UP);
			animIndex = animateIteration % image[dirIndex].length;
			break;

		case LEFT:
			dirIndex = imageDirIndex.get(LEFT);
			animIndex = animateIteration % image[dirIndex].length;
			break;

		case DOWN:
			dirIndex = imageDirIndex.get(DOWN);
			animIndex = animateIteration % image[dirIndex].length;
			break;

		case RIGHT:
			dirIndex = imageDirIndex.get(RIGHT);
			animIndex = animateIteration % image[dirIndex].length;
			break;

		default:
			dirIndex = imageDirIndex.get(DOWN);
			animIndex = animateIteration % image[dirIndex].length;
			break;
		}



		if (moveInfo instanceof MovementInfoHero && ((MovementInfoHero) moveInfo).heroState != HERO_STATE.ALIVE) {
			if (animateIteration >= super.heroDyingDirIndex.size()) {
				moveInfo.moveDirection = DIRECTION.STAY;
				animIndex = image[dirIndex].length - 1;
			}
		}


		return new Position(dirIndex, animIndex);
	}


	private Position getDrawPosition(MovementInfo moveInfo) {

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


		if (moveInfo instanceof MovementInfoHero && ((MovementInfoHero) moveInfo).heroState != HERO_STATE.ALIVE) {
			if (animateIteration >= super.heroDyingDirIndex.size()) {
				moveInfo.lastPosition = new Position(y / blockHeight, x / blockWidth);
			}
		}


		return new Position(y, x);
	}


	public void updateImage(ArrayList<MovementInfo> moveInfo) {

		movementsInfo = moveInfo;
		onAnimation = true;
		animateIteration = 0;
		repaint();

		if (maze.getDragonList().size() == 0)
			this.doorImg = super.openDoorImg;
	}



	public void setMaze(Maze maze) {

		this.maze = maze;

		Position exitPos = maze.getExitPosition();

		if (exitPos.x == 0) {
			this.doorImg = rotate(super.closedDoorImg, Math.PI/2);
			super.openDoorImg = rotate(super.openDoorImg, Math.PI/2);
		}
		else if (exitPos.x == maze.getDimension() - 1) {
			this.doorImg = rotate(super.closedDoorImg, -Math.PI/2);
			super.openDoorImg = rotate(super.openDoorImg, -Math.PI/2);
		}
		else if (exitPos.y == maze.getDimension() - 1) {
			this.doorImg = rotate(super.closedDoorImg, Math.PI);
			super.openDoorImg = rotate(super.openDoorImg, Math.PI);
		}
		else
			this.doorImg = super.closedDoorImg;
	}



	public boolean animationAllowed() {

		return !onAnimation;
	}


	public void heroDyingAnimate() {
		timer.setDelay(500);

	}


	public void setGameFinished(boolean gm) {
		gameFinished = gm;
	}
}
