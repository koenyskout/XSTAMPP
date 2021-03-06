/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 *     Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model.hazacc;

import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;

import xstampp.astpa.model.hazacc.Accident;

/**
 * Test class for accidents
 * 
 * @author Fabian Toth
 * 
 */
public class AccidentTest {
	
	/**
	 * Test of a accident
	 * 
	 * @author Fabian Toth
	 * 
	 */
	@Test
	public void accidentTest() {
		Accident accident = new Accident("Test", "Test description", 1);
		Assert.assertEquals("Test", accident.getTitle());
		Assert.assertEquals("Test description", accident.getDescription());
		Assert.assertEquals(1, accident.getNumber());
		Assert.assertNull(accident.getLinks());
		
		accident = new Accident();
		accident.setTitle("Test");
		Assert.assertEquals("Test", accident.getTitle());
		accident.setDescription("Test description");
		Assert.assertEquals("Test description", accident.getDescription());
		UUID id = UUID.randomUUID();
		accident.setId(id);
		Assert.assertEquals(id, accident.getId());
		accident.setNumber(5);
		Assert.assertEquals(5, accident.getNumber());
		accident.setLinks("1, 2, 3");
		Assert.assertEquals("1, 2, 3", accident.getLinks());
	}
}
