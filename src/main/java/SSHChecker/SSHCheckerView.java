package SSHChecker;

import SSHChecker.Model.SSHsTableModel;
import SSHChecker.Editor.IntegerEditor;
import SSHChecker.Renderer.CheckBoxRenderer;
import SSHChecker.Renderer.IntegerRenderer;
import SSHChecker.Renderer.LabelwithFlagRenderer;
import SSHChecker.Renderer.LabelwithIconRenderer;
import SSHChecker.Renderer.ProgressRenderer;
import SSHFunction.SSHCheckBL;
import SSHFunction.SSHCheckLive;
import SSHFunction.SSHChecking;
import SSHFunction.SSHCheckingFactory;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.exception.GeoIp2Exception;
import com.maxmind.geoip2.model.CountryResponse;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.StringSelection;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventObject;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.DefaultCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.event.CellEditorListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.text.BadLocationException;
import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 *
 * @author Củ Chuối
 */
public class SSHCheckerView extends javax.swing.JFrame {

    /**
     * Creates new form SSHCheckerView
     */
    private void loadGUIPrefers() {
        super.setSize(prefers.JFramePrefsW, prefers.JFramePrefsH);
        Dimension dim = Toolkit.getDefaultToolkit().getScreenSize();
        super.setLocation(dim.width / 2 - super.getSize().width / 2,
                dim.height / 2 - super.getSize().height / 2);
        jSplitPane1.setDividerLocation(prefers.JFramePrefsDivider1);
        jSplitPane1.setDividerLocation(prefers.JFramePrefsDivider2);
    }

    private void loadIPDatabase() {
        insertLog(setTagtoText(processtext, "ProcessText", "Loading database GeoIP..."));
        try {
            dbreader = new DatabaseReader.Builder(ipDB).build();
        } catch (IOException ex) {
            insertLog(setTagtoText(errortext, "ErrorText",
                    SSHCheckerView.class.getName() + ": " + ex));
        }
        insertLog(setTagtoText(processtext, "ProcessText", "Done!"));
    }

    private void initLogEditor() {
        htmldoc = (HTMLDocument) jEditorPane_log.getDocument();
        htmleditorKit = (HTMLEditorKit) jEditorPane_log.getEditorKit();
        insertLog(welcometext);
    }

    private void initTables() {
        sshlistTable();
        settingTable();

        jTableModel_sshlist = (SSHsTableModel) jTable_sshlist.getModel();
        jTableModel_setting = (DefaultTableModel) jTable_setting.getModel();
    }

    class closeingEvent extends WindowAdapter {

        @Override
        public void windowClosing(WindowEvent e) {
            int W = e.getComponent().getWidth();
            int H = e.getComponent().getHeight();
            int dvder1 = jSplitPane1.getDividerLocation();
            int dvder2 = jSplitPane2.getDividerLocation();
            prefers.setJFramePrefs(W, H, dvder1, dvder2);
            int Timeout = (int) jTableModel_setting.getValueAt(0, 1);
            String RCWhen = (String) jTableModel_setting.getValueAt(1, 1);
            int RCCount = (int) jTableModel_setting.getValueAt(2, 1);
            boolean IsBLC = (boolean) jTableModel_setting.getValueAt(3, 1);
            boolean IsLC = (boolean) jTableModel_setting.getValueAt(4, 1);
            boolean IsFC = (boolean) jTableModel_setting.getValueAt(5, 1);
            int BLCTCount = (int) jTableModel_setting.getValueAt(6, 1);
            int LCTCount = (int) jTableModel_setting.getValueAt(7, 1);
            int FCTCount = (int) jTableModel_setting.getValueAt(8, 1);
            prefers.setJTableSettingPrefs(Timeout,
                    RCWhen, RCCount, BLCTCount, LCTCount, FCTCount, IsBLC, IsLC, IsFC);
            super.windowClosing(e);
        }

    }

    public SSHCheckerView() {

        initComponents();
        initLogEditor();
        initTables();
        loadGUIPrefers();
        loadIPDatabase();
        super.addWindowListener(new closeingEvent());
    }

    // <editor-fold defaultstate="collapsed" desc="Setting Table">          
    private void settingTable() {

        String[] ColName = new String[]{"Setting", "Value"};
        Object[][] RowData;
        RowData = new Object[][]{
            {"TimeOut(ms)", prefers.JTableSettingTimeout},
            {"Rechecking when", prefers.JTableSettingRCWhen},
            {"Rechecking count", prefers.JTableSettingRCCount},
            {"Blacklist checking", prefers.JTableSettingIsBLC},
            {"Live checking", prefers.JTableSettingIsLC},
            {"Fresh checking", prefers.JTableSettingIsFC},
            {"BLChecking Thread count", prefers.JTableSettingBLCTCount},
            {"LChecking  Thread count", prefers.JTableSettingLCTCount},
            {"FChecking Thread count", prefers.JTableSettingFCTCount}
        };
        boolean[] ColEditable = new boolean[]{false, true};
        jTable_setting.setModel(new DefaultTableModel(RowData, ColName) {

            @Override
            public boolean isCellEditable(int row, int column) {
                return ColEditable[column];
            }

        });

        JComboBox RecheckingComboBox = new JComboBox();
        RecheckingComboBox.addItem("not-using");
        RecheckingComboBox.addItem("List's been finished");
        RecheckingComboBox.addItem("At that time");
        RecheckingComboBox.setSelectedItem(prefers.JTableSettingRCWhen);

        JCheckBox jcheckbox = new JCheckBox();
        jcheckbox.setHorizontalAlignment(SwingConstants.CENTER);

        TableColumn settingcol = jTable_setting.getColumnModel().getColumn(1);
        TableCellEditorManager Editor = new TableCellEditorManager(jTable_setting);
        TableCellRendererManager Renderer = new TableCellRendererManager(jTable_setting);

        Editor.setEditorAt(0, new IntegerEditor(5000, 60000));
        Editor.setEditorAt(1, new DefaultCellEditor(RecheckingComboBox));
        Editor.setEditorAt(2, new IntegerEditor(1, 10));
        Editor.setEditorAt(3, new DefaultCellEditor(jcheckbox));
        Editor.setEditorAt(4, new DefaultCellEditor(jcheckbox));
        Editor.setEditorAt(5, new DefaultCellEditor(jcheckbox));
        Editor.setEditorAt(6, new IntegerEditor(1, 100));
        Editor.setEditorAt(7, new IntegerEditor(1, 100));
        Editor.setEditorAt(8, new IntegerEditor(1, 100));

        Renderer.setRendererAt(0, new IntegerRenderer());
        Renderer.setRendererAt(2, new IntegerRenderer());
        Renderer.setRendererAt(3, new CheckBoxRenderer());
        Renderer.setRendererAt(4, new CheckBoxRenderer());
        Renderer.setRendererAt(5, new CheckBoxRenderer());
        Renderer.setRendererAt(6, new IntegerRenderer());
        Renderer.setRendererAt(7, new IntegerRenderer());
        Renderer.setRendererAt(8, new IntegerRenderer());

        jTable_setting.getColumn("Value").setCellEditor(Editor);
        jTable_setting.getColumn("Value").setCellRenderer(Renderer);
    }

    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="SSHList Table">          
    private void sshlistTable() {
        List<SSH> listSSH = new ArrayList<SSH>();
        listSSH.add(new SSH("150.214.194.231", "root", "server", "United States(US)"));
        listSSH.add(new SSH("23.119.70.233", "admin", "admin", "Spain(ES)"));
        listSSH.add(new SSH("173.48.64.194", "admin", "admin", "United States(US)"));

        jTable_sshlist.setModel(new SSHsTableModel(listSSH));
        HashMap<String, String> TextwithIcon_HashMap = new HashMap<String, String>();
        TextwithIcon_HashMap.put("Working", "/ICON/success.png");
        TextwithIcon_HashMap.put("Blacklist", "/ICON/warning.png");
        TextwithIcon_HashMap.put("Error", "/ICON/forbidden.png");
        TextwithIcon_HashMap.put("Checking", "/ICON/checking.png");
        TextwithIcon_HashMap.put("Waiting", "");

        HashMap<String, Color> TextwithColor_HashMap = new HashMap<String, Color>();
        TextwithColor_HashMap.put("Working", Color.GREEN);
        TextwithColor_HashMap.put("Blacklist", Color.gray);
        TextwithColor_HashMap.put("Error", Color.red);
        TextwithColor_HashMap.put("Checking", Color.orange);
        TextwithColor_HashMap.put("Waiting", Color.BLACK);

        jTable_sshlist.getColumn("Status").setCellRenderer(new LabelwithIconRenderer(
                TextwithIcon_HashMap,
                TextwithColor_HashMap
        ));
        jTable_sshlist.getColumn("Country").setCellRenderer(new LabelwithFlagRenderer());
        jTable_sshlist.getColumn("Blacklist%").setCellRenderer(new ProgressRenderer());

    }

    public void setSSHListData(SSH[] sshs) {
        for (SSH ssh : sshs) {
            addSSHListRowData(ssh);
        }
    }

    public void addSSHListRowData(SSH ssh) {

        jTableModel_sshlist.addRow(ssh);
    }

    public void clearSSHListData() {
        jTableModel_sshlist.removeAllRow();
    }

    public void removeSSHListRow(int i) {
        jTableModel_sshlist.removeRow(i);
    }
    //</editor-fold>
    // <editor-fold defaultstate="collapsed" desc="TextPane log">

    public final void insertLog(String htmltext) {
        try {
            htmleditorKit.insertHTML(htmldoc, htmldoc.getLength(),
                    htmltext, 0, 0, null);
        } catch (BadLocationException | IOException ex) {
            System.err.println(SSHCheckerView.class.getName() + ": " + ex);
        }
    }

    public void clearLog() {
        jEditorPane_log.setText("");
    }

    public final void copyLog() {
        StringBuilder sb = new StringBuilder();
        Document doc = Jsoup.parse(jEditorPane_log.getText());
        Elements elements = doc.select("p");
        for (Element e : elements) {
            sb.append(e.text() + "\n");
        }
        StringSelection selection = new StringSelection(sb.toString());
        Clipboard clipboard = Toolkit.getDefaultToolkit().getSystemClipboard();
        clipboard.setContents(selection, selection);

    }

    public final String setTagtoText(String htmltext, String tag, String text) {
        return htmltext.replaceAll("\\[" + tag + "\\]", text);
    }
    //</editor-fold>

    class TableCellRendererManager implements TableCellRenderer {

        protected HashMap renderers;
        protected TableCellRenderer renderer, defaultrenderer;
        protected JTable table;

        public TableCellRendererManager(JTable table) {
            this.table = table;
            renderers = new HashMap();
            defaultrenderer = new DefaultTableCellRenderer();
        }

        public void setRendererAt(int row, TableCellRenderer renderer) {
            renderers.put(row, renderer);
        }

        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
            renderer = (TableCellRenderer) renderers.get(row);
            if (renderer != null) {
                return renderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            } else {
                return defaultrenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
            }
        }

    }

    class TableCellEditorManager implements TableCellEditor {

        protected HashMap editors;
        protected TableCellEditor editor, defaultEditor;
        protected JTable table;

        public TableCellEditorManager(JTable table) {
            this.table = table;
            editors = new HashMap();
            defaultEditor = new DefaultCellEditor(new JTextField());

        }

        public void setEditorAt(int row, TableCellEditor editor) {
            this.editors.put(row, editor);
        }

        public TableCellEditor getEditorAt(int row) {
            return (TableCellEditor) editors.get(row);
        }

        @Override
        public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column) {

            return editor.getTableCellEditorComponent(table, value, isSelected, row, column);
        }

        @Override
        public Object getCellEditorValue() {
            return editor.getCellEditorValue();
        }

        @Override
        public boolean isCellEditable(EventObject anEvent) {
            try {
                selectEditor((MouseEvent) anEvent);
                return editor.isCellEditable(anEvent);
            } catch (Exception e) {
                return false;
            }

        }

        @Override
        public boolean shouldSelectCell(EventObject anEvent) {
            try {
                selectEditor((MouseEvent) anEvent);
                return editor.shouldSelectCell(anEvent);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public boolean stopCellEditing() {
            return editor.stopCellEditing();
        }

        @Override
        public void cancelCellEditing() {
            editor.cancelCellEditing();
        }

        @Override
        public void addCellEditorListener(CellEditorListener l) {
            editor.addCellEditorListener(l);
        }

        @Override
        public void removeCellEditorListener(CellEditorListener l) {
            editor.removeCellEditorListener(l);
        }

        protected void selectEditor(MouseEvent e) {
            int row;
            if (e == null) {
                row = table.getSelectionModel().getAnchorSelectionIndex();
            } else {
                row = table.rowAtPoint(e.getPoint());

            }
            editor = (TableCellEditor) editors.get(row);
            if (editor == null) {
                editor = defaultEditor;
            }
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPopupMenu_log = new javax.swing.JPopupMenu();
        jPopupMenu_log_copy = new javax.swing.JMenuItem();
        jPopupMenu_log_clear = new javax.swing.JMenuItem();
        jPopupMenu_table = new javax.swing.JPopupMenu();
        jPopupMenu_table_add = new javax.swing.JMenuItem();
        jPopupMenu_table_delete = new javax.swing.JMenuItem();
        jPopupMenu_table_check = new javax.swing.JMenuItem();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jScrollPane_log = new javax.swing.JScrollPane();
        jEditorPane_log = new javax.swing.JEditorPane();
        jScrollPane_setting = new javax.swing.JScrollPane();
        jTable_setting = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane_sshlist = new javax.swing.JScrollPane();
        jTable_sshlist = new javax.swing.JTable();
        jMenu = new javax.swing.JMenuBar();
        jMenu_file = new javax.swing.JMenu();
        jMenu_file_new = new javax.swing.JMenuItem();
        jMenu_file_open = new javax.swing.JMenuItem();
        jMenu_file_save = new javax.swing.JMenuItem();
        jMenu_file_saveas = new javax.swing.JMenuItem();
        jMenu_help = new javax.swing.JMenu();
        jMenu_help_about = new javax.swing.JMenuItem();

        jPopupMenu_log_copy.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/copy.png"))); // NOI18N
        jPopupMenu_log_copy.setText("copy");
        jPopupMenu_log_copy.setContentAreaFilled(false);
        jPopupMenu_log_copy.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenu_log_copyActionPerformed(evt);
            }
        });
        jPopupMenu_log.add(jPopupMenu_log_copy);

        jPopupMenu_log_clear.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/garbage.png"))); // NOI18N
        jPopupMenu_log_clear.setText("clear");
        jPopupMenu_log_clear.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenu_log_clearActionPerformed(evt);
            }
        });
        jPopupMenu_log.add(jPopupMenu_log_clear);

        jPopupMenu_table_add.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/add.png"))); // NOI18N
        jPopupMenu_table_add.setText("Add");
        jPopupMenu_table_add.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenu_table_addActionPerformed(evt);
            }
        });
        jPopupMenu_table.add(jPopupMenu_table_add);

        jPopupMenu_table_delete.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/delete.png"))); // NOI18N
        jPopupMenu_table_delete.setText("jMenuItem5");
        jPopupMenu_table_delete.setText("Delete");
        jPopupMenu_table_delete.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenu_table_deleteActionPerformed(evt);
            }
        });
        jPopupMenu_table.add(jPopupMenu_table_delete);

        jPopupMenu_table_check.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.SHIFT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jPopupMenu_table_check.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/check.png"))); // NOI18N
        jPopupMenu_table_check.setText("jMenuItem6");
        jPopupMenu_table_check.setText("Check");
        jPopupMenu_table_check.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jPopupMenu_table_checkActionPerformed(evt);
            }
        });
        jPopupMenu_table.add(jPopupMenu_table_check);

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("SSH Checker");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));
        setPreferredSize(new java.awt.Dimension(600, 550));

        jSplitPane1.setDividerLocation(300);
        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        jSplitPane1.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jSplitPane2.setDividerLocation(270);

        jScrollPane_log.setHorizontalScrollBarPolicy(javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
        jScrollPane_log.setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        jEditorPane_log.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Log", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Tahoma", 0, 11), new java.awt.Color(51, 0, 255))); // NOI18N
        jEditorPane_log.setContentType("text/html"); // NOI18N
        jEditorPane_log.setComponentPopupMenu(jPopupMenu_log);
        jEditorPane_log.setCursor(new java.awt.Cursor(java.awt.Cursor.TEXT_CURSOR));
        jScrollPane_log.setViewportView(jEditorPane_log);

        jSplitPane2.setRightComponent(jScrollPane_log);

        jScrollPane_setting.setPreferredSize(new java.awt.Dimension(200, 402));

        jTable_setting.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {

            }
        ));
        jScrollPane_setting.setViewportView(jTable_setting);

        jSplitPane2.setTopComponent(jScrollPane_setting);

        jSplitPane1.setBottomComponent(jSplitPane2);
        jSplitPane2.getAccessibleContext().setAccessibleName("");
        jSplitPane2.getAccessibleContext().setAccessibleDescription("");

        jTable_sshlist.setAutoCreateRowSorter(true);
        jTable_sshlist.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {},
                {}
            },
            new String [] {

            }
        ));
        jTable_sshlist.setComponentPopupMenu(jPopupMenu_table);
        jScrollPane_sshlist.setViewportView(jTable_sshlist);

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 803, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane_sshlist, javax.swing.GroupLayout.DEFAULT_SIZE, 803, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 200, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addComponent(jScrollPane_sshlist, javax.swing.GroupLayout.DEFAULT_SIZE, 200, Short.MAX_VALUE))
        );

        jSplitPane1.setTopComponent(jPanel1);

        jMenu_file.setText("File");

        jMenu_file_new.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        jMenu_file_new.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/new.png"))); // NOI18N
        jMenu_file_new.setText("New");
        jMenu_file_new.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu_file_newActionPerformed(evt);
            }
        });
        jMenu_file.add(jMenu_file_new);

        jMenu_file_open.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        jMenu_file_open.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/open.png"))); // NOI18N
        jMenu_file_open.setText("Open");
        jMenu_file_open.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu_file_openActionPerformed(evt);
            }
        });
        jMenu_file.add(jMenu_file_open);

        jMenu_file_save.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        jMenu_file_save.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/save.png"))); // NOI18N
        jMenu_file_save.setText("Save");
        jMenu_file_save.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu_file_saveActionPerformed(evt);
            }
        });
        jMenu_file.add(jMenu_file_save);

        jMenu_file_saveas.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.ALT_MASK | java.awt.event.InputEvent.CTRL_MASK));
        jMenu_file_saveas.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/save.png"))); // NOI18N
        jMenu_file_saveas.setText("Save as..");
        jMenu_file_saveas.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu_file_saveasActionPerformed(evt);
            }
        });
        jMenu_file.add(jMenu_file_saveas);

        jMenu.add(jMenu_file);

        jMenu_help.setText("Help");

        jMenu_help_about.setIcon(new javax.swing.ImageIcon(getClass().getResource("/ICON/about.png"))); // NOI18N
        jMenu_help_about.setText("About");
        jMenu_help_about.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jMenu_help_aboutActionPerformed(evt);
            }
        });
        jMenu_help.add(jMenu_help_about);

        jMenu.add(jMenu_help);

        setJMenuBar(jMenu);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 479, Short.MAX_VALUE)
        );

        getAccessibleContext().setAccessibleDescription("");

        pack();
    }// </editor-fold>//GEN-END:initComponents


    private void jPopupMenu_log_copyActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenu_log_copyActionPerformed
        copyLog();
        insertLog(setTagtoText(processtext, "ProcessText", "Copied log!"));
    }//GEN-LAST:event_jPopupMenu_log_copyActionPerformed

    private void jPopupMenu_log_clearActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenu_log_clearActionPerformed
        if (JOptionPane.showConfirmDialog(this,
                "Are you sure?",
                "Clear log?",
                JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            clearLog();
        }
    }//GEN-LAST:event_jPopupMenu_log_clearActionPerformed

    private void jMenu_file_newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu_file_newActionPerformed
        if (globalsaveFile == null) {
            if ((int) jTableModel_sshlist.getValueAt(0, 0) != 0) {
                int Choose = JOptionPane.showConfirmDialog(this,
                        "Do you want to save before clearing all data?",
                        "Saving file?",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (Choose == JOptionPane.YES_OPTION) {
                    jMenu_file_saveActionPerformed(evt);
                } else if (Choose == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
        }
        clearSSHListData();
        clearLog();
        insertLog(welcometext);
        globalsaveFile = null;

    }//GEN-LAST:event_jMenu_file_newActionPerformed

    private void jPopupMenu_table_addActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenu_table_addActionPerformed
        File sshFile = OpenFileDialog();
        if (sshFile != null) {

            int count = SSHListStringIntoTable(ReadFile(sshFile));
            if (count != 0) {
                insertLog(setTagtoText(processtext, "ProcessText", "Added file: " + sshFile.getName()));
                insertLog(setTagtoText(processtext, "ProcessText", "Total: " + count));
            }
        }
    }//GEN-LAST:event_jPopupMenu_table_addActionPerformed

    private void jPopupMenu_table_deleteActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenu_table_deleteActionPerformed

        int[] rowSelected = jTable_sshlist.getSelectedRows();
        Arrays.sort(rowSelected);
        int index;
        for (int i = rowSelected.length - 1; i >= 0; i--) {
            index = (int) jTable_sshlist.getValueAt(rowSelected[i], 0);
            removeSSHListRow(index - 1);
        }
    }//GEN-LAST:event_jPopupMenu_table_deleteActionPerformed

    private void jMenu_file_openActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu_file_openActionPerformed
        if (globalsaveFile == null) {
            if ((int) jTableModel_sshlist.getValueAt(0, 0) != 0) {
                int Choose = JOptionPane.showConfirmDialog(this,
                        "Do you want to save before clearing all data?",
                        "Saving file?",
                        JOptionPane.YES_NO_CANCEL_OPTION);
                if (Choose == JOptionPane.YES_OPTION) {
                    jMenu_file_saveActionPerformed(evt);
                } else if (Choose == JOptionPane.CANCEL_OPTION) {
                    return;
                }
            }
        }
        File sshFile = OpenFileDialog();
        if (sshFile != null) {
            clearSSHListData();
            int count = SSHListStringIntoTable(ReadFile(sshFile));
            if (count != 0) {
                insertLog(setTagtoText(processtext, "ProcessText", "Added file: " + sshFile.getName()));
                insertLog(setTagtoText(processtext, "ProcessText", "Total: " + count));
            }
            globalsaveFile = sshFile;
        }
    }//GEN-LAST:event_jMenu_file_openActionPerformed

    private void jMenu_file_saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu_file_saveActionPerformed
        if (globalsaveFile == null) {
            globalsaveFile = SaveFileDialog();
        }
        if (globalsaveFile != null) {
            insertLog(setTagtoText(processtext, "ProcessText", "Saved: " + globalsaveFile));
            List<SSH> sshs = jTableModel_sshlist.getAllRow();
            SSHListArrayIntoFile(globalsaveFile, sshs);
        }
    }//GEN-LAST:event_jMenu_file_saveActionPerformed

    private void jMenu_file_saveasActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu_file_saveasActionPerformed
        if ((globalsaveFile = SaveFileDialog()) != null) {
            insertLog(setTagtoText(processtext, "ProcessText", "Saved: " + globalsaveFile));
            List<SSH> sshs = jTableModel_sshlist.getAllRow();
            SSHListArrayIntoFile(globalsaveFile, sshs);
        }
    }//GEN-LAST:event_jMenu_file_saveasActionPerformed

    private void jMenu_help_aboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jMenu_help_aboutActionPerformed
        JOptionPane.showMessageDialog(this,
                "Contact: cuchuoisoft@gmail.com",
                "About",
                JOptionPane.PLAIN_MESSAGE);
    }//GEN-LAST:event_jMenu_help_aboutActionPerformed


    private void jPopupMenu_table_checkActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jPopupMenu_table_checkActionPerformed
        
        check c = new check();
        c.start();
    }//GEN-LAST:event_jPopupMenu_table_checkActionPerformed
    class check extends Thread {
        
        @Override
        public void run() {
            //Setting for starting
            boolean isLiveC = (boolean) jTableModel_setting.getValueAt(LIVECHECKING_ROW, 1);
            boolean isFreshC = (boolean) jTableModel_setting.getValueAt(FRESHCHECKING_ROW, 1);
            boolean isBListC = (boolean) jTableModel_setting.getValueAt(BLACKLISTCHECKING_ROW, 1);
            
            String[] ID = new String[]{
                SSHCheckingFactory.LIVEID, 
                SSHCheckingFactory.FRESHID,
                SSHCheckingFactory.BLACKLISTID
            };
            boolean[] isCheck = new boolean[]{
                isLiveC, 
                isFreshC, 
                isBListC
            };
            int[] countThread = new int[]{
                (int) jTableModel_setting.getValueAt(LCHECKINGTHREADCOUNT_ROW, 1),
                (int) jTableModel_setting.getValueAt(FCHECKINGTHREADCOUNT_ROW, 1),
                (int) jTableModel_setting.getValueAt(BLCHECKINGTHREADCOUNT_ROW, 1)
            };
            int[] timeout = new int[]{
                (int) jTableModel_setting.getValueAt(LIVETIMEOUT_ROW, 1),
                (int) jTableModel_setting.getValueAt(FTIMEOUT_ROW, 1),
                (int) jTableModel_setting.getValueAt(BLTIMEOUT_ROW, 1)
            };
            //Now! start checking...
            List<SSH> listSSH = new ArrayList<SSH>(jTableModel_sshlist.getAllRow());
            for (int i = 0; i < ID.length; i++) {
                if (!(isCheck[i]&&SSHCheckingFactory.isAvaliable(ID[i]))) continue;
                
                //Add threads to ExecutorService
                ExecutorService pool = Executors.newFixedThreadPool(countThread[i]);
                ArrayList<Future> al = new ArrayList<Future>();
                for (SSH ssh : listSSH) {
                    SSHChecking check = SSHCheckingFactory.getSSHChecking(
                            ID[i],
                            ssh,
                            timeout[i],
                            jTableModel_sshlist
                    );
                    
                    al.add(pool.submit(check));
                }
                //waiting for threads to finish
                for (Future future : al) {
                    try {
                        if(future!=null) future.get();
                    } catch (InterruptedException | ExecutionException ex) {
                        Logger.getLogger(SSHCheckerView.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                //will remove all problem SSH when Livechecking has done
                if (ID[i].equals(SSHCheckingFactory.LIVEID)) {
                    List<SSH> clone = new ArrayList<SSH>();
                    for (SSH ssh : listSSH) {
                        if (!ssh.getStatus().equals("Working")) {
                            clone.add(ssh);
                        }
                    }
                    listSSH.removeAll(clone);
                }
            }
        }
    }

    private void SSHListArrayIntoFile(File file, List<SSH> sshs) {
        try (FileOutputStream out = new FileOutputStream(file)) {
            BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(out));
            for (SSH ssh : sshs) {
                bw.write(ssh.toString());
                bw.newLine();
                bw.flush();
            }

        } catch (FileNotFoundException ex) {
            insertLog(setTagtoText(errortext, "ErrorText",
                    SSHCheckerView.class.getName() + ": File not found !"));
        } catch (IOException ex) {
            insertLog(setTagtoText(errortext, "ErrorText",
                    SSHCheckerView.class.getName() + ": " + ex));
        }
    }

    private int SSHListStringIntoTable(String list) {
        Pattern pattern = Pattern.compile(
                "(\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}) ?\\| ?(\\w+) ?\\| ?(\\w+) ?\\|"
        );
        Matcher matcher = pattern.matcher(list);
        CountryResponse response = null;
        String countryCode = null;
        String countryName = null;
        String ip = null;
        String username = null, password = null;
        int count = 0;

        do {
            try {
                ip = matcher.group(1);
                username = matcher.group(2);
                password = matcher.group(3);
                response = dbreader.country(
                        InetAddress.getByName(ip));
                countryCode = response.getCountry().getIsoCode();
                countryName = response.getCountry().getName();
                count++;
                addSSHListRowData(new SSH(ip, username, password, countryName + "(" + countryCode + ")"));
            } catch (IllegalStateException | IOException | GeoIp2Exception ex) {

            }

        } while (matcher.find());
        return count;
    }

    private String ReadFile(File file) {

        StringBuilder sb = new StringBuilder();
        File f = file;
        try {
            FileReader in = new FileReader(f);
            int line;
            while ((line = in.read()) != -1) {
                sb.append((char) line);
            }

        } catch (FileNotFoundException ex) {
            insertLog(setTagtoText(errortext, "ErrorText",
                    SSHCheckerView.class.getName() + ": File not found !"));
        } catch (IOException ex) {
            insertLog(setTagtoText(errortext, "ErrorText",
                    SSHCheckerView.class.getName() + ": " + ex));
        }
        return sb.toString();
    }

    private File OpenFileDialog() {
        JFileChooser filechooser = new JFileChooser();
        filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        filechooser.setFileFilter(new FileNameExtensionFilter("SSH file text (.txt)", "txt"));
        if (filechooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            return filechooser.getSelectedFile().getAbsoluteFile();
        }
        return null;
    }

    private File SaveFileDialog() {
        JFileChooser filechooser = new JFileChooser();
        filechooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
        filechooser.setFileFilter(new FileNameExtensionFilter("SSH file text (.txt)", "txt"));
        if (filechooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            return filechooser.getSelectedFile().getAbsoluteFile();
        }
        return null;
    }

    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.windows.WindowsLookAndFeel");
        } catch (ClassNotFoundException |
                InstantiationException |
                IllegalAccessException |
                UnsupportedLookAndFeelException ex) {

            java.util.logging.Logger.getLogger(SSHCheckerView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        //</editor-fold>
        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new SSHCheckerView().setVisible(true);
            }
        });
    }
// <editor-fold defaultstate="collapsed" desc="global variable">   
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JEditorPane jEditorPane_log;
    private javax.swing.JMenuBar jMenu;
    private javax.swing.JMenu jMenu_file;
    private javax.swing.JMenuItem jMenu_file_new;
    private javax.swing.JMenuItem jMenu_file_open;
    private javax.swing.JMenuItem jMenu_file_save;
    private javax.swing.JMenuItem jMenu_file_saveas;
    private javax.swing.JMenu jMenu_help;
    private javax.swing.JMenuItem jMenu_help_about;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPopupMenu jPopupMenu_log;
    private javax.swing.JMenuItem jPopupMenu_log_clear;
    private javax.swing.JMenuItem jPopupMenu_log_copy;
    private javax.swing.JPopupMenu jPopupMenu_table;
    private javax.swing.JMenuItem jPopupMenu_table_add;
    private javax.swing.JMenuItem jPopupMenu_table_check;
    private javax.swing.JMenuItem jPopupMenu_table_delete;
    private javax.swing.JScrollPane jScrollPane_log;
    private javax.swing.JScrollPane jScrollPane_setting;
    private javax.swing.JScrollPane jScrollPane_sshlist;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTable_setting;
    private javax.swing.JTable jTable_sshlist;
    // End of variables declaration//GEN-END:variables

    private SSHsTableModel jTableModel_sshlist;
    private DefaultTableModel jTableModel_setting;
    private HTMLDocument htmldoc = null;
    private HTMLEditorKit htmleditorKit = null;
    private final String defaultlogtext
            = "<p><strong><span style=\"color:#0000CD;\">[CHECKING&nbsp;SUCCESSFUL]</span></strong></p>\n"
            + "<p><strong>Path :</strong> <em><span style=\"color:#808080;\">[Path]s</span></em></p>\n"
            + "<p>[n_Working] <span style=\"color:#008000;\"><strong>Working</strong></span> | "
            + "[n_Error] <strong><span style=\"color:#FF0000;\">Error</span></strong> | "
            + "[n_Blacklist] <strong><span style=\"color:#D3D3D3;\">Blacklist</span></strong></p>\n"
            + "<p><strong>Total SSH</strong> : [n_TotalSSH]</p>\n"
            + "<p><strong>Total time</strong> : [t_TotalTime] seconds</p>\n"
            + "<p>--------------------------------------</p>\n";
    private final String countrytext
            = "<p><strong>[CountryName]</strong> : <span style=\"color:#FF8C00;\">[n_SSH]</span></p>\n";
    private final String welcometext
            = "<p><strong><span style=\"font-size:18px;\"><span style=\"color:#0000CD;\">"
            + "Hi, thank you for using the tool.</span></span></strong></p>\n"
            + "<p><strong><span style=\"color:#008000;\">Author</span></strong> : <span style=\"color:#FF8C00;\">Củ Chuối</span></p>\n";
    private final String errortext
            = "<p><span style=\"color:#FF0000;\">[ErrorText]</span></p>\n";
    private final String processtext
            = "<p><font color=\"#696969\">[ProcessText]</font></p\n>";
    private DatabaseReader dbreader = null;
    private File ipDB = new File(getClass().getResource("/Database/GeoLite2-Country.mmdb").getPath());
    private File globalsaveFile = null;
    private SSHCheckerPreferences prefers = new SSHCheckerPreferences();

    private final int LIVETIMEOUT_ROW           = 0;
    private final int RECHECKINGWHEN_ROW        = 1;
    private final int RECHECKINGCOUNT_ROW       = 2;
    private final int BLACKLISTCHECKING_ROW     = 3;
    private final int LIVECHECKING_ROW          = 4;
    private final int FRESHCHECKING_ROW         = 5;
    private final int BLCHECKINGTHREADCOUNT_ROW = 6;
    private final int LCHECKINGTHREADCOUNT_ROW  = 7;
    private final int FCHECKINGTHREADCOUNT_ROW  = 8;
    private final int BLTIMEOUT_ROW             = 0;
    private final int FTIMEOUT_ROW              = 0;
    
    //</editor-fold>
}
