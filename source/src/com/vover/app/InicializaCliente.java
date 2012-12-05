package com.vover.app;

import java.text.ParseException;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import com.vover.gui.anonimo.JanelaAnonimo;

import de.javasoft.plaf.synthetica.SyntheticaBlackEyeLookAndFeel;

public class InicializaCliente {

	public static void main(String[] args) throws UnsupportedLookAndFeelException, ParseException {

		UIManager.setLookAndFeel(new SyntheticaBlackEyeLookAndFeel());
		new JanelaAnonimo();
	}
}