/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package myftp2;

import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.DefaultListModel;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

/**
 *
 * @author Kenny
 */
public class FTPView extends javax.swing.JFrame {

    /**
     * Creates new form FTPView
     */
    private ArrayList<FileNode> data;
    private ArrayList<FileNode> ftpFolder;
    private static String traverse="";
    private static String traverse2="";
    private String activeDir;
    private  String hn,un,pwd;
    public FTPView() throws IOException {
        initComponents();
        setTitle("MyFTP V0.01");
        this.setLocationRelativeTo(null);
        ArrayList<String> driveLetter = new LocalFile().getDriveLetter();
        for(int i=0;i<driveLetter.size();i++){
            this.jComboBox1.addItem(driveLetter.get(i));
        }
        
        activeDir = this.jComboBox1.getSelectedItem().toString();
         data = new LocalFile().getLocalFiles(activeDir);
        this.jTextField1.setText(activeDir); 
        String localFile[] = new String[data.size()];
        for(int i=0;i<data.size();i++){
            localFile[i] = data.get(i).getName();
        }
        initFTPClient("/");
        DefaultListModel listModel;
        listModel = new DefaultListModel();
        for(int i=0;i<localFile.length;i++){
            listModel.addElement(localFile[i]);
        }
        this.jList1.setModel(listModel);
        
        this.jComboBox1.addItemListener(new ItemListener(){
            @Override
            public void itemStateChanged(ItemEvent e) {
                JComboBox cb = (JComboBox)e.getSource();
                Object  item= e.getItem();
                
                if(e.getStateChange() == ItemEvent.SELECTED){
                    jTextField1.setText(cb.getSelectedItem().toString());
                    
                    activeDir = cb.getSelectedItem().toString();
                      data = new LocalFile().getLocalFiles(activeDir);
        String localFile[] = new String[data.size()];
        for(int i=0;i<data.size();i++){
            localFile[i] = data.get(i).getName();
        }
        DefaultListModel listModel = new DefaultListModel();
        for(int i=0;i<localFile.length;i++){
            listModel.addElement(localFile[i]);
        }
        jList1.setModel(listModel);
                    System.out.println(cb.getSelectedItem().toString());
                }
                //throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
            
            }
        
        });
        //this.jList1 = new JList(localFile);
        //this.jList1.setModel(new DefaultListModel());
        this.jList1.addMouseListener(new MouseAdapter(){
            public void mouseClicked(MouseEvent evt) {
                //String traverse=jComboBox1.getSelectedItem().toString()+"";
               
        JList list = (JList)evt.getSource();
        if (evt.getClickCount() == 2) {

            // Double-click detected
            int index = list.locationToIndex(evt.getPoint());
            traverse=jTextField1.getText();
            traverse+="\\";
            traverse+=jList1.getSelectedValue();
            //System.out.println(jList1.getSelectedValue());   
            System.out.println(traverse);
            jTextField1.setText(traverse);
            data = new LocalFile().getLocalFiles(traverse);
            DefaultListModel listModel = new DefaultListModel();
            for(int i=0;i<data.size();i++){
                listModel.addElement(data.get(i).getName());
            }
            jList1.setModel(listModel);
            
    }
        
            }});
        jList2.addMouseListener(new MouseAdapter(){
        
        public void mouseClicked(MouseEvent evt) {
            JList list = (JList)evt.getSource();
            if(evt.getClickCount()==2){
                int index = list.locationToIndex(evt.getPoint());
               // System.out.println(ftpFolder.get(index).getName());
                if(ftpFolder.get(index).getType().equals("dir")){
                    DefaultListModel listModel;
        listModel = new DefaultListModel();
                    try {
                        //data = new LocalFile().getLocalFiles(traverse);
                        traverse2=jTextField2.getText();
                        if(!traverse2.equals("/")){
                        traverse2+="/"+ftpFolder.get(index).getName();
                        }
                        else{
                            traverse2+=ftpFolder.get(index).getName();
                        }
                        FTPController ftpc = new FTPController(hn,un,pwd);
                        
                    // ftpc.doConnect();
                        System.out.println(traverse2);
                        
                        ftpFolder = ftpc.getListFiles2(traverse2);
                        
                        jTextField2.setText(ftpc.getWorkingDir());
                        for(int i=0;i<ftpFolder.size();i++){
            listModel.addElement(ftpFolder.get(i).getName());
        }
        jList2.setModel(listModel);
                    } catch (IOException ex) {
                        Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
                    }
        
                }
            }
        }
        });
    }
    
    public  void initList1(String path){
        
    }
    
    public void initList2(String path){
        DefaultListModel listModel = new DefaultListModel();
        
    }
    
    public void initFTPClient(String path) throws IOException{
       String hostNameValue = null,userNameValue = null,passwordValue = null;
       JLabel jHostName = new JLabel("Host Name");
        JTextField hostName = new JTextField();
        JLabel jUserName = new JLabel("Username");
        JTextField userName = new JTextField();
        JLabel jPassword = new JLabel("Password");
        JTextField password = new JPasswordField(30);
        Object[] ob = {jHostName,hostName, jUserName, userName, jPassword,password};
        int result = JOptionPane.showConfirmDialog(null, ob, "FTP Connect Details", JOptionPane.OK_CANCEL_OPTION);
 
        if (result == JOptionPane.OK_OPTION) {
             hostNameValue = hostName.getText();
             userNameValue = userName.getText();
             passwordValue = password.getText();
             this.hn = hostNameValue;
             this.un = userNameValue;
             this.pwd = passwordValue;
            //Here is some validation code
        }
                FTPController x = new FTPController(hostNameValue,userNameValue,passwordValue);
        x.doConnect();
        ArrayList<FileNode> y = x.getListFiles(path);
        System.out.println("Besar "+y.size());
        ftpFolder = y;
        jTextField2.setText(x.getCurrentDir());
//        for(int i=0;i<y.size();i++){
//            System.out.println(y.get(i).getName()+" "+y.get(i).getType());
//        }
        DefaultListModel listModel;
        listModel = new DefaultListModel();
        for(int i=0;i<y.size();i++){
            listModel.addElement(y.get(i).getName());
        }
        this.jList2.setModel(listModel);
        //x.berhenti();
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        jComboBox1 = new javax.swing.JComboBox<>();
        jScrollPane2 = new javax.swing.JScrollPane();
        jScrollPane1 = new javax.swing.JScrollPane();
        jList1 = new javax.swing.JList<>();
        jTextField1 = new javax.swing.JTextField();
        jButton7 = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jPanel2 = new javax.swing.JPanel();
        jComboBox2 = new javax.swing.JComboBox<>();
        jScrollPane4 = new javax.swing.JScrollPane();
        jScrollPane3 = new javax.swing.JScrollPane();
        jList2 = new javax.swing.JList<>();
        jTextField2 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jButton5 = new javax.swing.JButton();
        jButton8 = new javax.swing.JButton();
        jButton9 = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel1.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane1.setViewportView(jList1);

        jScrollPane2.setViewportView(jScrollPane1);

        jButton7.setText("back");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jButton7)
                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                        .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                        .addComponent(jComboBox1, 0, 157, Short.MAX_VALUE)
                        .addComponent(jTextField1)))
                .addContainerGap(21, Short.MAX_VALUE))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jTextField1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(3, 3, 3)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 112, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jButton7)
                .addContainerGap(51, Short.MAX_VALUE))
        );

        jButton1.setText(">>");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        jButton2.setText("<<");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });

        jPanel2.setBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)));

        jScrollPane3.setViewportView(jList2);

        jScrollPane4.setViewportView(jScrollPane3);

        jButton6.setText("back");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jTextField2)
                    .addComponent(jComboBox2, 0, 175, Short.MAX_VALUE)
                    .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 0, Short.MAX_VALUE)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jButton6)))
                .addContainerGap())
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jComboBox2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(8, 8, 8)
                .addComponent(jTextField2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 114, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(31, 31, 31)
                .addComponent(jButton6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jButton3.setText("create Dir ++");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });

        jButton4.setText("delete dir --");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        jButton5.setText("delete file");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        jButton8.setText("Rename Folder");
        jButton8.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton8ActionPerformed(evt);
            }
        });

        jButton9.setText("Rename File");
        jButton9.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton9ActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(41, 41, 41)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jButton1)
                            .addComponent(jButton2)))
                    .addGroup(layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addComponent(jButton3))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                        .addGap(18, 18, 18)
                        .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(layout.createSequentialGroup()
                                .addGap(10, 10, 10)
                                .addComponent(jButton9))
                            .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                .addComponent(jButton4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 24, Short.MAX_VALUE)
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(110, 110, 110)
                .addComponent(jButton2)
                .addGap(18, 18, 18)
                .addComponent(jButton1)
                .addGap(18, 18, 18)
                .addComponent(jButton3)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton8)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jButton9)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, layout.createSequentialGroup()
                .addGroup(layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(layout.createSequentialGroup()
                        .addGap(33, 33, 33)
                        .addComponent(jPanel2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(layout.createSequentialGroup()
                        .addContainerGap(37, Short.MAX_VALUE)
                        .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(59, 59, 59))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        //System.out.println(this.jList1.getSelectedValue().toString());
        String fullPath = this.jTextField1.getText()+"\\";
        //if(this.jTextField1.getText().length() < 1){
            fullPath+=this.jList1.getSelectedValue();
        
System.out.println(fullPath);
       FTPController t = new FTPController(hn,un,pwd);
        try {
            //t.doConnect();
           boolean isSuccess = t.uploadFile(fullPath, jList1.getSelectedValue(),jTextField2.getText());
           if(isSuccess){
              t.berhenti();
               String workingDir = this.jTextField2.getText();
               this.ftpFolder = t.getListFiles2(workingDir);
               if(ftpFolder==null){
                   System.out.println("yes");
               }
               DefaultListModel listModel = new DefaultListModel();
               for(int i=0;i<ftpFolder.size();i++){
                   
                   listModel.addElement(ftpFolder.get(i).getName());
               }
               this.jList2.setModel(listModel);
           }
        } catch (IOException ex) {
            Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
        }
       
    }//GEN-LAST:event_jButton1ActionPerformed

    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        // TODO add your handling code here:
        FTPController ftpc = new FTPController(this.hn,this.un,this.pwd);
        //System.out.println(this.jTextField1.toString());
        String workingDir = "";
        String chosenFile = "";
        String targetDir = "";
        String targetFName = "";
        String result="";
        if(this.jTextField1.getText().equals("")){
                workingDir = this.jTextField2.getText();
                chosenFile = this.jList2.getSelectedValue();
                targetDir = this.jTextField1.getText();
                //System.out.println(workingDir +" "+chosenFile);
                result+=workingDir+"/"+chosenFile;
            try {
                ftpc.downloadFile(workingDir, chosenFile, targetDir, chosenFile);
            } catch (IOException ex) {
                Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            workingDir = this.jTextField2.getText();
            chosenFile = this.jList2.getSelectedValue();
            targetDir = this.jTextField1.getText();
            targetFName = chosenFile;
            
            try {
               boolean isSuccess = ftpc.downloadFile(workingDir, chosenFile, targetDir, targetFName);
               
               if(isSuccess){
                   ftpc.berhenti();
                   workingDir = this.jTextField1.getText();
                   data = new LocalFile().getLocalFiles(workingDir);
                   DefaultListModel listModel = new DefaultListModel();
                   for(int i=0;i<data.size();i++){
                       listModel.addElement(data.get(i).getName());
                   }
                   jList1.setModel(listModel);
               }
                
            } catch (IOException ex) {
                Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        }
       
    }//GEN-LAST:event_jButton2ActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
       
        String dirName = JOptionPane.showInputDialog(null, "Direcotry Name ?");
        //System.out.println(dirName);
        FTPController ftp = new FTPController(this.hn,this.un,this.pwd);
        try {
            boolean isSuccess = ftp.createDirectory(this.jTextField2.getText(), dirName);
            if(isSuccess){
                ftp.berhenti();
                String workingDir = this.jTextField2.getText();
                this.ftpFolder = ftp.getListFiles2(workingDir);
                DefaultListModel listModel = new DefaultListModel();
                for(int i=0;i<ftpFolder.size();i++){
                    listModel.addElement(ftpFolder.get(i).getName());
                    
                }
                this.jList2.setModel(listModel);
            }
        } catch (IOException ex) {
            Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        String workingDir = this.jTextField2.getText();
        String dirName = this.jList2.getSelectedValue();
        FTPController ftpc = new FTPController(this.hn,this.un,this.pwd);
        try {
           boolean isSuccess =  ftpc.deleteDirectory(workingDir, dirName);
           if(isSuccess){
               ftpc.berhenti();
                workingDir = this.jTextField2.getText();
                this.ftpFolder = ftpc.getListFiles2(workingDir);
               if(ftpFolder==null){
                   System.out.println("yes");
               }
               DefaultListModel listModel = new DefaultListModel();
               for(int i=0;i<ftpFolder.size();i++){
                   
                   listModel.addElement(ftpFolder.get(i).getName());
               }
               this.jList2.setModel(listModel);

               
           }
        } catch (IOException ex) {
            Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton4ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        String workingDir = this.jTextField2.getText();
        String fileName = this.jList2.getSelectedValue();
        FTPController ftpc = new FTPController(this.hn,this.un,this.pwd);
        try {
           boolean isSuccess = ftpc.deleteFile(workingDir, fileName);
           if(isSuccess){
               workingDir = this.jTextField2.getText();
               this.ftpFolder = ftpc.getListFiles2(workingDir);
               DefaultListModel listModel = new DefaultListModel();
               for(int i=0;i<ftpFolder.size();i++){
                   
                   listModel.addElement(ftpFolder.get(i).getName());
               }
               this.jList2.setModel(listModel);
           }
        } catch (IOException ex) {
            Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton5ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        String currentPath = this.jTextField2.getText();
        if(currentPath.equals("/")){
            this.jTextField2.setText("/");
            JOptionPane.showMessageDialog(null, "Sdh, jangan balik lagi", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else{
        StringProc stp = new StringProc(currentPath);
        String prevPath = stp.getPrevPathftp();
        if(prevPath.length()<1){
            prevPath = "/";
        }
        FTPController ftpc = new FTPController(this.hn,this.un,this.pwd);
        try {
            ftpFolder = ftpc.getListFiles2(prevPath);
            DefaultListModel listModel = new DefaultListModel();
            for(int i=0;i<ftpFolder.size();i++){
                listModel.addElement(ftpFolder.get(i).getName());
            }
            jList2.setModel(listModel);
            this.jTextField2.setText(prevPath);
            //ftpc.berhenti();
        } catch (IOException ex) {
            Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        System.out.println(prevPath);
        }
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        String currentPath = this.jTextField1.getText();
        if(currentPath.length()<1){
           currentPath = jComboBox1.getSelectedItem().toString();
           JOptionPane.showMessageDialog(null, "Sdh, jangan balik lagi", "Error", JOptionPane.ERROR_MESSAGE);
        }
        else{
            StringProc stp = new StringProc(currentPath);
            String prevPath = stp.getPrevLocalFile();
            if(prevPath.length()<1){
                prevPath = jComboBox1.getSelectedItem().toString();
            }
            data = new LocalFile().getLocalFiles(prevPath);
            DefaultListModel listModel = new DefaultListModel();
            for(int i=0;i<data.size();i++){
                listModel.addElement(data.get(i).getName());
            }
            jList1.setModel(listModel);
            this.jTextField1.setText(prevPath);
            System.out.println(prevPath);
        }
    }//GEN-LAST:event_jButton7ActionPerformed

    private void jButton8ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton8ActionPerformed
        // TODO add your handling code here:
        String currentPath = this.jTextField2.getText();
        System.out.println(currentPath);
        //System.out.println(jList2.getSelectedValue());
        String dir = jList2.getSelectedValue();
        //String hasil = currentPath+dir;
        String newDir = JOptionPane.showInputDialog(null, "New Directory Name");
        String hasil = "";
        if(currentPath.length()<2){
            hasil+=currentPath+dir;
        }
        else{
            hasil+=currentPath+"/"+dir;
            
        }
        String oldDir = hasil;
        StringProc sp = new StringProc(hasil);
        hasil = sp.getPrevPathftp();
        if(!hasil.equals("/")){
            hasil+="/"+newDir;
        }
        System.out.println(oldDir+" "+hasil);
        FTPController ftpc = new FTPController(this.hn,this.un,this.pwd);
        try {
            //ftpc.renameFolder(newDir, newDir)
//       String tet = x.getPrevPathftp();
//       newDir+=tet+"/"+newDir;
//        System.out.println(newDir);
boolean isSuccess = ftpc.renameFile(oldDir, hasil);

if(isSuccess){
    ftpc.berhenti();
    ftpFolder = ftpc.getListFiles2(this.jTextField2.getText());
    DefaultListModel listModel = new DefaultListModel();
    for(int i=0;i<ftpFolder.size();i++){
        listModel.addElement(ftpFolder.get(i).getName());
    }
    jList2.setModel(listModel);
}


//
//FTPController ftpc = new FTPController(this.hn,this.un,this.pwd);
//try {
//ftpc.renameFolder(currentPath, newDir);
//} catch (IOException ex) {
//Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
//}
        } catch (IOException ex) {
            Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }//GEN-LAST:event_jButton8ActionPerformed

    private void jButton9ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton9ActionPerformed
        // TODO add your handling code here:
        String currentPath = this.jTextField2.getText();
        System.out.println(currentPath);
        //System.out.println(jList2.getSelectedValue());
        String dir = jList2.getSelectedValue();
        //String hasil = currentPath+dir;
        String newDir = JOptionPane.showInputDialog(null, "New Directory Name");
        String hasil = "";
        if(currentPath.length()<2){
            hasil+=currentPath+dir;
        }
        else{
            hasil+=currentPath+"/"+dir;
            
        }
        String oldDir = hasil;
        StringProc sp = new StringProc(hasil);
        hasil = sp.getPrevPathftp();
        if(!hasil.equals("/")){
            hasil+="/"+newDir;
        }
        System.out.println(oldDir+" "+hasil);
        FTPController ftpc = new FTPController(this.hn,this.un,this.pwd);
        try {
            //ftpc.renameFolder(newDir, newDir)
//       String tet = x.getPrevPathftp();
//       newDir+=tet+"/"+newDir;
//        System.out.println(newDir);
boolean isSuccess = ftpc.renameFile(oldDir, hasil);
if(isSuccess){
    ftpc.berhenti();
    ftpFolder = ftpc.getListFiles2(this.jTextField2.getText());
    DefaultListModel listModel = new DefaultListModel();
    for(int i=0;i<ftpFolder.size();i++){
        listModel.addElement(ftpFolder.get(i).getName());
    }
    jList2.setModel(listModel);
}
        } catch (IOException ex) {
            Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_jButton9ActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FTPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FTPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FTPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FTPView.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                try {
                    new FTPView().setVisible(true);
                } catch (IOException ex) {
                    Logger.getLogger(FTPView.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JButton jButton8;
    private javax.swing.JButton jButton9;
    private javax.swing.JComboBox<String> jComboBox1;
    private javax.swing.JComboBox<String> jComboBox2;
    private javax.swing.JList<String> jList1;
    private javax.swing.JList<String> jList2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTextField jTextField1;
    private javax.swing.JTextField jTextField2;
    // End of variables declaration//GEN-END:variables
}
