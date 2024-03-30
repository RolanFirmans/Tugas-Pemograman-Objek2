
package transaksi;

import java.awt.HeadlessException;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;
import static javax.management.remote.JMXConnectorFactory.connect;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import koneksi.koneksi;
import master.pelanggan;
import static transaksi.Transaksi.txt_kembalian;
import static transaksi.Transaksi.txt_totalharga2;
import static transaksi.Transaksi.txt_uang;

/**
 *
 * @author Rolan Firmansyah
 */
public class Transaksi extends javax.swing.JFrame {
 DefaultTableModel table = new DefaultTableModel();
  


    /**
     * Creates new form formTransaksi
     */
    public Transaksi() {
        initComponents();
        koneksi.getKoneksi();
        totalBiaya();
        tanggal();
        
        tb_keranjang.setModel(table);
        table.addColumn("ID");
        table.addColumn("Nama Barang");
        table.addColumn("Nama Pelanggan");
        table.addColumn("Harga");
        table.addColumn("Jumlah");
        table.addColumn("Total Harga");
        
        tampilData();
    }
   //tanggal
    public void tanggal(){
        Date now = new Date();  
        txt_tanggal.setDate(now);    
    }
    
     private void tampilData(){
        //untuk mengahapus baris setelah input
        int row = tb_keranjang.getRowCount();
        for(int a = 0 ; a < row ; a++){
            table.removeRow(0);
        }
        
         String query = "SELECT *, (harga * jumlah_barang) AS total_harga FROM `transaksi`";

        
       try{
            Connection connect = koneksi.getKoneksi();//memanggil koneksi
            Statement sttmnt = connect.createStatement();//membuat statement
            ResultSet rslt = sttmnt.executeQuery(query);//menjalanakn query
           
            while (rslt.next()){
               Locale locale = new Locale("id", "ID");
               DecimalFormatSymbols symbols = new DecimalFormatSymbols(locale);
               symbols.setGroupingSeparator('.');
               DecimalFormat decimalFormat = new DecimalFormat("Rp #,###", symbols);
               String formattedValue = decimalFormat.format(rslt.getInt(6));
               
                
                //menampung data sementara
                    String kode = rslt.getString("id_transaksi");
                    String nama = rslt.getString("nama_barang");
                    String pelanggan = rslt.getString("nama_pelanggan");
                    String harga = rslt.getString("harga");
                    String jumlah = rslt.getString("jumlah_barang");
                    String total = rslt.getString("total_harga");
                    
                Object[] ob = new Object[6];
                    ob[0] = rslt.getString(2);
                    ob[1] = rslt.getString(4);
                    ob[2] = rslt.getString(5);
                    ob[3] = decimalFormat.format(rslt.getInt(6));
                    //String biaya = 
                    ob[4] = rslt.getString(3);
                    ob[5] = decimalFormat.format(rslt.getInt(8));
                   
                    
                   // ob[5] = biaya;
                
                    
                 // Menambahakan ke dalama array
                 String [] data = {kode,nama,pelanggan,harga,jumlah,total};
                
                
                //menambahakan baris sesuai dengan data yang tersimpan diarray
                table.addRow(ob);
            }
                //mengeset nilai yang ditampung agar muncul di table
                tb_keranjang.setModel(table);
          
        }catch(Exception e){
            System.out.println(e);
        }
       
    }
    private void clear(){
        
        txt_kodebarang2.setText(null);
        txt_namabarang2.setText(null);
        txt_harga2.setText(null);
        txt_jumlah2.setText(null);
        txt_totalharga.setText(null);
        txt_pelanggan.setText(null);
    }
    
    private void keranjang(){
        
        String kode = txt_kodebarang2.getText();
        String pelanggan = txt_pelanggan.getText();
        String nama = txt_namabarang2.getText();
        String harga = txt_harga2.getText();
        String jumlah = txt_jumlah2.getText();
        String total = txt_totalharga.getText();
        //String pelanggan = txt_namapelanggan.getText();
         // Mendapatkan tanggal terpilih dari JDateChooser
        Date tanggalTerpilih = txt_tanggal.getDate();

    
        if (tanggalTerpilih == null) {
            JOptionPane.showMessageDialog(null, "Tanggal belum dipilih!", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }
    
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String tanggal = dateFormat.format(tanggalTerpilih);
        
        
        try{
            
        // Query untuk mendapatkan ID terakhir
        // panggil koneksi
        Connection connect = koneksi.getKoneksi();
        String queryGetLastId = "SELECT MAX(id_transaksi) FROM transaksi";
        Statement statement = connect.createStatement();
        ResultSet rs = statement.executeQuery(queryGetLastId);
       
        int lastId = 0;
            if (rs.next()) {
                lastId = rs.getInt(1);
        }
        int newId = lastId + 1;
        
        //query untuk memasukan data
        String query = "INSERT INTO `transaksi` (`tgl_transaksi`, `id_transaksi`, `kode_barang` ,`nama_barang`,`nama_pelanggan` ,`harga`, `jumlah_barang`, `total_harga`) "
                + "VALUES ('"+tanggal+"','"+newId+"', '"+kode+"' ,'"+nama+"', '"+pelanggan+"' ,'"+harga+"', '"+jumlah+"', '"+total+"')";
        
       
            //menyiapkan statement untuk di eksekusi
            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
            ps.executeUpdate(query);
            JOptionPane.showMessageDialog(null,"Data Masuk Ke-Keranjang");
            
        }catch(SQLException | HeadlessException e){
            System.out.println(e);
            JOptionPane.showMessageDialog(null,"Data Gagal Disimpan");
            
        }finally{
            tampilData();
            clear();
            
        }
        totalBiaya();
    
    }
    
    
    private void hapusData(){
        int i = tb_keranjang.getSelectedRow();
        String kode = table.getValueAt(i, 0).toString();
        Connection connect = koneksi.getKoneksi();

        String deleteQuery = "DELETE FROM `transaksi` WHERE `transaksi`.`id_transaksi` = '"+kode+"' ";
        String resetQuery = "UPDATE `transaksi` SET `id_transaksi` = `id_transaksi` - 1 WHERE `id_transaksi` > '"+kode+"' ";

        try {
            PreparedStatement deletePS = connect.prepareStatement(deleteQuery);
            deletePS.execute();

            PreparedStatement resetPS = connect.prepareStatement(resetQuery);
            resetPS.executeUpdate();

            tampilData();
            clear();
            totalBiaya();
        } catch (SQLException | HeadlessException e) {
            System.out.println(e);
            JOptionPane.showMessageDialog(null, "Data Gagal Dihapus");
        }

    }
    
//       private void editData(){
//        int i = tb_keranjang.getSelectedRow();
//        
//        String kode = table.getValueAt(i, 0).toString();
//        String nama = txt_pelanggan.getText();
//        String jumlah = txt_jumlah2.getText();
////        String stok = txt_stok.getText();
//        
////        SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
////        String tanggal = String.valueOf(date.format(txt_tanggal.getDate()));
//        
//        Connection connect = koneksi.getKoneksi();
//        
//        String query = "UPDATE `transaksi` SET `nama_pelanggan` = '"+nama+"', `jumlah_barang` = '"+jumlah+"' "
//                + "WHERE `transaksi`.`id_transaksi` = '"+kode+"';";
//
//        try{
//            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(query);
//            ps.executeUpdate(query);
//            JOptionPane.showMessageDialog(null , "Data Update");
//        }catch(SQLException | HeadlessException e){
//            System.out.println(e);
//            JOptionPane.showMessageDialog(null, "Gagal Update");
//        }finally{
//            tampilData();
//            clear();
//        }
//    }
       
    private void totalBiaya(){
        
        try {
        Connection connect = koneksi.getKoneksi(); // Memanggil koneksi
        String query = "SELECT SUM(jumlah_barang * harga) FROM transaksi"; // Query untuk menghitung total harga
        Statement statement = connect.createStatement();
        ResultSet result = statement.executeQuery(query);

        if (result.next()) {
            int totalBiaya = result.getInt(1);
            txt_totalharga2.setText(String.valueOf(totalBiaya));
        }
        } catch (SQLException e) {
        System.out.println(e);
    }

}

    
    private void total(){
        String harga = txt_harga2.getText();
        String jumlah = txt_jumlah2.getText();
        
        int hargaa = Integer.parseInt(harga);
        try{
            int jumlahh = Integer.parseInt(jumlah);
            int total = hargaa * jumlahh;
            String total_harga = Integer.toString(total);
            txt_totalharga.setText(total_harga);
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Only Number");
            txt_jumlah2.setText(null);
        }
    }
    
    private void reset(){
        txt_uang.setText(null);
    }
    
    private void kembalian(){
        String total = txt_totalharga2.getText();
        String uang = txt_uang.getText();
        
        int totals = Integer.parseInt(total);
        try{
            int uangs = Integer.parseInt(uang);     
            int kembali = (uangs - totals);
            String fix = Integer.toString(kembali);
            txt_kembalian.setText(fix);
            JOptionPane.showMessageDialog(null, "Transaksi Berhasil!");
        }catch(NumberFormatException | HeadlessException e){
            JOptionPane.showMessageDialog(null, "Invalid Payment");
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

        jPanel2 = new javax.swing.JPanel();
        txt_jumlah2 = new javax.swing.JTextField();
        txt_namabarang2 = new javax.swing.JTextField();
        txt_totalharga = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        btn_barang = new javax.swing.JButton();
        txt_harga2 = new javax.swing.JTextField();
        jPanel3 = new javax.swing.JPanel();
        txt_uang = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        txt_kembalian = new javax.swing.JTextField();
        jButton4 = new javax.swing.JButton();
        jScrollPane1 = new javax.swing.JScrollPane();
        tb_keranjang = new javax.swing.JTable();
        jButton5 = new javax.swing.JButton();
        txt_totalharga2 = new javax.swing.JTextField();
        jButton6 = new javax.swing.JButton();
        jButton7 = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        txt_tanggal = new com.toedter.calendar.JDateChooser();
        txt_kodebarang2 = new javax.swing.JTextField();
        jLabel7 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txt_pelanggan = new javax.swing.JTextField();
        jLabel9 = new javax.swing.JLabel();
        jLabel10 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);

        jPanel2.setBackground(new java.awt.Color(139, 203, 152));

        txt_jumlah2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_jumlah2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_jumlah2ActionPerformed(evt);
            }
        });
        txt_jumlah2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_jumlah2KeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_jumlah2KeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_jumlah2KeyTyped(evt);
            }
        });

        txt_namabarang2.setEditable(false);
        txt_namabarang2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_namabarang2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_namabarang2ActionPerformed(evt);
            }
        });

        txt_totalharga.setEditable(false);
        txt_totalharga.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_totalharga.setAutoscrolls(false);
        txt_totalharga.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txt_totalhargaMouseReleased(evt);
            }
        });
        txt_totalharga.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalhargaActionPerformed(evt);
            }
        });

        jButton1.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_Magnetic_Card_30px.png"))); // NOI18N
        jButton1.setText("BAYAR");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });

        btn_barang.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        btn_barang.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_search_30px_1.png"))); // NOI18N
        btn_barang.setToolTipText("");
        btn_barang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btn_barangMouseClicked(evt);
            }
        });
        btn_barang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btn_barangActionPerformed(evt);
            }
        });

        txt_harga2.setEditable(false);
        txt_harga2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_harga2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_harga2ActionPerformed(evt);
            }
        });
        txt_harga2.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_harga2KeyReleased(evt);
            }
        });

        jPanel3.setBackground(new java.awt.Color(51, 51, 255));

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 0, Short.MAX_VALUE)
        );

        txt_uang.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_uang.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_uang.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_uangActionPerformed(evt);
            }
        });
        txt_uang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_uangKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_uangKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_uangKeyTyped(evt);
            }
        });

        jLabel3.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setText("Nama Barang");

        jLabel4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setText("Harga");

        jLabel5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setText("Jumlah");

        jLabel11.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel11.setForeground(new java.awt.Color(255, 255, 255));
        jLabel11.setText("Total Harga");

        txt_kembalian.setEditable(false);
        txt_kembalian.setFont(new java.awt.Font("Dialog", 1, 24)); // NOI18N
        txt_kembalian.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_kembalian.setEnabled(false);
        txt_kembalian.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kembalianActionPerformed(evt);
            }
        });

        jButton4.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_rewind_30px.png"))); // NOI18N
        jButton4.setText("  BACK");
        jButton4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton4ActionPerformed(evt);
            }
        });

        tb_keranjang.setFont(new java.awt.Font("Times New Roman", 0, 14)); // NOI18N
        tb_keranjang.setModel(new javax.swing.table.DefaultTableModel(
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
        tb_keranjang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                tb_keranjangMouseClicked(evt);
            }
        });
        jScrollPane1.setViewportView(tb_keranjang);

        jButton5.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_shopping_cart_30px_2.png"))); // NOI18N
        jButton5.setText("  ADD");
        jButton5.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton5ActionPerformed(evt);
            }
        });

        txt_totalharga2.setEditable(false);
        txt_totalharga2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_totalharga2.setHorizontalAlignment(javax.swing.JTextField.CENTER);
        txt_totalharga2.setEnabled(false);
        txt_totalharga2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                txt_totalharga2MouseReleased(evt);
            }
        });
        txt_totalharga2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_totalharga2ActionPerformed(evt);
            }
        });

        jButton6.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_delete_30px.png"))); // NOI18N
        jButton6.setText("  DELETE");
        jButton6.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton6ActionPerformed(evt);
            }
        });

        jButton7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jButton7.setIcon(new javax.swing.ImageIcon(getClass().getResource("/meubel/img/icons8_reset_30px.png"))); // NOI18N
        jButton7.setText("  RESET");
        jButton7.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton7ActionPerformed(evt);
            }
        });

        jPanel4.setBackground(new java.awt.Color(192, 192, 192));

        jLabel1.setFont(new java.awt.Font("Tw Cen MT", 1, 18)); // NOI18N
        jLabel1.setForeground(new java.awt.Color(255, 255, 255));
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("TRANSAKSI");
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

        jLabel2.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel2.setForeground(new java.awt.Color(255, 255, 255));
        jLabel2.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel2.setText("KEMBALIAN");

        txt_tanggal.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N

        txt_kodebarang2.setEditable(false);
        txt_kodebarang2.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_kodebarang2.setHorizontalAlignment(javax.swing.JTextField.LEFT);
        txt_kodebarang2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_kodebarang2ActionPerformed(evt);
            }
        });

        jLabel7.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setText("Kode Barang");

        jLabel8.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel8.setForeground(new java.awt.Color(255, 255, 255));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText("KERANJANG");

        txt_pelanggan.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        txt_pelanggan.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                txt_pelangganActionPerformed(evt);
            }
        });
        txt_pelanggan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txt_pelangganKeyPressed(evt);
            }
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txt_pelangganKeyReleased(evt);
            }
            public void keyTyped(java.awt.event.KeyEvent evt) {
                txt_pelangganKeyTyped(evt);
            }
        });

        jLabel9.setFont(new java.awt.Font("Dialog", 1, 14)); // NOI18N
        jLabel9.setForeground(new java.awt.Color(255, 255, 255));
        jLabel9.setText("Nama Pelanggan");

        jLabel10.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel10.setForeground(new java.awt.Color(255, 255, 255));
        jLabel10.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel10.setText("Rp.");

        jLabel12.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel12.setForeground(new java.awt.Color(255, 255, 255));
        jLabel12.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel12.setText("Rp.");

        jLabel13.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel13.setForeground(new java.awt.Color(255, 255, 255));
        jLabel13.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel13.setText("Rp.");

        jLabel14.setFont(new java.awt.Font("Tw Cen MT", 1, 24)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(255, 255, 255));
        jLabel14.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel14.setText("Rp.");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap(24, Short.MAX_VALUE)
                .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 50, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 283, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addComponent(txt_kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 313, javax.swing.GroupLayout.PREFERRED_SIZE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(0, 0, Short.MAX_VALUE)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(jLabel10, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(jLabel13, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.PREFERRED_SIZE, 107, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                    .addComponent(txt_uang, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addComponent(txt_totalharga2, javax.swing.GroupLayout.PREFERRED_SIZE, 314, javax.swing.GroupLayout.PREFERRED_SIZE))))
                                        .addGap(92, 92, 92))
                                    .addGroup(jPanel2Layout.createSequentialGroup()
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addComponent(jLabel7)
                                            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addComponent(txt_kodebarang2, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(18, 18, 18)
                                                    .addComponent(btn_barang, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                    .addComponent(txt_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, 180, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                .addGroup(jPanel2Layout.createSequentialGroup()
                                                    .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                                        .addComponent(jLabel11)
                                                        .addComponent(txt_jumlah2, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel5)
                                                        .addComponent(txt_harga2, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                        .addComponent(jLabel4)
                                                        .addComponent(txt_namabarang2, javax.swing.GroupLayout.PREFERRED_SIZE, 282, javax.swing.GroupLayout.PREFERRED_SIZE))
                                                    .addGap(226, 226, 226)
                                                    .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 194, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                    .addGap(109, 109, 109))))
                                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 37, Short.MAX_VALUE))
                                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                        .addGap(1, 1, 1)
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(txt_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 281, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(0, 0, Short.MAX_VALUE))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addComponent(jLabel9)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                                .addComponent(jLabel6)))
                                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 293, Short.MAX_VALUE)
                                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addGap(38, 38, 38))
                                            .addGroup(jPanel2Layout.createSequentialGroup()
                                                .addGap(47, 47, 47)
                                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 482, javax.swing.GroupLayout.PREFERRED_SIZE)
                                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)))))
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 125, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jLabel3)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jButton6)))
                        .addGap(50, 50, 50))
                    .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 235, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(297, 297, 297))))
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(164, 164, 164)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(jButton4)
                    .addComponent(jButton5, javax.swing.GroupLayout.PREFERRED_SIZE, 111, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addGap(13, 13, 13)
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(44, 44, 44)
                .addComponent(jLabel7)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txt_kodebarang2, javax.swing.GroupLayout.PREFERRED_SIZE, 42, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel9)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 8, Short.MAX_VALUE)
                        .addComponent(txt_pelanggan, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(8, 8, 8)
                        .addComponent(jLabel3)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_namabarang2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_harga2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(jLabel5)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(txt_jumlah2, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(jLabel11)
                        .addGap(53, 53, 53))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addGap(57, 57, 57)
                                .addComponent(jLabel6)
                                .addGap(34, 34, 34))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                                .addComponent(txt_tanggal, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addComponent(jLabel8, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)))
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addGap(18, 18, 18)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_totalharga2, javax.swing.GroupLayout.PREFERRED_SIZE, 46, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel10, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_uang, javax.swing.GroupLayout.PREFERRED_SIZE, 49, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel13, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton1)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 23, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txt_kembalian, javax.swing.GroupLayout.PREFERRED_SIZE, 33, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(txt_totalharga, javax.swing.GroupLayout.PREFERRED_SIZE, 40, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel12, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel14, javax.swing.GroupLayout.PREFERRED_SIZE, 38, javax.swing.GroupLayout.PREFERRED_SIZE)))
                            .addGroup(jPanel2Layout.createSequentialGroup()
                                .addComponent(jButton6, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jButton7, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(btn_barang)
                        .addGap(0, 0, Short.MAX_VALUE)))
                .addComponent(jButton5)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton4)
                .addGap(70, 70, 70))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );

        pack();
        setLocationRelativeTo(null);
    }// </editor-fold>//GEN-END:initComponents

    private void txt_jumlah2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_jumlah2ActionPerformed
        // TODO add your handling code here:
         totalBiaya();
    }//GEN-LAST:event_txt_jumlah2ActionPerformed

    private void txt_jumlah2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jumlah2KeyReleased
        // TODO add your handling code here:
        total();
    }//GEN-LAST:event_txt_jumlah2KeyReleased

    private void txt_jumlah2KeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jumlah2KeyTyped
        // TODO add your handling code here:
      
    }//GEN-LAST:event_txt_jumlah2KeyTyped

    private void txt_namabarang2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_namabarang2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_namabarang2ActionPerformed

    private void txt_totalhargaMouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_totalhargaMouseReleased
        // TODO add your handling code here:
//        total();
    }//GEN-LAST:event_txt_totalhargaMouseReleased

    private void txt_totalhargaActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalhargaActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalhargaActionPerformed

    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        // TODO add your handling code here:
        kembalian();
        //        tambahData();
        //        JOptionPane.showMessageDialog(null, "Transaksi Berhasil !");
        //        new struk.struk().setVisible(true);
    }//GEN-LAST:event_jButton1ActionPerformed

    private void btn_barangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btn_barangMouseClicked
    
    }//GEN-LAST:event_btn_barangMouseClicked

    private void btn_barangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btn_barangActionPerformed
        stok_barang m = new stok_barang();
        m.setVisible(true);
        this.setVisible(false);
    }//GEN-LAST:event_btn_barangActionPerformed

    private void txt_harga2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_harga2ActionPerformed
        // TODO add your handling code here:
        // Menghapus tanda titik dari string harga

    }//GEN-LAST:event_txt_harga2ActionPerformed

    private void txt_uangActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_uangActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_uangActionPerformed

    private void txt_uangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_uangKeyPressed
        // TODO add your handling code here:

    }//GEN-LAST:event_txt_uangKeyPressed

    private void txt_uangKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_uangKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_uangKeyReleased

    private void txt_uangKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_uangKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_uangKeyTyped

    private void txt_kembalianActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kembalianActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_kembalianActionPerformed

    private void jButton4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton4ActionPerformed
        // TODO add your handling code here:
        new user.menu_user().setVisible(true);
        dispose();

    }//GEN-LAST:event_jButton4ActionPerformed

    private void tb_keranjangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tb_keranjangMouseClicked
        // TODO add your handling code here:
        
        // TODO add your handling code here:
        
        int baris = tb_keranjang.getSelectedRow();

        String nama = table.getValueAt(baris,2).toString();
        txt_pelanggan.setText(nama);

        String harga = table.getValueAt(baris, 4).toString();
        txt_jumlah2.setText(harga);

//        String stok = table.getValueAt(baris, 3).toString();
//        txt_stok.setText(stok);

//        String tanggal = table.getValueAt(baris, 4).toString();
//
//        Date convert = null;
//        try{
//            convert = new SimpleDateFormat("yyyy-MM-dd").parse(tanggal);
//        }catch(Exception e){
//            System.out.println(e);
//        }
//        txt_tanggal.setDate(convert);
    }//GEN-LAST:event_tb_keranjangMouseClicked

    private void jButton5ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton5ActionPerformed
        // TODO add your handling code here:
        keranjang();
    }//GEN-LAST:event_jButton5ActionPerformed

    private void txt_totalharga2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txt_totalharga2MouseReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalharga2MouseReleased

    private void txt_totalharga2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_totalharga2ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_totalharga2ActionPerformed

    private void jButton6ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton6ActionPerformed
        // TODO add your handling code here:
        hapusData();
        txt_uang.setText(null);
        txt_kembalian.setText(null);
    }//GEN-LAST:event_jButton6ActionPerformed

    private void jButton7ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton7ActionPerformed
        // TODO add your handling code here:
        txt_uang.setText("");
        txt_kembalian.setText("");
//        try{
//            String clear = "TRUNCATE `tb_keranjang`";
//            Connection connect = koneksi.getKoneksi();
//            PreparedStatement ps = (PreparedStatement) connect.prepareStatement(clear);
//            ps.execute();
//            //keranjang();
//
//        }catch(Exception e){
//            System.out.println(e);
//        }finally{
//            tampilData();
//            totalBiaya();
//            hapusData();
//            txt_uang.setText(null);
//            txt_kembalian.setText(null);
//        }

    }//GEN-LAST:event_jButton7ActionPerformed

    private void txt_kodebarang2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_kodebarang2ActionPerformed
                // TODO add your handling code here:
    }//GEN-LAST:event_txt_kodebarang2ActionPerformed

    private void txt_pelangganActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_txt_pelangganActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pelangganActionPerformed

    private void txt_pelangganKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pelangganKeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pelangganKeyReleased

    private void txt_pelangganKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pelangganKeyTyped
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pelangganKeyTyped

    private void txt_pelangganKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_pelangganKeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_pelangganKeyPressed

    private void txt_jumlah2KeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_jumlah2KeyPressed
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_jumlah2KeyPressed

    private void txt_harga2KeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txt_harga2KeyReleased
        // TODO add your handling code here:
    }//GEN-LAST:event_txt_harga2KeyReleased

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
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(Transaksi.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new Transaksi().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btn_barang;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton4;
    private javax.swing.JButton jButton5;
    private javax.swing.JButton jButton6;
    private javax.swing.JButton jButton7;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTable tb_keranjang;
    public javax.swing.JTextField txt_harga2;
    public javax.swing.JTextField txt_jumlah2;
    public static javax.swing.JTextField txt_kembalian;
    public javax.swing.JTextField txt_kodebarang2;
    public javax.swing.JTextField txt_namabarang2;
    public javax.swing.JTextField txt_pelanggan;
    private com.toedter.calendar.JDateChooser txt_tanggal;
    public javax.swing.JTextField txt_totalharga;
    public static javax.swing.JTextField txt_totalharga2;
    public static javax.swing.JTextField txt_uang;
    // End of variables declaration//GEN-END:variables

    
}
