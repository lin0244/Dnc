/*
 * Copyright (C) 2014 by Herbert Roider <herbert.roider@utanet.at>
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * 
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package org.roiderh.dnc.toolbar;

import java.util.ArrayList;
import org.openide.util.NbPreferences;
import java.util.prefs.Preferences;
import java.util.prefs.PreferenceChangeListener;
import java.util.prefs.PreferenceChangeEvent;
import javax.swing.JOptionPane;
import javax.swing.text.JTextComponent;
import org.roiderh.dnc.DNCPanel;
import org.roiderh.dnc.Properties;
import jssc.SerialPort;
import jssc.SerialPortException;
import javax.swing.text.BadLocationException;
/**
 * this panel is shown in the toolbar.
 *
 * @author Herbert Roider <herbert.roider@utanet.at>
 */
public class DnctoolbarPanel extends javax.swing.JPanel implements PreferenceChangeListener {

        public int current_config_index = -1;
        org.roiderh.dnc.serial.SerialJDialog jDialogReceive = null;

        /**
         * Creates new form DncPanel
         */
        public DnctoolbarPanel() {
                initComponents();

                Preferences pref = NbPreferences.forModule(DNCPanel.class);
                ArrayList<Properties> properties = DNCPanel.readPreferences();
                ((ConfigPropertiesListModel) this.jComboBoxConfigs.getModel()).setProperties(properties);

                pref.addPreferenceChangeListener(this);

        }

        @Override
        public void preferenceChange(PreferenceChangeEvent evt) {
                Properties p = (Properties) this.jComboBoxConfigs.getSelectedItem();
                ArrayList<Properties> properties = DNCPanel.readPreferences();
                ((ConfigPropertiesListModel) this.jComboBoxConfigs.getModel()).setProperties(properties);
                this.jComboBoxConfigs.setSelectedItem(p);

        }

        /**
         * This method is called from within the constructor to initialize the
         * form. WARNING: Do NOT modify this code. The content of this method is
         * always regenerated by the Form Editor.
         */
        @SuppressWarnings("unchecked")
        // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
        private void initComponents() {

                jComboBoxConfigs = new javax.swing.JComboBox();
                jButtonSend = new javax.swing.JButton();
                jButtonReceive = new javax.swing.JButton();

                jComboBoxConfigs.setModel(new org.roiderh.dnc.toolbar.ConfigPropertiesListModel());
                jComboBoxConfigs.setMaximumSize(new java.awt.Dimension(100, 24));
                jComboBoxConfigs.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jComboBoxConfigsActionPerformed(evt);
                        }
                });

                org.openide.awt.Mnemonics.setLocalizedText(jButtonSend, org.openide.util.NbBundle.getMessage(DnctoolbarPanel.class, "DnctoolbarPanel.jButtonSend.text")); // NOI18N
                jButtonSend.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonSendActionPerformed(evt);
                        }
                });

                org.openide.awt.Mnemonics.setLocalizedText(jButtonReceive, org.openide.util.NbBundle.getMessage(DnctoolbarPanel.class, "DnctoolbarPanel.jButtonReceive.text")); // NOI18N
                jButtonReceive.addActionListener(new java.awt.event.ActionListener() {
                        public void actionPerformed(java.awt.event.ActionEvent evt) {
                                jButtonReceiveActionPerformed(evt);
                        }
                });

                javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
                this.setLayout(layout);
                layout.setHorizontalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createSequentialGroup()
                                .addComponent(jComboBoxConfigs, javax.swing.GroupLayout.PREFERRED_SIZE, 202, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonSend)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButtonReceive))
                );
                layout.setVerticalGroup(
                        layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                .addComponent(jComboBoxConfigs, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addComponent(jButtonSend)
                                .addComponent(jButtonReceive))
                );
        }// </editor-fold>//GEN-END:initComponents

        private void jComboBoxConfigsActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jComboBoxConfigsActionPerformed
                // TODO add your handling code here:
                System.out.println("Combo configs selection");
        }//GEN-LAST:event_jComboBoxConfigsActionPerformed

        private void jButtonSendActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonSendActionPerformed
                this.SendOrReceive(false);
        }//GEN-LAST:event_jButtonSendActionPerformed
        private void SendOrReceive(boolean receive){
                  Properties p = (Properties) this.jComboBoxConfigs.getSelectedItem();
                if (p == null) {
                        JOptionPane.showMessageDialog(null, "Error: no RS232 Config selected.");
                        return;
                }

                JTextComponent ed = org.netbeans.api.editor.EditorRegistry.lastFocusedComponent();
                if (ed == null) {
                        JOptionPane.showMessageDialog(null, "Error: no open editor");
                        return;
                }
                javax.swing.text.Document doc = ed.getDocument();
                if (doc == null) {
                        JOptionPane.showMessageDialog(null, "Error: no open Document");
                        return;
                }

                SerialPort serialPort = new SerialPort(p.port);
              

                if (jDialogReceive == null) {
                        jDialogReceive = new org.roiderh.dnc.serial.SerialJDialog(org.openide.windows.WindowManager.getDefault().getMainWindow(), true);
                }

                jDialogReceive.setPort(serialPort, doc, receive);
                try {
                        serialPort.openPort();//Open serial port
                        serialPort.setParams(p.baud,
                                p.databits,
                                p.stopbits,
                                p.parity);//Set params.
                        int mask = SerialPort.MASK_RXCHAR + SerialPort.MASK_CTS + SerialPort.MASK_DSR;//Prepare mask
                        serialPort.setEventsMask(mask);//Set mask

                        serialPort.addEventListener(jDialogReceive);//Add SerialPortEventListener
                        if(receive == false){
                                String s = doc.getText(0, doc.getLength());
                                serialPort.writeString(s);
                        }
                } catch (SerialPortException ex) {
                        JOptionPane.showMessageDialog(null, "Error: " + ex.getLocalizedMessage());
                        System.out.println(ex);
                        return;
                }catch(BadLocationException bex){
                         JOptionPane.showMessageDialog(null, "Error: " + bex.getLocalizedMessage());
                         return;
                }

                jDialogReceive.setVisible(true);
                
        }
        
        private void jButtonReceiveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButtonReceiveActionPerformed

                this.SendOrReceive(true);
               

        }//GEN-LAST:event_jButtonReceiveActionPerformed


        // Variables declaration - do not modify//GEN-BEGIN:variables
        private javax.swing.JButton jButtonReceive;
        private javax.swing.JButton jButtonSend;
        private javax.swing.JComboBox jComboBoxConfigs;
        // End of variables declaration//GEN-END:variables
}
