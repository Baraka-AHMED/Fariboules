package utilitaire;

public class AwtToGui {
	
	public static gui.Point convertAwtPointToGuiPoint(java.awt.Point awtPoint) {
	    return new gui.Point(awtPoint.x, awtPoint.y);
	}


}
