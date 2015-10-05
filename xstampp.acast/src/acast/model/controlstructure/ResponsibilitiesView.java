package acast.model.controlstructure;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Observable;
import java.util.UUID;

import org.eclipse.jface.layout.TableColumnLayout;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.util.IPropertyChangeListener;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.jface.viewers.ColumnWeightData;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.TableEditor;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.MessageBox;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.IPartListener2;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPartReference;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.PlatformUI;

import xstampp.model.IDataModel;
import xstampp.model.ObserverValue;
import xstampp.ui.common.ProjectManager;
import xstampp.ui.editors.StandartEditorPart;
import acast.Activator;
import acast.controlstructure.CSAbstractEditor;
import acast.model.causalfactor.ICausalComponent;
import acast.model.controlstructure.components.ComponentType;
import acast.model.interfaces.IControlStructureEditorDataModel;

public class ResponsibilitiesView extends StandartEditorPart implements IPropertyChangeListener {

	public String requirementsType;
	// button names
	public static final String role = "Role";

	public static final String responsibilities = "Safety Related Responsibilities";

	public static final String unsafeActions = "Unsafe Decisions and Control Actions";

	public static final String context = "Context in which decisions made";

	public static final String flaws = "Proces/Mental Model Flaws";

	private int count = 0;


	private String selectedItem;

	/**
	 * Interface to communicate with the data model.
	 */
	private IControlStructureEditorDataModel dataInterface;

	/**
	 * ViewPart ID.
	 */
	public static final String ID = "acast.steps.step2_2_1";

	private final IPreferenceStore store = Activator.getDefault().getPreferenceStore();

	private List<Composite> list = new ArrayList<Composite>();

	private String responsibility;

	private Combo combo;

	private TableViewer viewer;

	@Override
	public String getId() {
		return ID;
	}

	public void setDataModelInterface(IDataModel dataInterface) {
		this.dataInterface = (IControlStructureEditorDataModel) dataInterface;
		this.dataInterface.addObserver(this);
	}

	public void removeFromDataModel(UUID name, String id, String view) {
		switch (view) {
		case ResponsibilitiesView.responsibilities:
			dataInterface.removeResponsibility(name, id);
			break;
		case ResponsibilitiesView.context:
			dataInterface.removeContext(name, id);
			break;
		case ResponsibilitiesView.flaws:
			dataInterface.removeFlaw(name, id);
			break;
		case ResponsibilitiesView.unsafeActions:
			dataInterface.removeunsafeAction(name, id);
			break;
		}
	}

	public void updateReponsibilites(UUID name, String view) {

		switch (view) {
		case ResponsibilitiesView.responsibilities:
			if (dataInterface.getResponsibilitiesList(name) != null
					&& !dataInterface.getResponsibilitiesList(name).isEmpty()) {
				for (Responsibility resp : dataInterface.getResponsibilitiesList(name)) {
					TableItem item = new TableItem(viewer.getTable(), SWT.None);
					item.setText(new String[] { resp.getId(), resp.getDescription() });
				}
			}
			break;

		case ResponsibilitiesView.context:
			if (dataInterface.getContextList(name) != null && !dataInterface.getContextList(name).isEmpty()) {
				for (Responsibility resp : dataInterface.getContextList(name)) {
					TableItem item = new TableItem(viewer.getTable(), SWT.None);
					item.setText(new String[] { resp.getId(), resp.getDescription() });

				}
			}
			break;

		case ResponsibilitiesView.flaws:
			if (dataInterface.getFlawsList(name) != null && !dataInterface.getFlawsList(name).isEmpty()) {
				for (Responsibility resp : dataInterface.getFlawsList(name)) {
					TableItem item = new TableItem(viewer.getTable(), SWT.None);
					item.setText(new String[] { resp.getId(), resp.getDescription() });

				}
			}
			break;

		case ResponsibilitiesView.unsafeActions:
			if (dataInterface.getUnsafeActionsList(name) != null
					&& !dataInterface.getUnsafeActionsList(name).isEmpty()) {
				for (Responsibility resp : dataInterface.getUnsafeActionsList(name)) {
					TableItem item = new TableItem(viewer.getTable(), SWT.None);
					item.setText(new String[] { resp.getId(), resp.getDescription() });

				}
			}
			break;

		}

	}

	@Override
	public void propertyChange(PropertyChangeEvent event) {

	}

	public void updateTableComponentsInDataStructure(UUID name, String id, String newName, String newId, String view) {
		switch (view) {
		case ResponsibilitiesView.responsibilities:
			dataInterface.updateResponsibility(name, id, newId);
			;
			break;
		case ResponsibilitiesView.context:
			dataInterface.updateContext(name, id, newId);
			break;
		case ResponsibilitiesView.flaws:
			dataInterface.updateFlaws(name, id, newId);
			break;
		case ResponsibilitiesView.unsafeActions:
			this.dataInterface.updateUnsafeAction(name, id, newId);
			break;
		}
	}

	public void applyDataToDataModel(UUID name, String id, String description, boolean responsibilityAlreadyExits,
			String view) {

		switch (view) {
		case ResponsibilitiesView.responsibilities:
			if (!responsibilityAlreadyExits) {
				dataInterface.addResponsibility(name, id, description);
			} else {
				dataInterface.changeResponsibility(name, id, description);

			}
			break;
		case ResponsibilitiesView.context:
			if (!responsibilityAlreadyExits) {
				dataInterface.addContext(name, id, description);
			} else {
				dataInterface.changeContext(name, id, description);
			}
			break;
		case ResponsibilitiesView.flaws:
			if (!responsibilityAlreadyExits) {
				dataInterface.addFlaw(name, id, description);
			} else {
				dataInterface.changeFlaw(name, id, description);
			}
			break;
		case ResponsibilitiesView.unsafeActions:
			if (!responsibilityAlreadyExits) {
				dataInterface.addUnsafeAction(name, id, description);
			} else {
				dataInterface.changeUnsafeAction(name, id, description);
			}
			break;
		}

	}

	@Override
	public void update(Observable dataModelController, Object updatedValue) {
		super.update(dataModelController, updatedValue);
		ObserverValue type = (ObserverValue) updatedValue;
		switch (type) {
		case UNSAVED_CHANGES:

			break;
		case SAVE:
			break;
		default:
			break;
		}
	}

	@Override
	public void createPartControl(final Composite parent) {
		setDataModelInterface(ProjectManager.getContainerInstance().getDataModel(getProjectID()));
		Activator.getDefault().getPreferenceStore().addPropertyChangeListener(this);

		parent.setLayout(new GridLayout(1, false));
		parent.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		PlatformUI.getWorkbench().getActiveWorkbenchWindow().getActivePage().addPartListener(new IPartListener2() {

			@Override
			public void partVisible(IWorkbenchPartReference partRef) {
				TableView.visible = false;
			}

			@Override
			public void partOpened(IWorkbenchPartReference partRef) {
			}

			@Override
			public void partInputChanged(IWorkbenchPartReference partRef) {

			}

			@Override
			public void partHidden(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partDeactivated(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partClosed(IWorkbenchPartReference partRef) {
				// TODO Auto-generated method stub

			}

			@Override
			public void partBroughtToTop(IWorkbenchPartReference partRef) {

				if (partRef.getId().equals("acast.steps.step2_2")) {

					if (TableView.visible) {
						IWorkbenchWindow window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
						IWorkbenchPage page = window.getActivePage();
						page.hideView(page.findView("A-CAST.view1"));
					}
					if (count >= 1) {
						if (CSAbstractEditor.alreadyStarted) {
							combo.removeAll();
							for (String c : CSAbstractEditor.identifiers.keySet()) {

								combo.add(c);

							}
							combo.select(combo.getItemCount() - 1);
						}
					}

				}
			}

			@Override
			public void partActivated(IWorkbenchPartReference partRef) {

			}
		});

		Composite layout = new Composite(parent, SWT.NONE);
		layout.setLayout(new GridLayout(1, false));
		layout.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite description = new Composite(layout, SWT.NONE);
		description.setLayoutData(new GridData(SWT.FILL, SWT.NONE, true, false, 1, 1));

		description.setLayout(new GridLayout(5, false));

		this.responsibility = this.responsibilities;

		Label lblResponsibilities = new Label(description, SWT.RIGHT);

		lblResponsibilities.setText("Name of the Role: ");
		lblResponsibilities.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true, 1, 1));

		if (count < 1) {
			combo = new Combo(description, SWT.DROP_DOWN | SWT.RIGHT);
			combo.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(SelectionEvent e) {
					viewer.getTable().removeAll();
					selectedItem = combo.getItem(combo.getSelectionIndex());
					updateReponsibilites(CSAbstractEditor.identifiers.get(selectedItem), responsibility);
				}
			});
			combo.setLayoutData(new GridData(SWT.NONE, SWT.FILL, true, true, 1, 1));
			for (ICausalComponent x : dataInterface.getCasualComponents()) {
				combo.add(x.getText());
				CSAbstractEditor.identifiers.put(x.getText(), x.getId());
			}
			if (combo.getItemCount() > 0) {
				combo.select(combo.getItemCount() - 1);
				selectedItem = combo.getItem(combo.getSelectionIndex());
				count++;
			}
		}

		Button btnAdd = new Button(description, SWT.CENTER);
		btnAdd.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, true, false, 1, 1));
		btnAdd.setImage(Activator.getImageDescriptor("/icons/add.png").createImage());
		btnAdd.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				int count;
				if (dataInterface.getCasualComponents().isEmpty()) {
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Warning");
					dialog.setMessage("Create Components in Controlstructure first");

					dialog.open();
					return;
				}
				if (viewer.getTable().getItemCount() == 0) {
					count = 1;
				} else {
					count = viewer.getTable().getItemCount() + 1;
				}
				TableItem item = new TableItem(viewer.getTable(), SWT.None);
				item.setText(new String[] { count + "", "" });
				applyDataToDataModel(CSAbstractEditor.identifiers.get(selectedItem), item.getText(0), item.getText(1), false,
						responsibility);
			}
		});

		Button btnDel = new Button(description, SWT.CENTER);
		btnDel.setLayoutData(new GridData(SWT.RIGHT, SWT.NONE, false, false, 1, 1));
		btnDel.setImage(Activator.getImageDescriptor("/icons/delete.png").createImage());

		btnDel.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				if (viewer.getTable().getSelectionIndex() == -1) {
					MessageBox dialog = new MessageBox(parent.getShell(), SWT.ICON_INFORMATION | SWT.OK);
					dialog.setText("Warning");
					dialog.setMessage("No Event selected");

					dialog.open();
					return;
				}
				int itemIndex = viewer.getTable().getSelectionIndex();
				removeFromDataModel(CSAbstractEditor.identifiers.get(selectedItem), viewer.getTable().getItem(itemIndex).getText(0),
						responsibility);
				viewer.getTable().remove(itemIndex);
				for (int i = itemIndex; i < viewer.getTable().getItemCount(); i++) {
					updateTableComponentsInDataStructure(CSAbstractEditor.identifiers.get(selectedItem),
							viewer.getTable().getItem(i).getText(0), selectedItem, i + 1 + "", responsibility);
					viewer.getTable().getItem(i).setText(0, i + 1 + "");
				}

			}
		});

		GridLayout buttons = new GridLayout(1, true);
		buttons.verticalSpacing = 1;

		Composite data = new Composite(layout, SWT.BORDER);
		data.setLayout(new GridLayout(5, false));
		data.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite tab = new Composite(data, SWT.None);
		tab.setLayout(buttons);
		tab.setLayoutData(new GridData(SWT.NONE, SWT.FILL, false, true, 1, 1));
		tab.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_WHITE));

		final Button safety = new Button(tab, SWT.NONE);
		safety.setText(ResponsibilitiesView.responsibilities);
		safety.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));
		safety.setEnabled(false);
		safety.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));

	

		final Button unsafeActionsBtn = new Button(tab, SWT.NONE);
		unsafeActionsBtn.setText(ResponsibilitiesView.unsafeActions);
		unsafeActionsBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));

		final Button flawsBtn = new Button(tab, SWT.NONE);
		flawsBtn.setText(ResponsibilitiesView.flaws);
		flawsBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));
		
		final Button contextBtn = new Button(tab, SWT.NONE);
		contextBtn.setText(ResponsibilitiesView.context);
		contextBtn.setLayoutData(new GridData(SWT.FILL, SWT.None, true, false, 1, 2));

		final Color defaultColor = unsafeActionsBtn.getBackground();

		safety.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				responsibility = ResponsibilitiesView.responsibilities;
				contextBtn.setEnabled(true);
				flawsBtn.setEnabled(true);
				unsafeActionsBtn.setEnabled(true);
				contextBtn.setBackground(defaultColor);
				flawsBtn.setBackground(defaultColor);
				unsafeActionsBtn.setBackground(defaultColor);
				safety.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
				safety.setEnabled(false);
				PlatformUI.getWorkbench().getDisplay().getActiveShell().forceFocus();
				viewer.getTable().removeAll();
				updateReponsibilites(CSAbstractEditor.identifiers.get(selectedItem), responsibility);
			}
		});

		contextBtn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				responsibility = ResponsibilitiesView.context;
				safety.setEnabled(true);
				flawsBtn.setEnabled(true);
				unsafeActionsBtn.setEnabled(true);
				safety.setBackground(defaultColor);
				flawsBtn.setBackground(defaultColor);
				unsafeActionsBtn.setBackground(defaultColor);
				contextBtn.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
				contextBtn.setEnabled(false);
				PlatformUI.getWorkbench().getDisplay().getActiveShell().forceFocus();
				viewer.getTable().removeAll();
				updateReponsibilites(CSAbstractEditor.identifiers.get(selectedItem), responsibility);
			}
		});

		unsafeActionsBtn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				responsibility = (ResponsibilitiesView.unsafeActions);
				safety.setEnabled(true);
				flawsBtn.setEnabled(true);
				contextBtn.setEnabled(true);
				safety.setBackground(defaultColor);
				flawsBtn.setBackground(defaultColor);
				contextBtn.setBackground(defaultColor);
				unsafeActionsBtn.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
				unsafeActionsBtn.setEnabled(false);
				PlatformUI.getWorkbench().getDisplay().getActiveShell().forceFocus();
				viewer.getTable().removeAll();
				updateReponsibilites(CSAbstractEditor.identifiers.get(selectedItem), responsibility);
			}
		});

		flawsBtn.addListener(SWT.Selection, new Listener() {

			@Override
			public void handleEvent(Event event) {
				responsibility = ResponsibilitiesView.flaws;
				safety.setEnabled(true);
				unsafeActionsBtn.setEnabled(true);
				contextBtn.setEnabled(true);
				safety.setBackground(defaultColor);
				unsafeActionsBtn.setBackground(defaultColor);
				contextBtn.setBackground(defaultColor);
				flawsBtn.setBackground(parent.getDisplay().getSystemColor(SWT.COLOR_RED));
				flawsBtn.setEnabled(false);
				PlatformUI.getWorkbench().getDisplay().getActiveShell().forceFocus();
				viewer.getTable().removeAll();
				updateReponsibilites(CSAbstractEditor.identifiers.get(selectedItem), responsibility);
			}
		});

		Composite buttonComposite = new Composite(tab, SWT.BOTTOM);
		buttonComposite.setLayout(new GridLayout(2, false));
		buttonComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 1, 1));

		Composite tableComposite = new Composite(data, SWT.NONE);
		tableComposite.setLayout(new GridLayout(1, false));
		tableComposite.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true, 4, 1));

		viewer = new TableViewer(tableComposite, SWT.FULL_SELECTION);

		TableColumn c1 = new TableColumn(viewer.getTable(), SWT.NONE);
		c1.setText("ID");
		TableColumnLayout tableColumnLayoutC1 = new TableColumnLayout();
		tableColumnLayoutC1.setColumnData(c1, new ColumnWeightData(10, 25, true));
		tableComposite.setLayout(tableColumnLayoutC1);

		TableColumn c2 = new TableColumn(viewer.getTable(), SWT.NONE);
		c2.setText("Description");
		TableColumnLayout tableColumnLayoutC2 = new TableColumnLayout();
		tableColumnLayoutC2.setColumnData(c2, new ColumnWeightData(90, 80, true));
		tableComposite.setLayout(tableColumnLayoutC2);

		final TableEditor editor = new TableEditor(viewer.getTable());
		editor.horizontalAlignment = SWT.LEFT;
		editor.grabHorizontal = true;
		viewer.getTable().addListener(SWT.MouseDown, new Listener() {
			@Override
			public void handleEvent(Event event) {
				Rectangle clientArea = viewer.getTable().getClientArea();
				Point pt = new Point(event.x, event.y);
				int index = viewer.getTable().getTopIndex();
				while (index < viewer.getTable().getItemCount()) {
					boolean visible = false;
					final TableItem item = viewer.getTable().getItem(index);
					for (int i = 0; i < viewer.getTable().getColumnCount(); i++) {
						Rectangle rect = item.getBounds(i);
						if (rect.contains(pt)) {
							final int column = i;
							if (column != 0) {
								if (column == 1) {
									final Text text = new Text(viewer.getTable(), SWT.NONE);
									Listener textListener = new Listener() {
										@Override
										public void handleEvent(final Event e) {
											switch (e.type) {
											case SWT.FocusOut:
												applyDataToDataModel(CSAbstractEditor.identifiers.get(selectedItem), item.getText(0),
														text.getText(), true, responsibility);
												item.setText(column, text.getText());
												text.dispose();
												break;
											case SWT.Traverse:
												switch (e.detail) {
												case SWT.TRAVERSE_RETURN:
													item.setText(column, text.getText());
												case SWT.TRAVERSE_ESCAPE:
													text.dispose();
													e.doit = false;
												}
												break;
											}
										}
									};
									text.addListener(SWT.FocusOut, textListener);
									text.addListener(SWT.Traverse, textListener);
									editor.setEditor(text, item, i);
									text.setText(item.getText(i));
									text.selectAll();
									text.setFocus();
									return;
								} else {
									return;
								}

							}
						}
						if (!visible && rect.intersects(clientArea)) {
							visible = true;
						}
					}
					if (!visible) {
						return;
					}
					index++;
				}
			}
		});

		if (combo.getItemCount() > 0) {
			updateReponsibilites(CSAbstractEditor.identifiers.get(combo.getItem(combo.getSelectionIndex())),
					ResponsibilitiesView.this.responsibility);
		}
		viewer.getTable().setHeaderVisible(true);
		viewer.getTable().setLinesVisible(true);

	}

}