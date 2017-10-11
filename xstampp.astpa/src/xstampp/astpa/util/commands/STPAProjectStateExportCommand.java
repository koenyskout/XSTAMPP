/*******************************************************************************
 * Copyright (c) 2013, 2017 Lukas Balzer, Asim Abdulkhaleq, Stefan Wagner
 * Institute of Software Technology, Software Engineering Group
 * University of Stuttgart, Germany
 * 
 * All rights reserved. This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License v1.0 which
 * accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *******************************************************************************/
package xstampp.astpa.util.commands;

import org.eclipse.core.commands.AbstractHandler;
import org.eclipse.core.commands.ExecutionEvent;
import org.eclipse.core.commands.ExecutionException;
import org.eclipse.ui.PlatformUI;

import xstampp.astpa.model.DataModelController;
import xstampp.astpa.util.jobs.statistics.STPAStatisticsJob;
import xstampp.ui.navigation.IProjectSelection;
import xstampp.ui.navigation.ProjectExplorer;

public class STPAProjectStateExportCommand extends AbstractHandler {

  @Override
  public Object execute(ExecutionEvent event) throws ExecutionException {
    Object currentSelection = PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage()
        .getSelection(ProjectExplorer.ID); // $NON-NLS-1$

    // if the currentSelection is a stepSelector than it is transfered in a
    // proper object
    if (currentSelection instanceof IProjectSelection
        && ((IProjectSelection) currentSelection).getProjectData() instanceof DataModelController) {
      IProjectSelection selector = ((IProjectSelection) currentSelection);
      STPAStatisticsJob job = new STPAStatisticsJob(
          (DataModelController) selector.getProjectData());
      job.schedule();
    }
    return false;
  }

}
