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

package xstampp.astpa.controlstructure.controller.editparts;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import org.eclipse.draw2d.ConnectionAnchor;
import org.eclipse.draw2d.IFigure;
import org.eclipse.draw2d.geometry.Point;
import org.eclipse.draw2d.geometry.Translatable;
import org.eclipse.gef.CompoundSnapToHelper;
import org.eclipse.gef.ConnectionEditPart;
import org.eclipse.gef.EditPolicy;
import org.eclipse.gef.NodeEditPart;
import org.eclipse.gef.Request;
import org.eclipse.gef.RequestConstants;
import org.eclipse.gef.SnapToGeometry;
import org.eclipse.gef.SnapToGrid;
import org.eclipse.gef.SnapToHelper;
import org.eclipse.gef.editparts.AbstractGraphicalEditPart;
import org.eclipse.gef.editpolicies.SnapFeedbackPolicy;
import org.eclipse.gef.requests.CreateConnectionRequest;
import org.eclipse.gef.requests.ReconnectRequest;
import org.eclipse.swt.widgets.Control;

import xstampp.astpa.controlstructure.CSAbstractEditor;
import xstampp.astpa.controlstructure.CSEditor;
import xstampp.astpa.controlstructure.IControlStructureEditor;
import xstampp.astpa.controlstructure.controller.policys.CSDeletePolicy;
import xstampp.astpa.controlstructure.controller.policys.CSDirectEditManager;
import xstampp.astpa.controlstructure.controller.policys.CSDirectEditPolicy;
import xstampp.astpa.controlstructure.controller.policys.CSEditPolicy;
import xstampp.astpa.controlstructure.figure.CSFigure;
import xstampp.astpa.controlstructure.figure.ComponentFigure;
import xstampp.astpa.controlstructure.figure.IControlStructureFigure;
import xstampp.astpa.controlstructure.utilities.CSCellEditorLocator;
import xstampp.astpa.controlstructure.utilities.CSDirectEditor;
import xstampp.astpa.controlstructure.utilities.CSTextLabel;
import xstampp.astpa.model.controlstructure.components.ComponentType;
import xstampp.astpa.model.controlstructure.interfaces.IConnection;
import xstampp.astpa.model.controlstructure.interfaces.IRectangleComponent;
import xstampp.astpa.model.interfaces.IControlStructureEditorDataModel;

/**
 * The CSAbstractEditPart is a child from AbstractGraphicalEditPart.
 * Additionally it allows component changes due to the implementation of
 * PropertyChangeListener
 * 
 * since it defines the two methods activate and deactivate, each child of this
 * class also sets PropertyChangeListener of its model.
 * 
 * @version 1.1
 * @author Lukas Balzer, Aliaksei Babkovich
 * 
 */
public abstract class CSAbstractEditPart extends AbstractGraphicalEditPart
		implements IControlStructureEditPart, NodeEditPart {
	private final String stepId;
	private IControlStructureEditorDataModel dataModel;
	private List<IConnection> connectionRegisty;
	private int layer;

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @param model
	 *            The DataModel which contains all model classes
	 * @param stepId
	 *            this steps id
	 * @param layer TODO
	 */
	public CSAbstractEditPart(IControlStructureEditorDataModel model,
			String stepId, Integer layer) {
		this.layer = layer;
		this.stepId = stepId;
		this.dataModel = model;
		this.connectionRegisty = new ArrayList<IConnection>();
	}

	@Override
	protected IFigure createFigure() {
		ComponentFigure tmpFigure = new ComponentFigure(this.getId(), false);
		tmpFigure
				.setParent(((CSAbstractEditPart) this.getParent()).getFigure());
		return tmpFigure;
	}

	/**
	 * This installs the policies which define the provided edit-actions like
	 * 
	 * <ul>
	 * <li>resize
	 * <li>move
	 * 
	 * </ul>
	 * 
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#createEditPolicies()
	 */
	@Override
	protected void createEditPolicies() {
		/*
		 * the Edit role is a constant which tells the program in what policy is
		 * to use in what situation when performed,
		 * performRequest(EditPolicy.constant) is called
		 */
		this.installEditPolicy("Snap Feedback", new SnapFeedbackPolicy()); //$NON-NLS-1$
		this.installEditPolicy(EditPolicy.DIRECT_EDIT_ROLE,
				new CSDirectEditPolicy(this.dataModel, this.stepId));
		this.installEditPolicy(EditPolicy.LAYOUT_ROLE, new CSEditPolicy(
				this.dataModel, this.stepId));
		this.installEditPolicy(EditPolicy.COMPONENT_ROLE, new CSDeletePolicy(
				this.dataModel, this.stepId));
	}

	/**
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.gef.editparts.AbstractEditPart#performRequest(org.eclipse.gef.Request)
	 * @author Lukas Balzer, Aliaksei Babkovich
	 */
	@Override
	public void performRequest(Request req) {
		if (req.getType() == RequestConstants.REQ_DIRECT_EDIT) {
			this.performDirectEditing();
			this.refreshVisuals();
		}
	}

	@Override
	protected void refreshVisuals() {
		this.register();

		if (this.dataModel.getComponent(this.getId()) == null) {
			this.getViewer().getEditPartRegistry().remove(this);
		} else {
			IControlStructureFigure figureTemp = this.getFigure();
			IRectangleComponent modelTemp = this.dataModel.getComponent(this
					.getId());
			String stepID = (String) this.getViewer().getProperty(
					IControlStructureEditor.STEP_EDITOR);

			// increase the number of refreshes so the logic updates exactly
			for (int i = 0; i <= 2; i++) {
				figureTemp.setText(modelTemp.getText());

				figureTemp.setLayout(modelTemp.getLayout(stepID
						.equals(CSEditor.ID)));
			}

			this.refreshChildren();
			
			this.getViewer().getControl().redraw();
			
			
			for (Object child : this.getChildren()) {
				((IControlStructureEditPart) child).refresh();
			}
		}

	}

	@Override
	public void refresh() {
		for (IRectangleComponent f : this.getModelChildren()) {
			if ((f.getComponentType() == ComponentType.CONTROLACTION)
					&& (this.getDataModel().getControlAction(
							f.getControlActionLink()) == null)) {
				this.getDataModel().removeComponent(f.getId());
			}
		}
		this.refreshVisuals();
	}

	/**
	 * a function that is called whenever a DirectEdit action is performed
	 * 
	 * @author Lukas Balzer
	 */
	private void performDirectEditing() {
		CSTextLabel label = ((CSFigure) this.getFigure()).getTextField();
		CSDirectEditManager manager = new CSDirectEditManager(this,
				CSDirectEditor.class, new CSCellEditorLocator(label), label);
		manager.show();
		this.refreshVisuals();
	}

	/**
	 * This function refreshes the EditPartRegistry with respect to the ASTPA
	 * DataModel, when a connection editPart is registered that doesn't have a
	 * model in the dataModel it's deleted
	 * 
	 * @author Lukas Balzer
	 * 
	 */
	public void refreshConnections() {
		CSConnectionEditPart editPart;
		List<IConnection> tmpRegistry = new ArrayList<IConnection>();
		for (IConnection connection : this.dataModel.getConnections()) {

			editPart = (CSConnectionEditPart) this
					.createOrFindConnection(connection);

			tmpRegistry.add(connection);
			editPart.refresh();
		}
		for (IConnection conn : this.connectionRegisty) {
			if (!tmpRegistry.contains(conn)) {
				editPart = (CSConnectionEditPart) this
						.createOrFindConnection(conn);
				editPart.removeNotify();
				this.getViewer().getEditPartRegistry().remove(conn);
			}
		}
		this.connectionRegisty = tmpRegistry;

	}

	/**
	 * @author Benedikt Markt
	 */
	@Override
	public Object getAdapter(Class key) {
		if (key == SnapToHelper.class) {

			List<SnapToHelper> helpers = new ArrayList<SnapToHelper>();
			helpers.add(new SnapToGeometry(this));

			helpers.add(new SnapToGrid(this));

			if (helpers.size() == 0) {
				return null;
			}

			return new CompoundSnapToHelper(
					helpers.toArray(new SnapToHelper[0]));

		}
		return super.getAdapter(key);
	}

	@Override
	public List<IRectangleComponent> getModelChildren() {
		return ((IRectangleComponent) this.getModel()).getChildren();
	}

	@Override
	public List<IConnection> getModelSourceConnections() {

		return this.connectionRegisty;
	}

	@Override
	public List<IConnection> getModelTargetConnections() {
		return this.connectionRegisty;
	}

	/**
	 * this function is not needed
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(
			ConnectionEditPart connection) {
		return null;
	}

	/**
	 * this function is not needed
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(
			ConnectionEditPart connection) {
		return null;
	}

	/**
	 * this function returns the connection anchor depending on the request
	 * 
	 * @author Lukas Balzer
	 */
	@Override
	public ConnectionAnchor getSourceConnectionAnchor(Request request) {
		Point ref;
		if (request instanceof CreateConnectionRequest) {
			ref = ((CreateConnectionRequest) request).getLocation();
		} else {
			ReconnectRequest reqTemp = ((ReconnectRequest) request);
			ref = reqTemp.getLocation();
		}

		((CSFigure) this.getFigure()).getConnectionAnchor(ref);
		return ((CSFigure) this.getFigure()).getConnectionAnchor(ref);
	}

	/**
	 * This function returns the targetConnectionAnchor for a specific
	 * Connection the anchor is identified by the negative partId of its source
	 * 
	 * @author Lukas Balzer
	 */
	@Override
	public ConnectionAnchor getTargetConnectionAnchor(Request request) {
		Point ref;
		if (request instanceof CreateConnectionRequest) {
			ref = ((CreateConnectionRequest) request).getLocation();
		} else {
			ReconnectRequest reqTemp = ((ReconnectRequest) request);
			ref = reqTemp.getLocation();
		}

		return ((CSFigure) this.getFigure()).getConnectionAnchor(ref);
	}

	/**
	 * 
	 * @author Lukas Balzer
	 * 
	 * @return The DataModel must be passed to the commands so that changes can
	 *         be made directly in the DataModel
	 */
	public IControlStructureEditorDataModel getDataModel() {
		return this.dataModel;
	}

	@Override
	public UUID getId() {
		return ((IRectangleComponent) this.getModel()).getId();
	}

	@Override
	public void translateToRoot(Translatable t) {
		this.getFigure().translateToAbsolute(t);
	}

	@Override
	public IControlStructureFigure getFigure() {
		return (IControlStructureFigure) super.getFigure();
	}

	/**
	 * @return the stepId
	 */
	public String getStepId() {
		return this.stepId;
	}

	/**
	 * @return the layer
	 */
	public int getLayer() {
		return layer;
	}

	/**
	 * @param layer the layer to set
	 */
	public void setLayer(int layer) {
		this.layer = layer;
	}
}
