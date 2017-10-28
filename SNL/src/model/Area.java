package model;

import java.awt.geom.Rectangle2D;

public class Area {
	Rectangle2D rect;
	boolean upIsBlock;
	
	public Area(double x, double y, double width, double height) {
		rect = new Rectangle2D.Double(x, y, width, height);
		upIsBlock = false;
	}

	public Area(double x, double y, double width, double height, boolean upIsBlock) {
		rect = new Rectangle2D.Double(x, y, width, height);
		this.upIsBlock = upIsBlock;
	}
	public Rectangle2D getRect() {
		return rect;
	}

	public void setRect(Rectangle2D rect) {
		this.rect = rect;
	}

	public boolean isCrush(Area area) {
		return rect.intersects(area.getRect());
	}

	public double getX() {
		return rect.getX();
	}

	public double getY() {
		return rect.getY();
	}

	public double getWidth() {
		return rect.getWidth();
	}

	public double getHeight() {
		return rect.getHeight();
	}

	/*
	 * this : character parameter : block
	 */
	public boolean isUnder(Area area) {
		double startX = getX();
		double endX = getX() + getWidth();

		double thisY = getY() + getHeight();

		// 객체간의 Y좌표 차이가 0~5 사이에 있을 때
		if (Math.abs(area.getY() - thisY) >= 0 && Math.abs(area.getY() - thisY) <= 5) {
			if ((startX >= area.getX() && startX <= area.getX() + area.getWidth())
					|| (endX >= area.getX() && endX <= area.getX() + area.getWidth())) {
				return true;
			}
		}
		return false;
	}

	/*
	 * this로 부터 param 이 오른쪽인지.
	 * */
	public boolean isRight(Area area) {
		double startY = getY();
		double endY = getY() + getHeight();
		double thisX = getX() + getWidth();

		if (Math.abs(area.getX() - thisX) >= 0 && Math.abs(area.getX() - thisX) <= 3) {
			if ((startY >= area.getY() && startY <= area.getY() + area.getHeight())
					|| (endY >= area.getY() && endY <= area.getY() + area.getHeight()))
				return true;
		}

		return false;
	}

	public boolean isLeft(Area area) {
		double startY = getY();
		double endY = getY()+getHeight();
		double thisX = getX();
		
		if(Math.abs(area.getX()+area.getWidth()-thisX) >= 0 && Math.abs(area.getX()+area.getWidth()-thisX) <=3) {
			if ((startY >= area.getY() && startY <= area.getY() + area.getHeight())
					|| (endY >= area.getY() && endY <= area.getY() + area.getHeight()))
				return true;		
		}
		return false;
	}

	@Override
	public String toString() {
		String rect = "[x=" + getX() + ", y="+getY()+", width="+getWidth()+", height="+getHeight()+"]";
		return "Area [rect=" + rect + ", upIsBlock=" + upIsBlock + "]\n";
		}
	
}
