package com.example.edgepoint.skadoosh_naga;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;

import java.util.ArrayList;
import java.util.List;

public class DatabaseAccess {
    private SQLiteOpenHelper openHelper;
    private SQLiteDatabase database;
    private static DatabaseAccess instance;

    private String[] columnsLogin = { "id" ,"username" ,"password"};
    private String[] columnsBatch = { "id" ,"batch_name","batch_count"};
    private String voters_table = "voters_table";
    private String upload_table = "upload_table";
    private String users_table = "users_table";
    private String batch_table = "batch_table";
    /**
     * Private constructor to aboid object creation from outside classes.
     *
     * @param context
     */
    private DatabaseAccess(Context context) {
        this.openHelper = new DatabaseOpenHelper(context);
    }

    /**
     * Return a singleton instance of DatabaseAccess.
     *
     * @param context the Context
     * @return the instance of DabaseAccess
     */
    public static DatabaseAccess getInstance(Context context) {
        if (instance == null) {
            instance = new DatabaseAccess(context);
        }
        return instance;
    }

    /**
     * Open the database connection.
     */
    public void open() {
        this.database = openHelper.getWritableDatabase();
    }

    /**
     * Close the database connection.
     */
    public void close() {
        if (database != null) {
            this.database.close();
        }
    }

    /**
     * Read all voters from the database.
     *
     * @return a List of voters
     */

    public boolean loginInfo(String username,String password, String table) {
        Cursor cursor = database.query(table, columnsLogin , "username=? AND password=?",new String[] {username,password}, null, null, null, null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public int graphlevelcount(String username,String password, String table) {
        int count = 0;
        Cursor cursor = database.query(table, columnsLogin , "username=? AND password=?",new String[] {username,password}, null, null, null, null);
        cursor.moveToFirst();
        count = cursor.getInt(cursor.getColumnIndex("id"));
        cursor.moveToNext();
        cursor.close();
        return count;
    }

    public boolean votersPreference(String password) {
        Cursor cursor = database.query("admin_table", columnsLogin , "password=?",new String[] {password}, null, null, null, null);
        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        return false;
    }

    public List<String> getDatabase(String v ,String sel,String group,int x) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(voters_table, null , sel, new String[] {v}, group, null, group + " ASC", null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(x));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public String getDeceasedStr(String votersname) {
        String str = "";
        Cursor cursor = database.query(voters_table, null , "VotersName=?", new String[] {votersname}, null, null, null, null);
        cursor.moveToFirst();
        str = cursor.getString(cursor.getColumnIndex("Deceased"));
        cursor.moveToNext();
        cursor.close();
        return str;
    }

    public List<String> getListinfo(String votersname) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(voters_table, null , "VotersName=?", new String[] {votersname}, null, null, null, null);
        cursor.moveToFirst();
        for(int i=8; i<11;i++)
        {
            list.add(cursor.getString(i));
        }
        cursor.close();
        return list;
    }

    public List<String> setDatabase(String group,int x) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(true,voters_table, null , null,null, group, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(x));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> setSeacrhDatabase(String group,int x) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(true,voters_table, null , null,null, group, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(x));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public List<String> setInfo(String v) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(voters_table, null , "VotersName=?",new String[] {v}, null, null, null, null);
        cursor.moveToFirst();
        for(int i=0; i<cursor.getColumnCount();i++)
        {
            list.add(cursor.getString(i));
        }
        cursor.close();
        return list;
    }

    public List<String> setUpload(String v) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(upload_table, null , "VotersName=?",new String[] {v}, null, null, null, null);
        cursor.moveToFirst();
        for(int i=0; i<cursor.getColumnCount();i++)
        {
            list.add(cursor.getString(i));
        }
        cursor.close();
        return list;
    }

    public String getBatchName() {
        String batchlist = "";
        Cursor cursor = database.query(batch_table, columnsBatch , "id=?",new String[] {"1"}, null, null, null, null);
        cursor.moveToFirst();
        batchlist = cursor.getString(1);
        cursor.moveToNext();
        cursor.close();
        return batchlist;
    }

    public int getBatchCount() {
        int batchlist = 0;
        Cursor cursor = database.query(batch_table, columnsBatch , "id=?",new String[] {"1"}, null, null, null, null);
        cursor.moveToFirst();
        batchlist = cursor.getInt(2);
        cursor.moveToNext();
        cursor.close();
        return batchlist;
    }

    public ArrayList<ViewInfoRecycler> getRecyclerVoterName(){
        ArrayList<ViewInfoRecycler> voters = new ArrayList<>();
        Cursor csr = database.query("voters_table",new String[]{"VotersName"},null,null,null,null,"VotersName ASC");
        while(csr.moveToNext()) {
            voters.add(new ViewInfoRecycler(csr.getString(0)));
        }
        csr.close();
        return voters;
    }

    public long getUploadDate() {

        Cursor cursor = database.rawQuery("SELECT MAX(UploadedAt) AS UploadedAt FROM voters_table WHERE UploadedAt !=''", null);
        cursor.moveToFirst();
        int index = cursor.getColumnIndex("UploadedAt");
        long Uploaddata = cursor.getLong(index);
        cursor.moveToNext();
        cursor.close();
        return Uploaddata;
    }

    public boolean updateBatch(String batch_name, int batch_count) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("batch_name",batch_name);
        contentValues.put("batch_count",batch_count);
        database.update(batch_table,contentValues,"id=?",new String[] {"1"});
        return true;
    }

    public boolean checkDuplicateBatch(String batch_name) {
        String[] tableColumns = new String[] {"Batch_Label"};
        Cursor cursor = database.query("LineGraph",tableColumns,"Batch_Label=?",new String[] {batch_name},null,null,null);

        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        // return all notes
        return false;
    }

    public boolean updateBatchFile(String batch_name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Batch_Label",batch_name);
        database.update("LineGraph",  contentValues, "Batch_Label=?",new String[] {batch_name});
        database.update("LineGraph_bgy",  contentValues, "Batch_Label=?",new String[] {batch_name});
        database.update("LineGraph_pct",  contentValues, "Batch_Label=?",new String[] {batch_name});
        return true;
    }

    public boolean insertBatch(String batch_name) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("Batch_Label",batch_name);
        database.insertWithOnConflict("LineGraph", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        database.insertWithOnConflict("LineGraph_bgy", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        database.insertWithOnConflict("LineGraph_pct", null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public boolean checkDuplicateUpload(String votersname) {
        String[] tableColumns = new String[] {"VotersName"};
        Cursor cursor = database.query(upload_table,tableColumns,"VotersName=?",new String[] {votersname},null,null,null);

        if(cursor!=null) {
            if(cursor.getCount()>0) {
                cursor.close();
                return true;
            }
        }
        cursor.close();
        // return all notes
        return false;
    }

    public boolean insertOnConflictData(String votersname,String Mayor,String Phone,String Encoder, String Indicator, String Deceased)  {
        ContentValues contentValues = new ContentValues();
        contentValues.put("VotersName",votersname);
        contentValues.put("Phone",Phone);
        contentValues.put("Encoder",Encoder);
        contentValues.put("Mayor",Mayor);
        contentValues.put("Indicator",Indicator);
        contentValues.put("Deceased",Deceased);
        database.update(voters_table, contentValues, "VotersName=?",new String[] {votersname});
        database.insertWithOnConflict(upload_table, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        return true;
    }

    public boolean updateData(String votersname,String Mayor,String Phone,String Encoder, String Indicator, String Deceased) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("VotersName",votersname);
        contentValues.put("Phone",Phone);
        contentValues.put("Encoder",Encoder);
        contentValues.put("Mayor",Mayor);
        contentValues.put("Indicator",Indicator);
        contentValues.put("Deceased",Deceased);
        database.update(voters_table, contentValues, "VotersName=?",new String[] {votersname});
        database.update(upload_table, contentValues, "VotersName=?",new String[] {votersname});
        return true;
    }

    public boolean updateFromServer(long uploadedat,String votersname,String Mayor,String Phone,String Indicator,String Deceased,String Encoder) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("UploadedAt",uploadedat);
        contentValues.put("Mayor",Mayor);
        contentValues.put("Phone",Phone);
        contentValues.put("Encoder",Encoder);
        contentValues.put("Deceased",Deceased);
        contentValues.put("Indicator",Indicator);

        database.update(voters_table, contentValues, "VotersName = ? AND Mayor != ?",new String[] {votersname,Mayor});

        return true;
    }

    public boolean updateLogin(int id,String username,String password) {
        ContentValues contentValues = new ContentValues();
        contentValues.put("id",id);
        contentValues.put("username",username);
        contentValues.put("password",password);
        database.update(users_table, contentValues, "id="+id,null);
        database.insertWithOnConflict(users_table, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        return true;
    }

    public List<String> getUploadDatabase(String group,int x) {
        List<String> list = new ArrayList<>();
        Cursor cursor = database.query(upload_table, null , null,null, group, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            list.add(cursor.getString(x));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public Integer getcountname(String namex, String columnName) {
        int count = 0;
        Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where " + columnName + "='" + namex + "' ", null);
        mCount.moveToFirst();
        count=mCount.getInt(0);
        mCount.close();
        return count;

    }

    public Integer getcountname2(String namex, String columnName, String brgy) {
        int count = 0;
        String[] selectionArg = {brgy, namex};
        Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where Barangay = ? and " + columnName + "= ? ", selectionArg);
        mCount.moveToFirst();
        count=mCount.getInt(0);
        mCount.close();
        return count;

    }

    public Integer getcountname3(String namex, String columnName, String precincto) {
        int count = 0;
        String[] selectionArg = {precincto, namex};
        Cursor mCount= database.rawQuery("select count(" + columnName + ") from voters_table where Precinct = ? and " + columnName + "= ? ", selectionArg);
        mCount.moveToFirst();
        count=mCount.getInt(0);
        mCount.close();
        return count;

    }

    public boolean insertCount(int xindex, String distinctnames, Integer distinctcount, String table_graph){
        ContentValues cv = new ContentValues();
        cv.put("yAxis", distinctcount);
        cv.put("xAxis", xindex);

        database.update(table_graph, cv, "Label=?", new String[]{distinctnames});

        return true;
    }

    public boolean minsertCount(int mxindex, String mdistinctnames, Integer mdistinctcount, String table_graph){
        ContentValues cv = new ContentValues();
        cv.put("yAxis", mdistinctcount);
        cv.put("xAxis", mxindex);

        database.update(table_graph, cv, "Label=?", new String[]{mdistinctnames});

        return true;
    }

    public ArrayList<BarEntry> getBarEntries(String table_graph) {
        ArrayList<BarEntry> rv = new ArrayList<>();
        Cursor csr = database.query(table_graph,new String[]{"xAxis","yAxis","Label"},null,null,null,null,null);
        while(csr.moveToNext()) {
            rv.add(new BarEntry(csr.getInt(0), csr.getInt(1), csr.getString(2)));
        }
        csr.close();
        return rv;
    }


    public List<Integer> getEntriesY(String table_graph){
        List<Integer> list = new ArrayList<>();
        Cursor cursor = database.query(table_graph, new String[]{"xAxis", "yAxis", "Label"}, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getInt(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<BarEntry> getBarEntriesM(String table_graph) {
        ArrayList<BarEntry> rv = new ArrayList<>();
        Cursor csr = database.query(table_graph,new String[]{"xAxis","yAxis","Label"},null,null,null,null,null);
        while(csr.moveToNext()) {
            rv.add(new BarEntry(csr.getInt(0), csr.getInt(1), csr.getString(2)));
        }
        csr.close();
        return rv;
    }

    public List<Integer> getEntriesYM(String table_graph){
        List<Integer> list = new ArrayList<>();
        Cursor cursor = database.query(table_graph, new String[]{"xAxis", "yAxis", "Label"}, null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getInt(1));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<Entry> getLineEntries(String table_graph, int x) {
        ArrayList<Entry> rv = new ArrayList<>();
        Cursor csr = database.query(table_graph,null,null,null,null,null,null);
        while(csr.moveToNext()) {
            rv.add(new Entry(csr.getInt(0),csr.getInt(x)));
        }
        csr.close();
        return rv;
    }

    public boolean insertLineCount(int LineCount, String LineColumn, String Batch_Name, String table_graph){
        ContentValues cv = new ContentValues();
        cv.put(LineColumn, LineCount);

        database.update(table_graph, cv, "Batch_Label=?", new String[]{Batch_Name});

        return true;
    }

    public boolean delete(){
        database.delete(upload_table,null,null);
        database.close();
        return true;
    }

    public boolean resetAllData(){

        ContentValues cv_voters = new ContentValues();
        cv_voters.put("Mayor","");
        cv_voters.put("UploadedAt","");
        cv_voters.put("Indicators","off");
        database.update(voters_table,cv_voters,"Mayor != ''",null);

        database.close();
        return true;
    }


    public boolean insertCountStacked(String distinctnames, Integer distinctcount, String table_graph){
        ContentValues cv = new ContentValues();
        cv.put("yAxisA", distinctcount);
//        cv.put("xAxis", xindex);
        database.update(table_graph, cv, "Label=?", new String[]{distinctnames});
        return true;
    }

    public boolean insertCountStackedS(String distinctnames, Integer distinctcount, String table_graph){
        ContentValues cv = new ContentValues();
        cv.put("yAxisB", distinctcount);
//        cv.put("xAxis", xindex);
        database.update(table_graph, cv, "Labels=?", new String[]{distinctnames});
        return true;
    }

    public List<Integer> getEntriesYStacked(){
        List<Integer> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT yAxisA + yAxisB FROM mayor_graph_stacked", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }


    public List<Integer> getEntriesYStackedM(){
        List<Integer> list = new ArrayList<>();
        Cursor cursor = database.rawQuery("SELECT yAxisA + yAxisB FROM mayor_graph_stackedM", null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            list.add(cursor.getInt(0));
            cursor.moveToNext();
        }
        cursor.close();
        return list;
    }

    public ArrayList<BarEntry> getBarEntriesStacked(String table_graph) {
        ArrayList<BarEntry> rv = new ArrayList<>();
        Cursor csr = database.query(table_graph,new String[]{"xAxis","yAxisA","yAxisB", "Label","Labels"},null,null,null,null,null);
        while(csr.moveToNext()) {
            rv.add(new BarEntry(csr.getInt(0), new float[] {csr.getFloat(2), csr.getFloat(1)}));
        }
        csr.close();
        return rv;
    }

    public ArrayList<Upload_List_Data> getAll_UploadData() {
        ArrayList<Upload_List_Data> dataList = new ArrayList<>();
        Cursor cursor = database.query(upload_table, null , null,null, null, null, null, null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Upload_List_Data list_Activity_Data = new Upload_List_Data();
            list_Activity_Data.setVotersname(cursor.getString(cursor.getColumnIndex("VotersName")));
            list_Activity_Data.setPhoneUpload(cursor.getString(cursor.getColumnIndex("Phone")));
            list_Activity_Data.setMayorUpload(cursor.getString(cursor.getColumnIndex("Mayor")));
            list_Activity_Data.setIndicator(cursor.getString(cursor.getColumnIndex("Indicator")));
            list_Activity_Data.setDeceased(cursor.getString(cursor.getColumnIndex("Deceased")));
            list_Activity_Data.setEncoder(cursor.getString(cursor.getColumnIndex("Encoder")));
            dataList.add(list_Activity_Data);
            cursor.moveToNext();
        }

        cursor.close();
        // return all notes
        return dataList;
    }
}
