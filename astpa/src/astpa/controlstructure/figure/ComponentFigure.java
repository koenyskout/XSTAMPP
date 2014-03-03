/*******************************************************************************
 * Copyright (c) 2013 A-STPA Stupro Team Uni Stuttgart (Lukas Balzer, Adam
 * Grahovac, Jarkko Heidenwag, Benedikt Markt, Jaqueline Patzek, Sebastian
 * Sieber, Fabian Toth, Patrick Wickenhäuser, Aliaksei Babkovich, Aleksander
 * Zotov).
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 *******************************************************************************/

package astpa.controlstructure.figure;

import java.util.UUID;

import org.eclipse.draw2d.ColorConstants;
import org.eclipse.draw2d.LineBorder;
import org.eclipse.draw2d.geometry.Dimension;

/**
 * 
 * Class contains necessary functions for components figures
 * 
 * @author Aliaksei Babkovich, Lukas Balzer
 * @version 1.0
 */
public class ComponentFigure extends CSFigure {
	
	/**
	 * COMPONENT_FIGURE_DEFWIDTH is the default width to which the layout is set
	 * when the user sets the Component from the palate without defining actual
	 * bounds
	 */
	public static final Dimension COMPONENT_FIGURE_DEFSIZE = new Dimension(120, 40);
	
	
	/**
	 * 
	 * 
	 * @author Lukas Balzer, Aliaksei Babkovich
	 * @param id the id which the figure inherits from its model
	 * 
	 */
	public ComponentFigure(UUID id) {
		
		super(id);
		this.setForegroundColor(ColorConstants.black);
		this.setBorder(new LineBorder(1));
		
	}
	
}
