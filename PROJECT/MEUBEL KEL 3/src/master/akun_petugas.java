/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package master;
import com.toedter.calendar.JDateChooser;
import koneksi.koneksi;
import login.login_petugas;
import java.awt.HeadlessException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
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
public class akun_petugas extends javax.swing.JFrame {
    DefaultTableModel table = new DefaultTableModel();
    
    /**
     * Creates new form formAddBarang
     */
    public akun_petugas() {
        initComponents();
        
        Date now = new Date();  
        tgl_daftar.setDate(now); 
        
        
        koneksi conn = new koneksi();
        koneksi.getKoneksi();
        
        table_user.setModel(table);
        table.addColumn("ID");
        table.addColumn("Nama Petugas");
        table.addColumn("Email");
        table.addColumn("Alamat");
        table.addColumn("Username");
        table.addColumn("Password");
        table.addColumn("Tanggal Pendaftaran");
        
        
        tampilData();
        
    }
    private void tampilData(){
        //untuk mengahapus baris setelah input
        int row = table_user.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
        String query = "SELECT * FROM `tb_datapetugas` ";
        
        try{
            Connection connect = koneksi.getKoneksi();//memanggil koneksi
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
            
            while (rslt.next()){
                //menampung data sementara
                   
                    String id= rslt.getString("id_petugas");
                    String nama = rslt.getString("nama_petugas");
                    String email = rslt.getString("email");
                    String alamat = rslt.getString("alamat");
                    String username = rslt.getString("username");
                    String password = rslt.getString("password");
                    String tanggal = rslt.getString("tanggal_pendaftaran");
                    
                //masukan semua data kedalam array
                String[] data = {id,nama,email,alamat,username,password,tanggal};
                //menambahakan baris sesuai dengan data yang tersimpan diarray
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
        txt_email.setText(null);
        txt_alamat.setText(null);
        txt_username.setText(null);
        txt_password.setText(null);
        tgl_daftar.setDate(null);
        
    }
    private void tambahData(){
//        String kode = txt_kodebarang.getText();
        // String id_petugas = txt_petugas.getText();
        String nama = txt_nama.getText();
        String email = txt_email.getText();
        String alamat = txt_alamat.getText();
        String username = txt_username.getText();
        String password = txt_password.getText();
        
            // Memastikan username dan password tidak sama
        if (username.equals(password)) {
            JOptionPane.showMessageDialog(null, "Username dan password tidak boleh sama");
            return; // Menghentikan eksekusi selanjutnya
        } 
        // Panggil koneksi
        Connection connect = koneksi.getKoneksi();
        String queryCheckUsername = "SELECT COUNT(*) FROM tb_datapetugas WHERE username = ?";
        try {
            PreparedStatement psCheckUsername = connect.prepareStatement(queryCheckUsername);
            psCheckUsername.setString(1, username);
            ResultSet rsCheckUsername = psCheckUsername.executeQuery();
            if (rsCheckUsername.next()) {
                int count = rsCheckUsername.getInt(1);
                if (count > 0) {
                    JOptionPane.showMessageDialog(null, "Username sudah digunakan");
                    return; // Menghentikan eksekusi selanjutnya
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        
        // Mendapatkan tanggal terpilih dari JDateChooser
        Date tanggalTerpilih = tgl_daftar.getDate();


        if (tanggalTerpilih == null) {
            JOptionPane.showMessageDialog(null, "Tanggal belum dipilih!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = dateFormat.format(tanggalTerpilih);


        int lastIdPetugas = 0;
        String queryLastId = "SELECT MAX(id_petugas) FROM tb_datapetugas";
                try {
                    
                    Statement sttmnt = connect.createStatement();//membuat statement
                    ResultSet resultSet = sttmnt.executeQuery(queryLastId);
                 if (resultSet.next()) {
                    lastIdPetugas = resultSet.getInt(1);
                }
                } catch (SQLException e) {
                        e.printStackTrace();
            }
        // Menetapkan nilai id_petugas berikutnya
        int nextIdPetugas = lastIdPetugas + 1;
        //panggil koneksi
       
        //query untuk memasukan data
        String query = "INSERT INTO `tb_datapetugas` (`id_petugas`, `nama_petugas`, `email`,`alamat`, `username`, `password`, `tanggal_pendaftaran`) "
                     + "VALUES ('"+nextIdPetugas+"', '"+nama+"', '"+email+"','"+alamat+"', '"+username+"', '"+password+"','"+tanggal+"')";
        
        try{
            //menyiapkan statement untuk di eksekusi
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.executeUpdate(query);
            JOptionPane.showMessageDialog(null,"Data Berhasil Disimpan");
            
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Data Gagal Disimpan");
            // Memastikan username dan password tidak sama dengan nama
       
        }finally{
            tampilData();
            clear();
            
        }
    }
    
    private void hapusData(){
        //ambill data no pendaftaran
        // Ambil data nomor pendaftaran (kode barang)
int i = table_user.getSelectedRow();
        String id = table.getValueAt(i, 0).toString();

        Connection connect = koneksi.getKoneksi();

        try {
        // Hapus data dengan kode barang tertentu
        String deleteQuery = "DELETE FROM `tb_datapetugas` WHERE `tb_datapetugas`.`id_petugas` = ?";
        PreparedStatement deletePs = connect.prepareStatement(deleteQuery);
        deletePs.setString(1, id);
        deletePs.execute();

        // Query untuk mengambil data dengan id_petugas lebih besar dari yang dihapus
        String selectQuery = "SELECT id_petugas FROM `tb_datapetugas` WHERE `tb_datapetugas`.`id_petugas` > ?";
        PreparedStatement selectPs = connect.prepareStatement(selectQuery);
        selectPs.setString(1, id);
        ResultSet rs = selectPs.executeQuery();

        // Mengatur ulang nomor pendaftaran dengan mengurangi 1 dari id_petugas yang lebih besar
        String updateQuery = "UPDATE `tb_datapetugas` SET `id_petugas` = ? WHERE `tb_datapetugas`.`id_petugas` = ?";
        PreparedStatement updatePs = connect.prepareStatement(updateQuery);
        while (rs.next()) {
        int currentId = rs.getInt("id_petugas");
        int newId = currentId - 1;
        updatePs.setInt(1, newId);
        updatePs.setInt(2, currentId);
        updatePs.executeUpdate();
        }

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
        String email = txt_email.getText();
        String alamat = txt_alamat.getText();
        String username = txt_username.getText();
        String password = txt_password.getText();
        
        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = date.format(tgl_daftar.getDate());
        
        Connection connect = koneksi.getKoneksi();
        
        String query = "UPDATE `tb_datapetugas` SET `nama_petugas` = '"+nama+"', `email` = '"+email+"', `alamat` = '"+alamat+"', `tanggal_pendaftaran` = '"+tanggal+"', "
                + "`username` = '"+username+"', `password` = '"+password+"' "
                + "WHERE `tb_datapetugas`.`id_petugas` = '"+id+"';";

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
        jPanel2 = new javax.swing.JPanel();
        jButton5 = new javax.swing.JButton();
        jButton4 = new javax.swing.JButton();
        jLabel9 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_email = new javax.swing.JTextField();
        jLabel12 = new javax.swing.JLabel();
        txt_username = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jButton1 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jScrollPane3 = new javax.swing.JScrollPane();
        table_user = new javax.swing.JTable();
        jScrollPane4 = new javax.swing.JScrollPane();
        txt_alamat = new javax.swing.JTextArea();
        jLabel13 = new javax.swing.JLabel();
        tgl_daftar = new com.toedter.calendar.JDateChooser();
        jLabel8 = new javax.swing.JLabel();
        txt_nama = new javax.swing.JTextField();
        txt_password = new javax.swing.JTextField();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(950, 600));
        getContentPane().setLayout(new org.netbeans.lib.awtextra.AbsoluteLayout());

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
        jPanel2.add(jButton5, new org.netbeans.lib.awtextra.AbsoluteConstraints(510, 420, 120, 40));

        jButton4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_delete_30px.png"))); // NOI18N
        jButton4.setText("  DELETE");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton4, new org.netbeans.lib.awtextra.AbsoluteConstraints(650, 420, 130, 40));

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Nama");
        jPanel2.add(jLabel9, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 110, 110, -1));

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Email");
        jPanel2.add(jLabel11, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 190, -1, -1));

        txt_email.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_email.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_emailActionPerformed(evt);
            }
        });
        jPanel2.add(txt_email, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 220, 265, 40));

        jLabel12.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setText("Username");
        jPanel2.add(jLabel12, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 110, 80, -1));

        txt_username.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_username.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_usernameActionPerformed(evt);
            }
        });
        jPanel2.add(txt_username, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 140, 260, 40));

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Password");
        jPanel2.add(jLabel7, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 180, 80, -1));

        jButton1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_add_30px.png"))); // NOI18N
        jButton1.setText("  ADD");
        jButton1.setBorderPainted(false);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton1, new org.netbeans.lib.awtextra.AbsoluteConstraints(360, 420, 130, 40));

        jButton3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_rewind_30px.png"))); // NOI18N
        jButton3.setText("  BACK");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        jPanel2.add(jButton3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 600, -1, -1));

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

        jPanel2.add(jScrollPane3, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 490, 860, 91));

        txt_alamat.setColumns(20);
        txt_alamat.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_alamat.setRows(5);
        jScrollPane4.setViewportView(txt_alamat);

        jPanel2.add(jScrollPane4, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 310, 265, 70));

        jLabel13.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setText("Alamat");
        jPanel2.add(jLabel13, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 270, -1, -1));

        tgl_daftar.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jPanel2.add(tgl_daftar, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 420, 265, 40));

        jLabel8.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setText("Tanggal Pendaftaran");
        jPanel2.add(jLabel8, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 390, 170, -1));

        txt_nama.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_nama.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namaActionPerformed(evt);
            }
        });
        jPanel2.add(txt_nama, new org.netbeans.lib.awtextra.AbsoluteConstraints(50, 140, 265, 40));

        txt_password.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_password.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_passwordActionPerformed(evt);
            }
        });
        jPanel2.add(txt_password, new org.netbeans.lib.awtextra.AbsoluteConstraints(590, 210, 260, 40));

        jPanel4.setBackground(new java.awt.Color(192, 192, 192));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("DATA PETUGAS");
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

        jPanel2.add(jPanel4, new org.netbeans.lib.awtextra.AbsoluteConstraints(270, 20, -1, -1));

        jLabel3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/daftarBarang/img/icons8_customs_120px.png"))); // NOI18N
        jPanel2.add(jLabel3, new org.netbeans.lib.awtextra.AbsoluteConstraints(710, 270, 140, 130));

        getContentPane().add(jPanel2, new org.netbeans.lib.awtextra.AbsoluteConstraints(0, 0, 950, 710));

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
        
        String email = table.getValueAt(baris, 2).toString();
        txt_email.setText(email);
        
        String alamat = table.getValueAt(baris, 3).toString();
        txt_alamat.setText(alamat);
        
        String username = table.getValueAt(baris, 4).toString();
        txt_username.setText(username);
        
        String password = table.getValueAt(baris, 5).toString();
        txt_password.setText(password);
        
        String tanggal = table.getValueAt(baris, 6).toString();

        Date convert = null;
        try{
            convert = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
        }catch(Exception e){
            System.out.println(e);
        }
        tgl_daftar.setDate(convert);
             
    }//GEN-LAST:event_table_userMouseClicked

    private void txt_passwordActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_passwordActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_passwordActionPerformed

    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        // TODO add your handling code here:
        new menu_admin().setVisible(true);
        dispose();
    }//GEN-LAST:event_jButton3ActionPerformed

    private void txt_emailActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_emailActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_emailActionPerformed

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        editData();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txt_usernameActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_usernameActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_usernameActionPerformed

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
            java.util.logging.Logger.getLogger(akun_petugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(akun_petugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(akun_petugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(akun_petugas.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                new akun_petugas().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton3;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTable jTable1;
    private javax.swing.JTable jTable2;
    private javax.swing.JTable table_user;
    private com.toedter.calendar.JDateChooser tgl_daftar;
    private javax.swing.JTextArea txt_alamat;
    private javax.swing.JTextField txt_email;
    private javax.swing.JTextField txt_nama;
    private javax.swing.JTextField txt_password;
    private javax.swing.JTextField txt_username;
    // End of variables declaration//GEN-END:variables

    private JDateChooser setDateFormatString(String string) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
