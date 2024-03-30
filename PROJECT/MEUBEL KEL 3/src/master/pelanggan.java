/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master;
import com.toedter.calendar.JDateChooser;
import koneksi.koneksi;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import static javax.management.remote.JMXConnectorFactory.connect;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;

/**
 *
 * @author Hadi Firmansyah
 */
public class pelanggan extends javax.swing.JFrame {
    DefaultTableModel table = new DefaultTableModel();
    
    /**
     * Creates new form formAddBarang
     */
    public pelanggan() {
        initComponents();
        
        Date now = new Date();  
        //tgl_daftar.setDate(now); 
        
        
        koneksi conn = new koneksi();
        koneksi.getKoneksi();
        
        table_user.setModel(table);
        table.addColumn("ID");
        table.addColumn("Nama Pelanggan");
        table.addColumn("Kelamin");
        table.addColumn("Alamat");
        table.addColumn("Telepon");

        tampilData();
        
    }
    private void tampilData(){
        //untuk mengahapus baris setelah input
        int row = table_user.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String query = "SELECT * FROM `tb_pelanggan` ";
        
        try{
            Connection connect = koneksi.getKoneksi();//memanggil koneksi
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                    String id= rslt.getString("id_pelanggan");
                    String nama = rslt.getString("nama_pelanggan");
                    String kelamin = rslt.getString("jenis_kelamin");
                    String alamat = rslt.getString("alamat");
                    String telepon = rslt.getString("telepon");
             String[] data = {id, nama, kelamin, alamat, telepon};
             table.addRow(data);
            }
                //mengeset nilai yang ditampung agar muncul di table
                table_user.setModel(table);
            
        }catch(Exception e){
            System.out.println(e);
        }
       
    }
    private void clear(){
//        txt_kodebarang.setText(null);
         //txt_id_petugas.setText(null);
        txt_nama.setText(null);
        rb1.setText(null);
        rb2.setText(null);
        txt_alamat.setText(null);
        txt_telepon.setText(null);
        //txt_password.setText(null);
        //tgl_daftar.setDate(null);
        
    }
    private void tambahData(){
//        String kode = txt_kodebarang.getText();
        // String id_petugas = txt_petugas.getText();
        String nama = txt_nama.getText();
//        String kelamin = txt_kelamin.getText();
        String alamat = txt_alamat.getText();
//        String telepon = txt_telepon.getText();

        // panggil koneksi
        Connection connect = koneksi.getKoneksi();
        String telepon = txt_telepon.getText();
        String regex = "^(\\+\\d{1,3}[- ]?)?\\d{10,}$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(telepon);
    
    if (!matcher.matches()) {
        JOptionPane.showMessageDialog(null, "Format nomor telepon tidak valid");
        return;
    }
        String kelamin = null;
        if (rb1.isSelected()){
            kelamin  = "Laki-Laki";
        }else if (rb2.isSelected()){
            kelamin = "Perempuan";
        }
        try {
        // Query untuk mendapatkan ID terakhir
        String queryGetLastId = "SELECT MAX(id_pelanggan) FROM tb_pelanggan";
        Statement statement = connect.createStatement();
        ResultSet rs = statement.executeQuery(queryGetLastId);
        
        int lastId = 0;
            if (rs.next()) {
                lastId = rs.getInt(1);
        }

        // Membuat ID baru dengan increment dari ID terakhir
        int newId = lastId + 1;

        // Query untuk memasukkan data
        String query = "INSERT INTO `tb_pelanggan` (`id_pelanggan`, `nama_pelanggan`, `jenis_kelamin`, `alamat`, `telepon`) "
        + "VALUES ('" + newId + "', '" + nama + "', '" + kelamin + "', '" + alamat + "', '" + telepon + "')";

        // Menyiapkan statement untuk dieksekusi
        PreparedStatement ps = connect.prepareStatement(query);
        ps.executeUpdate(query);
        JOptionPane.showMessageDialog(null, "Data Berhasil Disimpan");

        } catch (SQLException | HeadlessException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Disimpan");

        } finally {
        tampilData();
        clear();
        }

    }
    private void hapusData(){
        //ambill data no pendaftaran
        int i = table_user.getSelectedRow();
        String id = table.getValueAt(i, 0).toString();

        Connection connect = koneksi.getKoneksi();
        String kelamin = null;
        if (rb1.isSelected()){
            kelamin  = "Laki-Laki";
        }else if (rb2.isSelected()){
            kelamin = "Perempuan";
        }
        try {
        // Query untuk menghapus data
        String deleteQuery = "DELETE FROM `tb_pelanggan` WHERE `tb_pelanggan`.`id_pelanggan` = " + id + " ";
        PreparedStatement deletePs = connect.prepareStatement(deleteQuery);
        deletePs.execute();

        // Query untuk mengatur ulang ID
        String updateQuery = "UPDATE `tb_pelanggan` SET `id_pelanggan` = (`id_pelanggan` - 1) WHERE `id_pelanggan` > " + id + "";
        PreparedStatement updatePs = connect.prepareStatement(updateQuery);
        updatePs.executeUpdate();

        JOptionPane.showMessageDialog(null, "Data Berhasil Dihapus");

        } catch (SQLException | HeadlessException e) {
        System.out.println(e);
        JOptionPane.showMessageDialog(null, "Data Gagal Dihapus");

        } finally {
        tampilData();
        clear();
        }

        
    }
    private void editData(){
        int i = table_user.getSelectedRow();
        
        String id = table.getValueAt(i, 0).toString();
        String nama = txt_nama.getText();
//        String kelamin = txt_kelamin.getText();
        String alamat = txt_alamat.getText();
        
        String telepon = txt_telepon.getText();
        
        
      
        Connection connect = koneksi.getKoneksi();
        String kelamin = null;
        if (rb1.isSelected()){
            kelamin  = "Laki-Laki";
        }else if (rb2.isSelected()){
            kelamin = "Perempuan";
        }
        
        String query = "UPDATE `tb_pelanggan` SET `nama_pelanggan` = '"+nama+"',`jenis_kelamin` = '"+kelamin+"', `alamat` = '"+alamat+"', `telepon` = '"+telepon+"' "
               + "WHERE `tb_pelanggan`.`id_pelanggan` = '"+id+"';";


        try{
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.executeUpdate(query);
            JOptionPane.showMessageDialog(null , "Data Update");
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Gagal Update");
        }finally{
            tampilData();
            clear();
        }
    }

    
    
  
    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jScrollPane2 = new javax.swing.JScrollPane();
        jTable2 = new javax.swing.JTable();
        jPanel3 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        buttonGroup1 = new javax.swing.ButtonGroup();
        jPanel1 = new javax.swing.JPanel();
        jPanel2 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_user = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        txt_alamat = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        txt_nama = new javax.swing.JTextField();
        txt_telepon = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jButton10 = new javax.swing.JButton();
        rb2 = new javax.swing.JRadioButton();
        rb1 = new javax.swing.JRadioButton();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        jTable2.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane2.setViewportView(jTable2);

        jPanel3.setBackground(new java.awt.Color(204, 204, 255));

        jLabel2.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("DAFTAR BARANG");
        jLabel2.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(89, 89, 89)
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(111, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 100, Short.MAX_VALUE)
        );

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(950, 600));

        jPanel2.setBackground(new java.awt.Color(139, 203, 152));
        jPanel2.setMinimumSize(new java.awt.Dimension(1000, 611));
        jPanel2.setPreferredSize(new java.awt.Dimension(1000, 611));
        jPanel2.setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

        jButton5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_edit_30px.png"))); // NOI18N
        jButton5.setText("  EDIT");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(550, 430, 120, 40));

        jButton4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_delete_30px.png"))); // NOI18N
        jButton4.setText("  DELETE");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(690, 430, -1, 40));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Nama");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 110, -1));

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Jenis Kelamin");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, -1, -1));

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Telepon");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 400, 80, -1));

        jButton1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_add_30px.png"))); // NOI18N
        jButton1.setText("  ADD");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(400, 430, 120, 40));

        jButton3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_rewind_30px.png"))); // NOI18N
        jButton3.setText("  BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 430, -1, 40));

        table_user.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        table_user.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        table_user.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                table_userMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(table_user);

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 490, 890, 91));

        txt_alamat.setColumns(20);
        txt_alamat.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_alamat.setRows(5);
        jScrollPane4.setViewportView(txt_alamat);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 265, 70));

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Alamat");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, -1, -1));

        txt_nama.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namaActionPerformed(evt);
            }
        });
        jPanel2.add(txt_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 265, 40));

        txt_telepon.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_telepon.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_teleponActionPerformed(evt);
            }
        });
        jPanel2.add(txt_telepon, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 430, 270, 40));

        jPanel4.setBackground(new java.awt.Color(192, 192, 192));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DATA PELANGGAN");
        jLabel1.setBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED));

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addGap(97, 97, 97)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 260, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(103, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addGap(0, 0, Short.MAX_VALUE)
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 60, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(290, 20, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/—Pngtree—cartoon hand drawn push shopping_5044861 (1).png"))); // NOI18N
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(410, 110, 370, 280));

        jButton10.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton10.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_business_report_30px.png"))); // NOI18N
        jButton10.setText("LAPORAN");
        jButton10.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jButton10MouseClicked(evt);
            }
        });
        jButton10.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton10ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton10, new org.netbeans.lib.awtextra.AbsoluteConstraints(830, 380, 140, -1));

        rb2.setText("Perempuan");
        jPanel2.add(rb2, new org.netbeans.lib.awtextra.AbsoluteConstraints(190, 230, -1, -1));

        rb1.setText("Laki-Laki");
        jPanel2.add(rb1, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 230, -1, -1));

        getContentPane().add(jPanel2, java.awt.BorderLayout.CENTER);

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
         tambahData();
    }//GEN-LAST:event_jButton1ActionPerformed

    private void txt_namaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_namaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_namaActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
       // TODO add your handling code here:
       hapusData();
    }//GEN-LAST:event_jButton4ActionPerformed

    private void table_userMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_table_userMouseClicked
        int baris = table_user.getSelectedRow();
        
        String nama = table.getValueAt(baris,1).toString();
        txt_nama.setText(nama);
        
//        String kelamin = table.getValueAt(baris, 2).toString();
//        rb1.setText(kelamin);
   
        String alamat = table.getValueAt(baris, 3).toString();
        txt_alamat.setText(alamat);
        
        String telepon = table.getValueAt(baris, 4).toString();
        txt_telepon.setText(telepon);
       
    }//GEN-LAST:event_table_userMouseClicked

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        new menu_admin().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        editData();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txt_teleponActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_teleponActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_teleponActionPerformed

    private void jButton10MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jButton10MouseClicked
        // TODO add your handling code here
        transaksi.Data_Pelanggan bk = new transaksi.Data_Pelanggan();
        bk.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_jButton10MouseClicked

    private void jButton10ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton10ActionPerformed
//        // TODO add your handling code here:
//        transaksi.data_pelanggan bk = new transaksi.data_pelanggan();
//        bk.setVisible(true);
//        this.setVisible(false);
    }//GEN-LAST:event_jButton10ActionPerformed

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
            java.util.logging.Logger.getLogger(pelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(pelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(pelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(pelanggan.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new pelanggan().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.ButtonGroup buttonGroup1;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton10;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JRadioButton rb1;
    private javax.swing.JRadioButton rb2;
    private javax.swing.JTable table_user;
    private javax.swing.JTextArea txt_alamat;
    private javax.swing.JTextField txt_nama;
    private javax.swing.JTextField txt_telepon;
    // End of variables declaration//GEN-END:variables

    private JDateChooser setDateFormatString(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
