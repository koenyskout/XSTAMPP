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

package xstampp.model;

import java.util.Observer;

/**
 * Interface for the DataModel which defines the methods to handle the Observer
 * pattern. All other interfaces for the data model should extend this interface
 * 
 * @author Fabian Toth
 * 
 */
public interface IDataModel {

	/**
	 * Adds an observer to the set of observers for this object, provided that
	 * it is not the same as some observer already in the set.
	 * 
	 * @author Fabian Toth
	 * 
	 * @param observer
	 *            an observer to be added.
	 */
	void addObserver(Observer observer);

	/**
	 * Deletes an observer from the set of observers of this object.
	 * 
	 * @author Fabian Toth
	 * 
	 * @param observer
	 *            the observer to be deleted.
	 */
	void deleteObserver(Observer observer);

	/**
	 * Returns the number of observers of this Observable object.
	 * 
	 * @author Fabian Toth
	 * 
	 * @return the number of observers of this object.
	 */
	int countObservers();

	/**
	 * Prepares the data model for the export
	 * 
	 * @author Fabian Toth,Lukas Balzer
	 * @return whether successful or not
	 * 
	 */
	public boolean prepareForExport();

	/**
	 * Removes the preparations that were made for the export
	 * 
	 * @author Fabian Toth
	 * 
	 */
	public void prepareForSave();

	/**
	 * Triggers an update of the given value
	 * 
	 * @author Fabian Toth,Lukas Balzer
	 * 
	 * @param value
	 *            the given value to update
	 */
	void updateValue(ObserverValue delete);

	public String getProjectName();

	void setStored();

	boolean hasUnsavedChanges();

	boolean setProjectName(String projectName);

}