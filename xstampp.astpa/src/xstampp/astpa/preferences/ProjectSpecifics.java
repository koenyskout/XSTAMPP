/*******************************************************************************
 * Copyright (C) 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner Institute of SoftwareTechnology,
 * Software Engineering Group University of Stuttgart, Germany. All rights reserved. This program
 * and the accompanying materials are made available under the terms of the Eclipse Public License
 * v1.0 which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 * 
 * Contributors: Lukas Balzer - initial API and implementation
 ******************************************************************************/
package xstampp.astpa.preferences;

import java.util.UUID;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CTabFolder;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;

import xstampp.astpa.messages.Messages;
import xstampp.astpa.model.DataModelController;
import xstampp.model.IDataModel;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.common.contentassist.LabelWithAssist;
import xstampp.ui.common.projectsettings.ISettingsPage;
import xstampp.ui.common.shell.ModalShell;
import xstampp.usermanagement.api.AccessRights;

public class ProjectSpecifics implements ISettingsPage {

  private DataModelController controller;
  private BooleanSetting useScenariosSetting;
  private BooleanSetting switchCFandUCA;
  private BooleanSetting useHazardSeverity;
  private ModalShell modalShell;
  private BooleanSetting useHazardConstraints;
  private BooleanSetting useAccidentConstraints;

  public ProjectSpecifics() {
  }

  public ProjectSpecifics(UUID projectId) {
    IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(projectId);
    if (dataModel instanceof DataModelController) {
      this.controller = (DataModelController) dataModel;
    }
  }

  @Override
  public Composite createControl(CTabFolder control, ModalShell parent, UUID modelId) {
    this.modalShell = parent;
    IDataModel dataModel = ProjectManager.getContainerInstance().getDataModel(modelId);
    if (dataModel instanceof DataModelController) {
      this.controller = (DataModelController) dataModel;
    }

    Composite container = new Composite(control, SWT.None);
    container.setLayout(new GridLayout(2, false));
    container.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true));

    // add boolean widget for choosing whether or not to use the scenarios to analyse the causal
    // factors
    String title = Messages.ProjectSpecifics_UseCausalScenarios;
    String description = Messages.ProjectSpecifics_UseCausalScenariosTip;
    this.useScenariosSetting = new BooleanSetting(container, title, description,
        this.controller.isUseScenarios(), SWT.CHECK);
    this.useScenariosSetting.setMessage(
        String.format(Messages.ProjectSpecifics_ReopenViews, "Hazards, Accidents, UCA Table"));

    // add boolean widget for choosing whether or not to Switch Causal Factor and UCA columns in the
    // Causal Factors Table
    title = Messages.ProjectSpecifics_SwitchCFandUCATitle;
    description = Messages.ProjectSpecifics_SwitchCFandUCADesc;
    this.switchCFandUCA = new BooleanSetting(container, title, description,
        this.controller.getCausalFactorController().analyseFactorsPerUCA(), SWT.CHECK);
    this.switchCFandUCA
        .setMessage(String.format(Messages.ProjectSpecifics_ReopenViews, "Causal Factors"));

    // add a boolean widget for whether or not to use the severity analysis
    title = Messages.ProjectSpecifics_UseSeverity;
    description = Messages.ProjectSpecifics_UseSeverityTip;
    this.useHazardSeverity = new BooleanSetting(container, title, description,
        this.controller.isUseSeverity(), SWT.CHECK);
    this.useHazardSeverity
        .setMessage(String.format(Messages.ProjectSpecifics_ReopenViews, "Hazards"));

    Group constraintLinkingGroup = new Group(container, SWT.None);
    constraintLinkingGroup.setLayout(new GridLayout(2, false));
    constraintLinkingGroup.setLayoutData(new GridData(SWT.FILL, SWT.TOP, true, true, 2, 1));

    title = Messages.ProjectSpecifics_UseHazardConstraints;
    description = Messages.ProjectSpecifics_UseHazardConstraintsLong;
    this.useHazardConstraints = new BooleanSetting(constraintLinkingGroup, title, description,
        this.controller.getHazAccController().isUseHazardConstraints(), SWT.RADIO);
    this.useHazardConstraints.setMessage(String.format(Messages.ProjectSpecifics_ReopenViews,
        "Hazards, Accidents, Safety Constraints"));

    title = Messages.ProjectSpecifics_UseAccidentConstraints;
    description = Messages.ProjectSpecifics_UseAccidentConstraintsLong;
    this.useAccidentConstraints = new BooleanSetting(constraintLinkingGroup, title, description,
        !this.controller.getHazAccController().isUseHazardConstraints(), SWT.RADIO);
    this.useAccidentConstraints.setMessage(String.format(Messages.ProjectSpecifics_ReopenViews,
        "Hazards, Accidents, Safety Constraints"));
    container.setSize(400, 400);
    return container;
  }

  @Override
  public boolean validate() {
    return true;
  }

  @Override
  public boolean doAccept() {
    this.controller.setUseScenarios(this.useScenariosSetting.selected);
    this.controller.setUseSeverity(this.useHazardSeverity.selected);
    this.controller.getCausalFactorController()
        .setAnalyseFactorsPerUCA(this.switchCFandUCA.selected);
    this.controller.getHazAccController().setUseHazardConstraints(useHazardConstraints.selected);
    return true;
  }

  private class BooleanSetting {
    private boolean selected;
    private String message = null;
    private boolean useMessage = false;

    public BooleanSetting(Composite parent, String title, String description, boolean initial,
        int style) {

      Button boolSetting = new Button(parent, style);
      Composite comp = new Composite(parent, SWT.None);
      comp.setLayout(new GridLayout());
      boolSetting.setSelection(initial);
      this.selected = initial;
      boolSetting.setEnabled(controller.getUserSystem().checkAccess(AccessRights.ADMIN));

      new LabelWithAssist(comp, SWT.None, title, description);

      boolSetting.addSelectionListener(new SelectionAdapter() {
        @Override
        public void widgetSelected(SelectionEvent e) {
          selected = ((Button) e.getSource()).getSelection();
          if (modalShell.canAccept() && useMessage) {
            modalShell.setMessage(getMessage());
          }
        }
      });
    }

    /**
     * @return the message
     */
    public String getMessage() {
      return message;
    }

    /**
     * @param message
     *          the message to set
     */
    public void setMessage(String message) {
      this.message = message;
      this.useMessage = true;
    }

  }

  @Override
  public boolean isVisible(UUID projectId) {
    return true;
  }

  @Override
  public String getName() {
    return Messages.ProjectSpecifics_GeneralSettings;
  }

  @Override
  public void setName(String name) {
    // The name cannot be changed
  }

  @Override
  public String getId() {
    return "xstampp.astpa.settings.general"; //$NON-NLS-1$
  }
}
