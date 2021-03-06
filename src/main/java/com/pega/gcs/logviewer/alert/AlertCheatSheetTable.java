/*******************************************************************************
 * Copyright (c) 2017 Pegasystems Inc. All rights reserved.
 *
 * Contributors:
 *     Manu Varghese
 *******************************************************************************/
package com.pega.gcs.logviewer.alert;

import java.awt.Point;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JPopupMenu;
import javax.swing.JTable;
import javax.swing.SwingUtilities;

import com.pega.gcs.fringecommon.guiutilities.FilterTable;
import com.pega.gcs.fringecommon.guiutilities.RightClickMenuItem;

public class AlertCheatSheetTable extends FilterTable<Integer> {

	private static final long serialVersionUID = -233948403212671301L;

	public AlertCheatSheetTable(AlertCheatSheetTableModel alertCheatSheetTableModel) {
		super(alertCheatSheetTableModel);

		setAutoResizeMode(JTable.AUTO_RESIZE_OFF);

		addMouseListener(new MouseAdapter() {

			@Override
			public void mouseClicked(MouseEvent e) {

				if (SwingUtilities.isRightMouseButton(e)) {

					AlertCheatSheetTable alertCheatSheetTable = (AlertCheatSheetTable) e.getSource();
					AlertCheatSheetTableModel alertCheatSheetTableModel;
					alertCheatSheetTableModel = (AlertCheatSheetTableModel) alertCheatSheetTable.getModel();

					int[] selectedRows = alertCheatSheetTable.getSelectedRows();
					final List<Integer> selectedRowList = new ArrayList<Integer>();

					// in case the row was not selected when right clicking then
					// based on the point, select the row.
					Point point = e.getPoint();

					if ((selectedRows != null) && (selectedRows.length <= 1)) {

						int selectedRow = alertCheatSheetTable.rowAtPoint(point);

						if (selectedRow != -1) {
							// select the row first
							alertCheatSheetTable.setRowSelectionInterval(selectedRow, selectedRow);
							selectedRows = new int[] { selectedRow };
						}
					}

					for (int selectedRow : selectedRows) {
						selectedRowList.add(selectedRow);
					}

					int size = selectedRowList.size();

					if (size > 0) {

						JPopupMenu popupMenu = new JPopupMenu();

						if (size == 1) {

							RightClickMenuItem copyPDNUrl = new RightClickMenuItem("Copy PDN URL");

							copyPDNUrl.addActionListener(new ActionListener() {

								@Override
								public void actionPerformed(ActionEvent e) {

									Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();

									int rowIndex = selectedRowList.get(0);
									int columnIndex = AlertCheatSheetTableColumn.getTableColumnList()
											.indexOf(AlertCheatSheetTableColumn.PDNURL);

									String pdnURL = (String) alertCheatSheetTableModel.getValueAt(rowIndex,
											columnIndex);

									clipboard.setContents(new StringSelection(pdnURL), copyPDNUrl);

									popupMenu.setVisible(false);

								}
							});
							
							popupMenu.add(copyPDNUrl);
						}

						popupMenu.show(e.getComponent(), e.getX(), e.getY());
					}

				} else {
					super.mouseClicked(e);
				}
			}

		});
	}

}
