package de.MarkusTieger.diagnostics;

import java.awt.event.ActionEvent;

import javax.swing.JOptionPane;

public class DiagnosticReporter {
	
	public static void onEvent(ActionEvent e) {
		new Thread(DiagnosticReporter::start, "Diagnostic Reporter").start();
	}
	
	public static void start() {
		
		JOptionPane.showMessageDialog(null, "Please Close all Minecraft and Minecraft-Launcher instances and Press \"Ok\"", "Diagnostics", JOptionPane.INFORMATION_MESSAGE);
		
		
		
	}

}
