package com.threabba.android.pedometer.model;

import java.util.List;

/**
 * Created by jun on 16. 12. 4.
 */

public class Address {

    private Result result;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
    }

    public class Result{
        private int total;
        private String userquery;
        private List<Item> items;

        public int getTotal() {
            return total;
        }

        public void setTotal(int total) {
            this.total = total;
        }

        public String getUserquery() {
            return userquery;
        }

        public void setUserquery(String userquery) {
            this.userquery = userquery;
        }

        public List<Item> getItems() {
            return items;
        }

        public void setItems(List<Item> items) {
            this.items = items;
        }

        public class Item{
            private String address;
            private AddressTail addrdetail;
            private boolean isRoadAddress;
            private Point point;

            public String getAddress() {
                return address;
            }

            public void setAddress(String address) {
                this.address = address;
            }

            public AddressTail getAddrdetail() {
                return addrdetail;
            }

            public void setAddrdetail(AddressTail addrdetail) {
                this.addrdetail = addrdetail;
            }

            public boolean isRoadAddress() {
                return isRoadAddress;
            }

            public void setRoadAddress(boolean roadAddress) {
                isRoadAddress = roadAddress;
            }

            public Point getPoint() {
                return point;
            }

            public void setPoint(Point point) {
                this.point = point;
            }

            public class AddressTail{
                private String country;
                private String sido;
                private String sigugun;
                private String dongmyun;
                private String rest;

                public String getCountry() {
                    return country;
                }

                public void setCountry(String country) {
                    this.country = country;
                }

                public String getSido() {
                    return sido;
                }

                public void setSido(String sido) {
                    this.sido = sido;
                }

                public String getSigugun() {
                    return sigugun;
                }

                public void setSigugun(String sigugun) {
                    this.sigugun = sigugun;
                }

                public String getDongmyun() {
                    return dongmyun;
                }

                public void setDongmyun(String dongmyun) {
                    this.dongmyun = dongmyun;
                }

                public String getRest() {
                    return rest;
                }

                public void setRest(String rest) {
                    this.rest = rest;
                }
            }
            public class Point{
                private double x;
                private double y;

                public double getX() {
                    return x;
                }

                public void setX(double x) {
                    this.x = x;
                }

                public double getY() {
                    return y;
                }

                public void setY(double y) {
                    this.y = y;
                }
            }
        }
    }
}
