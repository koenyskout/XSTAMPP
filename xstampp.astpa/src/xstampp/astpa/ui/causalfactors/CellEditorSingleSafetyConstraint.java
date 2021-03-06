/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of Software
 * Technology, Software Engineering Group University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.ui.causalfactors;

import java.util.UUID;
import java.util.function.Consumer;

import org.eclipse.swt.graphics.GC;

import xstampp.astpa.model.BadReferenceModel;
import xstampp.astpa.model.interfaces.ICausalFactorDataModel;
import xstampp.astpa.model.interfaces.ITableModel;
import xstampp.astpa.model.linking.Link;
import xstampp.astpa.model.linking.LinkingType;
import xstampp.model.ObserverValue;
import xstampp.ui.common.grid.CellButtonAdd;
import xstampp.ui.common.grid.GridCellRenderer;
import xstampp.ui.common.grid.GridCellTextEditor;
import xstampp.ui.common.grid.GridWrapper;
import xstampp.ui.common.grid.GridWrapper.NebulaGridRowWrapper;

public class CellEditorSingleSafetyConstraint extends GridCellTextEditor {

  private ICausalFactorDataModel causalDataInterface;
  private Link causalEntrySc2Link;
  private ITableModel safetyOption;

  /**
   * 
   * @param gridWrapper
   * @param dataInterface
   * @param causalHazLink
   *          a Link of type {@link LinkingType#CausalEntryLink_SC2_LINK}
   */
  public CellEditorSingleSafetyConstraint(GridWrapper gridWrapper,
      ICausalFactorDataModel dataInterface, Link causalEntrySc2Link) {
    super(gridWrapper, "", causalEntrySc2Link.getId());
    setShowDelete(true);
    this.causalDataInterface = dataInterface;
    this.causalEntrySc2Link = causalEntrySc2Link;
    this.safetyOption = causalDataInterface.getSafetyConstraint(causalEntrySc2Link.getLinkB());
    setToolTip(safetyOption.getIdString());
  }

  @Override
  public String getCurrentText() {
    return safetyOption.getText();
  }

  @Override
  public String getCurrentTitle() {
    return safetyOption.getIdString();
  }

  @Override
  public void paint(GridCellRenderer renderer, GC gc, NebulaGridRowWrapper item) {
    clearCellButtons();
    if (safetyOption instanceof BadReferenceModel) {
      setReadOnly(true);
      setShowDelete(false);
      CellButtonAdd addButton = new CellButtonAdd(() -> {
        UUID constraintId = causalDataInterface.getCausalFactorController().addSafetyConstraint("");
        causalDataInterface.getLinkController().changeLink(causalEntrySc2Link,
            causalEntrySc2Link.getLinkA(), constraintId);
      });
      addButton.setToolTip("Add a new Safety Constraint");
      addCellButton(addButton);
      Consumer<UUID> linkAction = (id) -> causalDataInterface.getLinkController()
          .changeLink(causalEntrySc2Link, causalEntrySc2Link.getLinkA(), id);
      addCellButton(new CellButtonImportConstraint(getGridWrapper().getGrid(), causalDataInterface,
          linkAction));
      addCellButton(new CellButtonLinkToConstraint(getGridWrapper().getGrid(), causalDataInterface,
          linkAction));
    } else {
      setReadOnly(false);
      setShowDelete(true);
    }
    super.paint(renderer, gc, item);

  }

  @Override
  public void updateDataModel(String newText) {
    // the change is made in one of the three safety responsible controllers depending on the ID of
    // the safety constraint
    if (!(safetyOption instanceof BadReferenceModel)) {
      ObserverValue value = ObserverValue.SAFETY_CONSTRAINT;
      switch (safetyOption.getIdString().charAt(2)) {
      case ('1'):
        value = ObserverValue.UNSAFE_CONTROL_ACTION;
        break;
      case ('2'):
        value = ObserverValue.CAUSAL_FACTOR;
        break;
      }
      causalDataInterface.setModelTitle(safetyOption, newText, value);
    }
  }

  @Override
  public void delete() {
    if (!(safetyOption instanceof BadReferenceModel)) {
      if (this.causalDataInterface.getCausalFactorController()
          .removeSafetyConstraint(safetyOption.getId())) {
        causalDataInterface.getLinkController().changeLink(this.causalEntrySc2Link,
            this.causalEntrySc2Link.getLinkA(), null);
      }
    }
    this.safetyOption = BadReferenceModel.getBadReference();
  }

  @Override
  protected void editorOpening() {
    causalDataInterface.lockUpdate();
  }

  @Override
  protected void editorClosing() {
    causalDataInterface.releaseLockAndUpdate(new ObserverValue[] {});
  }

}
