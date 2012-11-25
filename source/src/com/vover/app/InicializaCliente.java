package com.vover.app;

import javax.swing.UIManager;

import com.vover.gui.anonimo.JanelaAnonimo;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;

public class InicializaCliente {

	public static void main(String[] args) {

		UIManager.put("Synthetica.window.decoration", Boolean.FALSE);
		try {
			UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
		}
		catch (Exception e) {
			e.printStackTrace();
		}
		new JanelaAnonimo();
	}
}