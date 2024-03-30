-- phpMyAdmin SQL Dump
-- version 5.2.0
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Waktu pembuatan: 08 Jul 2023 pada 09.19
-- Versi server: 10.4.27-MariaDB
-- Versi PHP: 8.0.25

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `meubel`
--

-- --------------------------------------------------------

--
-- Struktur dari tabel `admin`
--

CREATE TABLE `admin` (
  `id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `admin`
--

INSERT INTO `admin` (`id`, `username`, `password`) VALUES
(1, 'user', '12345');

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang_keluar`
--

CREATE TABLE `barang_keluar` (
  `no_keluar` varchar(20) NOT NULL,
  `kode_barang` varchar(20) NOT NULL,
  `nama_barang` varchar(20) NOT NULL,
  `harga` int(20) NOT NULL,
  `jumlah` int(20) NOT NULL,
  `tgl_keluar` date NOT NULL,
  `total_keluar` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `barang_keluar`
--

INSERT INTO `barang_keluar` (`no_keluar`, `kode_barang`, `nama_barang`, `harga`, `jumlah`, `tgl_keluar`, `total_keluar`) VALUES
('1', '3', 'Meja Makan', 55000, 4, '2023-06-20', 110000),
('10', 'KD011', 'Rak Sepatu', 100000, 5, '2023-07-07', 500000),
('2', '2', 'meja', 20000, 2, '2023-07-01', 40000),
('3', 'KD0004', 'Meja', 100, 2, '2023-07-01', 200),
('4', 'KD005', 'Lemari', 20000, 2, '2023-07-01', 40000),
('5', 'KD006', 'Rak', 20000, 3, '2023-07-01', 60000),
('6', 'KD007', 'Meja Belajar', 180000, 1, '2023-07-28', 180000),
('7', 'KD008', 'Kasur', 100000, 2, '2023-07-07', 200000),
('8', 'KD009', 'Meja', 200000, 2, '2023-07-07', 400000),
('9', 'KD010', 'Rak Baju', 100000, 2, '2023-07-07', 200000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `barang_masuk`
--

CREATE TABLE `barang_masuk` (
  `no_masuk` int(20) NOT NULL,
  `tgl_masuk` date NOT NULL,
  `nama_distributor` char(20) NOT NULL,
  `kode_barang` varchar(20) NOT NULL,
  `nama_barang` varchar(20) NOT NULL,
  `harga` int(20) NOT NULL,
  `jumlah` int(20) NOT NULL,
  `total_harga` int(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `barang_masuk`
--

INSERT INTO `barang_masuk` (`no_masuk`, `tgl_masuk`, `nama_distributor`, `kode_barang`, `nama_barang`, `harga`, `jumlah`, `total_harga`) VALUES
(1, '2023-07-01', 'Rolan', 'KD001', 'Meja Makan', 20000, 2, 40000),
(2, '2023-07-01', 'Madya', 'KD002', 'Meja Belajar', 300000, 4, 1200000),
(3, '2023-07-01', 'Badar', 'KD003', 'Lemari', 200000, 2, 400000),
(4, '2023-07-01', 'Abil', 'KD004', 'Sofa', 200000, 2, 400000),
(5, '2023-07-26', 'Bayu', 'KD005', 'Rak Sepatu', 40000, 2, 80000),
(6, '2023-07-07', 'Bayu', 'KD006', 'Rak Baju', 1000000, 4, 4000000);

-- --------------------------------------------------------

--
-- Struktur dari tabel `distributor`
--

CREATE TABLE `distributor` (
  `id_distributor` char(10) NOT NULL,
  `nama_distributor` varchar(20) NOT NULL,
  `telepon` varchar(20) NOT NULL,
  `alamat` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `distributor`
--

INSERT INTO `distributor` (`id_distributor`, `nama_distributor`, `telepon`, `alamat`) VALUES
('1', 'Erwin', '989898989', 'Bandung'),
('2', 'Rolan', '082112345678', 'Jakart');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_databarang`
--

CREATE TABLE `tb_databarang` (
  `kode_barang` varchar(50) NOT NULL,
  `nama_barang` varchar(50) NOT NULL,
  `harga` int(20) NOT NULL,
  `stok` int(20) NOT NULL,
  `tanggal` date NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_databarang`
--

INSERT INTO `tb_databarang` (`kode_barang`, `nama_barang`, `harga`, `stok`, `tanggal`) VALUES
('KD001', 'Lemari', 200000, 2, '2023-07-06'),
('KD002', 'Rak Sepatu', 400000, 10, '2023-07-07'),
('KD003', 'Sofa', 500000, 2, '2023-07-06'),
('KD004', 'Lemari', 300000, 5, '2023-07-05'),
('KD005', 'Kasur', 100000, 10, '2023-07-21'),
('KD006', 'Rak Baju', 100000, 10, '2023-07-08');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_datapetugas`
--

CREATE TABLE `tb_datapetugas` (
  `id_petugas` int(11) NOT NULL,
  `nama_petugas` varchar(20) NOT NULL,
  `email` varchar(20) NOT NULL,
  `alamat` varchar(20) NOT NULL,
  `tanggal_pendaftaran` date NOT NULL,
  `username` varchar(30) NOT NULL,
  `password` varchar(30) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_datapetugas`
--

INSERT INTO `tb_datapetugas` (`id_petugas`, `nama_petugas`, `email`, `alamat`, `tanggal_pendaftaran`, `username`, `password`) VALUES
(1, 'Nagin', 'Nagin@Gmail.com', 'CibungBulang', '2023-06-19', 'Nagin', '12345'),
(2, 'Abil', 'Abil@gmail.com', 'Bandung', '2023-06-19', 'Abil', '12345'),
(3, 'Ilham', 'Ilham@gmail.com', 'Unjani', '2023-06-20', 'Ilham', '12345'),
(4, 'Rolan', 'Rolan@gmail.com', 'CBL', '2023-07-02', 'Rolan', '12345');

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_keranjang`
--

CREATE TABLE `tb_keranjang` (
  `id_transaksi` int(10) NOT NULL,
  `kode_barang` varchar(20) NOT NULL,
  `nama_barang` varchar(20) NOT NULL,
  `harga` int(20) NOT NULL,
  `jumlah` int(20) NOT NULL,
  `total_harga` int(30) NOT NULL,
  `tgl_transaksi` date NOT NULL,
  `id_pelanggan` int(10) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Trigger `tb_keranjang`
--
DELIMITER $$
CREATE TRIGGER `cancel` AFTER DELETE ON `tb_keranjang` FOR EACH ROW BEGIN
UPDATE tb_databarang SET
stok = stok + OLD.jumlah
WHERE kode_barang = OLD.kode_barang;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `cancel_2` AFTER DELETE ON `tb_keranjang` FOR EACH ROW BEGIN
DELETE FROM transaksi
WHERE kode_barang = OLD.kode_barang;
END
$$
DELIMITER ;
DELIMITER $$
CREATE TRIGGER `stok_habis` AFTER INSERT ON `tb_keranjang` FOR EACH ROW BEGIN
DELETE FROM tb_databarang
WHERE stok = 0
AND
kode_barang = NEW.kode_barang;
END
$$
DELIMITER ;

-- --------------------------------------------------------

--
-- Struktur dari tabel `tb_pelanggan`
--

CREATE TABLE `tb_pelanggan` (
  `id_pelanggan` int(20) NOT NULL,
  `nama_pelanggan` varchar(20) NOT NULL,
  `jenis_kelamin` varchar(20) NOT NULL,
  `alamat` varchar(20) NOT NULL,
  `telepon` varchar(20) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `tb_pelanggan`
--

INSERT INTO `tb_pelanggan` (`id_pelanggan`, `nama_pelanggan`, `jenis_kelamin`, `alamat`, `telepon`) VALUES
(1, 'LISA', 'Perempuan', 'Thailand', '+6282112345678'),
(2, 'Bayu', 'Laki-Laki', 'Bandung', '+6281234567890');

-- --------------------------------------------------------

--
-- Struktur dari tabel `transaksi`
--

CREATE TABLE `transaksi` (
  `tgl_transaksi` date NOT NULL,
  `id_transaksi` int(11) NOT NULL,
  `kode_barang` varchar(50) NOT NULL,
  `nama_barang` varchar(50) NOT NULL,
  `nama_pelanggan` varchar(20) NOT NULL,
  `harga` int(11) NOT NULL,
  `jumlah_barang` int(10) NOT NULL,
  `total_harga` int(11) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Dumping data untuk tabel `transaksi`
--

INSERT INTO `transaksi` (`tgl_transaksi`, `id_transaksi`, `kode_barang`, `nama_barang`, `nama_pelanggan`, `harga`, `jumlah_barang`, `total_harga`) VALUES
('2023-07-01', 1, 'KD003', 'Sofa', '', 500000, 2, 1000000),
('2023-07-04', 2, 'KD001', 'Lemari', 'Erwin', 200000, 2, 400000),
('2023-07-07', 3, 'KD003', 'Sofa', '', 500000, 3, 1500000),
('2023-07-07', 4, 'KD004', 'Lemari', '', 300000, 2, 600000),
('2023-07-07', 5, 'KD003', 'Sofa', 'Bayu', 500000, 3, 1500000);

--
-- Indexes for dumped tables
--

--
-- Indeks untuk tabel `admin`
--
ALTER TABLE `admin`
  ADD PRIMARY KEY (`id`);

--
-- Indeks untuk tabel `barang_keluar`
--
ALTER TABLE `barang_keluar`
  ADD PRIMARY KEY (`no_keluar`);

--
-- Indeks untuk tabel `barang_masuk`
--
ALTER TABLE `barang_masuk`
  ADD PRIMARY KEY (`no_masuk`),
  ADD KEY `id_distributor` (`nama_distributor`);

--
-- Indeks untuk tabel `distributor`
--
ALTER TABLE `distributor`
  ADD PRIMARY KEY (`id_distributor`);

--
-- Indeks untuk tabel `tb_databarang`
--
ALTER TABLE `tb_databarang`
  ADD PRIMARY KEY (`kode_barang`);

--
-- Indeks untuk tabel `tb_datapetugas`
--
ALTER TABLE `tb_datapetugas`
  ADD PRIMARY KEY (`id_petugas`);

--
-- Indeks untuk tabel `tb_keranjang`
--
ALTER TABLE `tb_keranjang`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `kode_barang` (`kode_barang`),
  ADD KEY `id_pelanggan` (`id_pelanggan`);

--
-- Indeks untuk tabel `tb_pelanggan`
--
ALTER TABLE `tb_pelanggan`
  ADD PRIMARY KEY (`id_pelanggan`);

--
-- Indeks untuk tabel `transaksi`
--
ALTER TABLE `transaksi`
  ADD PRIMARY KEY (`id_transaksi`),
  ADD KEY `nama_barang` (`nama_barang`);

--
-- AUTO_INCREMENT untuk tabel yang dibuang
--

--
-- AUTO_INCREMENT untuk tabel `tb_datapetugas`
--
ALTER TABLE `tb_datapetugas`
  MODIFY `id_petugas` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8;

--
-- AUTO_INCREMENT untuk tabel `tb_pelanggan`
--
ALTER TABLE `tb_pelanggan`
  MODIFY `id_pelanggan` int(20) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=8729;

--
-- Ketidakleluasaan untuk tabel pelimpahan (Dumped Tables)
--

--
-- Ketidakleluasaan untuk tabel `tb_keranjang`
--
ALTER TABLE `tb_keranjang`
  ADD CONSTRAINT `tb_keranjang_ibfk_2` FOREIGN KEY (`id_transaksi`) REFERENCES `transaksi` (`id_transaksi`);
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
