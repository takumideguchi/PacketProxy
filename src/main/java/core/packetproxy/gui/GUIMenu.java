/*
 * Copyright 2019 DeNA Co., Ltd.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package packetproxy.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileNameExtensionFilter;

import packetproxy.common.I18nString;
import packetproxy.model.Database;
import packetproxy.model.Packets;

@SuppressWarnings("serial")
public class GUIMenu extends JMenuBar {
	final static private String defaultDir = System.getProperty("user.home");
	GUIMenu self;
	JFrame owner;
	public GUIMenu(JFrame owner) {
		self = this;
		this.owner = owner;
		JMenu file_menu = new JMenu(I18nString.get("Project"));
		this.add(file_menu);
		JMenuItem save_sqlite = new JMenuItem(I18nString.get("Save packets to sqlite3 file"), KeyEvent.VK_S);
		file_menu.add(save_sqlite);
		save_sqlite.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				WriteFileChooserWrapper filechooser = new WriteFileChooserWrapper(owner, "sqlite3");
				filechooser.addFileChooserListener(new WriteFileChooserWrapper.FileChooserListener() {
					@Override
					public void onApproved(File file, String extension) {
						try {
							Database.getInstance().Save(file.getAbsolutePath());
							JOptionPane.showMessageDialog(null, I18nString.get("Data saved successfully"));
						}catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, I18nString.get("Data can't be saved with error"));
						}
					}

					@Override
					public void onCanceled() {}

					@Override
					public void onError() {
						JOptionPane.showMessageDialog(null, I18nString.get("Data can't be saved with error"));
					}
				});
				filechooser.showSaveDialog();
			}
		});
		JMenuItem save_txt = new JMenuItem(I18nString.get("Save packets to text file"), KeyEvent.VK_S);
		file_menu.add(save_txt);
		save_txt.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				WriteFileChooserWrapper filechooser = new WriteFileChooserWrapper(owner, "txt");
				filechooser.addFileChooserListener(new WriteFileChooserWrapper.FileChooserListener() {
					@Override
					public void onApproved(File file, String extension) {
						try {
							Packets.getInstance().outputAllPackets(file.getAbsolutePath());
							JOptionPane.showMessageDialog(null, I18nString.get("Data saved successfully"));
						}catch (Exception e1) {
							e1.printStackTrace();
							JOptionPane.showMessageDialog(null, I18nString.get("Data can't be saved with error"));
						}
					}

					@Override
					public void onCanceled() {}

					@Override
					public void onError() {
						JOptionPane.showMessageDialog(null, I18nString.get("Data can't be saved with error"));
					}
				});
				filechooser.showSaveDialog();
			}
		});
		JMenuItem load_menu = new JMenuItem(I18nString.get("Load packets from sqlite3 file"), KeyEvent.VK_L);
		file_menu.add(load_menu);
		load_menu.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				try {
					JFileChooser filechooser = new JFileChooser();
					filechooser.setCurrentDirectory(new File(defaultDir));
					filechooser.setFileFilter(new FileNameExtensionFilter("*.sqlite3", "sqlite3"));
					filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
					int selected = filechooser.showOpenDialog(SwingUtilities.getRoot(self));
					if (selected == JFileChooser.APPROVE_OPTION) {
						File file = filechooser.getSelectedFile();
						Database.getInstance().Load(file.getAbsolutePath());
					}
				} catch (Exception e1) {
					e1.printStackTrace();
					JOptionPane.showMessageDialog(null, I18nString.get("Data can't be loaded with error"));
				}
			}
		});
	}
}
