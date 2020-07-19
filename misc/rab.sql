-- phpMyAdmin SQL Dump
-- version 5.0.1
-- https://www.phpmyadmin.net/
--
-- Host: 127.0.0.1
-- Generation Time: Jun 26, 2020 at 10:52 AM
-- Server version: 10.4.11-MariaDB
-- PHP Version: 7.4.2

SET SQL_MODE = "NO_AUTO_VALUE_ON_ZERO";
SET AUTOCOMMIT = 0;
START TRANSACTION;
SET time_zone = "+00:00";


/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8mb4 */;

--
-- Database: `rab`
--

-- --------------------------------------------------------

--
-- Stand-in structure for view `costperuraian`
-- (See below for the actual view)
--
CREATE TABLE `costperuraian` (
`kdUraian` varchar(6)
,`CostBahan` double
,`CostTenaga` double
);

-- --------------------------------------------------------

--
-- Table structure for table `items`
--

CREATE TABLE `items` (
  `kdItems` varchar(6) NOT NULL DEFAULT '',
  `nama` varchar(25) DEFAULT NULL,
  `deskripsi` text DEFAULT NULL,
  `satuan` enum('ltr','lbr','m','m²','m³','kg','btg','OH') DEFAULT NULL,
  `harga` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `items`
--

INSERT INTO `items` (`kdItems`, `nama`, `deskripsi`, `satuan`, `harga`) VALUES
('MT0001', 'Air', '', 'ltr', 2200),
('MT0002', 'Amplas', 'Grit 300', 'lbr', 4500),
('MT0003', 'Krikil', 'Pecahan Granit 2/3 cm', 'm²', 135000),
('MT0004', 'Plywood', '120 x 240 cm tebal = 4mm', 'lbr', 65000),
('MT0005', 'Pasir ', 'pasangan', 'm³', 350000),
('MT0006', 'Krikil ', 'Pecahan Granit 3/5 cm', 'm³', 150000),
('MT0007', 'Batako padat', '', 'kg', 2160),
('MT0008', 'Besi Beton', '', 'kg', 12000),
('MT0009', 'Dolken Kayu ', 'dia.8-10/400cm', 'btg', 30000),
('MT0010', 'Kawat Ikat', '', 'kg', 20000),
('MT0011', 'Papan', 'Kelas IV papan maal', 'm³', 2500000),
('MT0012', 'Paku', '', 'kg', 15000),
('MT0013', 'Pasir pasangan ', '', 'm³', 300000),
('MT0014', 'Batu belah granit', '', 'm³', 420000),
('MT0015', 'Kayu', 'Kelas III/Begesting', 'm³', 4500000),
('MT0016', 'Keramik lantai ', '40 x 40 cm ex.Mulia (corak)', 'm²', 50400),
('MT0017', 'keramik dinding ', '20 x 25 cm ex. Mulia (corek)', 'm²', 44400),
('MT0018', 'Semen', 'Warna', 'kg', 1600),
('MT0019', 'Semen', 'Tiga Roda', 'kg', 1440),
('MT0020', 'Pipa PVC ', 'dia. 1/2 inch', 'm', 30600),
('MT0021', 'Pipa PVC ', 'dia. 3/4 inch', 'm', 42600),
('MT0022', 'Pipa PVC ', 'dia. 2 inch', 'm', 104400),
('MT0023', 'Cat Mowilex Eksterior', '', 'kg', 40000),
('MT0024', 'Cat Mowilex Interior', '', 'kg', 40000),
('MT0025', 'Plamur', 'Tembok', 'kg', 30000),
('MT0026', 'Minyak Bekisting', '', 'ltr', 45000),
('MT0027', 'Batu Kali', 'Pondasi', 'm³', 260000);

-- --------------------------------------------------------

--
-- Table structure for table `kalkulasi`
--

CREATE TABLE `kalkulasi` (
  `kdKalkulasi` varchar(4) DEFAULT NULL,
  `kdUraian` varchar(6) DEFAULT NULL,
  `satuan` enum('ltr','lbr','m','m²','m³','kg','btg') NOT NULL,
  `volume` double NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `kalkulasi`
--

INSERT INTO `kalkulasi` (`kdKalkulasi`, `kdUraian`, `satuan`, `volume`) VALUES
('PK03', 'B.1', 'm³', 56),
('PK01', 'B.1', 'm³', 30),
('PK01', 'C.3', 'm³', 3),
('PK03', 'G.8a', 'm³', 12),
('PK04', 'E.12', 'm³', 20),
('PK04', 'D.4a', 'm³', 34),
('PK02', 'G.8h', 'm³', 7.81),
('PK02', 'E.5a', 'm³', 11),
('PK02', 'D.4a', 'm³', 4),
('PK02', 'G.7', 'm³', 10);

-- --------------------------------------------------------

--
-- Table structure for table `konsumen`
--

CREATE TABLE `konsumen` (
  `kdKonsumen` varchar(4) NOT NULL COMMENT 'Kode Konsumen',
  `nmKonsumen` varchar(30) NOT NULL COMMENT 'Nama Konsumen',
  `alamat` text NOT NULL COMMENT 'Alamat Konsumen',
  `noTelp` varchar(12) NOT NULL COMMENT 'No Telepon Konsumen'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `konsumen`
--

INSERT INTO `konsumen` (`kdKonsumen`, `nmKonsumen`, `alamat`, `noTelp`) VALUES
('K000', 'Cobalah', 'cobalah', '01234567'),
('K001', 'PT. Timah Tbk.', 'Jl. Medan Merdeka Timur 15\r\nJakarta Pusat\r\nJakarta, Indonesia', '022123528000');

-- --------------------------------------------------------

--
-- Table structure for table `koordinator`
--

CREATE TABLE `koordinator` (
  `kdKoordinator` varchar(4) NOT NULL COMMENT 'Kode Koordinator',
  `nmKoordinator` varchar(30) NOT NULL COMMENT 'Nama Koordinator',
  `jabatan` text NOT NULL COMMENT 'Jabatan',
  `noTelp` varchar(12) NOT NULL COMMENT 'Nomor Telepon'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Tabel Surat Tugas';

--
-- Dumping data for table `koordinator`
--

INSERT INTO `koordinator` (`kdKoordinator`, `nmKoordinator`, `jabatan`, `noTelp`) VALUES
('KD01', 'M. Masykur, S.T.', 'Koordinator Tim Monumentasi', '08131562898'),
('KD02', 'Dadang Rachyatna', 'Koordinator Tim GPS', '087819280031'),
('KD03', 'Sigit Haerudin', 'Koordinator Tim Poligon', '08957782981'),
('KD04', 'Kunto Seno Adji, S.T.', 'Koordinator Tim Prosesing Data', '081355790376');

-- --------------------------------------------------------

--
-- Table structure for table `kwitansi`
--

CREATE TABLE `kwitansi` (
  `noKwitansi` varchar(6) NOT NULL,
  `jumlahBayar` double NOT NULL DEFAULT 0,
  `sisaBayar` double NOT NULL DEFAULT 0,
  `kdProyek` varchar(4) NOT NULL,
  `kdKonsumen` varchar(4) NOT NULL,
  `noSPH` varchar(4) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `mkalkulasi`
--

CREATE TABLE `mkalkulasi` (
  `kdKalkulasi` varchar(4) NOT NULL,
  `kdProyek` varchar(4) NOT NULL,
  `Deskripsi` text NOT NULL,
  `subKalkulasi` tinyint(1) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `mkalkulasi`
--

INSERT INTO `mkalkulasi` (`kdKalkulasi`, `kdProyek`, `Deskripsi`, `subKalkulasi`) VALUES
('PK01', 'B101', 'PEK. PAGAR PANEL BETON PJ ± 34.000,- M\' (34 KM)', 0),
('PK02', 'B101', 'PEK. PLANG NAMA uk. 70 x 100 CM', 0),
('PK03', 'B101', 'PEK. POS PENGAMANAN uk. 3.50 x 3.50 M', 1),
('PK04', 'B101', 'PEK. FINISHING DLL', 0),
('PK05', 'B101', 'PEK. Coba-coba', 0);

-- --------------------------------------------------------

--
-- Table structure for table `muraian`
--

CREATE TABLE `muraian` (
  `kdUraian` varchar(6) NOT NULL,
  `kdProyek` varchar(4) DEFAULT NULL,
  `Deskripsi` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='Master table uraian';

--
-- Dumping data for table `muraian`
--

INSERT INTO `muraian` (`kdUraian`, `kdProyek`, `Deskripsi`) VALUES
('B.1', 'B101', '1 m³ Galian Tanah Dalam s/d 1 m manual'),
('B0.1', 'B000', 'Cobalah'),
('C.3', 'B101', '1 m³ Pasang Pondasi Batu gunung 1 : 5'),
('D.4a', 'B101', '1 m² Dinding batako padat uk. 9X15x30 cm  ad. 1 : 4  '),
('E.10', 'B101', '1 m² Pasang Plesteran 1 : 5 Tebal 20 mm  '),
('E.12', 'B101', 'Memasang 1 m2 acian  '),
('E.5a', 'B101', '1 m² Pasang Plesteran 1 : 5 Tebal 15 mm   '),
('G.2', 'B101', ' 1 m³ Beton Mutu K175  '),
('G.4', 'B101', '1 m³ Beton Mutu K225  '),
('G.7', 'B101', 'Pembesian 1 kg Dengan Besi polos  '),
('G.8', 'B101', '1 m² Bekisting Untuk Kolom dan balok Praktis  '),
('G.8a', 'B101', '1 m² Pemasangan Bekisting Sloof  '),
('G.8b', 'B101', '1 m² Pemasangan Bekisting kolom  '),
('G.8h', 'B101', '1 m² Bekisting Untuk Kolom dan balok Praktis  ');

-- --------------------------------------------------------

--
-- Table structure for table `proyek`
--

CREATE TABLE `proyek` (
  `kdProyek` varchar(4) NOT NULL,
  `deskripsi` text NOT NULL,
  `nilai` double NOT NULL,
  `tglMulai` date NOT NULL,
  `tglSelesai` date NOT NULL,
  `kdKonsumen` varchar(4) NOT NULL,
  `Wilayah` text NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `proyek`
--

INSERT INTO `proyek` (`kdProyek`, `deskripsi`, `nilai`, `tglMulai`, `tglSelesai`, `kdKonsumen`, `Wilayah`) VALUES
('B000', 'Coba', 123456, '2019-06-05', '2019-06-05', 'K000', 'ulala'),
('B101', 'PEKERJAAN PEMASANGAN PAGARPANEL BETON<br> PANJANG ± 34.000,- M\' & POS JAGA SATPAM uk. 3.50 X 3.50 M<br>BERIKUT PASANG PLANK NAMA<br>PADA LAHAN KOSONG MILIK PT Timah Tbk.\r\n', 25000000000, '2019-01-30', '2019-06-30', 'K001', 'Jl. Jenderal Sudirman 51\r\nPangkal Pinang 33121,\r\nBangka, Indonesia');

-- --------------------------------------------------------

--
-- Table structure for table `riwayatkwitansi`
--

CREATE TABLE `riwayatkwitansi` (
  `tglKwitansi` date NOT NULL,
  `noKwitansi` varchar(6) NOT NULL,
  `Bayar` double NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `sph`
--

CREATE TABLE `sph` (
  `noSPH` varchar(30) NOT NULL COMMENT 'Nomor Surat Penawaran harga',
  `jasa` double NOT NULL DEFAULT 0 COMMENT 'Biaya Jasa total dari proyek',
  `ppn` tinyint(3) NOT NULL DEFAULT 0 COMMENT 'Pajak Pertambahan Nilai',
  `GrandTotal` double NOT NULL DEFAULT 0 COMMENT 'Biaya keseluruhan termasuk Pajak',
  `kdProyek` varchar(4) NOT NULL COMMENT 'Foreign Key untuk table Proyek'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

-- --------------------------------------------------------

--
-- Table structure for table `st`
--

CREATE TABLE `st` (
  `kodeSt` varchar(4) NOT NULL,
  `tglLaporanSt` date NOT NULL,
  `kdProyek` varchar(4) NOT NULL,
  `listKoordinator` text NOT NULL,
  `tglMulaiSt` date NOT NULL,
  `tglSelesaiSt` date NOT NULL,
  `kdKonsumen` varchar(6) NOT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='Surat Tugas';

--
-- Dumping data for table `st`
--

INSERT INTO `st` (`kodeSt`, `tglLaporanSt`, `kdProyek`, `listKoordinator`, `tglMulaiSt`, `tglSelesaiSt`, `kdKonsumen`) VALUES
('ST01', '0120-06-24', 'B101', 'KD01|KD02|KD03|KD04', '2020-06-24', '2020-06-30', 'K001');

-- --------------------------------------------------------

--
-- Table structure for table `tenaga`
--

CREATE TABLE `tenaga` (
  `kdTenaga` varchar(3) NOT NULL DEFAULT '',
  `nmTenaga` varchar(25) DEFAULT NULL,
  `satuan` enum('OH') DEFAULT 'OH',
  `harga` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1;

--
-- Dumping data for table `tenaga`
--

INSERT INTO `tenaga` (`kdTenaga`, `nmTenaga`, `satuan`, `harga`) VALUES
('S01', 'Pekerja', 'OH', 156240),
('S02', 'Mandor', 'OH', 209361),
('S03', 'Tukang', 'OH', 179676),
('S04', 'Kepala Tukang', 'OH', 196862);

-- --------------------------------------------------------

--
-- Table structure for table `uraian`
--

CREATE TABLE `uraian` (
  `kdUraian` varchar(6) DEFAULT NULL,
  `kdItems` varchar(6) DEFAULT NULL,
  `jmlItems` double DEFAULT NULL,
  `kdTenaga` varchar(3) DEFAULT NULL,
  `jmlTenaga` double DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=latin1 COMMENT='tabel untuk uraian pekerjaan';

--
-- Dumping data for table `uraian`
--

INSERT INTO `uraian` (`kdUraian`, `kdItems`, `jmlItems`, `kdTenaga`, `jmlTenaga`) VALUES
('C.3', 'MT0027', 1.2, 'S01', 1.5),
('C.3', 'MT0019', 136, 'S03', 0.75),
('C.3', 'MT0005', 0.544, 'S04', 0.075),
('B.1', NULL, NULL, 'S01', 0.75),
('B.1', NULL, NULL, 'S02', 0.025),
('G.2', 'MT0019', 326, 'S01', 1.65),
('G.2', 'MT0005', 0.6, 'S03', 0.275),
('G.2', 'MT0001', 215, 'S04', 0.028),
('G.2', 'MT0003', 0.75, 'S02', 0.083),
('G.4', 'MT0019', 352, 'S01', 1.65),
('D.4a', 'MT0007', 18, 'S01', 0.3),
('D.4a', 'MT0019', 11, 'S03', 0.1),
('D.4a', 'MT0005', 0.035, 'S04', 0.01),
('D.4a', NULL, NULL, 'S02', 0.015),
('G.7', 'MT0008', 1.05, 'S01', 0.007),
('G.7', 'MT0010', 0.015, 'S03', 0.007),
('G.7', NULL, NULL, 'S04', 0.0007),
('G.7', NULL, NULL, 'S02', 0.0004),
('E.5a', 'MT0019', 10.368, 'S01', 0.6),
('E.5a', 'MT0005', 0.052, 'S03', 0.3),
('E.5a', NULL, NULL, 'S04', 0.03),
('E.5a', NULL, NULL, 'S02', 0.03),
('E.10', 'MT0019', 6.912, 'S01', 0.4),
('E.12', 'MT0019', 3.25, 'S01', 0.2),
('E.12', NULL, NULL, 'S03', 0.1),
('E.12', NULL, NULL, 'S04', 0.01),
('E.12', NULL, NULL, 'S02', 0.01),
('G.8h', 'MT0015', 0.0063, 'S01', 0.52),
('G.8h', 'MT0004', 0.0875, 'S03', 0.26),
('G.8h', 'MT0012', 0.3, 'S04', 0.026),
('G.8h', 'MT0026', 0.1, 'S02', 0.026),
('G.8a', 'MT0015', 0.0113, 'S01', 0.52),
('G.8b', 'MT0012', 0.4, 'S01', 0.66),
('G.8b', 'MT0009', 0.5, 'S03', 0.33),
('G.8b', 'MT0015', 0.0138, 'S04', 0.033),
('G.8b', NULL, NULL, 'S02', 0.033),
('B0.1', 'MT0020', 5, 'S04', 1),
('B0.1', 'MT0023', 8, 'S01', 4);

-- --------------------------------------------------------

--
-- Table structure for table `user`
--

CREATE TABLE `user` (
  `ID` varchar(7) NOT NULL,
  `Username` varchar(16) NOT NULL,
  `Password` varchar(16) NOT NULL,
  `Level` int(11) NOT NULL DEFAULT 1 COMMENT '0 - Administrator\r\n1 - Staff\r\n2 - Direktur'
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;

--
-- Dumping data for table `user`
--

INSERT INTO `user` (`ID`, `Username`, `Password`, `Level`) VALUES
('58X0VYJ', 'Syalam', 'kabar007', 2),
('FXC353I', 'Devops', 'rabfxunindra', 0),
('WR2KV9Q', 'RTFM', 'hodgepodge', 1);

-- --------------------------------------------------------

--
-- Stand-in structure for view `vwperuraian`
-- (See below for the actual view)
--
CREATE TABLE `vwperuraian` (
`kdUraian` varchar(6)
,`Deskripsi` text
,`Bahan` mediumtext
,`qtyBahan` mediumtext
,`Tenaga` mediumtext
,`qtyTenaga` mediumtext
);

-- --------------------------------------------------------

--
-- Stand-in structure for view `vwperuraianfix`
-- (See below for the actual view)
--
CREATE TABLE `vwperuraianfix` (
`kdUraian` varchar(6)
,`Deskripsi` text
,`Bahan` mediumtext
,`qtyBahan` mediumtext
,`Tenaga` mediumtext
,`qtyTenaga` mediumtext
,`kdProyek` varchar(4)
);

-- --------------------------------------------------------

--
-- Structure for view `costperuraian`
--
DROP TABLE IF EXISTS `costperuraian`;

CREATE ALGORITHM=TEMPTABLE DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `costperuraian`  AS  select `u`.`kdUraian` AS `kdUraian`,if(`u`.`kdItems` is null and `u`.`jmlItems` is null,0,sum(`i`.`harga` * `u`.`jmlItems`)) AS `CostBahan`,if(`u`.`kdTenaga` is null and `u`.`jmlTenaga` is null,0,sum(`t`.`harga` * `u`.`jmlTenaga`)) AS `CostTenaga` from ((`uraian` `u` left join `items` `i` on(`i`.`kdItems` = `u`.`kdItems`)) left join `tenaga` `t` on(`t`.`kdTenaga` = `u`.`kdTenaga`)) group by `u`.`kdUraian` ;

-- --------------------------------------------------------

--
-- Structure for view `vwperuraian`
--
DROP TABLE IF EXISTS `vwperuraian`;

CREATE ALGORITHM=TEMPTABLE DEFINER=`root`@`localhost` SQL SECURITY INVOKER VIEW `vwperuraian`  AS  select `uraian`.`kdUraian` AS `kdUraian`,`muraian`.`Deskripsi` AS `Deskripsi`,group_concat(`uraian`.`kdItems` separator '|') AS `Bahan`,group_concat(`uraian`.`jmlItems` separator '|') AS `qtyBahan`,group_concat(`uraian`.`kdTenaga` separator '|') AS `Tenaga`,group_concat(`uraian`.`jmlTenaga` separator '|') AS `qtyTenaga` from (`uraian` join `muraian`) where `uraian`.`kdUraian` = `muraian`.`kdUraian` group by `uraian`.`kdUraian` ;

-- --------------------------------------------------------

--
-- Structure for view `vwperuraianfix`
--
DROP TABLE IF EXISTS `vwperuraianfix`;

CREATE ALGORITHM=TEMPTABLE DEFINER=`root`@`localhost` SQL SECURITY DEFINER VIEW `vwperuraianfix`  AS  select `uraian`.`kdUraian` AS `kdUraian`,`muraian`.`Deskripsi` AS `Deskripsi`,group_concat(`uraian`.`kdItems` separator '|') AS `Bahan`,group_concat(`uraian`.`jmlItems` separator '|') AS `qtyBahan`,group_concat(`uraian`.`kdTenaga` separator '|') AS `Tenaga`,group_concat(`uraian`.`jmlTenaga` separator '|') AS `qtyTenaga`,`muraian`.`kdProyek` AS `kdProyek` from (`uraian` join `muraian`) where `uraian`.`kdUraian` = `muraian`.`kdUraian` group by `uraian`.`kdUraian` ;

--
-- Indexes for dumped tables
--

--
-- Indexes for table `items`
--
ALTER TABLE `items`
  ADD PRIMARY KEY (`kdItems`),
  ADD UNIQUE KEY `kdItems` (`kdItems`);

--
-- Indexes for table `kalkulasi`
--
ALTER TABLE `kalkulasi`
  ADD KEY `kdUraian` (`kdUraian`),
  ADD KEY `kdKalkulasi` (`kdKalkulasi`);

--
-- Indexes for table `konsumen`
--
ALTER TABLE `konsumen`
  ADD PRIMARY KEY (`kdKonsumen`);

--
-- Indexes for table `koordinator`
--
ALTER TABLE `koordinator`
  ADD PRIMARY KEY (`kdKoordinator`);

--
-- Indexes for table `kwitansi`
--
ALTER TABLE `kwitansi`
  ADD PRIMARY KEY (`noKwitansi`);

--
-- Indexes for table `mkalkulasi`
--
ALTER TABLE `mkalkulasi`
  ADD PRIMARY KEY (`kdKalkulasi`),
  ADD KEY `kdProyek` (`kdProyek`);

--
-- Indexes for table `muraian`
--
ALTER TABLE `muraian`
  ADD PRIMARY KEY (`kdUraian`) COMMENT 'primary key pada kdUraian',
  ADD KEY `muraian_ibfk_1` (`kdProyek`);

--
-- Indexes for table `proyek`
--
ALTER TABLE `proyek`
  ADD PRIMARY KEY (`kdProyek`);

--
-- Indexes for table `sph`
--
ALTER TABLE `sph`
  ADD PRIMARY KEY (`noSPH`);

--
-- Indexes for table `st`
--
ALTER TABLE `st`
  ADD PRIMARY KEY (`kodeSt`);

--
-- Indexes for table `tenaga`
--
ALTER TABLE `tenaga`
  ADD PRIMARY KEY (`kdTenaga`),
  ADD KEY `kdTenaga` (`kdTenaga`);

--
-- Indexes for table `uraian`
--
ALTER TABLE `uraian`
  ADD KEY `kdItems` (`kdItems`),
  ADD KEY `kdTenaga` (`kdTenaga`),
  ADD KEY `kdUraian` (`kdUraian`);

--
-- Indexes for table `user`
--
ALTER TABLE `user`
  ADD PRIMARY KEY (`ID`);

--
-- Constraints for dumped tables
--

--
-- Constraints for table `kalkulasi`
--
ALTER TABLE `kalkulasi`
  ADD CONSTRAINT `kalkulasi_ibfk_2` FOREIGN KEY (`kdUraian`) REFERENCES `muraian` (`kdUraian`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `kalkulasi_ibfk_3` FOREIGN KEY (`kdKalkulasi`) REFERENCES `mkalkulasi` (`kdKalkulasi`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `mkalkulasi`
--
ALTER TABLE `mkalkulasi`
  ADD CONSTRAINT `mkalkulasi_ibfk_1` FOREIGN KEY (`kdProyek`) REFERENCES `proyek` (`kdProyek`);

--
-- Constraints for table `muraian`
--
ALTER TABLE `muraian`
  ADD CONSTRAINT `muraian_ibfk_1` FOREIGN KEY (`kdProyek`) REFERENCES `proyek` (`kdProyek`) ON DELETE SET NULL ON UPDATE CASCADE;

--
-- Constraints for table `uraian`
--
ALTER TABLE `uraian`
  ADD CONSTRAINT `uraian_ibfk_1` FOREIGN KEY (`kdItems`) REFERENCES `items` (`kdItems`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `uraian_ibfk_2` FOREIGN KEY (`kdTenaga`) REFERENCES `tenaga` (`kdTenaga`) ON DELETE SET NULL ON UPDATE CASCADE,
  ADD CONSTRAINT `uraian_ibfk_3` FOREIGN KEY (`kdUraian`) REFERENCES `muraian` (`kdUraian`) ON DELETE SET NULL ON UPDATE CASCADE;
COMMIT;

/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
