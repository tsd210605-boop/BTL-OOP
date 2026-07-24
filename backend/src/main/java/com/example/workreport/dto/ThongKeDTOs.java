package com.example.workreport.dto;

import java.util.List;

public class ThongKeDTOs {
    public static class TongQuanDTO {
        private Double diemTBChung;
        private Integer soBaiDaCham;
        private String xepLoaiDiem;
        private Integer tongBaiTap;
        private Integer soBaiDangHoatDong;
        private Integer soBaiDaKetThuc;
        private Integer tongSinhVien;
        private String tenLop;
        private Double tyLeHoanThanh;
        private String xepLoaiHoanThanh;
        public TongQuanDTO() {}

        public Double getDiemTBChung() {return diemTBChung;}
        public Integer getSoBaiDaCham() {return soBaiDaCham;}
        public String getXepLoaiDiem() {return xepLoaiDiem;}
        public Integer getTongBaiTap() {return tongBaiTap;}
        public Integer getSoBaiDangHoatDong() {return soBaiDangHoatDong;}
        public Integer getSoBaiDaKetThuc() {return soBaiDaKetThuc;}
        public Integer getTongSinhVien() {return tongSinhVien;}
        public String getTenLop() {return tenLop;}
        public Double getTyLeHoanThanh() {return tyLeHoanThanh;}
        public String getXepLoaiHoanThanh() {return xepLoaiHoanThanh;}

        public static Builder builder() {return new Builder();}
        public static class Builder {
            private final TongQuanDTO o = new TongQuanDTO();
            public Builder diemTBChung(Double v) {
                o.diemTBChung = v;
                return this;
            }
            public Builder soBaiDaCham(Integer v) {
                o.soBaiDaCham = v;
                return this;
            }
            public Builder xepLoaiDiem(String v) {
                o.xepLoaiDiem = v;
                return this;
            }
            public Builder tongBaiTap(Integer v) {
                o.tongBaiTap = v;
                return this;
            }
            public Builder soBaiDangHoatDong(Integer v) {
                o.soBaiDangHoatDong = v;
                return this;
            }
            public Builder soBaiDaKetThuc(Integer v) {
                o.soBaiDaKetThuc = v;
                return this;
            }
            public Builder tongSinhVien(Integer v) {
                o.tongSinhVien = v;
                return this;
            }
            public Builder tenLop(String v) {
                o.tenLop = v;
                return this;
            }
            public Builder tyLeHoanThanh(Double v) {
                o.tyLeHoanThanh = v;
                return this;
            }
            public Builder xepLoaiHoanThanh(String v) {
                o.xepLoaiHoanThanh = v;
                return this;
            }
            public TongQuanDTO build() {
                return o;
            }
        }
    }

    public static class TienDoNopBaiDTO {
        private List<BaiTapBarDTO> danhSachBaiTap;
        public TienDoNopBaiDTO() {}
        public TienDoNopBaiDTO(List<BaiTapBarDTO> danhSachBaiTap) {
            this.danhSachBaiTap = danhSachBaiTap;
        }
        public List<BaiTapBarDTO> getDanhSachBaiTap() {
            return danhSachBaiTap;
        }
        public static class BaiTapBarDTO {
            private String baiTapID;
            private String tenBaiTap;
            private Long daNop;
            private Long daCham;
            private Long chuaNop;
            public BaiTapBarDTO() {}

            public String getBaiTapID() {return baiTapID;}
            public String getTenBaiTap() {return tenBaiTap;}
            public Long getDaNop() {return daNop;}
            public Long getDaCham() {return daCham;}
            public Long getChuaNop() {return chuaNop;}
            public static Builder builder() {return new Builder();}
            public static class Builder {
                private final BaiTapBarDTO o = new BaiTapBarDTO();
                public Builder baiTapID(String v) {
                    o.baiTapID = v;
                    return this;
                }
                public Builder tenBaiTap(String v) {
                    o.tenBaiTap = v;
                    return this;
                }
                public Builder daNop(Long v) {
                    o.daNop = v;
                    return this;
                }
                public Builder daCham(Long v) {
                    o.daCham = v;
                    return this;
                }
                public Builder chuaNop(Long v) {
                    o.chuaNop = v;
                    return this;
                }
                public BaiTapBarDTO build() {
                    return o;
                }
            }
        }
    }

    public static class DiemTrungBinhDTO {
        private List<DiemTheoKyDTO> danhSach;
        public DiemTrungBinhDTO() {}
        public DiemTrungBinhDTO(List<DiemTheoKyDTO> danhSach) {
            this.danhSach = danhSach;
        }
        public List<DiemTheoKyDTO> getDanhSach() {
            return danhSach;
        }
        public static class DiemTheoKyDTO {
            private String tenBaiTap;
            private Double diemCaNhan;
            private Double diemTBLop;
            public DiemTheoKyDTO() {}

            public String getTenBaiTap() {return tenBaiTap;}
            public Double getDiemCaNhan() {return diemCaNhan;}
            public Double getDiemTBLop() {return diemTBLop;}
            public static Builder builder() {return new Builder();}
            public static class Builder {
                private final DiemTheoKyDTO o = new DiemTheoKyDTO();
                public Builder tenBaiTap(String v) {
                    o.tenBaiTap = v;
                    return this;
                }
                public Builder diemCaNhan(Double v) {
                    o.diemCaNhan = v;
                    return this;
                }
                public Builder diemTBLop(Double v) {
                    o.diemTBLop = v;
                    return this;
                }
                public DiemTheoKyDTO build() {
                    return o;
                }
            }
        }
    }

    public static class TrangThaiBaiNopDTO {
        private Long tongBaiNop;
        private Long daCham;
        private Long daNop;
        private Long nopTre;
        private Long chuaNop;
        public TrangThaiBaiNopDTO() {}

        public Long getTongBaiNop() {return tongBaiNop;}
        public Long getDaCham() {return daCham;}
        public Long getDaNop() {return daNop;}
        public Long getNopTre() {return nopTre;}
        public Long getChuaNop() {return chuaNop;}
        public static Builder builder() {return new Builder();}
        public static class Builder {
            private final TrangThaiBaiNopDTO o = new TrangThaiBaiNopDTO();
            public Builder tongBaiNop(Long v) {
                o.tongBaiNop = v;
                return this;
            }
            public Builder daCham(Long v) {
                o.daCham = v;
                return this;
            }
            public Builder daNop(Long v) {
                o.daNop = v;
                return this;
            }
            public Builder nopTre(Long v) {
                o.nopTre = v;
                return this;
            }
            public Builder chuaNop(Long v) {
                o.chuaNop = v;
                return this;
            }
            public TrangThaiBaiNopDTO build() {
                return o;
            }
        }
    }

    public static class ChiTietBaiTapDTO {
        private List<BaiTapRowDTO> rows;
        public ChiTietBaiTapDTO() {}
        public ChiTietBaiTapDTO(List<BaiTapRowDTO> rows) {
            this.rows = rows;
        }
        public List<BaiTapRowDTO> getRows() {return rows;}
        public static class BaiTapRowDTO {
            private String tenBaiTap;
            private String tenMonHoc;
            private Double diem;
            private String trangThai;
            private String nhanTrangThai;
            private String ngayNop;
            public BaiTapRowDTO() {}
            public String getTenBaiTap() {return tenBaiTap;}
            public String getTenMonHoc() {return tenMonHoc;}
            public Double getDiem() {return diem;}
            public String getTrangThai() {return trangThai;}
            public String getNhanTrangThai() {return nhanTrangThai;}
            public String getNgayNop() {return ngayNop;}
            public static Builder builder() {return new Builder();}
            public static class Builder {
                private final BaiTapRowDTO o = new BaiTapRowDTO();
                public Builder tenBaiTap(String v) {
                    o.tenBaiTap = v;
                    return this;
                }
                public Builder tenMonHoc(String v) {
                    o.tenMonHoc = v;
                    return this;
                }
                public Builder diem(Double v) {
                    o.diem = v;
                    return this;
                }
                public Builder trangThai(String v) {
                    o.trangThai = v;
                    return this;
                }
                public Builder nhanTrangThai(String v) {
                    o.nhanTrangThai = v;
                    return this;
                }
                public Builder ngayNop(String v) {
                    o.ngayNop = v;
                    return this;
                }
                public BaiTapRowDTO build() {
                    return o;
                }
            }
        }
    }
}

