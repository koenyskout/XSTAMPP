/*******************************************************************************
 * Copyright (C) 2018 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors:
 * Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology, Software
 * Engineering Group University of Stuttgart, Germany - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.model;

import java.util.UUID;

public final class BadReferenceModel extends ATableModel {

  private static BadReferenceModel instance;
  private UUID id;

  private BadReferenceModel() {
    this.id = UUID.randomUUID();
  }

  public static BadReferenceModel getBadReference() {
    if (instance == null) {
      instance = new BadReferenceModel();
    }
    return instance;
  }

  @Override
  public String getText() {
    return "";
  }

  @Override
  public UUID getId() {
    return this.id;
  }

  @Override
  public boolean setNumber(int i) {
    return false;
  }

  @Override
  public int getNumber() {
    return 0;
  }

  @Override
  public String getTitle() {
    return "";
  }

  @Override
  public String getDescription() {
    return null;
  }

  @Override
  public String getIdString() {
    return "";
  }

}
