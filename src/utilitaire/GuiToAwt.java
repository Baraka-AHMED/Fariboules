package utilitaire;

public class GuiToAwt {
	
	public static java.awt.Point convertGuiPointToAwtPoint(gui.Point guiPoint) {
	    return new java.awt.Point(guiPoint.x, guiPoint.y);
	}

}
