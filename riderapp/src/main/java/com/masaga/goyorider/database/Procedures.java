package com.masaga.goyorider.database;

/**
 * Created by mTech on 03-Mar-2017.
 */
public final class Procedures {

    // make the constructor private.
    private Procedures(){}

    public static class tbl_driver_info{

        public static final String CREATE = "CREATE TABLE  IF NOT EXISTS "
                + Tables.tbl_driver_info.name +
                "("
                +  Tables.tbl_driver_info.autoid + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                +  Tables.tbl_driver_info.mibuid  + " VARCHAR, "
                +  Tables.tbl_driver_info.sarthinm  + " VARCHAR ,"
                +  Tables.tbl_driver_info.mob1  + " VARCHAR ,"
                +  Tables.tbl_driver_info.mob2  + " VARCHAR ,"
                +  Tables.tbl_driver_info.adharno  + " VARCHAR ,"
                +  Tables.tbl_driver_info.ownrship  + " VARCHAR ,"
                +  Tables.tbl_driver_info.vehno  + " VARCHAR ,"
                +  Tables.tbl_driver_info.yrsold  + " INTEGER ,"
                +  Tables.tbl_driver_info.btchno + " VARCHAR ,"
                +  Tables.tbl_driver_info.howmny + " INTEGER ,"
                +  Tables.tbl_driver_info.driving  + " VARCHAR ,"
                +  Tables.tbl_driver_info.vehtype + " VARCHAR,"
                +  Tables.tbl_driver_info.alruseing  + " VARCHAR ,"
                +  Tables.tbl_driver_info.goyointr  + " VARCHAR ,"
                +  Tables.tbl_driver_info.doyohv  + " VARCHAR ,"
                +  Tables.tbl_driver_info.prefloc  + " VARCHAR ,"
                +  Tables.tbl_driver_info.createon  + " VARCHAR ,"
                +  Tables.tbl_driver_info.createdby  + " VARCHAR,"
                +  Tables.tbl_driver_info.sentToserver  + " VARCHAR ,"
                +  Tables.tbl_driver_info.remarks  + " VARCHAR ,"
                +  Tables.tbl_driver_info.lat + " VARCHAR,"
                +  Tables.tbl_driver_info.lon+ " VARCHAR,"
                +  Tables.tbl_driver_info.profpic + " VARCHAR,"
                +  Tables.tbl_driver_info.profpic_upload + " VARCHAR,"
                +  Tables.tbl_driver_info.servrid + " VARCHAR,"

                +  Tables.tbl_driver_info.doc1 + " VARCHAR,"
                +  Tables.tbl_driver_info.doc1_upload+ " VARCHAR,"
                +  Tables.tbl_driver_info.doc2 + " VARCHAR,"
                +  Tables.tbl_driver_info.doc2_upload+ " VARCHAR,"
                +  Tables.tbl_driver_info.doc3+ " VARCHAR,"
                +  Tables.tbl_driver_info.doc3_upload+ " VARCHAR,"
                +  Tables.tbl_driver_info.doc4+ " VARCHAR,"
                +  Tables.tbl_driver_info.doc4_upload+ " VARCHAR"
                +")";

        public static final String DROP =  "DROP TABLE IF EXISTS " + Tables.tbl_driver_info.name ;

        public static final String INSERT = "INSERT INTO " + Tables.tbl_driver_info.name
                + " ("
                + Tables.tbl_driver_info.mibuid +  "," + Tables.tbl_driver_info.sarthinm + "," +  Tables.tbl_driver_info.mob1+ ","
                +  Tables.tbl_driver_info.mob2 + "," +  Tables.tbl_driver_info.adharno +","+  Tables.tbl_driver_info.ownrship +","
                +  Tables.tbl_driver_info.vehno +","+  Tables.tbl_driver_info.yrsold +"," +  Tables.tbl_driver_info.btchno +","
                +  Tables.tbl_driver_info.howmny +","+  Tables.tbl_driver_info.driving +"," +  Tables.tbl_driver_info.alruseing +","
                +  Tables.tbl_driver_info.goyointr +","+  Tables.tbl_driver_info.doyohv +"," +  Tables.tbl_driver_info.prefloc +","
                +  Tables.tbl_driver_info.createon +","+  Tables.tbl_driver_info.createdby+","+  Tables.tbl_driver_info.sentToserver
                +","+  Tables.tbl_driver_info.vehtype+","+  Tables.tbl_driver_info.remarks+","+  Tables.tbl_driver_info.lat
                +","+  Tables.tbl_driver_info.lon+","+  Tables.tbl_driver_info.profpic+","+Tables.tbl_driver_info.profpic_upload+","+  Tables.tbl_driver_info.doc1
                +","+  Tables.tbl_driver_info.doc1_upload+","+  Tables.tbl_driver_info.doc2+","+  Tables.tbl_driver_info.doc2_upload
                +","+  Tables.tbl_driver_info.doc3+","+  Tables.tbl_driver_info.doc3_upload+","+  Tables.tbl_driver_info.doc4
                +","+  Tables.tbl_driver_info.doc4_upload
                + ")"
                + " VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

    }
}
